package src;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TimeEntryPanel extends JPanel {
    public TimeEntryPanel() {
        setLayout(new GridLayout(10, 2));

        add(new JLabel("Employee ID:"));
        JTextField empIdField = new JTextField();
        add(empIdField);

        add(new JLabel("Hours Worked:"));
        JTextField hoursWorkedField = new JTextField();
        add(hoursWorkedField);

        add(new JLabel("PTO Hours:"));
        JTextField ptoField = new JTextField();
        add(ptoField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                int employeeId = Integer.parseInt(empIdField.getText());
                double hoursWorked = Double.parseDouble(hoursWorkedField.getText());
                double ptoHours = Double.parseDouble(ptoField.getText());

                try (Connection conn = DatabaseHelper.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(
                             "INSERT INTO payroll (employee_id, hours_worked, pto_hours, gross_pay, net_pay) " +
                                     "VALUES (?, ?, ?, ?, ?)")) {

                    stmt.setInt(1, employeeId);
                    stmt.setDouble(2, hoursWorked);
                    stmt.setDouble(3, ptoHours);
                    stmt.setDouble(4, 0); // Placeholder for gross pay
                    stmt.setDouble(5, 0); // Placeholder for net pay

                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Time entry saved successfully!");
                }
            } catch (NumberFormatException | SQLException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input or database error!", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        add(saveButton);
    }
}