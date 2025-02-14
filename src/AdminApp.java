package src;
import javax.swing.*;

public class AdminApp extends JFrame {
    public AdminApp() {
        setTitle("Admin Panel");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Employee Management", new EmployeeManagementPanel());
        tabbedPane.addTab("Payroll", new PayrollPanel());
        tabbedPane.addTab("Options", new OptionsPanel(this));

        add(tabbedPane);
    }
}
