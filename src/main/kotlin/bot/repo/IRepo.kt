package bot.repo

import bot.repo.model.UserModel
import org.telegram.telegrambots.meta.api.objects.Update

interface IRepo {
    fun rememberUser(userModel: UserModel)
    fun getPhoto(update: Update)

}