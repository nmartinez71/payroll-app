package com.payrollapp.Employee;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.payrollapp.Admin.EmployeeManagementPanel;
import com.payrollapp.EmployeeClass;
import com.payrollapp.EmployeeInfoPanel;
import com.payrollapp.OptionsPanel;

public class EmployeeApp extends JFrame {
    public EmployeeApp(EmployeeClass employee, boolean isAdmin) {  
        setTitle("Employee Panel");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        
        int employeeId = employee.getEmployeeId();  

        
        JTabbedPane tabbedPane2 = new JTabbedPane();
        EmployeeInfoPanel employeeInfoPanel = new EmployeeInfoPanel(String.valueOf(employeeId));
        tabbedPane2.addTab("Employee Info", employeeInfoPanel);
        
        JTabbedPane tabbedPane1 = new JTabbedPane();
        tabbedPane1.addTab("Time Entry", new TimeEntryPanel(employee)); 
        
        if (isAdmin) {
            EmployeeManagementPanel employeeScreen = new EmployeeManagementPanel();
            tabbedPane1.addTab("Employee Management", employeeScreen);  
        }

        tabbedPane1.addTab("Options", new OptionsPanel(this));
    
        
        add(tabbedPane1, BorderLayout.CENTER);
        add(tabbedPane2, BorderLayout.WEST);
    
        setVisible(true);
    }
}
