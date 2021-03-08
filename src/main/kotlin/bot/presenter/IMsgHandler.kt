package bot.presenter

import bot.repo.model.UserModel
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update

interface IMsgHandler {
    fun handleCommands(update: Update)
    fun handlePhotoOrDoc(update: Update)
    fun handleMessageToEncode(update: Update)
    fun saveOrUpdateUser(userModel: UserModel)
}