import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginPage {
    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("Login");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(30, 30, 30)); // Dark theme

        // Create panel
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        // Labels and text fields
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setForeground(Color.WHITE);
        JTextField txtUsername = new JTextField();

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(Color.WHITE);
        JPasswordField txtPassword = new JPasswordField();

        // Buttons
        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.WHITE);
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(128, 128, 128));
        btnCancel.setForeground(Color.WHITE);

        // Add components to panel
        panel.add(lblUsername);
        panel.add(txtUsername);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(btnLogin);
        panel.add(btnCancel);

        // Add panel to frame
        frame.add(panel);
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);

        // Add action listeners for login and cancel
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            // Validate login credentials
            if (validateLogin(username, password)) {
                JOptionPane.showMessageDialog(frame, "Login Successful!");
                frame.dispose(); // Close login page
                // Proceed to the main application (like main menu or dashboard)
                // For example, you could call a method to open the main menu
                // openMainMenu();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password. Please try again.");
            }
        });

        btnCancel.addActionListener(e -> frame.dispose()); // Close login page on cancel
    }

    // Validate the login credentials by checking the database
    private static boolean validateLogin(String username, String password) {
        boolean isValid = false;

        // Connect to database and check if the username and password are correct
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "")) { // No password
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                isValid = true; // If a record is found, the login is valid
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error connecting to the database.");
        }

        return isValid;
    }
}
