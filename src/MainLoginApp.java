package src;
import javax.swing.*;

import src.Admin.AdminApp;
import src.Employee.EmployeeApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainLoginApp extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeComboBox;

    public MainLoginApp() {

        try (Connection conn = DatabaseHelper.getConnection()) {
            System.out.println("Connected to the database successfully!"); // Debug statement
        } catch (Exception e) {
            System.err.println("Failed to connect to the database!"); // Debug statement
            e.printStackTrace();
        }


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
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = userIdField.getText();
                String password = new String(passwordField.getPassword());
                String userType = (String) userTypeComboBox.getSelectedItem();

                if (authenticate(userId, password, userType)) {
                    JOptionPane.showMessageDialog(MainLoginApp.this, "Login Successful!");
                    openUserPanel(userType);
                } else {
                    JOptionPane.showMessageDialog(MainLoginApp.this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(loginButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        panel.add(exitButton);

        add(panel);
    }

    private boolean authenticate(String userId, String password, String userType) {
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employees WHERE employee_id = ? AND pay_type = ?")) {
            stmt.setString(1, userId);
            stmt.setString(2, userType.equals("Admin") ? "Salary" : "Hourly");
            System.out.println("Executing query: " + stmt.toString()); // Debug statement
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Employee found: " + rs.getString("first_name") + " " + rs.getString("last_name")); // Debug statement
                return true;
            } else {
                System.out.println("No matching employee found."); // Debug statement
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
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