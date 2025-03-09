package com.payrollapp.Admin;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class EmployeeManagementPanel extends JPanel {

    private EmployeeListPanel employeeListPanel;

    public EmployeeManagementPanel() {
        setLayout(new BorderLayout());

        employeeListPanel = new EmployeeListPanel();

        JTabbedPane tabbedPane1 = new JTabbedPane();
        JScrollPane scrollPane = new JScrollPane(employeeListPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tabbedPane1.addTab("Employee List", scrollPane);

        JTabbedPane tabbedPane2 = new JTabbedPane();
        tabbedPane2.addTab("Add Employee", new AddEmployeePanel(employeeListPanel));
        tabbedPane2.addTab("Edit Employee", new EditEmployeePanel(employeeListPanel));
        tabbedPane2.addTab("Delete Employee", new DeleteEmployeePanel(employeeListPanel));
        tabbedPane2.addTab("Enter Employee Time", new UpdateEmployeeHoursPanel(employeeListPanel));
        tabbedPane2.addTab("Employee Payroll", new PayrollEmployeePanel(employeeListPanel));

        add(tabbedPane1, BorderLayout.CENTER);
        add(tabbedPane2, BorderLayout.SOUTH);
    }
}