import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class TransactionHistory {
    public void showTransactionHistoryForm() {
    // Your code to display the form
}
 {
        // Create frame
        JFrame frame = new JFrame("Transaction History");
        frame.setSize(700, 500); // Adjusted size to accommodate more columns
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(30, 30, 30)); // Dark background

        // Table columns
        String[] columns = {"Transaction ID", "Book ID", "Member ID", "Issue Date", "Due Date", "Return Date", "Status", "Fine"};
        
        // Default data (empty initially)
        Object[][] data = {};

        // Table setup
        JTable table = new JTable(new DefaultTableModel(data, columns));
        table.setFillsViewportHeight(true);
        table.setBackground(new Color(50, 50, 50)); // Dark background for the table
        table.setForeground(Color.WHITE);
        table.setGridColor(Color.GRAY);
        table.setRowHeight(25);

        // Scroll pane for table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(new Color(30, 30, 30));
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));

        // Add components to frame
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null); // Center the window on the screen
        frame.setVisible(true); // Make the frame visible

        // Database connection
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "")) {
            String sql = "SELECT TransactionID, BookID, MemberID, IssueDate, DueDate, ReturnDate, Status, Fine FROM transactions";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table.getModel();

            // Loop through the result set and add rows to the table
            while (rs.next()) {
                String transactionID = rs.getString("TransactionID");
                String bookID = rs.getString("BookID");
                String memberID = rs.getString("MemberID");
                String issueDate = rs.getString("IssueDate");
                String dueDate = rs.getString("DueDate");
                String returnDate = rs.getString("ReturnDate");
                String status = rs.getString("Status");
                double fine = rs.getDouble("Fine");

                model.addRow(new Object[]{transactionID, bookID, memberID, issueDate, dueDate, returnDate, status, fine});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading data from database.");
        }
    }
}
