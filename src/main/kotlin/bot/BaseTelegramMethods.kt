package bot

import org.telegram.telegrambots.meta.api.methods.GetFile
import java.io.File
import java.lang.reflect.Method
import java.lang.reflect.TypeVariable

interface BaseTelegramMethods {
    fun execDownloadFile(getFile: GetFile): File
}
