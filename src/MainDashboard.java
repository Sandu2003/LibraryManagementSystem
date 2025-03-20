import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainDashboard {
    public static void main(String[] args) {
        // Show the login screen first
        SwingUtilities.invokeLater(() -> {
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.showLoginScreen();
        });
    }

    // Handle login
    public static void loginSuccessful(JFrame frame) {
        // Create the main frame after successful login
        frame.dispose();  // Close the login frame

        JFrame mainFrame = new JFrame("Library Management System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 500);

        // Set a dark background
        mainFrame.getContentPane().setBackground(new Color(30, 30, 30));

        // Create a title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(45, 45, 45));
        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 255, 255));
        titlePanel.add(titleLabel);

        // Create a panel for buttons (Navigation Bar)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 3, 15, 15)); 
        buttonPanel.setBackground(new Color(30, 30, 30));

        // Add buttons with custom styling
        String[] buttons = {
            "Add Book", "View Books", "Update/Delete Book",
            "Add Member", "View Members", "Add transaction","Update/Delete Member",
            "Issue Book", "Return Book", "Transaction History",
            "Summary Report", "Exit"
        };

        for (String btnText : buttons) {
            JButton button = new JButton(btnText);
            button.setFont(new Font("SansSerif", Font.PLAIN, 16));
            button.setBackground(new Color(70, 130, 180)); 
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 2)); 
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            buttonPanel.add(button);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleButtonAction(e.getActionCommand(), mainFrame);
                }
            });
        }

        // Add panels to the frame
        mainFrame.setLayout(new BorderLayout(10, 10)); 
        mainFrame.add(titlePanel, BorderLayout.NORTH);
        mainFrame.add(buttonPanel, BorderLayout.CENTER);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private static void handleButtonAction(String action, JFrame frame) {
        switch (action) {
            case "Add Book":
                showAddBookForm(); 
                break;
            case "Add Member":
                showAddMemberForm(); 
                break;
            case "Issue Book":
                showIssuedBooks();
                break;
            case "Return Book":
                showIssuedBooks();
                break;
            case "View Members":
                showSearchMember();
                break;
            case "Summary Report":
                showSummaryReportForm();
                break;
            case "Transaction History":
                showTransactionHistoryForm();
                break;
            case "Add transaction":
                showTransactionMForm();
            break;
            case "Update/Delete Book":
                ShowUpdateDeleteBook() ;
            break;
            case "Update/Delete Member":
                ShowUpdateDeleteMember() ;
            break;
             case "View Books":
                ViewBooks() ;
            break;
            case "Exit":
                frame.dispose();
                break;
            default:
                JOptionPane.showMessageDialog(frame, action + " clicked!");
                break;
        }
    }

    // Method to open the Add Book form
    private static void showAddBookForm() {
        SwingUtilities.invokeLater(() -> {
            AddBook addBook = new AddBook();
            addBook.showAddBookForm();
        });
    }

    private static void showAddMemberForm() {
        SwingUtilities.invokeLater(() -> {
            AddMember addMember = new AddMember();
            addMember.showAddMemberForm();
        });
    }

    private static void showIssuedBooks() {
        SwingUtilities.invokeLater(() -> {
            IssueBook IssueBook = new IssueBook();
            IssueBook.showIssuedBooks();
        });
    }

    private static void showSearchMember() {
        SwingUtilities.invokeLater(() -> {
            SearchMember SearchMember = new SearchMember();
            SearchMember.showSearchMember();
        });
    }

    private static void showSummaryReportForm() {
        SwingUtilities.invokeLater(() -> {
            SummaryReport SummaryReport = new SummaryReport();
            SummaryReport.showSummaryReportForm();
        });
    }

    private static void showTransactionHistoryForm() {
        SwingUtilities.invokeLater(() -> {
            TransactionHistory transactionHistory = new TransactionHistory();
            transactionHistory.showTransactionHistoryForm();
        });
    }

    private static void showTransactionMForm() {
        SwingUtilities.invokeLater(() -> {
            TransactionM transactionM = new TransactionM();
            transactionM.showTransactionMForm();
        });
    }

    private static void ShowUpdateDeleteBook() {
        SwingUtilities.invokeLater(() -> {
            UpdateDeleteBook UpdateDeleteBook = new UpdateDeleteBook();
            UpdateDeleteBook.ShowUpdateDeleteBook();
        });
    }

    private static void ShowUpdateDeleteMember() {
        SwingUtilities.invokeLater(() -> {
            UpdateDeleteMember UpdateDeleteMember = new UpdateDeleteMember();
            UpdateDeleteMember.ShowUpdateDeleteMember();
        });
    }

    private static void ViewBooks() {
        SwingUtilities.invokeLater(() -> {
            ViewBooks ViewBooks = new ViewBooks();
            ViewBooks.ViewBooks();
        });
    }

    // Login Screen Class
    static class LoginScreen {
        public void showLoginScreen() {
            JFrame loginFrame = new JFrame("Login");
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setSize(400, 300);
            loginFrame.setLocationRelativeTo(null);

            JPanel loginPanel = new JPanel();
            loginPanel.setLayout(new GridLayout(3, 2, 10, 10));
            loginPanel.setBackground(new Color(30, 30, 30));

            JLabel usernameLabel = new JLabel("Username: ");
            usernameLabel.setForeground(Color.WHITE);
            JTextField usernameField = new JTextField();
            
            JLabel passwordLabel = new JLabel("Password: ");
            passwordLabel.setForeground(Color.WHITE);
            JPasswordField passwordField = new JPasswordField();

            JButton loginButton = new JButton("Login");
            loginButton.setBackground(new Color(70, 130, 100));
            loginButton.setForeground(Color.WHITE);
            loginButton.setFocusPainted(false);

            loginButton.addActionListener(e -> {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                
                if (username.equals("admin") && String.valueOf(password).equals("password")) {
                    // If login is successful
                    JOptionPane.showMessageDialog(loginFrame, "Login Successful!");
                    loginSuccessful(loginFrame);
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid credentials, try again.");
                }
            });

            loginPanel.add(usernameLabel);
            loginPanel.add(usernameField);
            loginPanel.add(passwordLabel);
            loginPanel.add(passwordField);
            loginPanel.add(loginButton);

            loginFrame.add(loginPanel);
            loginFrame.setVisible(true);
        }
    }
}
