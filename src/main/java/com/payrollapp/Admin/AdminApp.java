package com.payrollapp.Admin;
import javax.swing.*;

import com.payrollapp.OptionsPanel;

public class AdminApp extends JFrame {
    public AdminApp() {
        setTitle("Admin Panel");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Employee Management", new EmployeeManagementPanel());
        tabbedPane.addTab("Payroll", new PayrollPanel());
        tabbedPane.addTab("Options", new OptionsPanel(this));

        add(tabbedPane);
    }
}
