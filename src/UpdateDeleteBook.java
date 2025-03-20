import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UpdateDeleteBook {
    public void ShowUpdateDeleteBook() {
        // Create frame
        JFrame frame = new JFrame("Update/Delete Book");
        frame.setSize(400, 500); // Increased size to accommodate new fields
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(30, 30, 30)); // Dark theme

        // Create panel with GridBagLayout for more flexibility
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add spacing between components
        gbc.anchor = GridBagConstraints.WEST; // Align labels to the left

        // Labels and text fields
        JLabel lblBookID = new JLabel("Book ID:");
        lblBookID.setForeground(Color.WHITE);
        JTextField txtBookID = new JTextField(20); // Set the width of the text field

        JLabel lblBookTitle = new JLabel("Book Title:");
        lblBookTitle.setForeground(Color.WHITE);
        JTextField txtBookTitle = new JTextField(20);

        JLabel lblBookAuthor = new JLabel("Author:");
        lblBookAuthor.setForeground(Color.WHITE);
        JTextField txtBookAuthor = new JTextField(20);

        JLabel lblBookGenre = new JLabel("Genre:");
        lblBookGenre.setForeground(Color.WHITE);
        JTextField txtBookGenre = new JTextField(20);

        JLabel lblBookAvailability = new JLabel("Availability (true/false):");
        lblBookAvailability.setForeground(Color.WHITE);
        JTextField txtBookAvailability = new JTextField(20);

        // Add components to the panel with GridBagConstraints
        gbc.gridx = 0; gbc.gridy = 0; panel.add(lblBookID, gbc);
        gbc.gridx = 1; panel.add(txtBookID, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(lblBookTitle, gbc);
        gbc.gridx = 1; panel.add(txtBookTitle, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(lblBookAuthor, gbc);
        gbc.gridx = 1; panel.add(txtBookAuthor, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(lblBookGenre, gbc);
        gbc.gridx = 1; panel.add(txtBookGenre, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(lblBookAvailability, gbc);
        gbc.gridx = 1; panel.add(txtBookAvailability, gbc);

        // Buttons
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBackground(new Color(70, 130, 180)); // Blue color
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(255, 69, 0)); // Red color for delete
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(128, 128, 128));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 14));

        // Add buttons with GridBagConstraints
        gbc.gridx = 0; gbc.gridy = 5; panel.add(btnUpdate, gbc);
        gbc.gridx = 1; panel.add(btnDelete, gbc);
        gbc.gridx = 0; gbc.gridy = 6; panel.add(btnCancel, gbc);

        // Action listeners for update and delete
        btnUpdate.addActionListener(e -> {
            String bookID = txtBookID.getText();
            String bookTitle = txtBookTitle.getText();
            String bookAuthor = txtBookAuthor.getText();
            String bookGenre = txtBookGenre.getText();
            String availabilityStr = txtBookAvailability.getText();

            // Validate input
            if (bookID.isEmpty() || bookTitle.isEmpty() || bookAuthor.isEmpty() || bookGenre.isEmpty() || availabilityStr.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all fields!");
                return;
            }

            boolean availability = Boolean.parseBoolean(availabilityStr); // Convert to boolean

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "")) {
                String sql = "UPDATE books SET BookTitle = ?, Author = ?, Genre = ?, Availability = ? WHERE BookID = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, bookTitle);
                stmt.setString(2, bookAuthor);
                stmt.setString(3, bookGenre);
                stmt.setBoolean(4, availability);
                stmt.setString(5, bookID);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(frame, "Book updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "No book found with that ID.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error updating the book.");
            }
        });

        btnDelete.addActionListener(e -> {
            String bookID = txtBookID.getText();

            // Validate input
            if (bookID.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a Book ID to delete!");
                return;
            }

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "")) {
                String sql = "DELETE FROM books WHERE BookID = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, bookID);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(frame, "Book deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to delete book.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error deleting the book.");
            }
        });

        btnCancel.addActionListener(e -> frame.dispose());

        // Add panel to frame
        frame.add(panel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }
}

