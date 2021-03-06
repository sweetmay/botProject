package bot.repo.model

interface UserDAO {
    fun getUserById(id: Int): User?
    fun saveUser(user: User)
    fun updateUser(user: User)
    fun findAllUsers(): List<User>
}