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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Date;
import java.util.List;

public class LibraryAppGUI extends JFrame {

    private static final LibrarianService librarianService = new LibrarianService();
    private static final BookService bookService = new BookService();
    private static final TransactionService transactionService = new TransactionService();
    private static final MemberService memberService = new MemberService();

    private static Librarian loggedInLibrarian;

    public LibraryAppGUI() {
    	
        // Set up the frame
        setTitle("Library Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Set window background image
        setContentPane(createBackgroundPanel());

        // Initialize components
        showLoginPanel();
    }

    // Create the background panel with the image
    private JPanel createBackgroundPanel() {
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);  // Call superclass method to ensure proper painting

                // Load the image from the resources folder
                URL imageUrl = getClass().getResource("/img/lms.jpg");

                if (imageUrl != null) {
                    ImageIcon imageIcon = new ImageIcon(imageUrl);
                    Image image = imageIcon.getImage();

                    // Draw the image scaled to fit the size of the panel
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                } else {
                    System.out.println("Error: Image not found.");
                }
            }
        };

        // Set the size of the panel (full window size)
        backgroundPanel.setPreferredSize(new Dimension(800, 600));
        backgroundPanel.setLayout(new BorderLayout());

        return backgroundPanel;
    }

    // Create the login panel
    private JPanel createLoginPanel() {
        // Create a JPanel with a transparent background to overlay on the image
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setOpaque(false);  // Make the login panel transparent to see the background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Add some padding between components
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Add email label and text field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);  // Change label text color to white
        loginPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        JTextField emailField = new JTextField(20);
        emailField.setOpaque(false);  // Make the text field transparent
        emailField.setFont(new Font("Arial", Font.PLAIN, 20)); // Increase font size
        emailField.setForeground(Color.WHITE);
        loginPanel.add(emailField, gbc);

        // Add password label and text field
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);  // Change label text color to white
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setOpaque(false);  // Make the password field transparent
        passwordField.setFont(new Font("Arial", Font.PLAIN, 20)); // Increase font size
        passwordField.setForeground(Color.WHITE);
        loginPanel.add(passwordField, gbc);

        // Add login button
        gbc.gridx = 1;
        gbc.gridy = 2;
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 0, 0, 0));  // Make the button background transparent
        loginButton.setForeground(Color.WHITE);  // Set text color to white
        loginButton.setFont(new Font("Arial", Font.BOLD, 24));  // Increase font size
        loginButton.setFocusPainted(false);  // Remove the focus ring around the button
        loginButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));  // Optional: Add a border to make the button more visible
        loginPanel.add(loginButton, gbc);

        // Add an action listener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                if (librarianLogin(email, password)) {
                    showMainMenu();
                } else {
                    JOptionPane.showMessageDialog(LibraryAppGUI.this, "Invalid login credentials. Try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return loginPanel;
    }

    // Show Login Panel
    private void showLoginPanel() {
        JPanel loginPanel = createLoginPanel();

        // Add the background panel with the login panel on top
        getContentPane().removeAll();
        getContentPane().add(createBackgroundPanel(), BorderLayout.CENTER);
        getContentPane().add(loginPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }


    // Librarian login method
    private boolean librarianLogin(String email, String password) {
        loggedInLibrarian = librarianService.validateLibrarianLogin(email, password);
        if (loggedInLibrarian != null) {
            // Successfully logged in, store the librarian in session
            LibrarianSession.setLoggedInLibrarian(loggedInLibrarian);
            System.out.println("Login successful!");
            return true;
        } else {
            // Invalid credentials
            System.out.println("Invalid login credentials.");
            return false;
        }
        
    }
 // Show main menu after successful login
    private void showMainMenu() {
        JPanel mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new GridBagLayout());  // Use GridBagLayout for better control of layout
        mainMenuPanel.setOpaque(false);  // Make the panel transparent to show the background

        // Define button style
        Font buttonFont = new Font("Arial", Font.BOLD, 18);
        Color buttonBackground = new Color(0, 123, 255);  // A nice blue color for buttons
        Color buttonForeground = Color.WHITE;  // White text color

        // GridBagConstraints to control button placement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Padding between buttons
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;  // Buttons should expand horizontally

        // Create buttons for menu options
        JButton librarianButton = createButton("Librarian Management", buttonFont, buttonBackground, buttonForeground);
        JButton memberButton = createButton("Member Management", buttonFont, buttonBackground, buttonForeground);
        JButton bookButton = createButton("Book Management", buttonFont, buttonBackground, buttonForeground);
        JButton transactionButton = createButton("Transaction Management", buttonFont, buttonBackground, buttonForeground);
        JButton logoutButton = createButton("Logout", buttonFont, buttonBackground, buttonForeground);

        // Add buttons to the panel
        mainMenuPanel.add(librarianButton, gbc);
        gbc.gridy++;  // Move to next row
        mainMenuPanel.add(memberButton, gbc);
        gbc.gridy++;  // Move to next row
        mainMenuPanel.add(bookButton, gbc);
        gbc.gridy++;  // Move to next row
        mainMenuPanel.add(transactionButton, gbc);
        gbc.gridy++;  // Move to next row
        mainMenuPanel.add(logoutButton, gbc);

        // Add action listeners to buttons
        librarianButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLibrarianMenu();
            }
        });

        memberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMemberMenu();
            }
        });

        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBookMenu();
            }
        });

        transactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTransactionMenu();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loggedInLibrarian = null;
                showLoginPanel();
            }
        });

        // Clear existing content and add the new main menu
        getContentPane().removeAll();
        getContentPane().add(mainMenuPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // Create a styled button
    private JButton createButton(String text, Font font, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);  // Remove focus outline
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));  // Add border to make button visible
        button.setPreferredSize(new Dimension(300, 50));  // Set preferred size for buttons
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));  // Change cursor to hand on hover

        // Add mouse listeners for hover effect (optional)
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 153, 255));  // Change color on hover
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 123, 255));  // Reset color when mouse exits
            }
        });

        return button;
    }
    
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 24));  // Larger font size for readability
        button.setBackground(new Color(0, 123, 255));  // Blue background
        button.setForeground(Color.WHITE);  // White text
        button.setFocusPainted(false);  // Remove the focus ring
        button.setPreferredSize(new Dimension(200, 50));  // Set button size
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));  // Change cursor to hand on hover

        // Add a hover effect (optional)
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 153, 255));  // Lighter blue on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0, 123, 255));  // Original blue color
            }
        });
    }

    // Show librarian management menu
    private void showLibrarianMenu() {
    	JPanel librarianMenuPanel = new JPanel();
    	librarianMenuPanel.setLayout(new GridBagLayout());  // Use GridBagLayout for better control of layout
    	librarianMenuPanel.setOpaque(false);  // Make the panel transparent to show the background

        // Define button style
        Font buttonFont = new Font("Arial", Font.BOLD, 18);
        Color buttonBackground = new Color(0, 123, 255);  // A nice blue color for buttons
        Color buttonForeground = Color.WHITE;  // White text color
        

        // GridBagConstraints to control button placement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Padding between buttons
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;  // Buttons should expand horizontally


		JButton addLibrarianButton = createButton("Add Librarian",  buttonFont, buttonBackground, buttonForeground);
        JButton viewLibrariansButton = createButton("View Librarians",  buttonFont, buttonBackground, buttonForeground);
        JButton returnButton = createButton("Return",buttonFont, buttonBackground, buttonForeground);
        // Add buttons to the panel
        librarianMenuPanel.add(addLibrarianButton, gbc);
        gbc.gridy++;  // Move to next row
        librarianMenuPanel.add(viewLibrariansButton, gbc);
        gbc.gridy++;  // Move to next row
        librarianMenuPanel.add(returnButton, gbc);
        gbc.gridy++;  // Move to next row
        
        addLibrarianButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addLibrarian();
            }
        });

        viewLibrariansButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewLibrarians();
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loggedInLibrarian = null;
                showMainMenu();
            }
        });

        getContentPane().removeAll();
        getContentPane().add(librarianMenuPanel);
        revalidate();
        repaint();
    }
    //add librarian
    private void addLibrarian() {
        // Create panel with GridBagLayout for better control over component placement
        JPanel addLibrarianPanel = new JPanel();
        addLibrarianPanel.setLayout(new GridBagLayout());
        addLibrarianPanel.setOpaque(false);  // Make the background transparent

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Add some padding between components

        Font inputFont = new Font("Arial", Font.PLAIN, 20);
        JTextField nameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JTextField userTypeField = new JTextField(20);

        // Create styled button for adding librarian
        JButton addButton = new JButton("Add Librarian");
        JButton backButton = new JButton("BACK");
        styleButton(addButton);
        styleButton(backButton);

        // Add Name label and text field
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);  // White label text
        addLibrarianPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        nameField.setOpaque(false);  // Make text field transparent
        nameField.setFont(inputFont);
        nameField.setForeground(Color.WHITE);
        addLibrarianPanel.add(nameField, gbc);

        // Add Email label and text field
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);  // White label text
        addLibrarianPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        emailField.setOpaque(false);  // Make text field transparent
        emailField.setFont(inputFont);
        emailField.setForeground(Color.WHITE);
        addLibrarianPanel.add(emailField, gbc);

        // Add Password label and password field
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);  // White label text
        addLibrarianPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        passwordField.setOpaque(false);  // Make password field transparent
        passwordField.setFont(inputFont);
        passwordField.setForeground(Color.WHITE);
        addLibrarianPanel.add(passwordField, gbc);

        // Add User Type label and text field
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel userTypeLabel = new JLabel("User Type (admin/member):");
        userTypeLabel.setForeground(Color.WHITE);  // White label text
        addLibrarianPanel.add(userTypeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        userTypeField.setOpaque(false);  // Make text field transparent
        userTypeField.setFont(inputFont);
        userTypeField.setForeground(Color.WHITE);
        addLibrarianPanel.add(userTypeField, gbc);

        // Add the Add Button
        gbc.gridx = 1;
        gbc.gridy = 4;
        addLibrarianPanel.add(addButton, gbc);

        // Add BACK button
        gbc.gridx = 1;
        gbc.gridy = 5;
        addLibrarianPanel.add(backButton, gbc);

        // Action listener for add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String userType = userTypeField.getText();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || userType.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields must be filled!");
                    return;
                }

                // Create new Librarian object
                Librarian newLibrarian = new Librarian();
                newLibrarian.setName(name);
                newLibrarian.setEmail(email);
                newLibrarian.setPassword(password);
                newLibrarian.setUserType(userType);

                // Add librarian to the system
                librarianService.addLibrarian(newLibrarian);
                

                // Assuming that the service method assigns a generated librarianId to the newLibrarian object
                // Show the generated librarianId in a message dialog
                JOptionPane.showMessageDialog(null, "Librarian added successfully! Librarian ID: " + newLibrarian.getLibrarianId());

                // Return to the librarian menu
                showLibrarianMenu(); 
            }
        });

        // Action listener for BACK button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLibrarianMenu(); // Go back to librarian menu
            }
        });

        // Set the panel into the frame
        getContentPane().removeAll();
        getContentPane().add(addLibrarianPanel);
        revalidate();
        repaint();
    }

    //view librarian
    private void viewLibrarians() {
        List<Librarian> librarians = librarianService.getAllLibrarians();
        
        if (librarians.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No librarians found.");
        } else {
            // Table column names
            String[] columnNames = {"Name", "Email", "User Type"};

            // Prepare table data from librarians list
            Object[][] data = new Object[librarians.size()][3];

            for (int i = 0; i < librarians.size(); i++) {
                Librarian librarian = librarians.get(i);
                data[i][0] = librarian.getName();
                data[i][1] = librarian.getEmail();
                data[i][2] = librarian.getUserType();
            }

            // Create JTable with custom styling
            JTable librarianTable = new JTable(data, columnNames);
            librarianTable.setFillsViewportHeight(true);
            librarianTable.setRowHeight(30);  // Increase row height for better readability
            librarianTable.setFont(new Font("Arial", Font.PLAIN, 16));
            librarianTable.setForeground(Color.WHITE);  // Text color
            librarianTable.setBackground(new Color(40, 40, 40));  // Dark background
            librarianTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
            librarianTable.getTableHeader().setBackground(new Color(0, 123, 255));  // Blue header background
            librarianTable.getTableHeader().setForeground(Color.WHITE);  // Header text color

            // Create a scroll pane to wrap the table
            JScrollPane scrollPane = new JScrollPane(librarianTable);

            // Panel for the view librarians screen
            JPanel viewLibrariansPanel = new JPanel();
            viewLibrariansPanel.setLayout(new BorderLayout());
            viewLibrariansPanel.setOpaque(false);  // Transparent background

            // Add the table inside the scroll pane to the panel
            viewLibrariansPanel.add(scrollPane, BorderLayout.CENTER);

            // Create and style the BACK button
            JButton backButton = new JButton("BACK");
            styleButton(backButton);  // Assuming `styleButton()` is defined elsewhere
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showLibrarianMenu();  // Go back to librarian menu
                }
            });

            // Panel for the BACK button at the bottom of the table
            JPanel backPanel = new JPanel();
            backPanel.setOpaque(false);  // Transparent background
            backPanel.add(backButton);

            // Add the BACK panel to the bottom of the viewLibrariansPanel
            viewLibrariansPanel.add(backPanel, BorderLayout.SOUTH);

            // Set the panel into the frame
            getContentPane().removeAll();
            getContentPane().add(viewLibrariansPanel);
            revalidate();
            repaint();
        }
    }


    // Show member management menu
    private void showMemberMenu() {
    	JPanel memberMenuPanel = new JPanel();
    	memberMenuPanel.setLayout(new GridBagLayout());  // Use GridBagLayout for better control of layout
    	memberMenuPanel.setOpaque(false);  // Make the panel transparent to show the background
    	
    	// Define button style
        Font buttonFont = new Font("Arial", Font.BOLD, 18);
        Color buttonBackground = new Color(0, 123, 255);  // A nice blue color for buttons
        Color buttonForeground = Color.WHITE;  // White text color
        

        // GridBagConstraints to control button placement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Padding between buttons
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;  // Buttons should expand horizontally

        JButton addMemberButton = createButton("Add Member",buttonFont, buttonBackground, buttonForeground);
        JButton viewMembersButton = createButton("View Members",buttonFont, buttonBackground, buttonForeground);
        JButton returnButton = createButton("Return",buttonFont, buttonBackground, buttonForeground);
        // Add buttons to the panel
        memberMenuPanel.add(addMemberButton, gbc);
        gbc.gridy++;  // Move to next row
        memberMenuPanel.add(viewMembersButton, gbc);
        gbc.gridy++;  // Move to next row
        memberMenuPanel.add(returnButton, gbc);
        gbc.gridy++;  // Move to next row
        
        addMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMember();
            }
        });

        viewMembersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewMembers();
            }
        });
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loggedInLibrarian = null;
                showMainMenu();
            }
        });

        getContentPane().removeAll();
        getContentPane().add(memberMenuPanel);
        revalidate();
        repaint();
    }
    private void addMember() {
        // Create panel with GridBagLayout for better control over component placement
        JPanel addMemberPanel = new JPanel();
        addMemberPanel.setLayout(new GridBagLayout());
        addMemberPanel.setOpaque(false);  // Make the panel background transparent

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Add some padding between components

        Font inputFont = new Font("Arial", Font.PLAIN, 20);
        JTextField nameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField phoneField = new JTextField(20);

        // Create and style the Add Member button
        JButton addButton = new JButton("Add Member");
        JButton backButton = new JButton("BACK");
        styleButton(addButton);
        styleButton(backButton);

        // Add Name label and text field
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);  // White label text
        addMemberPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        nameField.setOpaque(false);  // Make text field transparent
        nameField.setFont(inputFont);
        nameField.setForeground(Color.WHITE);
        addMemberPanel.add(nameField, gbc);

        // Add Email label and text field
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);  // White label text
        addMemberPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        emailField.setOpaque(false);  // Make text field transparent
        emailField.setFont(inputFont);
        emailField.setForeground(Color.WHITE);
        addMemberPanel.add(emailField, gbc);

        // Add Phone label and text field
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setForeground(Color.WHITE);  // White label text
        addMemberPanel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        phoneField.setOpaque(false);  // Make text field transparent
        phoneField.setFont(inputFont);
        phoneField.setForeground(Color.WHITE);
        addMemberPanel.add(phoneField, gbc);

        // Add Add Member button
        gbc.gridx = 1;
        gbc.gridy = 3;
        addMemberPanel.add(addButton, gbc);

        // Add BACK button
        gbc.gridx = 1;
        gbc.gridy = 4;
        addMemberPanel.add(backButton, gbc);

        // Action listener for add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();

                if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields must be filled!");
                    return;
                }

                // Create new Member object
                Member newMember = new Member(name, email, phone);

                // Add the member to the system
                memberService.addMember(newMember);

                // Assuming that memberService assigns a generated memberId to newMember
                // Show the generated memberId in a message dialog
                JOptionPane.showMessageDialog(null, "Member added successfully! Member ID: " + newMember.getMemberId());

                // Return to the member menu
                showMemberMenu(); 
            }
        });

        // Action listener for back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMemberMenu(); // Go back to member menu
            }
        });

        // Set the panel into the frame
        getContentPane().removeAll();
        getContentPane().add(addMemberPanel);
        revalidate();
        repaint();
    }

    //view member
    private void viewMembers() {
        List<Member> members = memberService.getAllMembers();
        if (members.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No members found.");
        } else {
            // Create table data from members list
            String[] columnNames = {"Name", "Email", "Phone"};
            Object[][] data = new Object[members.size()][3];

            for (int i = 0; i < members.size(); i++) {
                Member member = members.get(i);
                data[i][0] = member.getName();
                data[i][1] = member.getEmail();
                data[i][2] = member.getPhone();
            }

            // Create a JTable to display the members in a tabular format
            JTable memberTable = new JTable(data, columnNames);
            memberTable.setFillsViewportHeight(true);
            memberTable.setRowHeight(30);  // Increase row height for readability

            // Customize the table appearance
            memberTable.setFont(new Font("Arial", Font.PLAIN, 16));
            memberTable.setForeground(Color.WHITE);
            memberTable.setBackground(new Color(40, 40, 40));  // Dark background for the table
            memberTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
            memberTable.getTableHeader().setBackground(new Color(0, 123, 255));  // Blue header

            // Add table inside a JScrollPane
            JScrollPane scrollPane = new JScrollPane(memberTable);

            // Create a panel for the view members screen
            JPanel viewMembersPanel = new JPanel();
            viewMembersPanel.setLayout(new BorderLayout());
            viewMembersPanel.setOpaque(false);  // Transparent background
            viewMembersPanel.add(scrollPane, BorderLayout.CENTER);

            // Add a return button to go back to the member menu
            JButton backButton = new JButton("BACK");
            styleButton(backButton);
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showMemberMenu();  // Go back to member menu
                }
            });

            // Add BACK button to bottom of panel
            JPanel returnPanel = new JPanel();
            returnPanel.setOpaque(false);  // Transparent background
            returnPanel.add(backButton);

            viewMembersPanel.add(returnPanel, BorderLayout.SOUTH);

            // Set the panel into the frame
            getContentPane().removeAll();
            getContentPane().add(viewMembersPanel);
            revalidate();
            repaint();
        }
    }


    // Show book management menu
    private void showBookMenu() {
    	JPanel bookMenuPanel = new JPanel();
        bookMenuPanel.setLayout(new GridBagLayout());  // Use GridBagLayout for better control of layout
        bookMenuPanel.setOpaque(false);  // Make the panel transparent to show the background

        // Define button style
        Font buttonFont = new Font("Arial", Font.BOLD, 18);
        Color buttonBackground = new Color(0, 123, 255);  // A nice blue color for buttons
        Color buttonForeground = Color.WHITE;  // White text color
        // GridBagConstraints to control button placement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Padding between buttons
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;  // Buttons should expand horizontally

        JButton addBookButton = createButton("Add Book",buttonFont, buttonBackground, buttonForeground);
        JButton viewBooksButton = createButton("View Books",buttonFont, buttonBackground, buttonForeground);
        JButton updateBookButton = createButton("Update Book",buttonFont, buttonBackground, buttonForeground);
        JButton deleteBookButton = createButton("Delete Book",buttonFont, buttonBackground, buttonForeground);
        JButton returnButton = createButton("Return",buttonFont, buttonBackground, buttonForeground);

     // Add buttons to the panel
        bookMenuPanel.add(addBookButton, gbc);
        gbc.gridy++;  // Move to next row
        bookMenuPanel.add(viewBooksButton, gbc);
        gbc.gridy++;  // Move to next row
        bookMenuPanel.add(updateBookButton, gbc);
        gbc.gridy++;  // Move to next row
        bookMenuPanel.add(deleteBookButton, gbc);
        gbc.gridy++;  // Move to next row
        bookMenuPanel.add(returnButton, gbc);
        gbc.gridy++;  // Move to next row
        
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });

        viewBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewBooks();
            }
        });

        updateBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBook();
            }
        });

        deleteBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBook();
            }
        });
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMainMenu();
            }
        });

        getContentPane().removeAll();
        getContentPane().add(bookMenuPanel);
        revalidate();
        repaint();
    }

    private void addBook() {
        JPanel addBookPanel = new JPanel();
        addBookPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for better control over placement
        addBookPanel.setOpaque(false);  // Make the background transparent

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Add some padding between components

        JTextField titleField = new JTextField(20);
        JTextField authorField = new JTextField(20);
        JTextField priceField = new JTextField(20);
        JTextField quantityField = new JTextField(20);

        // Style the Add Book button
        JButton addButton = new JButton("Add Book");
        JButton backButton = new JButton("BACK");
        styleButton(addButton);
        styleButton(backButton);

        // Add Title label and text field
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setForeground(Color.WHITE);
        addBookPanel.add(titleLabel, gbc);

        gbc.gridx = 1;
        titleField.setOpaque(false);
        titleField.setFont(new Font("Arial", Font.PLAIN, 20));
        titleField.setForeground(Color.WHITE);
        addBookPanel.add(titleField, gbc);

        // Add Author label and text field
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setForeground(Color.WHITE);
        addBookPanel.add(authorLabel, gbc);

        gbc.gridx = 1;
        authorField.setOpaque(false);
        authorField.setFont(new Font("Arial", Font.PLAIN, 20));
        authorField.setForeground(Color.WHITE);
        addBookPanel.add(authorField, gbc);

        // Add Price label and text field
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setForeground(Color.WHITE);
        addBookPanel.add(priceLabel, gbc);

        gbc.gridx = 1;
        priceField.setOpaque(false);
        priceField.setFont(new Font("Arial", Font.PLAIN, 20));
        priceField.setForeground(Color.WHITE);
        addBookPanel.add(priceField, gbc);

        // Add Quantity label and text field
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setForeground(Color.WHITE);
        addBookPanel.add(quantityLabel, gbc);

        gbc.gridx = 1;
        quantityField.setOpaque(false);
        quantityField.setFont(new Font("Arial", Font.PLAIN, 20));
        quantityField.setForeground(Color.WHITE);
        addBookPanel.add(quantityField, gbc);

        // Add Add Book button
        gbc.gridx = 1;
        gbc.gridy = 4;
        addBookPanel.add(addButton, gbc);

        // Add BACK button
        gbc.gridx = 1;
        gbc.gridy = 5;
        addBookPanel.add(backButton, gbc);

        // Action listener for add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String title = titleField.getText();
                    String author = authorField.getText();
                    String priceStr = priceField.getText();
                    String quantityStr = quantityField.getText();

                    if (title.isEmpty() || author.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "All fields must be filled!");
                        return;
                    }

                    // Parse price and quantity
                    int price = Integer.parseInt(priceStr);
                    int quantity = Integer.parseInt(quantityStr);

                    // Create new book object
                    Book newBook = new Book(title, author, price, quantity);

                    // Add the book to the system (bookService will handle the bookId generation)
                    bookService.addBook(newBook);

                    // Show message box with generated bookId after successful addition
                    JOptionPane.showMessageDialog(null, "Book added successfully! Book ID: " + newBook.getBookId());

                    // Return to the book menu
                    showBookMenu(); // Go back to book menu
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Price and Quantity must be valid numbers.");
                }
            }
        });

        // Action listener for back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBookMenu(); // Return to book menu
            }
        });

        // Set up the panel
        getContentPane().removeAll();
        getContentPane().add(addBookPanel);
        revalidate();
        repaint();
    }

    // Delete a book
    private void deleteBook() {
        JPanel deleteBookPanel = new JPanel();
        deleteBookPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for flexibility
        deleteBookPanel.setOpaque(false);  // Transparent background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Add some padding between components

        JTextField bookIdField = new JTextField(20);
        JButton deleteButton = new JButton("Delete Book");
        JButton backButton = new JButton("BACK");
        styleButton(deleteButton);
        styleButton(backButton);

        // Add Book ID label and text field
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel bookIdLabel = new JLabel("Book ID:");
        bookIdLabel.setForeground(Color.WHITE); 
        deleteBookPanel.add(bookIdLabel, gbc);

        gbc.gridx = 1;
        bookIdField.setOpaque(false);
        bookIdField.setFont(new Font("Arial", Font.PLAIN, 20));
        bookIdField.setForeground(Color.WHITE);
        deleteBookPanel.add(bookIdField, gbc);

        // Add Delete button
        gbc.gridx = 1;
        gbc.gridy = 1;
        deleteBookPanel.add(deleteButton, gbc);

        // Add BACK button
        gbc.gridx = 1;
        gbc.gridy = 2;
        deleteBookPanel.add(backButton, gbc);

        // Action listener for delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int bookId = Integer.parseInt(bookIdField.getText());
                    Book book = bookService.getBookById(bookId);

                    if (book != null) {
                        bookService.deleteBook(bookId);
                        JOptionPane.showMessageDialog(null, "Book deleted successfully.");
                        showBookMenu(); // Go back to book menu
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid book ID.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid book ID.");
                }
            }
        });

        // Action listener for back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBookMenu(); // Return to book menu
            }
        });

        getContentPane().removeAll();
        getContentPane().add(deleteBookPanel);
        revalidate();
        repaint();
    }

    //update book
    private void updateBook() {
        JPanel updateBookPanel = new JPanel();
        updateBookPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for flexibility
        updateBookPanel.setOpaque(false);  // Transparent background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Add some padding between components

        JTextField bookIdField = new JTextField(20);
        JTextField titleField = new JTextField(20);
        JTextField authorField = new JTextField(20);
        JTextField priceField = new JTextField(20);
        JTextField quantityField = new JTextField(20);
        JButton updateButton = new JButton("Update Book");
        JButton backButton = new JButton("BACK");
        styleButton(updateButton);
        styleButton(backButton);

        // Add Book ID label and text field
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel bookIdLabel = new JLabel("Book ID:");
        bookIdLabel.setForeground(Color.WHITE);
        updateBookPanel.add(bookIdLabel, gbc);

        gbc.gridx = 1;
        bookIdField.setOpaque(false);
        bookIdField.setFont(new Font("Arial", Font.PLAIN, 20));
        bookIdField.setForeground(Color.WHITE);
        updateBookPanel.add(bookIdField, gbc);

        // Add New Title label and text field
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel titleLabel = new JLabel("New Title:");
        titleLabel.setForeground(Color.WHITE);
        updateBookPanel.add(titleLabel, gbc);

        gbc.gridx = 1;
        titleField.setOpaque(false);
        titleField.setFont(new Font("Arial", Font.PLAIN, 20));
        titleField.setForeground(Color.WHITE);
        updateBookPanel.add(titleField, gbc);

        // Add New Author label and text field
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel authorLabel = new JLabel("New Author:");
        authorLabel.setForeground(Color.WHITE);
        updateBookPanel.add(authorLabel, gbc);

        gbc.gridx = 1;
        authorField.setOpaque(false);
        authorField.setFont(new Font("Arial", Font.PLAIN, 20));
        authorField.setForeground(Color.WHITE);
        updateBookPanel.add(authorField, gbc);

        // Add New Price label and text field
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel priceLabel = new JLabel("New Price:");
        priceLabel.setForeground(Color.WHITE);
        updateBookPanel.add(priceLabel, gbc);

        gbc.gridx = 1;
        priceField.setOpaque(false);
        priceField.setFont(new Font("Arial", Font.PLAIN, 20));
        priceField.setForeground(Color.WHITE);
        updateBookPanel.add(priceField, gbc);

        // Add New Quantity label and text field
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel quantityLabel = new JLabel("New Quantity:");
        quantityLabel.setForeground(Color.WHITE);
        updateBookPanel.add(quantityLabel, gbc);

        gbc.gridx = 1;
        quantityField.setOpaque(false);
        quantityField.setFont(new Font("Arial", Font.PLAIN, 20));
        quantityField.setForeground(Color.WHITE);
        updateBookPanel.add(quantityField, gbc);

        // Add Update button
        gbc.gridx = 1;
        gbc.gridy = 5;
        updateBookPanel.add(updateButton, gbc);

        // Add BACK button
        gbc.gridx = 1;
        gbc.gridy = 6;
        updateBookPanel.add(backButton, gbc);

        // Action listener for update button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int bookId = Integer.parseInt(bookIdField.getText());
                    Book book = bookService.getBookById(bookId);

                    if (book != null) {
                        String title = titleField.getText();
                        String author = authorField.getText();
                        String priceStr = priceField.getText();
                        String quantityStr = quantityField.getText();

                        // Update book details if provided
                        if (!title.isEmpty()) {
                            book.setTitle(title);
                        }
                        if (!author.isEmpty()) {
                            book.setAuthor(author);
                        }
                        if (!priceStr.isEmpty()) {
                            book.setPrice(Integer.parseInt(priceStr));
                        }
                        if (!quantityStr.isEmpty()) {
                            book.setQuantity(Integer.parseInt(quantityStr));
                        }

                        bookService.updateBook(book);
                        JOptionPane.showMessageDialog(null, "Book updated successfully.");
                        showBookMenu(); // Return to book menu
                    } else {
                        JOptionPane.showMessageDialog(null, "Book not found with ID: " + bookId);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid number format for Price and Quantity.");
                }
            }
        });

        // Action listener for back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBookMenu(); // Return to book menu
            }
        });

        getContentPane().removeAll();
        getContentPane().add(updateBookPanel);
        revalidate();
        repaint();
    }

    private void viewBooks() {
        List<Book> books = bookService.getAllBooks();
        
        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No books found.");
        } else {
            // Define column names for the book table
            String[] columnNames = {"Book ID", "Title", "Author", "Price", "Quantity"};
            
            // Prepare table data
            Object[][] data = new Object[books.size()][5];
            for (int i = 0; i < books.size(); i++) {
                Book book = books.get(i);
                data[i][0] = book.getBookId();
                data[i][1] = book.getTitle();
                data[i][2] = book.getAuthor();
                data[i][3] = book.getPrice();
                data[i][4] = book.getQuantity();
            }
            
            // Create JTable with custom styling
            JTable bookTable = new JTable(data, columnNames);
            bookTable.setFillsViewportHeight(true);
            bookTable.setRowHeight(30);  // Increase row height for readability
            bookTable.setFont(new Font("Arial", Font.PLAIN, 16));  // Table font
            bookTable.setForeground(Color.WHITE);  // Text color
            bookTable.setBackground(new Color(40, 40, 40));  // Dark background for the table
            
            // Style for the table header
            bookTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));  // Bold header font
            bookTable.getTableHeader().setBackground(new Color(0, 123, 255));  // Blue background for the header
            bookTable.getTableHeader().setForeground(Color.WHITE);  // Header text color
            
            // Add table to JScrollPane for scrolling capability
            JScrollPane scrollPane = new JScrollPane(bookTable);
            scrollPane.setPreferredSize(new Dimension(600, 300));  // Set preferred size for the scroll pane
            
            // Create a panel for the view books screen
            JPanel viewBooksPanel = new JPanel();
            viewBooksPanel.setLayout(new BorderLayout());
            viewBooksPanel.setOpaque(false);  // Transparent background
            
            // Add the scrollPane containing the table to the center of the panel
            viewBooksPanel.add(scrollPane, BorderLayout.CENTER);

            // Create and style the BACK button
            JButton backButton = new JButton("BACK");
            styleButton(backButton);  // Assuming the `styleButton()` method is defined elsewhere
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showBookMenu();  // Return to the book menu when the BACK button is pressed
                }
            });
            
            // Create a panel for the BACK button
            JPanel backPanel = new JPanel();
            backPanel.setOpaque(false);  // Transparent background for the panel
            backPanel.add(backButton);   // Add BACK button to the panel
            
            // Add the BACK panel at the bottom of the viewBooksPanel
            viewBooksPanel.add(backPanel, BorderLayout.SOUTH);

            // Set the panel into the frame
            getContentPane().removeAll();
            getContentPane().add(viewBooksPanel);
            revalidate();
            repaint();
        }
    }

    // Show transaction management menu
    private void showTransactionMenu() {
    	JPanel transactionMenuPanel = new JPanel();
        transactionMenuPanel.setLayout(new GridBagLayout());  // Use GridBagLayout for better control of layout
        transactionMenuPanel.setOpaque(false);  // Make the panel transparent to show the background
        // Define button style
        Font buttonFont = new Font("Arial", Font.BOLD, 18);
        Color buttonBackground = new Color(0, 123, 255);  // A nice blue color for buttons
        Color buttonForeground = Color.WHITE;  // White text color
        
        // GridBagConstraints to control button placement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Padding between buttons
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;  // Buttons should expand horizontally

        JButton issueBookButton = createButton("Issue Book",buttonFont, buttonBackground, buttonForeground);
        JButton returnBookButton = createButton("Return Book",buttonFont, buttonBackground, buttonForeground);
        JButton viewTransactionsButton = createButton("View Transactions",buttonFont, buttonBackground, buttonForeground);
        JButton returnButton = createButton("Return",buttonFont, buttonBackground, buttonForeground);
        // Add buttons to the panel
        transactionMenuPanel.add(issueBookButton, gbc);
        gbc.gridy++;  // Move to next row
        transactionMenuPanel.add(returnBookButton, gbc);
        gbc.gridy++;  // Move to next row
        transactionMenuPanel.add(viewTransactionsButton, gbc);
        gbc.gridy++;  // Move to next row
        transactionMenuPanel.add(returnButton, gbc);
        gbc.gridy++;  // Move to next row
        
        
        issueBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                issueBook();
            }
        });

        returnBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnBook();
            }
        });

        viewTransactionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewTransactions();
            }
        });
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
                showMainMenu();
            }
        });

        getContentPane().removeAll();
        getContentPane().add(transactionMenuPanel);
        revalidate();
        repaint();
    }

 // Issue a book
    private void issueBook() {
        JPanel issueBookPanel = new JPanel();
        issueBookPanel.setLayout(new GridBagLayout());  // Use GridBagLayout for flexibility
        issueBookPanel.setOpaque(false);  // Transparent background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Padding between components

        JTextField memberIdField = new JTextField(20);
        JTextField bookIdField = new JTextField(20);
        JButton issueButton = new JButton("Issue Book");
        JButton backButton = new JButton("BACK");
        styleButton(issueButton);  // Use the styleButton method for consistent button styling
        styleButton(backButton);  // Style the BACK button as well

        // Add Member ID label and text field
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel memberIdLabel = new JLabel("Member ID:");
        memberIdLabel.setForeground(Color.WHITE);  // White label text
        issueBookPanel.add(memberIdLabel, gbc);

        gbc.gridx = 1;
        memberIdField.setOpaque(false);  // Transparent background
        memberIdField.setFont(new Font("Arial", Font.PLAIN, 20));
        memberIdField.setForeground(Color.WHITE);
        issueBookPanel.add(memberIdField, gbc);

        // Add Book ID label and text field
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel bookIdLabel = new JLabel("Book ID:");
        bookIdLabel.setForeground(Color.WHITE);  // White label text
        issueBookPanel.add(bookIdLabel, gbc);

        gbc.gridx = 1;
        bookIdField.setOpaque(false);  // Transparent background
        bookIdField.setFont(new Font("Arial", Font.PLAIN, 20));
        bookIdField.setForeground(Color.WHITE);
        issueBookPanel.add(bookIdField, gbc);

        // Add Issue Book button
        gbc.gridx = 1;
        gbc.gridy = 2;
        issueBookPanel.add(issueButton, gbc);

        // Add BACK button at the bottom
        gbc.gridx = 1;
        gbc.gridy = 3;
        issueBookPanel.add(backButton, gbc);  // Add back button to the panel

        // Action listener for issue button
        issueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int memberId = Integer.parseInt(memberIdField.getText());
                    int bookId = Integer.parseInt(bookIdField.getText());
                    Member member = memberService.getMemberById(memberId);
                    Book book = bookService.getBookById(bookId);
                    Librarian librarian = LibrarianSession.getLoggedInLibrarian();
                    Date issueDate = new Date();
                    
                    Transaction transaction = new Transaction(book, member, librarian, issueDate, "Issued");
                    transactionService.addTransaction(transaction);

                    // Get the transaction ID from the transaction object
                    int transactionId = transaction.getId();
                    
                    // Show a message dialog with the transaction ID
                    JOptionPane.showMessageDialog(null, 
                        "Book issued successfully. Transaction ID: " + transactionId);
                    
                    showTransactionMenu(); // Go back to transaction menu
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Member ID and Book ID must be valid integers.");
                }
            }
        });

        // Action listener for back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTransactionMenu(); // Go back to the main menu
            }
        });

        getContentPane().removeAll();  // Remove existing content
        getContentPane().add(issueBookPanel);  // Add the panel with all components
        revalidate();
        repaint();
    }



 // Return a book
    private void returnBook() {
        JPanel returnBookPanel = new JPanel();
        returnBookPanel.setLayout(new GridBagLayout());  // Use GridBagLayout for flexibility
        returnBookPanel.setOpaque(false);  // Transparent background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Padding between components

        JTextField transactionIdField = new JTextField(20);
        JButton returnButton = new JButton("Return Book");
        JButton backButton = new JButton("BACK");
        styleButton(returnButton);  // Use the styleButton method for consistent button styling
        styleButton(backButton);

        // Add Transaction ID label and text field
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel transactionIdLabel = new JLabel("Transaction ID:");
        transactionIdLabel.setForeground(Color.WHITE);  // White label text
        returnBookPanel.add(transactionIdLabel, gbc);

        gbc.gridx = 1;
        transactionIdField.setOpaque(false);  // Transparent background
        transactionIdField.setFont(new Font("Arial", Font.PLAIN, 20));
        transactionIdField.setForeground(Color.WHITE);
        returnBookPanel.add(transactionIdField, gbc);

        // Add Return Book button
        gbc.gridx = 1;
        gbc.gridy = 1;
        returnBookPanel.add(returnButton, gbc);

        // Add BACK button in a separate grid cell to avoid overlap
        gbc.gridx = 1;
        gbc.gridy = 2;  // Place the BACK button below the Return Book button
        returnBookPanel.add(backButton, gbc);

        // Action listener for return button
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int transactionId = Integer.parseInt(transactionIdField.getText());
                    transactionService.returnBook(transactionId);
                    JOptionPane.showMessageDialog(null, "Book returned successfully.");
                    showTransactionMenu(); // Go back to transaction menu
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Transaction ID must be a valid integer.");
                }
            }
        });

        // Action listener for back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTransactionMenu(); // Go back to transaction menu
            }
        });

        getContentPane().removeAll();
        getContentPane().add(returnBookPanel);  // Add the panel with all components
        revalidate();
        repaint();
    }

    private void viewTransactions() {
        List<Transaction> transactions = transactionService.viewTransactions();

        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No transactions found.");
        } else {
            // Column names for the transactions table
            String[] columnNames = {"Transaction ID", "Book Title", "Member Name", "Librarian Name", "Issue Date", "Status"};
            
            // Data array to hold the transaction data
            Object[][] data = new Object[transactions.size()][6];
            
            // Fill the data array with transaction details
            for (int i = 0; i < transactions.size(); i++) {
                Transaction transaction = transactions.get(i);
                data[i][0] = transaction.getId();
                data[i][1] = transaction.getBook().getTitle();
                data[i][2] = transaction.getMember().getName();
                data[i][3] = transaction.getLibrarian().getName();
                data[i][4] = transaction.getIssueDate().toString(); // You can format the date as needed
                data[i][5] = transaction.getStatus();
            }

            // Create JTable with the data and column names
            JTable table = new JTable(data, columnNames);
            table.setFillsViewportHeight(true);  // Allow table to fill the height of the viewport
            table.setRowHeight(30);  // Set row height for better readability
            table.setFont(new Font("Arial", Font.PLAIN, 16));  // Set table font
            table.setForeground(Color.WHITE);  // Set table text color
            table.setBackground(new Color(40, 40, 40));  // Dark background for the table

            // Style for the table header
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));  // Set bold header font
            table.getTableHeader().setBackground(new Color(0, 123, 255));  // Set blue background for header
            table.getTableHeader().setForeground(Color.WHITE);  // Set white header text color
            
            // Wrap the table inside a JScrollPane
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(800, 300));  // Set preferred size for scroll pane

            // Create a panel to hold the table
            JPanel tablePanel = new JPanel();
            tablePanel.setLayout(new BorderLayout());
            tablePanel.setOpaque(false);  // Transparent background for the panel
            tablePanel.add(scrollPane, BorderLayout.CENTER);  // Add scroll pane containing the table to the center

            // Create and style the BACK button
            JButton returnButton = new JButton("BACK");
            styleButton(returnButton);  // Apply consistent button styling

            // Create a panel for the BACK button and add it
            JPanel buttonPanel = new JPanel();
            buttonPanel.setOpaque(false);  // Transparent background for button panel
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));  // Center the button in the panel
            buttonPanel.add(returnButton);  // Add BACK button to the panel

            // Set up the main layout for the content pane
            getContentPane().removeAll();  // Remove any existing content
            getContentPane().setLayout(new BorderLayout());  // Use BorderLayout for the content pane
            getContentPane().add(tablePanel, BorderLayout.CENTER);  // Add the table panel to the center
            getContentPane().add(buttonPanel, BorderLayout.SOUTH);  // Add the button panel to the bottom

            // Action listener for the BACK button
            returnButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    loggedInLibrarian = null;  // Clear the logged-in librarian information
                    showTransactionMenu();  // Go back to the transaction menu
                }
            });

            // Revalidate and repaint to update the frame content
            revalidate();
            repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LibraryAppGUI().setVisible(true);
            }
        });

    }
}

