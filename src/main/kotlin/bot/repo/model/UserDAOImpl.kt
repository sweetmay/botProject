package bot.repo.model

import org.hibernate.Session
import org.hibernate.Transaction

class UserDAOImpl : UserDAO{
    override fun getUserById(id: Int): User? {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(User::class.java, id)
    }

    override fun saveUser(user: User) {
        val session: Session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val tx1: Transaction = session.beginTransaction()
        session.save(user)
        tx1.commit()
        session.close()
    }

    override fun updateUser(user: User) {
        TODO("Not yet implemented")
    }

    override fun findAllUsers(): List<User> {
        TODO("Not yet implemented")
    }
}