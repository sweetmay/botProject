package bot.presenter

import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update

interface IMsgHandler {
    fun handleCommands(update: Update)
    fun handlePhoto(update: Update)
    fun handleAdditionalData(update: Update)
}