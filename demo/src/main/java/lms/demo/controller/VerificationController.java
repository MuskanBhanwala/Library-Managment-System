package lms.demo.controller;

import lms.demo.service.LibrarianService;

public class VerificationController {

    private static LibrarianService librarianService = new LibrarianService();

    public static void verifyEmail(String token) {
        // Lookup token in the database and find corresponding librarian
        // If the token matches, update the librarian's emailVerified flag to true

        // For now, let's assume the verification is successful
        System.out.println("Email verified successfully! Token: " + token);
        
        // After verifying, you would normally update the librarian's status in the database
        // librarianService.verifyEmail(token);
    }
}
