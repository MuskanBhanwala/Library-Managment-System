package lms.demo.service;

import lms.demo.dao.LibrarianDAO;
import lms.demo.model.Librarian;
import lms.demo.util.EmailUtility;

import java.util.List;
import java.util.UUID;

public class LibrarianService {

    private final LibrarianDAO librarianDAO = new LibrarianDAO();

    // Validate librarian login based on email and password
    public Librarian validateLibrarianLogin(String email, String password) {
        List<Librarian> librarians = librarianDAO.getAllLibrarians();
        
        for (Librarian librarian : librarians) {
            if (librarian.getEmail().equals(email) && librarian.getPassword().equals(password)) {
                System.out.println("Login successful");
                return librarian;  // Return the librarian if credentials match
            }
        }
        
        return null;  // Return null if no match found
    }

    // Add a new librarian
    public void addLibrarian(Librarian librarian) {
        // Set emailVerified to false by default when a new librarian is added
        librarian.setEmailVerified(false);
        
        // Save the librarian to the database
        librarianDAO.saveLibrarian(librarian);
        
        // Generate a unique token for email verification
        String token = generateVerificationToken();
        
        
        // Save the token to the database (you can store it in a "verification_tokens" table)
        saveVerificationTokenToDatabase(librarian, token);
        
        // Send a verification email with the link
        String verificationLink = "http://localhost:8080/verify-email?token=" + token;
       
     // Prepare librarian details to be included in the email
        String librarianDetails = "Name: " + librarian.getName() + "<br>" +
                                  "Email: " + librarian.getEmail() + "<br>" +
                                  "User Type: " + librarian.getUserType();
        
        EmailUtility.sendVerificationEmail(librarian.getEmail(), librarianDetails, verificationLink);
        
        System.out.println("Verification email sent to: " + librarian.getEmail());
        
    }

    // Generate a unique verification token
    private String generateVerificationToken() {
        return UUID.randomUUID().toString();  // Generate a random UUID for the token
    }

    // Save the verification token to the database (this method should be implemented)
    private void saveVerificationTokenToDatabase(Librarian librarian, String token) {
        // Implement the logic to save the token to the database (e.g., token table)
        System.out.println("Token generated for " + librarian.getEmail() + ": " + token);
    }

    // Verify the librarian's email using the token
    public boolean verifyEmail(String token) {
        boolean tokenValid = checkTokenInDatabase(token);
        
        if (tokenValid) {
            // Mark the librarian as verified (you can update this status in the database)
            updateEmailVerifiedStatus(token);
            System.out.println("Email successfully verified.");
            return true;
        } else {
            System.out.println("Invalid token.");
            return false;
        }
    }

    // Simulate checking the token in the database
    private boolean checkTokenInDatabase(String token) {
        // Simulate token check (replace with actual DB lookup)
        return token != null && !token.isEmpty();
    }

    // Update the librarian's emailVerified status after successful email verification
    private void updateEmailVerifiedStatus(String token) {
        // This should query the database to find the librarian associated with the token
        // Then update their emailVerified status to true.
        
        // For now, let's print a message for simulation:
        System.out.println("Email verified for token: " + token);
    }

    // Get a librarian by ID
    public Librarian getLibrarianById(int id) {
        return librarianDAO.getLibrarianById(id);
    }

    // Get a list of all librarians
    public List<Librarian> getAllLibrarians() {
        return librarianDAO.getAllLibrarians();
    }

    // Update an existing librarian
    public void updateLibrarian(Librarian librarian) {
        librarianDAO.updateLibrarian(librarian);
    }

    // Delete a librarian by ID
    public void deleteLibrarian(int id) {
        librarianDAO.deleteLibrarian(id);
    }
}
