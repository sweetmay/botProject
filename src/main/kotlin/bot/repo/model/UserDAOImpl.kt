package bot.repo.model

import bot.repo.db.HibernateSessionFactoryUtil
import org.hibernate.Session
import org.hibernate.Transaction

class UserDAOImpl : UserDAO{
    override fun getUserById(id: Int): UserModel? {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(UserModel::class.java, id)
    }

    override fun saveOrUpdate(userModel: UserModel) {
        val session: Session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val tx1: Transaction = session.beginTransaction()
        session.saveOrUpdate(userModel)
        tx1.commit()
        session.close()
    }

    override fun updateUser(userModel: UserModel) {
        val session: Session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val tx1: Transaction = session.beginTransaction()
        session.update(userModel)
        tx1.commit()
        session.close()
    }

    override fun findAllUsers(): List<UserModel> {
        TODO("Not yet implemented")
    }
}