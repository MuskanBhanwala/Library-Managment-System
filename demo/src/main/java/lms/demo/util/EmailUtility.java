package lms.demo.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailUtility {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String FROM_EMAIL = "anglesid03@gmail.com";  // Replace with your email
    private static final String EMAIL_PASSWORD = "gmpt sbze nrzl tdpf";  // Replace with your email password

    // Method to send the verification email
    public static void sendVerificationEmail(String toEmail, String librarianDetails, String verificationLink) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        });

        try {
        	// Construct the email message
            String subject = "Librarian Registration - Please Verify Your Email";
            String body = "<h1>Welcome to the Library System</h1>" +
                          "<p>Dear " + librarianDetails + ",</p>" +
                          "<p>Your account has been successfully created. Please verify your email to activate your account.</p>" +
                          "<p>Click the link below to verify your email:</p>" +
                          "<a href=\"" + verificationLink + "\">Verify Email</a>";

        	//create the message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            // Set the email content as HTML
            message.setContent(body, "text/html");
            
            // Send the email
            Transport.send(message);
            System.out.println("Verification email sent to: " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error sending verification email.");
        }
    }
}
