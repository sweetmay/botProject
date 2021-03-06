package bot

import bot.repo.IRepo
import bot.repo.LocalRepo
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
        final val API_KEY_PROP = "api"
        final val BOT_NAME_PROP = "name"
    }

    init {
        properties.load(input)
    }

    override fun getBotToken(): String =
        properties.getProperty(API_KEY_PROP)


    override fun getBotUsername(): String =
        properties.getProperty(BOT_NAME_PROP)

    override fun onUpdateReceived(update: Update?) {
        update?.message?.from?.let { 
        }
    }

}