package com.payrollapp;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OptionsPanel extends JPanel {
    public OptionsPanel(JFrame parentFrame) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton signOutButton = new JButton("Sign Out");
        signOutButton.addActionListener((ActionEvent e) -> {
            
            parentFrame.dispose();
            new MainLoginApp().setVisible(true);
        });
        
        
        add(signOutButton);
        JLabel versionLabel = new JLabel("Version: 1.0.2"); 
        add(versionLabel);
    }
}
