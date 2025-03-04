package com.payrollapp.Employee;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.payrollapp.Admin.AdminPanel;
import com.payrollapp.EmployeeInfoPanel;
import com.payrollapp.OptionsPanel;

public class EmployeeApp extends JFrame {
    public EmployeeApp(String employeeId, boolean isAdmin) {  // Add isAdmin flag to check if the user is admin
        setTitle("Employee Panel");
        setSize(1200, 800);
        /* setResizable(false); */
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        EmployeeInfoPanel employeeInfoPanel = new EmployeeInfoPanel(employeeId);
        add(employeeInfoPanel, BorderLayout.WEST);

        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Time Entry", new TimeEntryPanel());
        tabbedPane.addTab("Paycheck Calculation", new PaycheckPanel());
        tabbedPane.addTab("PTO", new PTOPanel());
        tabbedPane.addTab("Options", new OptionsPanel(this));

        
        if (isAdmin) {
            AdminPanel adminPanel = new AdminPanel(employeeId);
            tabbedPane.addTab("Admin Panel", adminPanel);  
        }

        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }
}

