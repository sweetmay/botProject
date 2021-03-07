package bot.presenter

import bot.BaseTelegramMethods
import bot.exception.InvalidFormatException
import bot.repo.IPhotoRepo
import bot.repo.model.UserDAO
import bot.repo.model.UserModel
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class MsgHandler(
    private val userDAO: UserDAO,
    private val photoRepo: IPhotoRepo,
    private val tgMethods: BaseTelegramMethods
) : IMsgHandler {

    override fun handleCommands(update: Update) {
        val msg = update.message
        if (msg.isCommand && msg.text.equals("/start")) {
            userDAO.saveOrUpdate(UserModel.createFromTGUser(msg.from))
            val greetMsg = SendMessage.builder()
                .chatId(msg.from.id.toString())
                .text("Hi, Send me any picture and I will convert it into a beautiful QR code")
                .build()
            tgMethods.askForData(greetMsg)
        }
    }

    override fun handlePhoto(update: Update) {
        val msg = update.message
        try {
            val savedPicPath = photoRepo.getPhoto(update)
            val curUser = userDAO.getUserById(msg.from.id)
            curUser?.photo_path = savedPicPath
            curUser?.let { userDAO.updateUser(it) }

            val encodeMsg = SendMessage.builder()
                .chatId(msg.from.id.toString())
                .text("Great, now send me a link to encode or just a plain text")
                .build()
            tgMethods.askForData(encodeMsg)
        } catch (e: InvalidFormatException) {
            val errorMsg = SendMessage.builder()
                .chatId(msg.from.id.toString())
                .text("Invalid file format")
                .build()
            tgMethods.showError(errorMsg)
        }
    }

    override fun handleAdditionalData(update: Update) {
        val msg = update.message
        if (!msg.text.isNullOrEmpty()) {
            userDAO.getUserById()
            val greetMsg = SendMessage.builder()
                .chatId(msg.from.id.toString())
                .text("Hi, Send me any picture and I will convert it into a beautiful QR code")
                .build()
            tgMethods.askForData(greetMsg)
        }
    }
    }

    private fun convertToQr(pathToFile: String, encodeMsg: String){
        val process = Runtime.getRuntime().exec("myqr $encodeMsg -p $pathToFile")
        val outStream = process.inputStream
        val reader = BufferedReader(InputStreamReader(outStream, StandardCharsets.UTF_8))
        reader.lines().forEach {
            println(it)
        }
    }

}