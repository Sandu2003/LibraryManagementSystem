import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class IssueBook {
    public void showIssuedBooks() {
        // Create frame
        JFrame frame = new JFrame("Issue Book");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(30, 30, 30)); // Dark theme
        frame.setLocationRelativeTo(null); // Center the window
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel for the navigation bar
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        navPanel.setBackground(new Color(45, 45, 45)); // Dark grey for navbar
        navPanel.setPreferredSize(new Dimension(600, 50));

        
        // Create panel for the "Issue Book" form
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new GridLayout(6, 2, 10, 10));

        // Labels and text fields
        JLabel lblBookID = new JLabel("Book ID:");
        lblBookID.setForeground(Color.WHITE);
        JTextField txtBookID = new JTextField();
        txtBookID.setBackground(new Color(50, 50, 50));
        txtBookID.setForeground(Color.WHITE);
        txtBookID.setCaretColor(Color.WHITE);

        JLabel lblMemberID = new JLabel("Member ID:");
        lblMemberID.setForeground(Color.WHITE);
        JTextField txtMemberID = new JTextField();
        txtMemberID.setBackground(new Color(50, 50, 50));
        txtMemberID.setForeground(Color.WHITE);
        txtMemberID.setCaretColor(Color.WHITE);

        JLabel lblIssueDate = new JLabel("Issue Date (yyyy-mm-dd):");
        lblIssueDate.setForeground(Color.WHITE);
        JTextField txtIssueDate = new JTextField();
        txtIssueDate.setBackground(new Color(50, 50, 50));
        txtIssueDate.setForeground(Color.WHITE);
        txtIssueDate.setCaretColor(Color.WHITE);

        // Add components to panel
        panel.add(lblBookID);
        panel.add(txtBookID);
        panel.add(lblMemberID);
        panel.add(txtMemberID);
        panel.add(lblIssueDate);
        panel.add(txtIssueDate);

        // Buttons
        JButton btnIssue = new JButton("Issue");
        btnIssue.setBackground(new Color(70, 130, 180));
        btnIssue.setForeground(Color.WHITE);
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(128, 128, 128));
        btnCancel.setForeground(Color.WHITE);

        // Add buttons to panel
        panel.add(btnIssue);
        panel.add(btnCancel);

        // Add the "Issue Book" tab to the tabbed pane
        tabbedPane.addTab("Issue Book", panel);

        // Add tabbed pane and nav bar to frame
        frame.setLayout(new BorderLayout());
        frame.add(navPanel, BorderLayout.NORTH);
        frame.add(tabbedPane, BorderLayout.CENTER);

        // Add action listeners for issuing the book
        btnIssue.addActionListener(e -> {
            String bookID = txtBookID.getText();
            String memberID = txtMemberID.getText();
            String issueDate = txtIssueDate.getText();

            // Validation
            if (bookID.isEmpty() || memberID.isEmpty() || issueDate.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields must be filled out.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidDate(issueDate)) {
                JOptionPane.showMessageDialog(frame, "Invalid date format. Use yyyy-mm-dd.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Database connection and inserting data
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "")) {
                String sql = "INSERT INTO issued_books (BookID, MemberID, IssueDate) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, bookID);
                stmt.setString(2, memberID);
                stmt.setString(3, issueDate);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(frame, "Book issued successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to issue book.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error connecting to the database.");
            }
        });

        // Add action listener for canceling
        btnCancel.addActionListener(e -> frame.dispose());

        // Add panel to frame
        frame.add(panel, BorderLayout.CENTER);

        // Set the frame to be visible
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }

    // Validation method for date
    private static boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
