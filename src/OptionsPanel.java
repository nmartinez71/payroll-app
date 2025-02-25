package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionsPanel extends JPanel {
    public OptionsPanel(JFrame parentFrame) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton signOutButton = new JButton("Sign Out");
        signOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the current window and return to the login screen
                parentFrame.dispose();
                new MainLoginApp().setVisible(true);
            }
        });
        add(signOutButton);
    }
}