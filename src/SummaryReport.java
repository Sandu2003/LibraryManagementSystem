
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SummaryReport {

     public void showSummaryReportForm() {
        // Create frame
        JFrame frame = new JFrame("Summary Report");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(30, 30, 30)); // Dark theme
        frame.setLayout(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("Library Summary Report", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Create panel for report area
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new BorderLayout());

        // Text area for report
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        reportArea.setForeground(Color.WHITE);
        reportArea.setBackground(new Color(45, 45, 45));
        reportArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);

        // Footer panel with "Close" button
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(30, 30, 30));
        JButton closeButton = new JButton("Close");
        closeButton.setBackground(new Color(128, 128, 128));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        footerPanel.add(closeButton);

        frame.add(footerPanel, BorderLayout.SOUTH);

        // Fetch data from database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "")) {
            String sql = "SELECT * FROM books";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            StringBuilder report = new StringBuilder();
            report.append(String.format("%-10s %-30s %-20s %-15s\n", "BookID", "Title", "Author", "Availability"));
            report.append("=".repeat(80)).append("\n");

            boolean dataFound = false;
            while (rs.next()) {
                report.append(String.format(
                        "%-10d %-30s %-20s %-15s\n",
                        rs.getInt("BookID"),
                        rs.getString("Title"),
                        rs.getString("Author"),
                        rs.getString("Availability").equals("1") ? "Available" : "Issued"
                ));
                dataFound = true;
            }

            if (!dataFound) {
                report.append("\nNo books found in the library database.");
            }

            reportArea.setText(report.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error fetching data from the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        // Close button functionality
        closeButton.addActionListener(e -> frame.dispose());

        // Add panel to frame
        frame.add(panel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }
}
