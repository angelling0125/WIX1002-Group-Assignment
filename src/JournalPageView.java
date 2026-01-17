import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

public class JournalPageView extends JFrame {

    private String userEmail;
    private JFrame welcomeFrame;

    private JList<String> dateList;
    private JTextArea journalArea;
    private DefaultListModel<String> listModel;
    private JLabel weatherLabel;
    private JLabel moodLabel;
    private String token;

    public JournalPageView(String userEmail, JFrame welcomeFrame) {
        this.userEmail = userEmail;
        this.welcomeFrame = welcomeFrame;

        setTitle("Journal");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Load token once
        try {
            Map<String, String> env = EnvLoader.loadEnv(".env");
            token = env.get("BEARER_TOKEN");
        } catch (Exception e) {
            token = "";
        }

        listModel = new DefaultListModel<>();
        loadDates();

        dateList = new JList<>(listModel);

        journalArea = new JTextArea();
        journalArea.setLineWrap(true);
        journalArea.setWrapStyleWord(true);

        weatherLabel = new JLabel("Weather: Unknown");
        moodLabel = new JLabel("Mood: Unknown");   
        
        weatherLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        moodLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JButton saveBtn = new JButton("Save / Update");
        JButton backBtn = new JButton("Back to Main Page");

        saveBtn.addActionListener(e -> saveJournal());
        backBtn.addActionListener(e -> {
            dispose();
            welcomeFrame.setVisible(true);
        });

        dateList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadJournal();
            }
        });

        /* ---------- LEFT PANEL (DATES) ---------- */
        JPanel left = new JPanel(new BorderLayout(5, 5));

        JPanel headerLeft = new JPanel(new GridLayout(2, 1));
        JLabel dateLabel = new JLabel("Select Journal Dates");
        JLabel viewDateLabel = new JLabel("(Click date to view journal)");

        headerLeft.add(dateLabel);
        headerLeft.add(viewDateLabel);
        left.add(headerLeft, BorderLayout.NORTH);
        left.add(new JScrollPane(dateList), BorderLayout.CENTER);

        /* ---------- RIGHT PANEL (JOURNAL) ---------- */
        JPanel right = new JPanel(new BorderLayout(5, 5));

        // Header: Journal Entry (left) + Weather (right)
        JPanel headerRight = new JPanel(new GridLayout(2, 2));
        JLabel journalLabel = new JLabel("Journal Entry");
        JLabel viewJournalLabel = new JLabel("(Click box to write / edit journal)");

        headerRight.add(journalLabel);
        headerRight.add(weatherLabel);
        headerRight.add(viewJournalLabel);
        headerRight.add(moodLabel);

        right.add(headerRight, BorderLayout.NORTH);
        right.add(new JScrollPane(journalArea), BorderLayout.CENTER);

        JPanel bottomRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomRight.add(saveBtn);
        bottomRight.add(backBtn);
        right.add(bottomRight, BorderLayout.SOUTH);

        /* ---------- SPLIT PANE ---------- */
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                left,
                right
        );
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.25);

        add(splitPane);
        setVisible(true);
    }

    /* ---------- LOAD DATES ---------- */
    private void loadDates() {
        listModel.clear();

        File folder = new File("journals/" + userEmail);
        if (!folder.exists()) folder.mkdirs();

        File[] files = folder.listFiles((d, name) -> name.endsWith(".txt"));
        if (files != null) {
            Arrays.sort(files);
            for (File f : files) {
                listModel.addElement(f.getName().replace(".txt", ""));
            }
        }

        String today = LocalDate.now().toString();
        if (!listModel.contains(today)) {
            listModel.addElement(today + " (Today)");
        }
    }

    /* ---------- LOAD JOURNAL ---------- */
    private void loadJournal() {
        String selected = dateList.getSelectedValue();
        if (selected == null) return;

        String date = selected.replace(" (Today)", "");
        File file = new File("journals/" + userEmail + "/" + date + ".txt");

        journalArea.setText("");
        weatherLabel.setText("Weather: Unknown");
        moodLabel.setText("Mood: Unknown");

        if (!file.exists()) return;

        StringBuilder journalText = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {

                // WEATHER LINE
                if (firstLine && line.startsWith("WEATHER:")) {
                    weatherLabel.setText("Weather: " + line.replace("WEATHER:", ""));
                    firstLine = false;
                    continue;
                }

                firstLine = false;
                // journalText.append(line).append(" ");
                journalArea.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ---------- MOOD CALCULATION (NOT STORED) ---------- */
    private void updateMood(String text) {
        if (text == null || text.trim().isEmpty() || token == null || token.isEmpty()) {
            moodLabel.setText("Mood: Unknown");
            return;
        }

        try {
            String moodResult = Mood.getMood(text, token);
            moodLabel.setText("Mood: " + moodResult);
        } catch (Exception e) {
            moodLabel.setText("Mood: Unknown");
        }
    }

    /* ---------- SAVE JOURNAL ---------- */
    private void saveJournal() {
        String selected = dateList.getSelectedValue();
        if (selected == null) return;

        String date = selected.replace(" (Today)", "");
        File file = new File("journals/" + userEmail + "/" + date + ".txt");

        String weather = "Unknown";
        updateMood(journalArea.getText());

        try {
            weather = WeatherExtraction.getTodayWeather("WP%20Kuala%20Lumpur");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println("WEATHER:" + weather);
            pw.println(journalArea.getText());
            JOptionPane.showMessageDialog(this, "Journal saved!");
            weatherLabel.setText("Weather: " + weather);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
