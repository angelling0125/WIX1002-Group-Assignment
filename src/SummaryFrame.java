import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.util.Map;

public class SummaryFrame extends JFrame {

    private JFrame welcomeFrame;

    public SummaryFrame(String username, JFrame welcomeFrame) {
        this.welcomeFrame = welcomeFrame;

        // Window Settings
        setTitle("Weekly Summary");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Title
        JLabel title = new JLabel("Weekly Mood & Weather Summary", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));

        // Table
        String[] columns = {"Date", "Mood", "Weather"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        // Prevent table editing
        JTable table = new JTable(model) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane scrollPane = new JScrollPane(table);

        // Load token once
        String token = "";
        try {
            Map<String, String> env = EnvLoader.loadEnv(".env");
            token = env.get("BEARER_TOKEN");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to load BEARER_TOKEN.\nMood analysis will be disabled.");
        }

        // Last 7 days
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);

            JournalData data = readJournal(username, date);

            String moodResult = "Unknown";

            try {
                if (!data.text.isEmpty() && token != null && !token.isEmpty()) {
                    moodResult = Mood.getMood(data.text, token);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            model.addRow(new Object[]{
                    date.toString(),
                    moodResult,
                    data.weather
            });
        }

        JButton backBtn = new JButton("Back to main page");
        backBtn.addActionListener(e -> {
            welcomeFrame.setVisible(true);
            dispose();
        });
        
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(backBtn, BorderLayout.SOUTH);
        setVisible(true);
    }

    // Read journal
    private JournalData readJournal(String username, LocalDate date) {
        File file = new File("journals/" + username + "/" + date + ".txt");

        if (!file.exists()) {
            return new JournalData("", "Unknown");
        }

        StringBuilder content = new StringBuilder();
        String weather = "Unknown";

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            
            // Read weather from first line
            String firstLine = br.readLine();
            if (firstLine != null && firstLine.startsWith("WEATHER:")) {
                weather = firstLine.replace("WEATHER:", "").trim();
            }
            
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append(" ");
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JournalData(content.toString(), weather);
    }

    private static class JournalData {
        String text;
        String weather;

        JournalData(String text, String weather) {
            this.text = text;
            this.weather = weather;
        }
    }
}
