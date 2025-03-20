import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.sql.Date;

public class TransactionM {

    // Transaction class with status field
    public static class Transaction {
        private int transactionID;
        private int bookID;
        private int memberID;
        private Date issueDate;
        private Date dueDate;
        private Date returnDate;
        private double fine;
        private String status;  // New field for transaction status

        public Transaction(int transactionID, int bookID, int memberID, Date issueDate, Date dueDate, Date returnDate, double fine, String status) {
            this.transactionID = transactionID;
            this.bookID = bookID;
            this.memberID = memberID;
            this.issueDate = issueDate;
            this.dueDate = dueDate;
            this.returnDate = returnDate;
            this.fine = fine;
            this.status = status;  // Initialize the status
        }

        // Getters and setters
        public int getTransactionID() { return transactionID; }
        public int getBookID() { return bookID; }
        public int getMemberID() { return memberID; }
        public Date getIssueDate() { return issueDate; }
        public Date getDueDate() { return dueDate; }
        public Date getReturnDate() { return returnDate; }
        public double getFine() { return fine; }
        public String getStatus() { return status; }  // Getter for status

        public void setStatus(String status) { this.status = status; }  // Setter for status
    }

    // TransactionDAO class (Database Operations)
    public static class TransactionDAO {

        // Method to insert a new transaction
        public int addTransaction(Transaction transaction) {
            String sql = "INSERT INTO transactions (BookID, MemberID, IssueDate, DueDate, ReturnDate, Fine, Status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "");
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, transaction.getBookID());
                stmt.setInt(2, transaction.getMemberID());
                stmt.setDate(3, transaction.getIssueDate());
                stmt.setDate(4, transaction.getDueDate());
                stmt.setDate(5, transaction.getReturnDate());
                stmt.setDouble(6, transaction.getFine());
                stmt.setString(7, transaction.getStatus());  // Set status

                return stmt.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return 0;
        }

        // Method to fetch a transaction by its ID
        public Transaction getTransactionByID(int transactionID) {
            String sql = "SELECT * FROM transactions WHERE TransactionID = ?";
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "");
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, transactionID);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int bookID = rs.getInt("BookID");
                    int memberID = rs.getInt("MemberID");
                    Date issueDate = rs.getDate("IssueDate");
                    Date dueDate = rs.getDate("DueDate");
                    Date returnDate = rs.getDate("ReturnDate");
                    double fine = rs.getDouble("Fine");
                    String status = rs.getString("Status");  // Retrieve status

                    return new Transaction(transactionID, bookID, memberID, issueDate, dueDate, returnDate, fine, status);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        // Method to update a transaction's return date, fine, and status
        public int updateTransaction(Transaction transaction) {
            String sql = "UPDATE transactions SET ReturnDate = ?, Fine = ?, Status = ? WHERE TransactionID = ?";
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "");
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setDate(1, transaction.getReturnDate());
                stmt.setDouble(2, transaction.getFine());
                stmt.setString(3, transaction.getStatus());  // Update status
                stmt.setInt(4, transaction.getTransactionID());

                return stmt.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return 0;
        }

        // Method to delete a transaction by its ID
        public int deleteTransaction(int transactionID) {
            String sql = "DELETE FROM transactions WHERE TransactionID = ?";
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "");
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, transactionID);

                return stmt.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return 0;
        }
    }

    // Constructor for TransactionM (Form to interact with transactions)
    public void showTransactionMForm() {
        // Set up the frame
        JFrame frame = new JFrame("Library Management System");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Set a darker theme for the frame
        frame.getContentPane().setBackground(new Color(45, 45, 45)); // Dark background

        // Panel for adding a transaction with darker theme
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 10, 10));  // Updated for 8 rows (one for status)
        panel.setBackground(new Color(45, 45, 45)); // Dark background

        // Adding labels and text fields for the form
        JLabel lblBookID = new JLabel("Book ID:");
        lblBookID.setForeground(Color.WHITE);
        JTextField txtBookID = new JTextField();

        JLabel lblMemberID = new JLabel("Member ID:");
        lblMemberID.setForeground(Color.WHITE);
        JTextField txtMemberID = new JTextField();

        JLabel lblIssueDate = new JLabel("Issue Date (YYYY-MM-DD):");
        lblIssueDate.setForeground(Color.WHITE);
        JTextField txtIssueDate = new JTextField();

        JLabel lblDueDate = new JLabel("Due Date (YYYY-MM-DD):");
        lblDueDate.setForeground(Color.WHITE);
        JTextField txtDueDate = new JTextField();

        JLabel lblReturnDate = new JLabel("Return Date (YYYY-MM-DD):");
        lblReturnDate.setForeground(Color.WHITE);
        JTextField txtReturnDate = new JTextField();

        JLabel lblFine = new JLabel("Fine:");
        lblFine.setForeground(Color.WHITE);
        JTextField txtFine = new JTextField();

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setForeground(Color.WHITE);
        String[] statuses = {"Issued", "Returned", "Overdue"};
        JComboBox<String> comboStatus = new JComboBox<>(statuses);

        // Add components to the panel
        panel.add(lblBookID);
        panel.add(txtBookID);
        panel.add(lblMemberID);
        panel.add(txtMemberID);
        panel.add(lblIssueDate);
        panel.add(txtIssueDate);
        panel.add(lblDueDate);
        panel.add(txtDueDate);
        panel.add(lblReturnDate);
        panel.add(txtReturnDate);
        panel.add(lblFine);
        panel.add(txtFine);
        panel.add(lblStatus);
        panel.add(comboStatus);

        // Button to add a transaction
        JButton btnAddTransaction = new JButton("Add Transaction");
        btnAddTransaction.setBackground(new Color(0, 122, 204)); // Blue button color
        btnAddTransaction.setForeground(Color.WHITE);
        btnAddTransaction.setFont(new Font("Arial", Font.BOLD, 14));

        // Add the button to the panel
        panel.add(btnAddTransaction);

        // Set up the action listener for the Add Transaction button
        btnAddTransaction.addActionListener(e -> {
            try {
                int bookID = Integer.parseInt(txtBookID.getText());
                int memberID = Integer.parseInt(txtMemberID.getText());
                Date issueDate = Date.valueOf(txtIssueDate.getText());
                Date dueDate = Date.valueOf(txtDueDate.getText());
                Date returnDate = null;

                // Only set the return date if the status is "Returned"
                if (comboStatus.getSelectedItem().equals("Returned")) {
                    returnDate = Date.valueOf(txtReturnDate.getText());
                }

                double fine = Double.parseDouble(txtFine.getText());
                String status = comboStatus.getSelectedItem().toString();

                Transaction transaction = new Transaction(0, bookID, memberID, issueDate, dueDate, returnDate, fine, status);
                TransactionDAO dao = new TransactionDAO();

                // Add the transaction to the database
                int result = dao.addTransaction(transaction);
                if (result > 0) {
                    JOptionPane.showMessageDialog(frame, "Transaction added successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Error adding transaction.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Invalid input. Please check the fields.");
            }
        });

        // Add the panel to the frame and set it visible
        frame.add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TransactionM().showTransactionMForm());
    }
}
