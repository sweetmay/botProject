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
import java.io.File

class MsgHandler(
    private val userDAO: UserDAO,
    private val photoRepo: IPhotoRepo,
    private val tgMethods: BaseTelegramMethods
) : IMsgHandler {

    private val currentUsers: ArrayList<UserModel> = userDAO.findAllUsers()
            as ArrayList<UserModel>

    /**
     * Handle incoming commands
     */
    override fun handleCommands(update: Update) {
        val msg = update.message
        saveUser(update)
        if(msg.isCommand && msg.text == "/start"){
            sendMessage(msg.from.id.toString(),
                "Hi, send me any picture and I will convert it into a qr code")
        }
    }

    private fun saveUser(update: Update) {
        val user = UserModel.createFromTGUser(update.message.from)
        if (!currentUsers.contains(user)) {
            currentUsers.add(user)
            saveOrUpdateUser(user)
        }

    }

    /**
     * Handle incoming photo/document
     */
    override fun handlePhotoOrDoc(update: Update) {
        val msg = update.message
        saveUser(update)
        if(msg.hasPhoto() || msg.hasDocument()){
            val user = getCurrentUser(msg)
            try {
                user?.let {it->
                    it.photo_path = photoRepo.getPhoto(update)
                    saveOrUpdateUser(it)
                    tryToMakeQr(user)
                }
            }catch (e: InvalidFormatException){
                sendMessage(msg.from.id.toString(),
                    "Invalid format")
            }

        }
    }

    /**
     * Handle incoming messages to encode
     */
    override fun handleMessageToEncode(update: Update) {
        val msg = update.message
        saveUser(update)
        if(!msg.isCommand && msg.hasText()){
            val user = getCurrentUser(msg)
            user?.let {it->
                it.encode_data = msg.text
                saveOrUpdateUser(it)
                tryToMakeQr(user)
            }
        }
    }

    private fun getCurrentUser(msg: Message): UserModel? {
        return currentUsers.find { userModel ->
            userModel.id == msg.from.id
        }
    }

    private fun tryToMakeQr(userModel: UserModel?) {
        userModel?.let {user->
            if(user.photo_path.isNotEmpty() && user.encode_data.isNotEmpty()){

                sendMessage(user.id.toString(), "Processing...")

                sendQrPhoto(user, File(photoRepo.getQrResult(user)))

                user.clearPastData()
                userDAO.saveOrUpdate(user)
                return
            }

            if(user.photo_path.isNullOrEmpty()){
                sendMessage(user.id.toString(), "Great! Now send me a picture")
            }

            if(user.encode_data.isNullOrEmpty()){
                sendMessage(user.id.toString(), "Great! Now send me a link or a message to encode")
            }
        }
    }

    /**
     * DB interaction
     */
    override fun saveOrUpdateUser(userModel: UserModel) {
        userDAO.saveOrUpdate(userModel)
    }

    private fun sendMessage(chatId: String, msgText: String) {
        val sendMessage = SendMessage
            .builder()
            .chatId(chatId)
            .text(msgText)
            .build()
        tgMethods.execSendMessage(sendMessage)
    }

    private fun sendQrPhoto(userModel: UserModel, file: File){
        val sendPhoto = SendPhoto
            .builder()
            .photo(InputFile(file))
            .chatId(userModel.id.toString())
            .caption("Encoded message: ${userModel.encode_data}")
            .build()
        tgMethods.replyWithResult(sendPhoto)
    }
}