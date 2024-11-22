package lms.demo.service;

import lms.demo.model.Librarian;

public class LibrarianSession {
    // Static variable to hold the current logged-in librarian
    private static Librarian loggedInLibrarian;

    // Method to set the logged-in librarian
    public static void setLoggedInLibrarian(Librarian librarian) {
        loggedInLibrarian = librarian;
    }

    // Method to get the logged-in librarian
    public static Librarian getLoggedInLibrarian() {
        return loggedInLibrarian;
    }

    // Method to clear the logged-in librarian (e.g., logout)
    public static void clear() {
        loggedInLibrarian = null;
    }
}
