import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.regex.*;

public class AddMember {
    public void showAddMemberForm() {
        // Create frame
        JFrame frame = new JFrame("Add Member");
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
        
        

        // Create panel for Add Member form
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new GridLayout(6, 2, 10, 10));

        // Labels and text fields
        JLabel lblMemberID = new JLabel("Member ID:");
        lblMemberID.setForeground(Color.WHITE);
        JTextField txtMemberID = new JTextField();

        JLabel lblName = new JLabel("Name:");
        lblName.setForeground(Color.WHITE);
        JTextField txtName = new JTextField();

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(Color.WHITE);
        JTextField txtEmail = new JTextField();

        JLabel lblMembershipType = new JLabel("Membership Type (Student/Teacher):");
        lblMembershipType.setForeground(Color.WHITE);
        JTextField txtMembershipType = new JTextField();

        // Add components to panel
        panel.add(lblMemberID);
        panel.add(txtMemberID);
        panel.add(lblName);
        panel.add(txtName);
        panel.add(lblEmail);
        panel.add(txtEmail);
        panel.add(lblMembershipType);
        panel.add(txtMembershipType);

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

        // Add the "Add Member" tab to the tabbed pane
        tabbedPane.addTab("Add Member", panel);
        
        // Add tabbed pane and nav bar to frame
        frame.setLayout(new BorderLayout());
        frame.add(navPanel, BorderLayout.NORTH);
        frame.add(tabbedPane, BorderLayout.CENTER);
        
        // Action listener for save button
        btnSave.addActionListener(e -> {
            String memberID = txtMemberID.getText();
            String name = txtName.getText();
            String email = txtEmail.getText();
            String membershipType = txtMembershipType.getText();

            // Validate input fields
            if (memberID.isEmpty() || name.isEmpty() || email.isEmpty() || membershipType.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields must be filled out!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate email format
            String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            Pattern pattern = Pattern.compile(emailPattern);
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                JOptionPane.showMessageDialog(frame, "Invalid email format!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Proceed with saving to the database
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "")) { // No password
                String sql = "INSERT INTO members (MemberID, Name, Email, MembershipType) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, memberID);
                stmt.setString(2, name);
                stmt.setString(3, email);
                stmt.setString(4, membershipType);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(frame, "Member added successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to add member.");
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

