package src.Admin;
import javax.swing.*;

import src.DatabaseHelper;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PayrollPanel extends JPanel {
    private JTextField empIdField;
    private JTextField hoursWorkedField;
    private JTextField resultField;

    public PayrollPanel() {
        setLayout(new GridLayout(10, 2));

        add(new JLabel("Employee ID:"));
        empIdField = new JTextField();
        add(empIdField);

        add(new JLabel("Hours Worked:"));
        hoursWorkedField = new JTextField();
        add(hoursWorkedField);

        JButton calculateButton = new JButton("Calculate Payroll");
        calculateButton.addActionListener(e -> calculatePayroll());
        add(calculateButton);

        resultField = new JTextField();
        resultField.setEditable(false);
        add(new JLabel("Net Pay:"));
        add(resultField);
    }

    private void calculatePayroll() {
        try {
            int employeeId = Integer.parseInt(empIdField.getText());
            double hoursWorked = Double.parseDouble(hoursWorkedField.getText());

            // Fetch employee details from the database
            try (Connection conn = DatabaseHelper.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT pay_type FROM employees WHERE employee_id = ?")) {
                stmt.setInt(1, employeeId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String payType = rs.getString("pay_type");
                    double grossPay = calculateGrossPay(hoursWorked, payType);
                    double netPay = calculateNetPay(grossPay);

                    resultField.setText(String.format("$%.2f", netPay));
                } else {
                    JOptionPane.showMessageDialog(this, "Employee not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input or database error!", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private double calculateGrossPay(double hoursWorked, String payType) {
        if (payType.equalsIgnoreCase("Hourly")) {
            // Calculate overtime (1.5x for hours beyond 8/day or on Saturdays)
            double regularHours = Math.min(hoursWorked, 8);
            double overtimeHours = Math.max(hoursWorked - 8, 0);
            return (regularHours * 15) + (overtimeHours * 15 * 1.5); // Assuming $15/hour
        } else if (payType.equalsIgnoreCase("Salary")) {
            // Salary employees are paid 8 hours/day (Monday to Friday)
            return 8 * 5 * 15; // Assuming $15/hour for 5 days
        }
        return 0;
    }

    private double calculateNetPay(double grossPay) {
        // Deductions: State Tax (3.15%), Federal Tax (7.65%), Social Security (6.2%), Medicare (1.45%)
        double stateTax = grossPay * 0.0315;
        double federalTax = grossPay * 0.0765;
        double socialSecurity = grossPay * 0.062;
        double medicare = grossPay * 0.0145;

        return grossPay - (stateTax + federalTax + socialSecurity + medicare);
    }
}