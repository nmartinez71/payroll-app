package com.payrollapp;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class OptionsPanel extends JPanel {
    public OptionsPanel(JFrame parentFrame) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton signOutButton = new JButton("Sign Out");
        signOutButton.addActionListener((ActionEvent e) -> {
            // Close the current window and return to the login screen
            parentFrame.dispose();
            new MainLoginApp().setVisible(true);
        });
        add(signOutButton);
    }
}