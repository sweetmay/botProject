package bot.repo

import bot.repo.model.UserModel
import org.telegram.telegrambots.meta.api.objects.Update

interface IPhotoRepo {
    fun getPhoto(update: Update): String
}