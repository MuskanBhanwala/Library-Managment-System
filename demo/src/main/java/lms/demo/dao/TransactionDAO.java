package lms.demo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import lms.demo.model.Book;
import lms.demo.model.Librarian;
import lms.demo.model.Member;
import lms.demo.model.Transaction;
import lms.demo.util.HibernateUtil;

public class TransactionDAO {


	 // Save a transaction
    public void addTransaction(Transaction transaction) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            org.hibernate.Transaction txn = session.beginTransaction();
            session.save(transaction);
            txn.commit();
        }
    }
	
    public static Transaction getTransactionById(int transactionId, String hql) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Transaction> query = session.createQuery(hql, Transaction.class);
            query.setParameter("transactionId", transactionId);
            return query.uniqueResult();  // Use JOIN FETCH in the query to load the Book immediately
        }
    }


 // Method to get all transactions with associated Book, Librarian, and Member eagerly fetched
    public static List<Transaction> getAllTransactions() {
        List<Transaction> transactions = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Using JOIN FETCH to eagerly load the associated Book, Librarian, and Member for each Transaction
            String hql = "FROM Transaction t " +
                         "JOIN FETCH t.book " +      // Eagerly fetch the associated Book
                         "JOIN FETCH t.librarian " + // Eagerly fetch the associated Librarian
                         "JOIN FETCH t.member";      // Eagerly fetch the associated Member
            Query<Transaction> query = session.createQuery(hql, Transaction.class);
            transactions = query.list();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching transactions.");
        }
        return transactions;
    }
 // Method to update an existing transaction (like when the book is returned)
    public static void update(Transaction transaction) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(transaction); // Update the transaction details in the database
            session.getTransaction().commit();
            System.out.println("Transaction updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error updating transaction.");
        }
    }
    
    
    public void deleteTransaction(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        org.hibernate.Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Transaction transaction = session.get(Transaction.class, id);
            if (transaction != null) {
                session.delete(transaction);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
