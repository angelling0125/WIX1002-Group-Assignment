import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

public class journalpage {

    static Scanner sc = new Scanner(System.in);

    // called from Main after login
    public static void openJournalPage(String username) {

        File userFolder = new File("journals/" + username);
        if (!userFolder.exists()) {
            userFolder.mkdirs();
        }

        File[] journalFiles = userFolder.listFiles();
        LocalDate today = LocalDate.now();

        System.out.println("=== Journal Dates ===");

        int count = 0;
        LocalDate[] dates = new LocalDate[50]; // enough for Year 1

        if (journalFiles != null) {
            for (File f : journalFiles) {
                String name = f.getName();
                if (name.endsWith(".txt")) {
                    LocalDate d = LocalDate.parse(name.replace(".txt", ""));
                    dates[count++] = d;
                }
            }
        }

        for (int i = 0; i < count; i++) {
            System.out.println((i + 1) + ". " + dates[i]);
        }

        System.out.println((count + 1) + ". " + today + " (Today)");

        System.out.print("Select a date: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == count + 1) {
            todayJournal(username, today);
        } else if (choice >= 1 && choice <= count) {
            viewJournal(username, dates[choice - 1]);
        }
    }

    static void todayJournal(String username, LocalDate date) {
        File file = getFile(username, date);

        if (!file.exists()) {
            System.out.println("Enter your journal entry for " + date + ":");
            String entry = sc.nextLine();
            saveJournal(file, entry);
            System.out.println("Journal saved.");
            return;
        }

        System.out.println("1. View Journal");
        System.out.println("2. Edit Journal");
        System.out.print("Choose: ");

        int option = sc.nextInt();
        sc.nextLine();

        if (option == 1)
            viewJournal(username, date);
        else if (option == 2)
            editJournal(username, date);
    }

    static void viewJournal(String username, LocalDate date) {
        File file = getFile(username, date);

        if (!file.exists()) {
            System.out.println("No journal found.");
            return;
        }

        System.out.println("=== Journal Entry (" + date + ") ===");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
        } catch (IOException e) {
        }
    }

    static void editJournal(String username, LocalDate date) {
        File file = getFile(username, date);

        System.out.println("Edit journal:");
        String entry = sc.nextLine();
        saveJournal(file, entry);
        System.out.println("Journal updated.");
    }

    static File getFile(String username, LocalDate date) {
        File folder = new File("journals/" + username);
        if (!folder.exists())
            folder.mkdirs();
        return new File(folder, date + ".txt");
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
