import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class ViewBooks {
    public void ViewBooks() {
        // Create the frame
        JFrame frame = new JFrame("View Books");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(30, 30, 30)); // Dark theme

        // Create panel
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new BorderLayout());

        // Create table to display books
        String[] columnNames = {"Book ID", "Title", "Author", "Genre", "Availability"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        table.setBackground(new Color(50, 50, 50));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(70, 130, 180));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create search bar
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(new Color(30, 30, 30));
        JLabel lblSearch = new JLabel("Search by Title or Author:");
        lblSearch.setForeground(Color.WHITE);
        JTextField txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(70, 130, 180));
        btnSearch.setForeground(Color.WHITE);
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Add panel to frame
        frame.add(panel);
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);

        // Database connection
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "")) { // No password
            String sql = "SELECT * FROM books";  // Fetch all books
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Loop through the results and populate the table
            while (rs.next()) {
                int bookID = rs.getInt("BookID");
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                String genre = rs.getString("Genre");
                String availability = rs.getBoolean("Availability") ? "Available" : "Not Available";
                model.addRow(new Object[]{bookID, title, author, genre, availability});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error fetching books from the database.");
        }

        // Search functionality
        btnSearch.addActionListener(e -> {
            String searchText = txtSearch.getText();
            if (!searchText.isEmpty()) {
                model.setRowCount(0);  // Clear the table

                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "")) {
                    String sql = "SELECT * FROM books WHERE Title LIKE ? OR Author LIKE ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, "%" + searchText + "%");
                    stmt.setString(2, "%" + searchText + "%");

                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        int bookID = rs.getInt("BookID");
                        String title = rs.getString("Title");
                        String author = rs.getString("Author");
                        String genre = rs.getString("Genre");
                        String availability = rs.getBoolean("Availability") ? "Available" : "Not Available";
                        model.addRow(new Object[]{bookID, title, author, genre, availability});
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error searching books.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a search term.");
            }
        });
    }
}
