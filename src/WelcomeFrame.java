import javax.swing.*;
import java.awt.*;
import java.time.*;

public class WelcomeFrame extends JFrame {

    private String email;

    public WelcomeFrame(String email, String name) {
        this.email = email;

        setTitle("Smart Journal");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Greeting logic
        LocalTime time = LocalTime.now(ZoneId.of("GMT+8"));
        int hour = time.getHour();
        String greeting;

        if (hour < 12) greeting = "Good Morning";
        else if (hour < 17) greeting = "Good Afternoon";
        else greeting = "Good Evening";

        // MAIN PANEL
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel greetingLabel = new JLabel(greeting + ", " + name);
        greetingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        greetingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Welcome to Smart Journal");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton journalButton = new JButton("Create, Edit & View Journal");
        JButton summaryButton = new JButton("View Weekly Mood Summary");
        JButton backButton = new JButton("Back to Login");

        journalButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        summaryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // BUTTON ACTIONS
        journalButton.addActionListener(e -> {
            new JournalPageView(email, this);
            dispose();
        });

        summaryButton.addActionListener(e -> {
            new SummaryFrame(email, this);
            setVisible(false);
        });

        backButton.addActionListener(e -> {
            new LoginView(new Login("UserData.txt"));
            dispose();
        });

        // ---- PERFECT CENTERING ----
        panel.add(Box.createVerticalGlue());

        panel.add(greetingLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(journalButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(summaryButton);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(backButton);

        panel.add(Box.createVerticalGlue());

        add(panel);
        setVisible(true);
    }
}
