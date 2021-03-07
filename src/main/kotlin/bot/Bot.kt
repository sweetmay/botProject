package bot

import bot.exception.InvalidFormatException
import bot.repo.PhotoRepo
import bot.repo.model.UserDAOImpl
import bot.repo.model.UserModel
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.GetFile
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import java.io.*
import java.util.*

class Bot: TelegramLongPollingBot(), BaseTelegramMethods {

    private val photoRepo = PhotoRepo(UserDAOImpl(), this)
    private val input = FileInputStream("app")
    private val properties: Properties = Properties()

    companion object{
        const val API_KEY_PROP = "api"
        const val BOT_NAME_PROP = "name"
    }

    init {
        properties.load(input)
//        val process = Runtime.getRuntime().exec("myqr https://github.com")
//        val outStream = process.inputStream
//        val reader = BufferedReader(InputStreamReader(outStream, StandardCharsets.UTF_8))
//        reader.lines().forEach {
//            println(it)
//        }
    }

    override fun getBotToken(): String =
        properties.getProperty(API_KEY_PROP)


    override fun getBotUsername(): String =
        properties.getProperty(BOT_NAME_PROP)

    override fun onUpdateReceived(update: Update?) {
        update?.let {
            photoRepo.rememberUser(UserModel.createFromTGUser(it.message.from))
            try {
                photoRepo.getPhoto(update)
            }catch (e: InvalidFormatException){
                val msg = SendMessage()
                msg.chatId = it.message.from.id.toString()
                msg.text = e.message.toString()
                execute(msg)
            }

        }
    }

    override fun execDownloadFile(getFile: GetFile): File {
        val file = execute(getFile)
        return downloadFile(file)
    }

}