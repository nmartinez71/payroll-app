package src.Admin;
import javax.swing.*;

public class EmployeeManagementPanel extends JPanel {
    public EmployeeManagementPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Add Employee", new AddEmployee());
        tabbedPane.addTab("Delete Employee", new DeleteEmployee());

        add(tabbedPane);
    }
}
