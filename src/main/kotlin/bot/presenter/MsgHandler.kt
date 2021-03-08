package bot.presenter

import bot.BaseTelegramMethods
import bot.exception.InvalidFormatException
import bot.repo.IPhotoRepo
import bot.repo.model.UserDAO
import bot.repo.model.UserModel
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class MsgHandler(
    private val userDAO: UserDAO,
    private val photoRepo: IPhotoRepo,
    private val tgMethods: BaseTelegramMethods
) : IMsgHandler {

    private val currentUsers: ArrayList<UserModel> = userDAO.findAllUsers()
            as ArrayList<UserModel>

    override fun handleCommands(update: Update) {
        val msg = update.message
        if (msg.isCommand && msg.text.equals("/start")) {
            val user = UserModel.createFromTGUser(msg.from)
            if (!currentUsers.contains(user)) {
                currentUsers.add(user)
                userDAO.saveOrUpdate(user)
            }

            val greetMsg = SendMessage.builder()
                .chatId(msg.from.id.toString())
                .text("Hi, Send me any picture and I will convert it into a beautiful QR code")
                .build()
            tgMethods.askForData(greetMsg)
        }
    }

    override fun handlePhoto(update: Update) {
        val msg = update.message
        if(update.hasMessage() &&
            (update.message.hasPhoto() || update.message.hasDocument())){
            try {
                val savedPicPath = photoRepo.getPhoto(update)
                val curUser = currentUsers.find {
                        user -> user.id == msg.from.id
                }
                curUser?.photo_path = savedPicPath
                curUser?.let { userDAO.updateUser(it) }

                if(curUser?.encode_data.isNullOrEmpty()){
                    val encodeMsg = SendMessage.builder()
                        .chatId(msg.from.id.toString())
                        .text("Great!, now send me a link to encode or just a plain text")
                        .build()
                    tgMethods.askForData(encodeMsg)
                }else {
                    curUser?.let {
                        sendQrResult(convertToQr(it), it)
                    }
                }

            } catch (e: InvalidFormatException) {
                val errorMsg = SendMessage.builder()
                    .chatId(msg.from.id.toString())
                    .text("Invalid file format")
                    .build()
                tgMethods.showError(errorMsg)
            }
            }
    }

    override fun handleAdditionalData(update: Update) {
        val msg = update.message
        if (!msg.isCommand && !msg.text.isNullOrEmpty()) {
            val user = currentUsers.find { user -> user.id == msg.from.id }
            user?.encode_data = msg.text

            if(!user?.photo_path.isNullOrEmpty()){
                sendMsgProcessing(msg)
                user?.let { sendQrResult(convertToQr(it), it) }
            }else {
                sendMsgPhotoAsk(msg)
            }
        }
    }

    private fun sendMsgPhotoAsk(msg: Message) {
        val askForPhoto = SendMessage.builder()
            .chatId(msg.from.id.toString())
            .text("Alright... what photo we should encode this message into? (send me a photo or a document)")
            .build()
        tgMethods.askForData(askForPhoto)
    }

    private fun sendQrResult(file: File, userModel: UserModel){
        val qrMsg = SendPhoto.builder()
            .chatId(userModel.id.toString())
            .photo(InputFile(file))
            .caption("Encoded message: ${userModel.encode_data}")
            .build()
        tgMethods.replyWithResult(qrMsg)
        userModel.clearPastData()
        userDAO.updateUser(userModel)
    }

    private fun convertToQr(userModel: UserModel): File {
        val execString = "myqr ${userModel.encode_data} -p ${userModel.photo_path} -c -d UserData/${userModel.id}"
        val process = Runtime.getRuntime()
            .exec(execString)
        val outStream = process.inputStream
        val reader =
            BufferedReader(InputStreamReader(outStream, StandardCharsets.UTF_8))
        var qrPath = String()
        reader.lines().forEach {
            if(it.startsWith("Check")){
                qrPath = it.substringAfter(": ")
                return@forEach
            }
        }
        return File(qrPath)
    }

    private fun sendMsgProcessing(message: Message){
        val processingMsg = SendMessage.builder()
            .chatId(message.from.id.toString())
            .text("Processing...")
            .build()
        tgMethods.askForData(processingMsg)
    }
}