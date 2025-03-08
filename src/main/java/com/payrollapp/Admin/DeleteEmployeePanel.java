package com.payrollapp.Admin;

import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.payrollapp.DatabaseHelper;

public class DeleteEmployeePanel extends JPanel {

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField departmentField;
    private JTextField jobTitleField;
    private JTextField statusField;
    private JTextField dobField;
    private JComboBox<String> genderComboBox;
    private JComboBox<String> payTypeComboBox;
    private JTextField emailField;
    private JTextField addressLine1Field;
    private JTextField addressLine2Field;
    private JTextField cityField;
    private JTextField stateField;
    private JTextField zipField;

    public DeleteEmployeePanel(EmployeeListPanel employeeListPanel) {
        setLayout(new GridLayout(18, 2));

        add(new JLabel("Employee ID:"));
        JTextField employeeidField = new JTextField();
        add(employeeidField);

        
        employeeidField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                pullEmployee(employeeidField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                
                clearEmployeeFields();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                
            }
        });

        
        add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        firstNameField.setEditable(false); 
        add(firstNameField);

        add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        lastNameField.setEditable(false);  
        add(lastNameField);

        add(new JLabel("Department:"));
        departmentField = new JTextField();
        departmentField.setEditable(false);  
        add(departmentField);

        add(new JLabel("Job Title:"));
        jobTitleField = new JTextField();
        jobTitleField.setEditable(false);  
        add(jobTitleField);

        add(new JLabel("Status:"));
        statusField = new JTextField();
        statusField.setEditable(false);  
        add(statusField);

        add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        dobField = new JTextField();
        dobField.setEditable(false);  
        add(dobField);

        add(new JLabel("Gender:"));
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderComboBox.setEnabled(false);  
        add(genderComboBox);

        add(new JLabel("Pay Type:"));
        payTypeComboBox = new JComboBox<>(new String[]{"Salary", "Hourly"});
        payTypeComboBox.setEnabled(false);  
        add(payTypeComboBox);

        add(new JLabel("Company Email:"));
        emailField = new JTextField();
        emailField.setEditable(false);  
        add(emailField);

        add(new JLabel("Address Line 1:"));
        addressLine1Field = new JTextField();
        addressLine1Field.setEditable(false); 
        add(addressLine1Field);

        add(new JLabel("Address Line 2:"));
        addressLine2Field = new JTextField();
        addressLine2Field.setEditable(false); 
        add(addressLine2Field);

        add(new JLabel("City:"));
        cityField = new JTextField();
        cityField.setEditable(false);  
        add(cityField);

        add(new JLabel("State:"));
        stateField = new JTextField();
        stateField.setEditable(false);
        add(stateField);

        add(new JLabel("ZIP:"));
        zipField = new JTextField();
        zipField.setEditable(false); 
        add(zipField);

        
        JButton deleteButton = new JButton("Delete Employee");
        deleteButton.addActionListener(e -> {
            if (employeeidField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Employee ID is required to delete!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int employeeId = Integer.parseInt(employeeidField.getText());

            
            deleteEmployee(employeeId);
            clearEmployeeFields();
            employeeListPanel.pullEmployee();
        });
        add(deleteButton);
    }

    
    private void pullEmployee(String employeeId) {
        if (employeeId.isEmpty()) {
            return;
        }

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employees WHERE employee_id = ?")) {
            stmt.setInt(1, Integer.parseInt(employeeId));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                firstNameField.setText(rs.getString("first_name"));
                lastNameField.setText(rs.getString("last_name"));
                departmentField.setText(rs.getString("department"));
                jobTitleField.setText(rs.getString("job_title"));
                statusField.setText(rs.getString("status"));
                dobField.setText(rs.getString("date_of_birth"));
                genderComboBox.setSelectedItem(rs.getString("gender"));
                payTypeComboBox.setSelectedItem(rs.getString("pay_type"));
                emailField.setText(rs.getString("company_email"));
                addressLine1Field.setText(rs.getString("address_line1"));
                addressLine2Field.setText(rs.getString("address_line2"));
                cityField.setText(rs.getString("city"));
                stateField.setText(rs.getString("state"));
                zipField.setText(rs.getString("zip"));
            } else {
                JOptionPane.showMessageDialog(this, "Employee not found!", "Error", JOptionPane.ERROR_MESSAGE);
                clearEmployeeFields();  
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    private void clearEmployeeFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        departmentField.setText("");
        jobTitleField.setText("");
        statusField.setText("");
        dobField.setText("");
        genderComboBox.setSelectedItem(null);
        payTypeComboBox.setSelectedItem(null);
        emailField.setText("");
        addressLine1Field.setText("");
        addressLine2Field.setText("");
        cityField.setText("");
        stateField.setText("");
        zipField.setText("");
    }

    
    private void deleteEmployee(int employeeId) {
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM employees WHERE employee_id = ?")) {
            stmt.setInt(1, employeeId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Employee deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Employee not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting employee!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
