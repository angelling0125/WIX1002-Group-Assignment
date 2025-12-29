
import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

public class journalpage {

    static Scanner sc = new Scanner(System.in);

    public static void openJournalPage() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(3); // sample: past 3 days

        LocalDate[] dates = new LocalDate[4];
        for (int i = 0; i < 4; i++) {
            dates[i] = start.plusDays(i);
        }

        System.out.println("=== Journal Dates ===");
        for (int i = 0; i < dates.length; i++) {
            if (dates[i].equals(today))
                System.out.println((i + 1) + ". " + dates[i] + " (Today)");
            else
                System.out.println((i + 1) + ". " + dates[i]);
        }

        System.out.println("Select a date to view journal, or create a new journal for today:");
        int choice = sc.nextInt();
        sc.nextLine();

        LocalDate selectedDate = dates[choice - 1];

        if (selectedDate.equals(today)) {
            todayJournal(selectedDate);
        } else {
            viewJournal(selectedDate);
        }
    }

    static void todayJournal(LocalDate date) {
        File file = getFile(date);

        if (!file.exists()) {
            System.out.println("Enter your journal entry for " + date + ":");
            String entry = sc.nextLine();
            saveJournal(file, entry);
            System.out.println("Journal saved successfully!");
        }

        System.out.println("Would you like to:");
        System.out.println("1. View Journal");
        System.out.println("2. Edit Journal");
        System.out.println("3. Back to Dates");

        int option = sc.nextInt();
        sc.nextLine();

        if (option == 1) {
            viewJournal(date);
        } else if (option == 2) {
            editJournal(date);
        }
    }

    static void viewJournal(LocalDate date) {
        File file = getFile(date);

        if (!file.exists()) {
            System.out.println("No journal entry found.");
            return;
        }

        System.out.println("=== Journal Entry for " + date + " ===");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
        } catch (IOException e) {
        }

        System.out.println("\nPress Enter to go back.");
        sc.nextLine();
    }

    static void editJournal(LocalDate date) {
        File file = getFile(date);

        System.out.println("Edit your journal entry for " + date + ":");
        String newEntry = sc.nextLine();
        saveJournal(file, newEntry);
        System.out.println("Journal updated successfully!");
    }

    static File getFile(LocalDate date) {
        File folder = new File("journals");
        if (!folder.exists())
            folder.mkdir();
        return new File("journals/" + date + ".txt");
    }

    static void saveJournal(File file, String text) {
        try {
            PrintWriter pw = new PrintWriter(file);
            pw.println(text);
            pw.close();
        } catch (IOException e) {
        }
    }
}