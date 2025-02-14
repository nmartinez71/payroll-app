package src;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaycheckPanel extends JPanel {
    public PaycheckPanel() {
        setLayout(new GridLayout(10, 2));

        add(new JLabel("Employee ID:"));
        JTextField empIdField = new JTextField();
        add(empIdField);

        JButton calculateButton = new JButton("Calculate Paycheck");
        calculateButton.addActionListener(e -> {
            try {
                int employeeId = Integer.parseInt(empIdField.getText());

                try (Connection conn = DatabaseHelper.getConnection();
                     PreparedStatement stmt = conn.prepareStatement("SELECT * FROM payroll WHERE employee_id = ?")) {
                    stmt.setInt(1, employeeId);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        double grossPay = rs.getDouble("gross_pay");
                        double netPay = rs.getDouble("net_pay");

                        JOptionPane.showMessageDialog(this,
                                "Gross Pay: $" + grossPay + "\nNet Pay: $" + netPay,
                                "Paycheck Details", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "No payroll data found for this employee!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException | SQLException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input or database error!", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        add(calculateButton);
    }
}
