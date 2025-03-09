package com.payrollapp.Employee;

import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.payrollapp.DatabaseHelper;
import com.payrollapp.EmployeeClass;

public class TimeEntryPanel extends JPanel {
    private EmployeeClass employee; 
    private JTextField[] hoursFields = new JTextField[7]; 
    private JTextField[] ptoFields = new JTextField[5]; 
    private JButton updateButton;

    public TimeEntryPanel(EmployeeClass employee) {
        this.employee = employee;
        setLayout(new GridLayout(12, 2)); 

        
        String employeeName = employee.getFirstName() + " " + employee.getLastName();
        JLabel nameLabel = new JLabel("Employee: " + employeeName);
        add(nameLabel);

        
        JLabel hourlyRateLabel = new JLabel("Hourly Rate: $" + employee.getHourlyRate());
        add(hourlyRateLabel);

        JLabel medicalCoverageLabel = new JLabel("Medical Coverage: " + employee.getMedicalCoverage());
        add(medicalCoverageLabel);

        JLabel dependentsLabel = new JLabel("Number of Dependents: " + employee.getNumDependents());
        add(dependentsLabel);

        
        if ("Salary".equals(employee.getPayType())) {
            
            String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            for (int i = 0; i < 7; i++) {
                add(new JLabel("Hours Worked on " + daysOfWeek[i] + ":"));
                hoursFields[i] = new JTextField(5);
                if (i >= 1 && i <= 5) {
                    hoursFields[i].setEditable(true); 
                } else {
                    hoursFields[i].setEditable(false); 
                }
                if (i >= 1 && i <= 5) {
                    hoursFields[i].setText("0"); 
                }
                add(hoursFields[i]);
            }
        } else {
            
            String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            for (int i = 0; i < 7; i++) {
                add(new JLabel("Hours Worked on " + daysOfWeek[i] + ":"));
                hoursFields[i] = new JTextField(5);
                add(hoursFields[i]);
            }
        }

        
        updateButton = new JButton("Update Hours");
        updateButton.addActionListener(e -> updateEmployeeHours());
        add(updateButton);
    }

    
    private int[] getHoursWorkedArray() {
        int[] hoursWorked = new int[7];
        for (int i = 0; i < 7; i++) {
            try {
                hoursWorked[i] = Integer.parseInt(hoursFields[i].getText());
            } catch (NumberFormatException e) {
                hoursWorked[i] = 0; 
            }
        }
        return hoursWorked;
    }

    
    private int[] getPTOHoursArray() {
        int[] ptoHours = new int[5];
        for (int i = 1; i <= 5; i++) {
            try {
                ptoHours[i - 1] = Integer.parseInt(hoursFields[i].getText());
            } catch (NumberFormatException e) {
                ptoHours[i - 1] = 0; 
            }
        }
        return ptoHours;
    }

    
    private void updateEmployeeHours() {
        String employeeIdText = String.valueOf(employee.getEmployeeId());

        if (employeeIdText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Employee ID is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        if ("Salary".equals(employee.getPayType())) {
            int[] ptoHours = getPTOHoursArray();
            saveEmployeePTO(employeeIdText, ptoHours);
        } else {
            
            int[] hoursWorked = getHoursWorkedArray();
            saveEmployeeHours(employeeIdText, hoursWorked);
        }
    }

    
    private void saveEmployeeHours(String employeeId, int[] hoursWorked) {
        StringBuilder hoursString = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            if (i > 0) hoursString.append(",");
            hoursString.append(hoursWorked[i]);
        }

        String query = "INSERT OR REPLACE INTO employee_hours (employee_id, week, hours_worked) " +
                       "VALUES (?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, employeeId);
            stmt.setInt(2, getCurrentWeek());  
            stmt.setString(3, hoursString.toString());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Hours updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating hours worked.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void saveEmployeePTO(String employeeId, int[] ptoHours) {
        StringBuilder ptoString = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (i > 0) ptoString.append(",");
            ptoString.append(ptoHours[i]);
        }

        String query = "INSERT OR REPLACE INTO employee_pto (employee_id, week, pto_hours) " +
                       "VALUES (?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, employeeId);
            stmt.setInt(2, getCurrentWeek());  
            stmt.setString(3, ptoString.toString()); 

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "PTO updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating PTO.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private int getCurrentWeek() {
        java.time.LocalDate currentDate = java.time.LocalDate.now();
        java.time.temporal.WeekFields weekFields = java.time.temporal.WeekFields.of(java.util.Locale.US);
        return currentDate.get(weekFields.weekOfYear());
    }
}
