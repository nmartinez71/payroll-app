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

public class UpdateEmployeeHoursPanel extends JPanel {
    private JTextField employeeIdField;
    private JTextField[] hoursFields = new JTextField[7]; 
    private JComboBox<String> weekDropdown; 
    private JButton updateButton;

    public UpdateEmployeeHoursPanel(EmployeeListPanel employeeListPanel) {
        setLayout(new GridLayout(12, 2));

        
        add(new JLabel("Enter Employee ID:"));
        employeeIdField = new JTextField(10);
        add(employeeIdField);

        
        add(new JLabel("Select Week:"));
        weekDropdown = new JComboBox<>(generateWeeks());
        add(weekDropdown);

        
        String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for (int i = 0; i < 7; i++) {
            add(new JLabel("Hours Worked on " + daysOfWeek[i] + ":"));
            hoursFields[i] = new JTextField(5);
            add(hoursFields[i]);
        }

        
        updateButton = new JButton("Update Hours");
        updateButton.addActionListener(e -> updateEmployeeHours());
        add(updateButton);

        
        employeeIdField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                loadEmployeeHours();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                loadEmployeeHours();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                loadEmployeeHours();
            }
        });

        weekDropdown.addActionListener(e -> loadEmployeeHours());
    }

    
    private String[] generateWeeks() {
        String[] weeks = new String[52];
        for (int i = 1; i <= 52; i++) {
            weeks[i - 1] = "Week " + i;
        }
        return weeks;
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

    
    private void updateEmployeeHours() {
        String employeeIdText = employeeIdField.getText();
        if (employeeIdText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Employee ID is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Employee ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        int[] hoursWorked = getHoursWorkedArray();

        
        int selectedWeek = getSelectedWeek();

        
        saveEmployeeHours(employeeId, selectedWeek, hoursWorked);
    }

    
    private int getSelectedWeek() {
        String selectedWeek = (String) weekDropdown.getSelectedItem();
        return Integer.parseInt(selectedWeek.replace("Week ", ""));
    }

    
    private void loadEmployeeHours() {
        String employeeIdText = employeeIdField.getText();
        if (employeeIdText.isEmpty()) {
            return; 
        }

        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdText);
        } catch (NumberFormatException e) {
            return; 
        }

        int selectedWeek = getSelectedWeek();

        String query = "SELECT hours_worked FROM employee_hours WHERE employee_id = ? AND week = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, employeeId);
            stmt.setInt(2, selectedWeek);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hoursWorked = rs.getString("hours_worked");
                String[] hoursArray = hoursWorked.split(",");
                for (int i = 0; i < 7; i++) {
                    hoursFields[i].setText(hoursArray[i]);
                }
            } else {
                
                for (int i = 0; i < 7; i++) {
                    hoursFields[i].setText("");
                }
                JOptionPane.showMessageDialog(this, "No hours found for the selected employee and week.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading hours worked.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void saveEmployeeHours(int employeeId, int week, int[] hoursWorked) {
        StringBuilder hoursString = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            if (i > 0) hoursString.append(",");
            hoursString.append(hoursWorked[i]);
        }

        String query = "INSERT OR REPLACE INTO employee_hours (employee_id, week, hours_worked) " +
                       "VALUES (?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, employeeId);
            stmt.setInt(2, week);
            stmt.setString(3, hoursString.toString());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Hours updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating hours worked.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
