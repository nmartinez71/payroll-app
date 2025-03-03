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

public class DeleteEmployee extends JPanel {

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

    public DeleteEmployee() {
        setLayout(new GridLayout(8, 2));

        add(new JLabel("Employee ID:"));
        JTextField employeeidField = new JTextField();
        add(employeeidField);

        // Listener to pull employee data when ID is entered or modified
        employeeidField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                pullEmployee(employeeidField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // Clear fields when employee ID is removed
                clearEmployeeFields();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Not necessary for document changes
            }
        });

        // Initialize and add fields for employee data
        add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        firstNameField.setEditable(false);  // Set as read-only
        add(firstNameField);

        add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        lastNameField.setEditable(false);  // Set as read-only
        add(lastNameField);

        add(new JLabel("Department:"));
        departmentField = new JTextField();
        departmentField.setEditable(false);  // Set as read-only
        add(departmentField);

        add(new JLabel("Job Title:"));
        jobTitleField = new JTextField();
        jobTitleField.setEditable(false);  // Set as read-only
        add(jobTitleField);

        add(new JLabel("Status:"));
        statusField = new JTextField();
        statusField.setEditable(false);  // Set as read-only
        add(statusField);

        add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        dobField = new JTextField();
        dobField.setEditable(false);  // Set as read-only
        add(dobField);

        add(new JLabel("Gender:"));
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderComboBox.setEnabled(false);  // Set combo box as read-only
        add(genderComboBox);

        add(new JLabel("Pay Type:"));
        payTypeComboBox = new JComboBox<>(new String[]{"Salary", "Hourly"});
        payTypeComboBox.setEnabled(false);  // Set combo box as read-only
        add(payTypeComboBox);

        add(new JLabel("Company Email:"));
        emailField = new JTextField();
        emailField.setEditable(false);  // Set as read-only
        add(emailField);

        add(new JLabel("Address Line 1:"));
        addressLine1Field = new JTextField();
        addressLine1Field.setEditable(false);  // Set as read-only
        add(addressLine1Field);

        add(new JLabel("Address Line 2:"));
        addressLine2Field = new JTextField();
        addressLine2Field.setEditable(false);  // Set as read-only
        add(addressLine2Field);

        add(new JLabel("City:"));
        cityField = new JTextField();
        cityField.setEditable(false);  // Set as read-only
        add(cityField);

        add(new JLabel("State:"));
        stateField = new JTextField();
        stateField.setEditable(false);  // Set as read-only
        add(stateField);

        add(new JLabel("ZIP:"));
        zipField = new JTextField();
        zipField.setEditable(false);  // Set as read-only
        add(zipField);

        // Delete employee button
        JButton deleteButton = new JButton("Delete Employee");
        deleteButton.addActionListener(e -> {
            if (employeeidField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Employee ID is required to delete!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int employeeId = Integer.parseInt(employeeidField.getText());

            // Delete the employee based on ID
            deleteEmployee(employeeId);
        });
        add(deleteButton);
    }

    // Method to retrieve employee data based on the entered ID and populate fields
    private void pullEmployee(String employeeId) {
        if (employeeId.isEmpty()) {
            return;
        }

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employees WHERE employee_id = ?")) {
            stmt.setInt(1, Integer.parseInt(employeeId));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Populate fields with employee data
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
                clearEmployeeFields();  // Clear fields if employee is not found
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Method to clear fields (e.g., when employee ID is removed or not found)
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

    // Method to delete an employee based on the ID
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
