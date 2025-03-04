package com.payrollapp.Admin;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.payrollapp.DatabaseHelper;

public class PayrollPanel extends JPanel {
    private JTextField empIdField;
    private JTextField hoursWorkedField;
    private JTextField resultField;

    public PayrollPanel() {
        setLayout(new GridLayout(20, 2));

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

    public PayrollPanel(JTextField empIdField, JTextField hoursWorkedField, JTextField resultField) {
        this.empIdField = empIdField;
        this.hoursWorkedField = hoursWorkedField;
        this.resultField = resultField;
    }

    private void calculatePayroll() {
        try {
            int employeeId = Integer.parseInt(empIdField.getText());
            double hoursWorked = Double.parseDouble(hoursWorkedField.getText());

           
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
            
            double regularHours = Math.min(hoursWorked, 8);
            double overtimeHours = Math.max(hoursWorked - 8, 0);
            return (regularHours * 15) + (overtimeHours * 15 * 1.5); 
        } else if (payType.equalsIgnoreCase("Salary")) {
            
            return 8 * 5 * 15;
        }
        return 0;
    }

    private double calculateNetPay(double grossPay) {
        double stateTax = grossPay * 0.0315;
        double federalTax = grossPay * 0.0765;
        double socialSecurity = grossPay * 0.062;
        double medicare = grossPay * 0.0145;

        return grossPay - (stateTax + federalTax + socialSecurity + medicare);
    }

    public JTextField getEmpIdField() {
        return empIdField;
    }

    public void setEmpIdField(JTextField empIdField) {
        this.empIdField = empIdField;
    }
}