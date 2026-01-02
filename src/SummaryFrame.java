import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.Map;

public class SummaryFrame extends JFrame {

    public SummaryFrame() {

        //Window Settings
        setTitle("Weekly Summary");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Title
        JLabel title = new JLabel("Weekly Mood & Weather Summary", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));

        //Table
        String[] columns = {"Date", "Mood", "Weather"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        String token = "";
        try {
            Map<String, String> env = EnvLoader.loadEnv(".env");
            token = env.get("BEARER_TOKEN");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load token");
        }

        //7 days data
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);

            String journalText = "Today was a good day.";

            String moodResult = "Unknown";
            String weatherResult = "Unknown";

            try {
                moodResult = mood.getMood(journalText, token);
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

        //Layout
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

}
