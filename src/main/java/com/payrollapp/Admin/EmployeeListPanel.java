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

import com.payrollapp.DatabaseHelper;

public class EmployeeListPanel extends JPanel {
    
    private static final int COLUMN_COUNT = 16; 
    private static final int ROW_COUNT = 0; 

    public EmployeeListPanel() {
        setLayout(new GridLayout(ROW_COUNT, COLUMN_COUNT)); 
    
        pullEmployee(); 
    }
    
    public void pullEmployee() {
        System.out.println("Pulling employees...");
        removeEmployeeRows();
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employees");
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {  // Increment row count for each employee
                // Add the employee data
                add(new JLabel(""));  // Empty placeholder for alignment (1st column)
                add(new JLabel(rs.getString("employee_id")));  // Employee ID
                add(new JLabel(rs.getString("first_name")));   // First Name
                add(new JLabel(rs.getString("last_name")));    // Last Name
                add(new JLabel(rs.getString("department")));   // Department
                add(new JLabel(rs.getString("job_title")));    // Job Title
                add(new JLabel(rs.getString("status")));       // Status
                add(new JLabel(rs.getString("date_of_birth"))); // Date of Birth
                add(new JLabel(rs.getString("gender")));       // Gender
                add(new JLabel(rs.getString("pay_type")));     // Pay Type
                add(new JLabel(rs.getString("company_email"))); // Company Email
                add(new JLabel(rs.getString("address_line1"))); // Address Line 1
                add(new JLabel(rs.getString("address_line2"))); // Address Line 2
                add(new JLabel(rs.getString("city")));         // City
                add(new JLabel(rs.getString("state")));        // State
                add(new JLabel(rs.getString("zip")));          // ZIP
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching employee data.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // After all components have been added, revalidate and repaint the panel
        revalidate();
        repaint();
        System.out.println(ROW_COUNT);
    }
    
    private void removeEmployeeRows() {
    // Clear all components from the panel
    removeAll();
    
    JButton fetchButton = new JButton("Re-Fetch Employees");
    fetchButton.addActionListener(e -> pullEmployee());
    add(fetchButton);
    // Re-add the header row and the button
    add(new JLabel("Employee ID:"));
    add(new JLabel("First Name:"));
    add(new JLabel("Last Name:"));
    add(new JLabel("Department:"));
    add(new JLabel("Job Title:"));
    add(new JLabel("Status:"));
    add(new JLabel("Date of Birth (YYYY-MM-DD):"));
    add(new JLabel("Gender:"));
    add(new JLabel("Pay Type:"));
    add(new JLabel("Company Email:"));
    add(new JLabel("Address Line 1:"));
    add(new JLabel("Address Line 2:"));
    add(new JLabel("City:"));
    add(new JLabel("State:"));
    add(new JLabel("Zip Code:"));
    
    revalidate();
    repaint();
}   


}
