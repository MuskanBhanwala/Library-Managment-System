package lms.demo.dao;

import lms.demo.model.Book;
import lms.demo.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class BookDAO {

    public void saveBook(Book book) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(book);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public Book getBookById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Book.class, id);
        } finally {
            session.close();
        }
    }

    public List<Book> getAllBooks() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("from Book", Book.class).getResultList();
        } finally {
            session.close();
        }
    }

    public void updateBook(Book book) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(book);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

 // Method to update an existing book (e.g., for updating quantity)
    public static void update(Book book) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(book); // Update the book record
            session.getTransaction().commit();
            System.out.println("Book updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error updating book.");
        }
    }
    public void deleteBook(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Book book = session.get(Book.class, id);
            if (book != null) {
                session.delete(book);
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
