package bot.repo.model

import org.hibernate.SessionFactory
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Configuration

class HibernateSessionFactoryUtil {
    companion object{
        private val sessionFactory: SessionFactory? = null
        fun getSessionFactory(){
            if(sessionFactory == null)  {
                try {
                    val config = Configuration().configure()
                    sessionFactory ?: {
                        config.addAnnotatedClass(User::class.java)
                        val builder = StandardServiceRegistryBuilder()
                            .applySettings(config.properties)


                    }
                }
            }
        }
    }

}