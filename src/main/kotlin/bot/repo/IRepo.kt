package bot.repo

import org.telegram.telegrambots.meta.api.objects.User

interface IRepo {
    fun rememberUser(user: User)
}