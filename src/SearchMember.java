import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SearchMember {
    public void showSearchMember() {
        // Create frame
        JFrame frame = new JFrame("Search Member");
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

        // Panel for the "Search Member" form
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        // Labels and text fields
        JLabel lblMemberID = new JLabel("Member ID:");
        lblMemberID.setForeground(Color.WHITE);
        JTextField txtMemberID = new JTextField();

        JLabel lblResult = new JLabel();
        lblResult.setForeground(Color.GREEN);
        lblResult.setVisible(false); // Initially hidden

        // Buttons
        JButton btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(70, 130, 180));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("SansSerif", Font.BOLD, 14));
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(128, 128, 128));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Add components to the panel
        panel.add(lblMemberID);
        panel.add(txtMemberID);
        panel.add(btnSearch);
        panel.add(btnCancel);
        panel.add(new JLabel()); // Empty space for alignment
        panel.add(lblResult);

        // Add "Search Member" tab to the tabbed pane
        tabbedPane.addTab("Search Member", panel);

        // Add tabbed pane and navigation bar to frame
        frame.setLayout(new BorderLayout());
        frame.add(navPanel, BorderLayout.NORTH);
        frame.add(tabbedPane, BorderLayout.CENTER);

        // Action listeners for searching member
        btnSearch.addActionListener(e -> {
            String memberID = txtMemberID.getText().trim();

            // Validate input
            if (memberID.isEmpty()) {
                lblResult.setText("Please enter a Member ID.");
                lblResult.setForeground(Color.RED);
                lblResult.setVisible(true);
                return;
            }

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "")) {
                String sql = "SELECT * FROM members WHERE MemberID = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, memberID);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    lblResult.setText("Member found: " + rs.getString("Name"));
                    lblResult.setForeground(Color.GREEN);
                    lblResult.setVisible(true);
                } else {
                    lblResult.setText("No member found with ID: " + memberID);
                    lblResult.setForeground(Color.RED);
                    lblResult.setVisible(true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error connecting to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action listener for cancel button
        btnCancel.addActionListener(e -> frame.dispose());

        // Show frame
        frame.setVisible(true);
    }
}
