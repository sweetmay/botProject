package bot.repo.model

import org.telegram.telegrambots.meta.api.objects.User
import javax.persistence.*


@Entity
@Table(name = "users")
data class UserModel(
    @Id
    @Column(name = "id", unique = true)
    var id: Int,
    @Column(name = "first_name")
    var first_name: String,
    @Column(name = "last_name")
    var last_name: String,
    @Column(name = "photo_path")
    var photo_path: String,
    @Column(name = "encode_data")
    var encode_data: String
) {

    companion object {
        fun createFromTGUser(user: User): UserModel {
            return UserModel(
                user.id,
                user.firstName,
                user.lastName,
                "",
                "")
        }
    }

    fun clearPastData(){
        photo_path = ""
        encode_data = ""
    }

    override fun equals(other: Any?): Boolean {
        return id == (other as UserModel).id
    }
}