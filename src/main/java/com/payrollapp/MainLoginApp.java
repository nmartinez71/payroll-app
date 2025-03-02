package com.payrollapp;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.mindrot.jbcrypt.BCrypt;

import com.payrollapp.Admin.AdminApp;
import com.payrollapp.Employee.EmployeeApp;

public class MainLoginApp extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeComboBox;

    public MainLoginApp() {

        

        setTitle("Login Screen");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2));

        panel.add(new JLabel("User ID:"));
        userIdField = new JTextField();
        panel.add(userIdField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("User Type:"));
        String[] userTypes = {"Admin", "Employee"};
        userTypeComboBox = new JComboBox<>(userTypes);
        panel.add(userTypeComboBox);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener((ActionEvent e) -> {
            String userId = userIdField.getText();
            String password = new String(passwordField.getPassword());
            String userType = (String) userTypeComboBox.getSelectedItem();
            
            if (authenticate(userId, password, userType)) {
                JOptionPane.showMessageDialog(MainLoginApp.this, "Login Successful!");
                openUserPanel(userType);
            } else {
                JOptionPane.showMessageDialog(MainLoginApp.this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(loginButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        panel.add(exitButton);

        /* JButton passwordButton = new JButton("gen pass");
        passwordButton.addActionListener((ActionEvent e) -> {
            String plainPassword = "password123"; 
            String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
            System.out.println("Hashed Password: " + hashedPassword);
        });
        panel.add(passwordButton); */
        

        add(panel);
    }
    

    public class PasswordGenerator {
        public void main(String[] args) {
            
        }
    }



    private boolean authenticate(String userId, String password, String userType) {
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employees WHERE employee_id = ? AND pay_type = ?")) {
            
            stmt.setString(1, userId);
            stmt.setString(2, userType.equals("Admin") ? "Salary" : "Hourly");

            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedHashedPassword = rs.getString("password"); 
                if (BCrypt.checkpw(password, storedHashedPassword)) {
                    System.out.println("Employee found: " + rs.getString("first_name") + " " + rs.getString("last_name"));
                    return true;
                } else {
                    System.out.println("Invalid password.");
                    return false;
                }
            } else {
                System.out.println("No matching employee found.");
                return false;
            }
        } catch (SQLException e) {
            // Log the error and show a user-friendly message
            Logger.getLogger(MainLoginApp.class.getName()).log(Level.SEVERE, "Error during authentication", e);
            JOptionPane.showMessageDialog(null, "An error occurred while authenticating. Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    
    }

    


    private void openUserPanel(String userType) {
        if (userType.equals("Admin")) {
            new AdminApp().setVisible(true);
        } else {
            new EmployeeApp().setVisible(true);
        }
        this.dispose();
    }

    public static void main(String[] args) {
        DatabaseHelper.initializeDatabase();
        SwingUtilities.invokeLater(() -> new MainLoginApp().setVisible(true));
    }
}
