package bot.repo.model

import org.telegram.telegrambots.meta.api.objects.User
import javax.persistence.*


@Entity
@Table(name = "users")
data class UserModel(
    @Id
    @Column(name = "id", unique = true)
    var id: Int,
    var first_name: String,
    var last_name: String,
    var photo_path: String = "",
    var encode_data: String = ""
) {

    companion object {
        fun createFromTGUser(user: User): UserModel {
            return UserModel(user.id, user.firstName, user.lastName)
        }
    }
}