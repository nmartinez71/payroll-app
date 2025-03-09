package com.payrollapp;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

import com.payrollapp.Employee.EmployeeApp;

public class MainLoginApp extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeComboBox;  
    
    private EmployeeClass employee;

    public MainLoginApp() {
        setTitle("Login Screen");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1));

        panel.add(new JLabel("Company Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        
        panel.add(new JLabel("User Type:"));
        userTypeComboBox = new JComboBox<>(new String[]{"Admin", "Employee"});
        panel.add(userTypeComboBox);

        
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener((ActionEvent e) -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String selectedUserType = (String) userTypeComboBox.getSelectedItem();
            
            employee = authenticate(email, password, selectedUserType);
            if (employee != null) {
                JOptionPane.showMessageDialog(MainLoginApp.this, "Login Successful!");
                openUserPanel(employee);
            } else {
                JOptionPane.showMessageDialog(MainLoginApp.this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(loginButton);

        
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener((ActionEvent e) -> System.exit(0));
        panel.add(exitButton);

        add(panel);
    }

    private EmployeeClass authenticate(String email, String password, String userType) {
        
        if ("Admin".equals(userType) && email.equals("HR0001")) {
            try (Connection conn = DatabaseHelper.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employees WHERE company_email = ?")) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
        
                if (rs.next()) {
                    String storedHashedPassword = rs.getString("password");
                    if (BCrypt.checkpw(password, storedHashedPassword)) {
                        
                        return createEmployeeFromResultSet(rs, true);  
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;  
        }

        
        if ("Employee".equals(userType)) {
            try (Connection conn = DatabaseHelper.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employees WHERE company_email = ?")) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
    
                if (rs.next()) {
                    String storedHashedPassword = rs.getString("password");
                    if (BCrypt.checkpw(password, storedHashedPassword)) {
                        
                        return createEmployeeFromResultSet(rs, false);  
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null; 
    }

    private EmployeeClass createEmployeeFromResultSet(ResultSet rs, boolean isAdmin) throws SQLException {
        if ("Salaried".equals(rs.getString("pay_type"))) {
            return new EmployeeClass(
                rs.getInt("employee_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("pay_type"),
                0.0,
                rs.getDouble("pay_info"),
                null,
                rs.getString("medical_coverage"),
                rs.getInt("num_dependents"),
                isAdmin 
            );
        } else {
            return new EmployeeClass(
                rs.getInt("employee_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("pay_type"),
                rs.getDouble("hourly_rate"),
                0.0,
                rs.getString("pay_info"),
                rs.getString("medical_coverage"),
                rs.getInt("num_dependents"),
                isAdmin 
            );
        }
    }

    private void openUserPanel(EmployeeClass employee) {
        
        boolean isAdmin = employee.isAdmin();
    
        EmployeeApp employeeApp = new EmployeeApp(employee, isAdmin);
        employeeApp.setVisible(true);
    
        employeeApp.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                logout();
            }
        });
    
        this.dispose();
    }
    
    private void logout() {
        System.out.println("Logging out employee: " + (employee != null ? employee.getEmployeeId() : "Unknown"));
        employee = null;
    
        SwingUtilities.invokeLater(() -> new MainLoginApp().setVisible(true));
    }

    public static void main(String[] args) {
        DatabaseHelper.initializeDatabase();
        SwingUtilities.invokeLater(() -> new MainLoginApp().setVisible(true));
    }
}
