import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ReturnBook {
    public void showReturnBook() {
        // Create frame
        JFrame frame = new JFrame("Return Book");
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

        // Create panel for the "Return Book" form
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new GridLayout(6, 2, 10, 10));

        // Labels and text fields
        JLabel lblBookID = new JLabel("Book ID:");
        lblBookID.setForeground(Color.WHITE);
        JTextField txtBookID = new JTextField();

        JLabel lblMemberID = new JLabel("Member ID:");
        lblMemberID.setForeground(Color.WHITE);
        JTextField txtMemberID = new JTextField();

        JLabel lblError = new JLabel(); // Error message label (initially hidden)
        lblError.setForeground(Color.RED);
        lblError.setVisible(false);

        // Add components to panel
        panel.add(lblBookID);
        panel.add(txtBookID);
        panel.add(lblMemberID);
        panel.add(txtMemberID);
        panel.add(lblError);

        // Buttons
        JButton btnReturn = new JButton("Return");
        btnReturn.setBackground(new Color(70, 130, 180));
        btnReturn.setForeground(Color.WHITE);
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(128, 128, 128));
        btnCancel.setForeground(Color.WHITE);

        panel.add(btnReturn);
        panel.add(btnCancel);

        // Add "Return Book" tab to the tabbed pane
        tabbedPane.addTab("Return Book", panel);

        // Add tabbed pane and navigation bar to frame
        frame.setLayout(new BorderLayout());
        frame.add(navPanel, BorderLayout.NORTH);
        frame.add(tabbedPane, BorderLayout.CENTER);

        // Action listeners
        btnReturn.addActionListener(e -> {
            String bookID = txtBookID.getText().trim();
            String memberID = txtMemberID.getText().trim();

            // Validate input fields
            if (bookID.isEmpty() || memberID.isEmpty()) {
                lblError.setText("Both fields are required.");
                lblError.setVisible(true);
                return;
            }
            if (!bookID.matches("\\d+") || !memberID.matches("\\d+")) {
                lblError.setText("Book ID and Member ID must be numeric.");
                lblError.setVisible(true);
                return;
            }

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "")) {
                // Validate book and member
                String checkSql = "SELECT * FROM issued_books WHERE BookID = ? AND MemberID = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setString(1, bookID);
                checkStmt.setString(2, memberID);

                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    // Book is issued; proceed to return
                    String updateSql = "UPDATE books SET Availability = '1' WHERE BookID = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                    updateStmt.setString(1, bookID);

                    int rows = updateStmt.executeUpdate();
                    if (rows > 0) {
                        String deleteIssuedSql = "DELETE FROM issued_books WHERE BookID = ? AND MemberID = ?";
                        PreparedStatement deleteStmt = conn.prepareStatement(deleteIssuedSql);
                        deleteStmt.setString(1, bookID);
                        deleteStmt.setString(2, memberID);
                        deleteStmt.executeUpdate();

                        JOptionPane.showMessageDialog(frame, "Book returned successfully!");
                        lblError.setVisible(false);
                        txtBookID.setText("");
                        txtMemberID.setText("");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to update book availability.");
                    }
                } else {
                    lblError.setText("This book is not issued to the specified member.");
                    lblError.setVisible(true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                lblError.setText("Database connection error.");
                lblError.setVisible(true);
            }
        });

        btnCancel.addActionListener(e -> frame.dispose());

        frame.setVisible(true);
    }
}
