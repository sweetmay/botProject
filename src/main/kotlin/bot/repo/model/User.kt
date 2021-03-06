package bot.repo.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "users")
data class User(@Id
                var id: Long,
                var first_name: String,
                var last_name: String)
