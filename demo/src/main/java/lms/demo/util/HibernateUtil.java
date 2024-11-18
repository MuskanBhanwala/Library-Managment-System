package lms.demo.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import lms.demo.model.Book;
import lms.demo.model.Member;
import lms.demo.model.Transaction;
import lms.demo.model.Librarian;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            // Create the SessionFactory from the Hibernate configuration file
            sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                                                .addAnnotatedClass(Book.class)
                                                .addAnnotatedClass(Member.class)
                                                .addAnnotatedClass(Transaction.class)
                                                .addAnnotatedClass(Librarian.class) // Add all model classes here
                                                .buildSessionFactory();
        } catch (Throwable ex) {
            // Handle initialization errors
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}
