package com.payrollapp.Admin;

import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.payrollapp.DatabaseHelper;
import com.payrollapp.PayrollCalculator;

public class PayrollEmployeePanel extends JPanel {
    private JTextField employeeIdField;
    private JComboBox<String> weekDropdown;
    private JTextField hoursWorkedField;
    private JTextField resultField;
    private JButton calculateButton;
    private JButton generateReportButton;
    private JButton generatePayrollFileButton;
    private Set<String> lockedEntries = new HashSet<>(); 
    private StringBuilder payrollReport; 

    public PayrollEmployeePanel(EmployeeListPanel employeeListPanel) {
        setLayout(new GridLayout(20, 2));

        add(new JLabel("Employee ID:"));
        employeeIdField = new JTextField();
        add(employeeIdField);

        add(new JLabel("Select Week:"));
        weekDropdown = new JComboBox<>(generateWeeks());
        add(weekDropdown);

        add(new JLabel("Hours Worked (Week):"));
        hoursWorkedField = new JTextField();
        hoursWorkedField.setEditable(false);
        add(hoursWorkedField);

        calculateButton = new JButton("Calculate Payroll");
        calculateButton.addActionListener(e -> calculatePayroll());
        add(calculateButton);

        add(new JLabel("Payroll Details:"));
        resultField = new JTextField();
        resultField.setEditable(false);
        add(resultField);

        
        generateReportButton = new JButton("Generate HR Report");
        generateReportButton.addActionListener(e -> generateHRReport());
        add(generateReportButton);

        
        generatePayrollFileButton = new JButton("Generate Payroll File");
        generatePayrollFileButton.addActionListener(e -> generatePayrollFile());
        add(generatePayrollFileButton);
    }

    private void calculatePayroll() {
        String employeeIdStr = employeeIdField.getText();
        String selectedWeek = (String) weekDropdown.getSelectedItem();

        if (employeeIdStr.isEmpty() || selectedWeek == null) {
            JOptionPane.showMessageDialog(this, "Please enter an Employee ID and select a week.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Employee ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String employeeWeekKey = employeeId + "-" + selectedWeek;

        if (lockedEntries.contains(employeeWeekKey)) {
            JOptionPane.showMessageDialog(this, "Payroll for this week is already locked.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseHelper.getConnection()) {
            
            PreparedStatement employeeStmt = conn.prepareStatement(
                "SELECT hourly_rate, medical_coverage, num_dependents FROM employees WHERE employee_id = ?");
            employeeStmt.setInt(1, employeeId);
            ResultSet empRs = employeeStmt.executeQuery();

            if (!empRs.next()) {
                JOptionPane.showMessageDialog(this, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double hourlyRate = empRs.getDouble("hourly_rate");
            String medicalCoverage = empRs.getString("medical_coverage");
            int numDependents = empRs.getInt("num_dependents");

            
            PreparedStatement hoursStmt = conn.prepareStatement(
                "SELECT hours_worked FROM employee_hours WHERE employee_id = ? AND week = ?");
            hoursStmt.setInt(1, employeeId);
            hoursStmt.setInt(2, Integer.parseInt(selectedWeek));
            ResultSet hoursRs = hoursStmt.executeQuery();

            if (!hoursRs.next()) {
                JOptionPane.showMessageDialog(this, "No hours recorded for this week.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] hoursArray = hoursRs.getString("hours_worked").split(",");
            int[] hoursWorked = new int[7];
            for (int i = 0; i < 7; i++) {
                hoursWorked[i] = Integer.parseInt(hoursArray[i].trim());
            }

            
            double grossPay = PayrollCalculator.calculatePay(hourlyRate, hoursWorked);
            double stateTax = PayrollCalculator.calculateStateTax(grossPay);
            double federalTax = PayrollCalculator.calculateFederalTax(grossPay);
            double socialSecurityTax = PayrollCalculator.calculateSocialSecurityTax(grossPay);
            double medicareTax = PayrollCalculator.calculateMedicareTax(grossPay);
            double medicalCost = PayrollCalculator.calculateMedicalCost(medicalCoverage);
            double dependentStipend = PayrollCalculator.calculateDependentStipend(numDependents);
            double netPay = grossPay - (stateTax + federalTax + socialSecurityTax + medicareTax + medicalCost) + dependentStipend;

            
            lockedEntries.add(employeeWeekKey);
            hoursWorkedField.setText(String.join(", ", hoursArray));
            hoursWorkedField.setEditable(false);

            
            resultField.setText(String.format("Gross Pay: $%.2f, Net Pay: $%.2f", grossPay, netPay));

            
            payrollReport = new StringBuilder();
            payrollReport.append("Employee ID: " + employeeId)
                .append("\nWeek: " + selectedWeek)
                .append("\nGross Pay: $" + grossPay)
                .append("\nState Tax: $" + stateTax)
                .append("\nFederal Tax: $" + federalTax)
                .append("\nSocial Security Tax: $" + socialSecurityTax)
                .append("\nMedicare Tax: $" + medicareTax)
                .append("\nMedical Cost: $" + medicalCost)
                .append("\nDependent Stipend: $" + dependentStipend)
                .append("\nNet Pay: $" + netPay);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving payroll data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void generateHRReport() {
        if (payrollReport == null || payrollReport.length() == 0) {
            JOptionPane.showMessageDialog(this, "No payroll data available to generate report.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            File reportFile = new File("payroll_report.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(reportFile));
            writer.write(payrollReport.toString());
            writer.close();
            JOptionPane.showMessageDialog(this, "HR report generated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error generating HR report.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void generatePayrollFile() {
        if (payrollReport == null || payrollReport.length() == 0) {
            JOptionPane.showMessageDialog(this, "No payroll data available to generate file.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            File payrollFile = new File("payroll_file.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(payrollFile));
            writer.write(payrollReport.toString());
            writer.close();
            JOptionPane.showMessageDialog(this, "Payroll file generated for printing!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error generating payroll file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String[] generateWeeks() {
        String[] weeks = new String[52];
        for (int i = 1; i <= 52; i++) {
            weeks[i - 1] = String.valueOf(i);
        }
        return weeks;
    }
}
