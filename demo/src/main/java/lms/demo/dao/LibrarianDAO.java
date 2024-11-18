package lms.demo.dao;

import lms.demo.model.Librarian;
import lms.demo.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class LibrarianDAO {

    public void saveLibrarian(Librarian librarian) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(librarian);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public Librarian getLibrarianById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Librarian.class, id);
        } finally {
            session.close();
        }
    }

    public List<Librarian> getAllLibrarians() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("from Librarian", Librarian.class).getResultList();
        } finally {
            session.close();
        }
    }

    public void updateLibrarian(Librarian librarian) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(librarian);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void deleteLibrarian(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Librarian librarian = session.get(Librarian.class, id);
            if (librarian != null) {
                session.delete(librarian);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
