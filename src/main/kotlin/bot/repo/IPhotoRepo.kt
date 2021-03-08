package bot.repo

import bot.repo.model.UserModel
import org.telegram.telegrambots.meta.api.objects.Update
import java.io.File

interface IPhotoRepo {

    /**
     * Returns path to saved photo
     */
    fun getPhoto(update: Update): String

    /**
     * Returns path to qr
     */
    fun getQrResult(userModel: UserModel): String
}