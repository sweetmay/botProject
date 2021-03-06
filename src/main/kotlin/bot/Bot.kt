package bot

import bot.repo.IRepo
import bot.repo.LocalRepo
import bot.repo.model.UserDAOImpl
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import java.io.FileInputStream
import java.io.InputStream
import java.util.*

class Bot(val repo: IRepo): TelegramLongPollingBot() {
    val input = FileInputStream("app.properties")
    val properties: Properties = Properties()

    companion object{
        val API_KEY_PROP = "api"
        val BOT_NAME_PROP = "name"
    }

    init {
        properties.load(input)
        //UserDAOImpl().saveUser(bot.repo.model.User(1, "cunt", "cunt1"))
    }

    override fun getBotToken(): String =
        properties.getProperty(API_KEY_PROP)


    override fun getBotUsername(): String =
        properties.getProperty(BOT_NAME_PROP)

    override fun onUpdateReceived(update: Update?) {
        update?.message?.from?.let {
            val msg =  SendMessage()
            msg.chatId = it.id.toString()
            msg.text = "Hi cunt " + it.firstName
            execute(msg)
        }
    }

}