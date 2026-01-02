import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.util.Map;

public class SummaryFrame extends JFrame {

    public SummaryFrame(String username) {

        // Window Settings
        setTitle("Weekly Summary");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Title
        JLabel title = new JLabel("Weekly Mood & Weather Summary", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));

        // Table
        String[] columns = {"Date", "Mood", "Weather"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Load token
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

            String journalText = readJournal(username, date);

            String moodResult = "Unknown";
            String weatherResult = "Unknown";

            try {
                if (journalText != null && !journalText.trim().isEmpty()
                        && token != null && !token.isEmpty()) {

                    moodResult = mood.getMood(journalText, token);
                }
                
                weatherResult = WeatherExtraction.getTodayWeather("WP%20Kuala%20Lumpur");
            } catch (Exception e) {
                e.printStackTrace();
            }

            model.addRow(new Object[]{
                    date.toString(),
                    moodResult,
                    weatherResult
            });
        }

        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Read journal
    private String readJournal(String username, LocalDate date) {
        File file = new File("journals/" + username + "/" + date + ".txt");

        if (!file.exists()) {
            return "";
        }

        StringBuilder content = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append(" ");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }

}
