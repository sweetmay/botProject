package bot.repo.model

interface UserDAO {
    fun getUserById(id: Int): UserModel?
    fun saveOrUpdate(userModel: UserModel)
    fun updateUser(userModel: UserModel)
    fun findAllUsers(): List<UserModel>
}