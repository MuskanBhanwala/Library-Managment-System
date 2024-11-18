package lms.demo;

import lms.demo.model.Book;
import lms.demo.model.Librarian;
import lms.demo.model.Member;
import lms.demo.model.Transaction;
import lms.demo.service.BookService;
import lms.demo.service.LibrarianService;
import lms.demo.service.LibrarianSession;
import lms.demo.service.MemberService;
import lms.demo.service.TransactionService;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class APP {

    private static final Scanner scanner = new Scanner(System.in);

    private static final LibrarianService librarianService = new LibrarianService();
    private static final BookService bookService = new BookService();
    private static final TransactionService transactionService = new TransactionService();
    private static final MemberService memberService = new MemberService();

    public static void main(String[] args) {
        System.out.println("Welcome to the Library System");

        // Call the librarian login method
        if (librarianLogin()) {
            showMainMenu();  // Show the main menu after successful login
        } else {
            System.out.println("Invalid login credentials. Exiting the system.");
        }
    }

    private static boolean librarianLogin() {
        System.out.println("Enter librarian email: ");
        String email = scanner.nextLine();

        System.out.println("Enter librarian password: ");
        String password = scanner.nextLine();

        // Call the service to validate the login
        Librarian librarian = librarianService.validateLibrarianLogin(email, password);
        
        if (librarian != null) {
            // Successfully logged in, store the librarian in session
            LibrarianSession.setLoggedInLibrarian(librarian);
            System.out.println("Login successful!");
            return true;
        } else {
            // Invalid credentials
            System.out.println("Invalid login credentials.");
            return false;
        }
    }

    private static void showMainMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Librarian Management");
            System.out.println("2. Member Management");
            System.out.println("3. Book Management");
            System.out.println("4. Transaction Management");
            System.out.println("5. Logout");

            System.out.print("Enter your choice (1-5): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    showLibrarianMenu();
                    break;
                case 2:
                    showMemberMenu();
                    break;
                case 3:
                    showBookMenu();
                    break;
                case 4:
                    showTransactionMenu();
                    break;
                case 5:
                    exit = true;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Librarian Management Menu
    private static void showLibrarianMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nLibrarian Menu:");
            System.out.println("1. Add a Librarian");
            System.out.println("2. Logout");

            System.out.print("Enter your choice (1-2): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    addLibrarian();
                    break;
                case 2:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    //add a new librarian
    private static void addLibrarian() {
        System.out.println("Enter librarian's name: ");
        String name = scanner.nextLine();
        System.out.println("Enter librarian's email: ");
        String email = scanner.nextLine();
        System.out.println("Enter librarian's password: ");
        String password = scanner.nextLine();
        System.out.println("Enter librarian's user type (admin/member): ");
        String userType = scanner.nextLine();

        // Create a new Librarian object
        Librarian newLibrarian = new Librarian();
        newLibrarian.setName(name);
        newLibrarian.setEmail(email);
        newLibrarian.setPassword(password);
        newLibrarian.setUserType(userType);

        // Call the service to register the librarian and send the verification email
        librarianService.addLibrarian(newLibrarian);

        System.out.println("Librarian added successfully! A verification email has been sent.");
    }

    // Member Management Menu
    private static void showMemberMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nMember Menu:");
            System.out.println("1. Add a Member");
            System.out.println("2. View All Members");
            System.out.println("3. Logout");

            System.out.print("Enter your choice (1-3): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    addMember();
                    break;
                case 2:
                    viewMembers();
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Add a new member
    private static void addMember() {
        System.out.println("Enter the new member's name: ");
        String name = scanner.nextLine();

        System.out.println("Enter the new member's email: ");
        String email = scanner.nextLine();

        System.out.println("Enter the new member's phone number: ");
        String phone = scanner.nextLine();

        // Create a new Member object
        Member newMember = new Member();
        newMember.setName(name);
        newMember.setEmail(email);
        newMember.setPhone(phone);

        // Call the service layer to add the member
        memberService.addMember(newMember);

        System.out.println("Member added successfully!");
    }

    
    // View all members
    private static void viewMembers() {
        List<Member> members = memberService.getAllMembers();
        if (members.isEmpty()) {
            System.out.println("No members found.");
        } else {
            System.out.println("\nMembers:");
            members.forEach(member -> System.out.println(member));
        }
    }

    // Book Management Menu
    private static void showBookMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nBook Menu:");
            System.out.println("1. View Books");
            System.out.println("2. Add a Book");
            System.out.println("3. Update a Book");
            System.out.println("4. Delete a Book");
            System.out.println("5. Logout");

            System.out.print("Enter your choice (1-5): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    viewBooks();
                    break;
                case 2:
                    addBook();
                    break;
                case 3:
                    updateBook();
                    break;
                case 4:
                    deleteBook();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    // View all books
    private static void viewBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books available.");
        } else {
            System.out.println("\nBooks in the library:");
            books.forEach(book -> System.out.println(book));
        }
    }

    // Add a new book
    private static void addBook() {
        System.out.println("Enter book title: ");
        String title = scanner.nextLine();

        System.out.println("Enter author name: ");
        String author = scanner.nextLine();

        System.out.println("Enter price: ");
        int price = scanner.nextInt();

        System.out.println("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character

        Book book = new Book(title, author, price, quantity);
        bookService.addBook(book);
        System.out.println("Book added successfully!");
    }    


    // Update an existing book
    private static void updateBook() {
        System.out.println("Enter the book ID to update: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character

        Book book = bookService.getBookById(bookId);
        if (book != null) {
            System.out.println("Updating book: " + book);

            System.out.println("Enter new title (leave empty to keep current): ");
            String title = scanner.nextLine();
            if (!title.isEmpty()) {
                book.setTitle(title);
            }

            System.out.println("Enter new author (leave empty to keep current): ");
            String author = scanner.nextLine();
            if (!author.isEmpty()) {
                book.setAuthor(author);
            }

            System.out.println("Enter new price (leave 0 to keep current): ");
            int price = scanner.nextInt();
            if (price != 0) {
                book.setPrice(price);
            }

            System.out.println("Enter new quantity (leave 0 to keep current): ");
            int quantity = scanner.nextInt();
            if (quantity != 0) {
                book.setQuantity(quantity);
            }

            bookService.updateBook(book);
            System.out.println("Book updated successfully!");
        } else {
            System.out.println("Book not found with ID: " + bookId);
        }
    }
    
    //delete a book
    private static void deleteBook() {
        System.out.println("Enter the book ID to delete: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character

        bookService.deleteBook(bookId);
        System.out.println("Book deleted successfully!");
    }

    // Transaction Management Menu
    private static void showTransactionMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nTransaction Menu:");
            System.out.println("1. Issue a Book");
            System.out.println("2. Return a Book");
            System.out.println("3. View Transactions");
            System.out.println("4. Logout");

            System.out.print("Enter your choice (1-4): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    issueBook();
                    break;
                case 2:
                    returnBook();
                    break;
                case 3:
                    viewTransactions();
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

 // Method to create a new transaction
    private static void issueBook() {
        // Make sure the librarian is logged in
        Librarian librarian = LibrarianSession.getLoggedInLibrarian();
        if (librarian == null) {
            System.out.println("You must log in as a librarian first.");
            return;
        }

        System.out.println("Enter the book ID to issue: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character

        System.out.println("Enter the member ID: ");
        int memberId = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character

        // Fetch book and member from the database
        Book book = bookService.getBookById(bookId);
        Member member = memberService.getMemberById(memberId);

        // Check if book and member are valid
        if (book == null || member == null) {
            System.out.println("Invalid book or member ID. Please try again.");
            return;
        }

        // Get the current date as the issue date
        Date issueDate = new Date();

        // Create the transaction (pass book, member, librarian, issueDate, and status)
        Transaction transaction = new Transaction(book, member, librarian, issueDate, "Issued");

        // Save the transaction
        transactionService.addTransaction(transaction);

        System.out.println("Transaction created successfully!");
    }

    //return book
    private static void returnBook() {
        System.out.println("Enter transaction ID to return: ");
        int transactionId = scanner.nextInt();

        transactionService.returnBook(transactionId);
    }


    // View all transactions
    private static void viewTransactions() {
        List<Transaction> transactions = TransactionService.viewTransactions();
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            System.out.println("\nTransactions:");
            transactions.forEach(transaction -> System.out.println(transaction));
        }
    }
}
