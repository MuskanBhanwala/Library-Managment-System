package lms.demo.service;

import lms.demo.dao.BookDAO;
import lms.demo.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookService {

    private final BookDAO bookDAO = new BookDAO();

    // Add a new book
    public void addBook(Book book) {
        bookDAO.saveBook(book);
    }

    // Get a book by ID
    public Book getBookById(int id) {
        return bookDAO.getBookById(id);
    }

    // Get a list of all books
    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    // Update an existing book
    public void updateBook(Book book) {
        bookDAO.updateBook(book);
    }

    // Delete a book by ID
    public void deleteBook(int id) {
        bookDAO.deleteBook(id);
    }
 // Method to search for books based on title or author
    private List<Book> books = new ArrayList<>();
    public List<Book> searchBooks(String searchTerm) {
        List<Book> filteredBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(searchTerm.toLowerCase()) ||
                book.getAuthor().toLowerCase().contains(searchTerm.toLowerCase())) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }

   
}
