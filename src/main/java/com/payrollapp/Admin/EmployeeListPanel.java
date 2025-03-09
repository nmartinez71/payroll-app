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
    
    private static final int COLUMN_COUNT = 20; 
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
            
            while (rs.next()) { 
                
                add(new JLabel(""));  
                add(new JLabel(rs.getString("employee_id")));  
                add(new JLabel(rs.getString("first_name")));   
                add(new JLabel(rs.getString("last_name")));    
                add(new JLabel(rs.getString("department")));   
                add(new JLabel(rs.getString("job_title")));    
                add(new JLabel(rs.getString("status")));       
                add(new JLabel(rs.getString("date_of_birth"))); 
                add(new JLabel(rs.getString("gender")));       
                add(new JLabel(rs.getString("pay_type")));     
                add(new JLabel(rs.getString("company_email"))); 
                add(new JLabel(rs.getString("address_line1"))); 
                add(new JLabel(rs.getString("address_line2"))); 
                add(new JLabel(rs.getString("city")));         
                add(new JLabel(rs.getString("state")));        
                add(new JLabel(rs.getString("zip")));          
                add(new JLabel(rs.getString("pay_info")));
                add(new JLabel(rs.getString("hourly_rate")));
                add(new JLabel(rs.getString("medical_coverage")));
                add(new JLabel(rs.getString("num_dependents")));
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching employee data.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        
        revalidate();
        repaint();
        System.out.println(ROW_COUNT);
    }
    
        private void removeEmployeeRows() {
        
        removeAll();
        
        JButton fetchButton = new JButton("Re-Fetch Employees");
        fetchButton.addActionListener(e -> pullEmployee());
        add(fetchButton);
        
        add(new JLabel("Employee ID:"));
        add(new JLabel("First Name:"));
        add(new JLabel("Last Name:"));
        add(new JLabel("Department:"));
        add(new JLabel("Job Title:"));
        add(new JLabel("Status:"));
        add(new JLabel("Date of Birth:"));
        add(new JLabel("Gender:"));
        add(new JLabel("Pay Type:"));
        add(new JLabel("Company Email:"));
        add(new JLabel("Address Line 1:"));
        add(new JLabel("Address Line 2:"));
        add(new JLabel("City:"));
        add(new JLabel("State:"));
        add(new JLabel("Zip Code:"));
        add(new JLabel("Hours Worked:"));
        add(new JLabel("Hourly Rate:"));
        add(new JLabel("Medical Plan:"));
        add(new JLabel("Dependents:"));
        
        revalidate();
        repaint();
    }   


}