package bot.repo.db

import bot.repo.model.UserModel
import org.hibernate.SessionFactory
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Configuration

class HibernateSessionFactoryUtil {


    companion object {
        private lateinit var factory: SessionFactory

        fun getSessionFactory(): SessionFactory {
            val config = Configuration().configure()
            config.addAnnotatedClass(UserModel::class.java)
            val builder = StandardServiceRegistryBuilder()
                .applySettings(config.properties)
            factory = config.buildSessionFactory(builder.build())
            return factory
    }
}

}