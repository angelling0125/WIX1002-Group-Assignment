/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.welcomeframe;

/**
 *
 * @author weini
 */
import javax.swing.*;
import java.awt.*;
import java.time.*;

public class WelcomeFrame extends JFrame {

    public WelcomeFrame(String name) {

        //Window settings
        setTitle("Smart Journal");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Code logic
        LocalTime time = LocalTime.now(ZoneId.of("GMT+8"));
        int hour = time.getHour();
        String greeting;

        if (hour < 12) {
            greeting = "Good Morning";
        } else if (hour < 17) {
            greeting = "Good Afternoon";
        } else {
            greeting = "Good Evening";
        }

        //Main panel
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel greetingLabel = new JLabel(greeting + " " + name, SwingConstants.CENTER);
        greetingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        greetingLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // horizontal centering for BoxLayout

        JLabel titleLabel = new JLabel("Welcome to Smart Journal", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(greetingLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalGlue());

        //Buttons
        JButton journalButton = new JButton("Create, Edit & View Journal");
        JButton summaryButton = new JButton("View Weekly Mood Summary");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        journalButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        summaryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(journalButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(summaryButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // bottom spacing

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

}
