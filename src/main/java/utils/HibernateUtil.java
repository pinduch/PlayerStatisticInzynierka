package utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by Mateusz on 30.11.2016.
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory(){
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory created failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory(){
        if (sessionFactory != null) {
            return sessionFactory;
        } else {
            sessionFactory = buildSessionFactory();
            return  sessionFactory;
        }
    }

    public static void shutdown(){
        getSessionFactory().close();
    }

}
