package bot.repo

import org.telegram.telegrambots.meta.api.objects.User

class LocalRepo: IRepo {
    override fun rememberUser(user: User) {
        println(user)
    }
}