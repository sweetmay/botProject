package bot

import bot.repo.IRepo
import bot.repo.LocalRepo
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User

class Bot(val repo: IRepo): TelegramLongPollingBot() {

    override fun getBotToken() = "1659108291:AAGR7Q0v-LUMvcIqltGAwYVJMzf3y8htUEs"

    override fun getBotUsername() = "BotProject"

    override fun onUpdateReceived(update: Update?) {
        update?.message?.from?.let { 
            repo.rememberUser(it)
        }
    }

}