package com.payrollapp.Admin;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class AdminPanel extends JPanel {
    public AdminPanel(String employeeId) {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Employee Management", new EmployeeManagementPanel());
        tabbedPane.addTab("Payroll", new PayrollPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
}

