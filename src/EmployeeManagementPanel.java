package src;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class EmployeeManagementPanel extends JPanel {
    public EmployeeManagementPanel() {
        setLayout(new GridLayout(15, 2));

        // Add form fields for employee details
        add(new JLabel("First Name:"));
        JTextField firstNameField = new JTextField();
        add(firstNameField);

        add(new JLabel("Last Name:"));
        JTextField lastNameField = new JTextField();
        add(lastNameField);

        add(new JLabel("Department:"));
        JTextField departmentField = new JTextField();
        add(departmentField);

        add(new JLabel("Job Title:"));
        JTextField jobTitleField = new JTextField();
        add(jobTitleField);

        add(new JLabel("Status:"));
        JTextField statusField = new JTextField();
        add(statusField);

        add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        JTextField dobField = new JTextField();
        add(dobField);

        add(new JLabel("Gender:"));
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        add(genderComboBox);

        add(new JLabel("Pay Type:"));
        JComboBox<String> payTypeComboBox = new JComboBox<>(new String[]{"Salary", "Hourly"});
        add(payTypeComboBox);

        add(new JLabel("Company Email:"));
        JTextField emailField = new JTextField();
        add(emailField);

        add(new JLabel("Address Line 1:"));
        JTextField addressLine1Field = new JTextField();
        add(addressLine1Field);

        add(new JLabel("Address Line 2:"));
        JTextField addressLine2Field = new JTextField();
        add(addressLine2Field);

        add(new JLabel("City:"));
        JTextField cityField = new JTextField();
        add(cityField);

        add(new JLabel("State:"));
        JTextField stateField = new JTextField();
        add(stateField);

        add(new JLabel("ZIP:"));
        JTextField zipField = new JTextField();
        add(zipField);

        add(new JLabel("Picture:"));
        JButton pictureButton = new JButton("Select Picture");
        pictureButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Picture selection functionality goes here.");
        });
        add(pictureButton);

        // Save Button to insert data into the database
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // Validate inputs
            if (!validateInputs(firstNameField, lastNameField, dobField, emailField)) {
                return;
            }

            try (Connection conn = DatabaseHelper.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO employees (first_name, last_name, department, job_title, status, date_of_birth, gender, pay_type, company_email, address_line1, address_line2, city, state, zip) " +
                                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

                stmt.setString(1, firstNameField.getText());
                stmt.setString(2, lastNameField.getText());
                stmt.setString(3, departmentField.getText());
                stmt.setString(4, jobTitleField.getText());
                stmt.setString(5, statusField.getText());
                stmt.setString(6, dobField.getText());
                stmt.setString(7, (String) genderComboBox.getSelectedItem());
                stmt.setString(8, (String) payTypeComboBox.getSelectedItem());
                stmt.setString(9, emailField.getText());
                stmt.setString(10, addressLine1Field.getText());
                stmt.setString(11, addressLine2Field.getText());
                stmt.setString(12, cityField.getText());
                stmt.setString(13, stateField.getText());
                stmt.setString(14, zipField.getText());

                stmt.setNull(15, java.sql.Types.BLOB); // Placeholder for picture (BLOB)

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Employee saved successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        add(saveButton);
    }

    private boolean validateInputs(JTextField firstNameField, JTextField lastNameField, JTextField dobField, JTextField emailField) {
        // Validate name fields
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "First name and last name are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate date of birth (must be at least 18 years old)
        try {
            LocalDate dob = LocalDate.parse(dobField.getText(), DateTimeFormatter.ISO_DATE);
            LocalDate now = LocalDate.now();
            int age = Period.between(dob, now).getYears();
            if (age < 18) {
                JOptionPane.showMessageDialog(this, "Employee must be at least 18 years old!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format! Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate email format
        if (!emailField.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}
