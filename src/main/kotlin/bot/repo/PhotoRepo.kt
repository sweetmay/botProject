package bot.repo

import bot.BaseTelegramMethods
import bot.exception.InvalidFormatException
import bot.repo.model.UserDAO
import bot.repo.model.UserModel
import org.apache.commons.io.FileUtils
import org.telegram.telegrambots.meta.api.methods.GetFile
import org.telegram.telegrambots.meta.api.objects.Document
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.PhotoSize
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.io.File


class PhotoRepo(
    private val tgMethods: BaseTelegramMethods
) : IPhotoRepo {

    override fun getPhoto(update: Update): String {

        getPhotoSize(update)?.let { photo ->
            if (!photo.filePath.isNullOrEmpty()) {
                return savePhoto(
                    tgMethods.execDownloadFile(GetFile(photo.filePath)),
                    update.message.from.id.toString()
                )
            } else {
                val getFileMethod = GetFile()
                getFileMethod.fileId = photo.fileId
                try {
                    return savePhoto(
                        tgMethods.execDownloadFile(getFileMethod),
                        update.message.from.id.toString()
                    )
                } catch (e: TelegramApiException) {
                    e.printStackTrace()
                }
            }
        }

        getDoc(update)?.let {doc->
            if(!doc.fileId.isNullOrEmpty()){
                return savePhoto(
                    tgMethods.execDownloadFile(GetFile(doc.fileId)),
                    update.message.from.id.toString()
                )
            }
        }

        return ""
    }

    private fun savePhoto(photo: File, id: String): String {
        val path = "UserData/" + id + "/" + photo.name.replaceAfter(".", "jpg")
        FileUtils.writeByteArrayToFile(File(path), photo.readBytes())
        return path
    }

    private fun getPhotoSize(update: Update): PhotoSize? {
        if (update.hasMessage() && update.message.hasPhoto()) {
            val photos = update.message.photo
            return photos.stream().max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null)
        } else {
            return null
        }
    }

    private fun getDoc(update: Update): Document? {
        val msg = update.message
        if(update.hasMessage() && msg.hasDocument()){
            when(msg.document.fileName.substringAfter(".")){
                "jpg" -> return msg.document
                "png" -> return msg.document
                "gif" -> return msg.document
                else -> throw InvalidFormatException("Неподдерживаемый формат")
            }
        }else {
            return null
        }
    }
}