package lms.demo.service;

import lms.demo.dao.BookDAO;
import lms.demo.model.Book;

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
}
