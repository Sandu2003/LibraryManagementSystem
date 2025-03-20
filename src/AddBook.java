import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddBook {
    public void showAddBookForm() {
        // Create frame
        JFrame frame = new JFrame("Add Book");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(30, 30, 30)); // Dark theme
        frame.setLocationRelativeTo(null); // Center the window
        
        // Create a tabbed pane for navigation
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Panel for the navigation bar
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        navPanel.setBackground(new Color(45, 45, 45)); // Dark grey for navbar
        navPanel.setPreferredSize(new Dimension(600, 50));
        
        
        // Create panel for Add Book form
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new GridLayout(6, 2, 10, 10));

        // Labels and text fields
        JLabel lblBookID = new JLabel("Book ID:");
        lblBookID.setForeground(Color.WHITE);
        JTextField txtBookID = new JTextField();

        JLabel lblTitle = new JLabel("Title:");
        lblTitle.setForeground(Color.WHITE);
        JTextField txtTitle = new JTextField();

        JLabel lblAuthor = new JLabel("Author:");
        lblAuthor.setForeground(Color.WHITE);
        JTextField txtAuthor = new JTextField();

        JLabel lblPublisher = new JLabel("Publisher:");
        lblPublisher.setForeground(Color.WHITE);
        JTextField txtPublisher = new JTextField();

        JLabel lblYear = new JLabel("Year of Publication:");
        lblYear.setForeground(Color.WHITE);
        JTextField txtYear = new JTextField();

        // Add components to panel
        panel.add(lblBookID);
        panel.add(txtBookID);
        panel.add(lblTitle);
        panel.add(txtTitle);
        panel.add(lblAuthor);
        panel.add(txtAuthor);
        panel.add(lblPublisher);
        panel.add(txtPublisher);
        panel.add(lblYear);
        panel.add(txtYear);

        // Buttons
        JButton btnSave = new JButton("Save");
        btnSave.setBackground(new Color(70, 130, 180));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(128, 128, 128));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 14));

        // Add buttons to panel
        panel.add(btnSave);
        panel.add(btnCancel);

        // Add the "Add Book" tab to the tabbed pane
        tabbedPane.addTab("Add Book", panel);
        
        // Add tabbed pane and nav bar to frame
        frame.setLayout(new BorderLayout());
        frame.add(navPanel, BorderLayout.NORTH);
        frame.add(tabbedPane, BorderLayout.CENTER);
        
        // Action listener for save button
        btnSave.addActionListener(e -> {
            String bookID = txtBookID.getText();
            String title = txtTitle.getText();
            String author = txtAuthor.getText();
            String publisher = txtPublisher.getText();
            String year = txtYear.getText();

            // Validate input fields
            if (bookID.isEmpty() || title.isEmpty() || author.isEmpty() || publisher.isEmpty() || year.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields must be filled out!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate year format (must be a number)
            try {
                Integer.parseInt(year);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid year format!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Proceed with saving to the database
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "")) { // No password
                String sql = "INSERT INTO books (BookID, Title, Author, Publisher, Year) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, bookID);
                stmt.setString(2, title);
                stmt.setString(3, author);
                stmt.setString(4, publisher);
                stmt.setString(5, year);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(frame, "Book added successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to add book.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error connecting to the database.");
            }
        });

        // Action listener for canceling
        btnCancel.addActionListener(e -> frame.dispose());

        // Set the frame visible
        frame.setVisible(true);
    }
}
