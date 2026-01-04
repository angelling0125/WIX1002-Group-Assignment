import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.util.Arrays;

public class JournalPageView extends JFrame {

    private String userEmail;
    private JFrame welcomeFrame; // reference to go back

    private JList<String> dateList;
    private JTextArea journalArea;
    private DefaultListModel<String> listModel;

    public JournalPageView(String userEmail, JFrame welcomeFrame) {
        this.userEmail = userEmail;
        this.welcomeFrame = welcomeFrame;

        setTitle("Journal");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        listModel = new DefaultListModel<>();
        loadDates();

        dateList = new JList<>(listModel);
        journalArea = new JTextArea();
        journalArea.setLineWrap(true);
        journalArea.setWrapStyleWord(true);

        JButton saveBtn = new JButton("Save / Update");
        JButton backBtn = new JButton("Back to Main Page");

        saveBtn.addActionListener(e -> saveJournal());
        backBtn.addActionListener(e -> {
            this.dispose();
            welcomeFrame.setVisible(true);
        });

        dateList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadJournal();
            }
        });

        // LEFT PANEL (DATES)
        JPanel left = new JPanel(new BorderLayout(5, 5));
        left.add(new JLabel("Select Journal Dates"), BorderLayout.NORTH);
        left.add(new JScrollPane(dateList), BorderLayout.CENTER);

        // RIGHT PANEL (JOURNAL)
        JPanel right = new JPanel(new BorderLayout(5, 5));
        right.add(new JLabel("Journal Entry"), BorderLayout.NORTH);
        right.add(new JScrollPane(journalArea), BorderLayout.CENTER);

        JPanel bottomRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomRight.add(saveBtn);
        bottomRight.add(backBtn);
        right.add(bottomRight, BorderLayout.SOUTH);

        // SPLIT PANE (unequal width)
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                left,
                right
        );
        splitPane.setDividerLocation(200); // left smaller
        splitPane.setResizeWeight(0.25);   // 25% left, 75% right

        add(splitPane);
        setVisible(true);
    }

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

    private void loadJournal() {
        String selected = dateList.getSelectedValue();
        if (selected == null) return;

        String date = selected.replace(" (Today)", "");
        File file = new File("journals/" + userEmail + "/" + date + ".txt");

        journalArea.setText("");

        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {

                // STEP 2: skip weather line
                if (firstLine && line.startsWith("WEATHER:")) {
                    firstLine = false;
                    continue;
                }

                firstLine = false;
                journalArea.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveJournal() {
        String selected = dateList.getSelectedValue();
        if (selected == null) return;

        String date = selected.replace(" (Today)", "");
        File file = new File("journals/" + userEmail + "/" + date + ".txt");

        // Get today's weather
        String weather = "Unknown";
        try {
            weather = WeatherExtraction.getTodayWeather("WP%20Kuala%20Lumpur");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Save weather + journal text
        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println("WEATHER:" + weather);   // <-- VERY IMPORTANT
            pw.println(journalArea.getText());
            JOptionPane.showMessageDialog(this, "Journal saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
