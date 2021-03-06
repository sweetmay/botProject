package bot.repo.model

import javax.persistence.Entity;
import javax.persistence.Id


@Entity
data class User(@Id
                var id: Long,
                var first_name: String,
                var last_name: String) {
}
