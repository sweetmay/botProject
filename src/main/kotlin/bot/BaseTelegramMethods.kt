package bot

import org.telegram.telegrambots.meta.api.methods.GetFile
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import java.io.File
import java.lang.reflect.Method
import java.lang.reflect.TypeVariable

interface BaseTelegramMethods {
    fun execDownloadFile(getFile: GetFile): File
    fun showError(msg: SendMessage)
    fun askForData(msg: SendMessage)
    fun replyWithResult(qrResult: SendPhoto)
}
