package bot

import bot.presenter.IMsgHandler
import bot.presenter.MsgHandler
import bot.repo.PhotoRepo
import bot.repo.model.UserDAO
import bot.repo.model.UserDAOImpl
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.GetFile
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.Update
import java.io.*
import java.util.*

class Bot: TelegramLongPollingBot(), BaseTelegramMethods {

    private val userDao: UserDAO = UserDAOImpl()
    private val msgHandler: IMsgHandler = MsgHandler(userDao,
        PhotoRepo(this),
        this)
    private val input = FileInputStream("app")
    private val properties: Properties = Properties()


    companion object{
        const val API_KEY_PROP = "api"
        const val BOT_NAME_PROP = "name"
    }

    init {
        properties.load(input)
        userDao.removePastDataForAllUsers(userDao.findAllUsers())
    }

    override fun getBotToken(): String =
        properties.getProperty(API_KEY_PROP)


    override fun getBotUsername(): String =
        properties.getProperty(BOT_NAME_PROP)

    override fun onUpdateReceived(update: Update?) {
        update?.let {
            msgHandler.handleCommands(update)
            msgHandler.handlePhotoOrDoc(update)
            msgHandler.handleMessageToEncode(update)
        }
    }

    override fun execDownloadFile(getFile: GetFile): File {
        val file = execute(getFile)
        return downloadFile(file)
    }

    override fun execSendMessage(msg: SendMessage) {
        execute(msg)
    }


    override fun replyWithResult(qrResult: SendPhoto) {
        execute(qrResult)
    }

}