package lms.demo.service;

import lms.demo.dao.BookDAO;
import lms.demo.dao.TransactionDAO;
import lms.demo.model.Book;
import lms.demo.model.Transaction;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;

public class TransactionService {

    private final TransactionDAO transactionDAO = new TransactionDAO();

    // Add a new transaction
    public void addTransaction(Transaction transaction) {
        transactionDAO.addTransaction(transaction);
        Book book = transaction.getBook();
        book.setQuantity(book.getQuantity() - 1);
        BookDAO.update(book);
    }

    public void returnBook(int transactionId) {
        // Get the transaction by ID with eager loading of the associated Book
        String hql = "FROM Transaction t JOIN FETCH t.book WHERE t.id = :transactionId";
        Transaction transaction = TransactionDAO.getTransactionById(transactionId, hql);
        
        if (transaction != null && "Issued".equals(transaction.getStatus())) {
            // Set the return date
            Date returnDate = new Date();
            transaction.setReturnDate(returnDate);
            transaction.setStatus("Returned");

            // Update the transaction in the database
            TransactionDAO.update(transaction);

            // Increase the book quantity after return
            Book book = transaction.getBook();
            book.setQuantity(book.getQuantity() + 1);
            BookDAO.update(book);

            System.out.println("Book returned successfully.");
        } else {
            System.out.println("Invalid transaction ID or the book has already been returned.");
        }
    }

 
    
 // Method to view all transactions
    public static List<Transaction> viewTransactions() {
        // Fetch all transactions using DAO
        List<Transaction> transactions = TransactionDAO.getAllTransactions();

        // Ensure that all associated books are initialized to prevent LazyInitializationException
        for (Transaction transaction : transactions) {
            // Initialize the associated Book for each transaction
            Hibernate.initialize(transaction.getBook());
        }

        // Print out the transactions
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            System.out.println("\nAll Transactions:");
            for (Transaction transaction : transactions) {
                System.out.println(transaction);  // toString method of Transaction should display relevant details
            }
        }
		return transactions;
    }


    // Method to update an existing transaction
    public void updateTransaction(Transaction transaction) {
        if (transaction != null) {
            // Update the transaction in the database
            TransactionDAO.update(transaction);
            System.out.println("Transaction updated successfully.");
        } else {
            System.out.println("Transaction is null. Cannot update.");
        }
    }

    // Delete a transaction by ID
    public void deleteTransaction(int id) {
        transactionDAO.deleteTransaction(id);
    }
}
