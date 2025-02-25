package src.Employee;
import javax.swing.*;

import src.OptionsPanel;
import src.TimeEntryPanel;

public class EmployeeApp extends JFrame {
    public EmployeeApp() {
        setTitle("Employee Panel");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Time Entry", new TimeEntryPanel());
        tabbedPane.addTab("Paycheck Calculation", new PaycheckPanel());
        tabbedPane.addTab("Options", new OptionsPanel(this));

        add(tabbedPane);
    }
}