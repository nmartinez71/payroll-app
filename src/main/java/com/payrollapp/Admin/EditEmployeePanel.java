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

public class EditEmployeePanel extends JPanel {

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

    public EditEmployeePanel(EmployeeListPanel employeeListPanel) {
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
            public void changedUpdate(DocumentEvent e) {}
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

        
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {
            if (employeeidField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Employee ID is required to update!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int employeeId = Integer.parseInt(employeeidField.getText());
            updateEmployee(employeeId);
            employeeListPanel.pullEmployee(); 
        });
        add(saveButton);
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

                
                setEditable(true);
            } else {
                JOptionPane.showMessageDialog(this, "Employee not found!", "Error", JOptionPane.ERROR_MESSAGE);
                clearEmployeeFields();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    
    private void setEditable(boolean editable) {
        firstNameField.setEditable(editable);
        lastNameField.setEditable(editable);
        departmentField.setEditable(editable);
        jobTitleField.setEditable(editable);
        statusField.setEditable(editable);
        dobField.setEditable(editable);
        genderComboBox.setEnabled(editable);
        payTypeComboBox.setEnabled(editable);
        emailField.setEditable(editable);
        addressLine1Field.setEditable(editable);
        addressLine2Field.setEditable(editable);
        cityField.setEditable(editable);
        stateField.setEditable(editable);
        zipField.setEditable(editable);
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

    
    private void updateEmployee(int employeeId) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String department = departmentField.getText();
        String jobTitle = jobTitleField.getText();
        String status = statusField.getText();
        String dob = dobField.getText();
        String gender = (String) genderComboBox.getSelectedItem();
        String payType = (String) payTypeComboBox.getSelectedItem();
        String email = emailField.getText();
        String addressLine1 = addressLine1Field.getText();
        String addressLine2 = addressLine2Field.getText();
        String city = cityField.getText();
        String state = stateField.getText();
        String zip = zipField.getText();

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "UPDATE employees SET first_name = ?, last_name = ?, department = ?, job_title = ?, status = ?, " +
                 "date_of_birth = ?, gender = ?, pay_type = ?, company_email = ?, address_line1 = ?, " +
                 "address_line2 = ?, city = ?, state = ?, zip = ? WHERE employee_id = ?")) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, department);
            stmt.setString(4, jobTitle);
            stmt.setString(5, status);
            stmt.setString(6, dob);
            stmt.setString(7, gender);
            stmt.setString(8, payType);
            stmt.setString(9, email);
            stmt.setString(10, addressLine1);
            stmt.setString(11, addressLine2);
            stmt.setString(12, city);
            stmt.setString(13, state);
            stmt.setString(14, zip);
            stmt.setInt(15, employeeId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Employee updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Employee update failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating employee!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
