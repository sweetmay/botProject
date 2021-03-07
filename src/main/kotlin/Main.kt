import bot.Bot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import org.apache.log4j.PropertyConfigurator

import java.util.Properties

fun main(args: Array<String>){
    loggerInit()
    botInit()

}

private fun botInit() {
    try {
        val botsApi = TelegramBotsApi(DefaultBotSession::class.java)
        botsApi.registerBot(Bot())
    } catch (e: TelegramApiException) {
        e.printStackTrace()
    }
}

private fun loggerInit() {
    val log4jProp = Properties()
    log4jProp.setProperty("log4j.rootLogger", "WARN")
    PropertyConfigurator.configure(log4jProp)
}