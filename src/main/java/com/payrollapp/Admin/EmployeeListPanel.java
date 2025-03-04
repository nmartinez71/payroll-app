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
        
    public EmployeeListPanel() {

        JButton fetchButton = new JButton("Re-Fetch Employees");
        fetchButton.addActionListener (e ->{

            
            pullEmployee();
        });
        add(fetchButton);

        setLayout(new GridLayout(0, 15, 1, 0));

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
        add(new JLabel("ZIP:"));

        pullEmployee();
    }

    private void pullEmployee() {

        for (int i = 15; i < getComponentCount(); i++) {
            remove(i);
        }

        try (Connection conn = DatabaseHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employees")) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
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

        revalidate();
        repaint();

        public void refreshEmployeeList() {
            pullEmployee();  
        }
    }
}
