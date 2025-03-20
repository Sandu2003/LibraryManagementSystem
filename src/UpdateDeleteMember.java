import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UpdateDeleteMember {
    public void ShowUpdateDeleteMember() {
        // Create frame
        JFrame frame = new JFrame("Update/Delete Member");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(30, 30, 30));

        // Create panel
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new GridLayout(7, 2, 10, 10)); // Increased rows to 7 to fit all components

        // Labels and text fields
        JLabel lblSearchMemberID = new JLabel("Search by Member ID:");
        lblSearchMemberID.setForeground(Color.WHITE);
        JTextField txtSearchMemberID = new JTextField();

        JButton btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(70, 130, 180));
        btnSearch.setForeground(Color.WHITE);

        JLabel lblName = new JLabel("Name:");
        lblName.setForeground(Color.WHITE);
        JTextField txtName = new JTextField();

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(Color.WHITE);
        JTextField txtEmail = new JTextField();

        JLabel lblMembershipType = new JLabel("Membership Type:");
        lblMembershipType.setForeground(Color.WHITE);
        JComboBox<String> cbMembershipType = new JComboBox<>(new String[]{"Student", "Teacher"});

        // Buttons
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBackground(new Color(34, 139, 34)); // Green
        btnUpdate.setForeground(Color.WHITE);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(178, 34, 34)); // Red
        btnDelete.setForeground(Color.WHITE);

        // Add components to panel
        panel.add(lblSearchMemberID);
        panel.add(txtSearchMemberID);
        panel.add(new JLabel()); // Spacer
        panel.add(btnSearch);
        panel.add(lblName);
        panel.add(txtName);
        panel.add(lblEmail);
        panel.add(txtEmail);
        panel.add(lblMembershipType);
        panel.add(cbMembershipType);
        panel.add(btnUpdate);
        panel.add(btnDelete);

        // Add action listeners
        btnSearch.addActionListener(e -> {
            // Get the Member ID entered by the user
            String memberID = txtSearchMemberID.getText();

            // Connect to the database and search for the member
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "");
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM members WHERE MemberID = ?")) {

                stmt.setString(1, memberID);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    // Populate the fields with the member's data
                    txtName.setText(rs.getString("Name"));
                    txtEmail.setText(rs.getString("Email"));
                    cbMembershipType.setSelectedItem(rs.getString("MembershipType"));
                } else {
                    JOptionPane.showMessageDialog(frame, "Member not found.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        btnUpdate.addActionListener(e -> {
            // Get the data from the input fields
            String memberID = txtSearchMemberID.getText();
            String name = txtName.getText();
            String email = txtEmail.getText();
            String membershipType = cbMembershipType.getSelectedItem().toString();

            if (memberID.isEmpty() || name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields!");
                return;
            }

            // Connect to the database and update the member's information
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "");
                 PreparedStatement stmt = conn.prepareStatement("UPDATE members SET Name = ?, Email = ?, MembershipType = ? WHERE MemberID = ?")) {

                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, membershipType);
                stmt.setString(4, memberID);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(frame, "Member updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to update member.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        btnDelete.addActionListener(e -> {
            // Get the Member ID entered by the user
            String memberID = txtSearchMemberID.getText();

            if (memberID.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a Member ID to delete!");
                return;
            }

            // Connect to the database and delete the member
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "");
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM members WHERE MemberID = ?")) {

                stmt.setString(1, memberID);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(frame, "Member deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to delete member.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        // Add panel to frame
        frame.add(panel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }
}
