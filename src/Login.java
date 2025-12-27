import java.io.*;
import java.util.ArrayList;
import java.security.MessageDigest;

public class Login {

    private String filePath;
    // filePath: Stores the location of UserData.txt.

    private ArrayList<User> userList = new ArrayList<>();

    // Call this in the main method:
    public Login(String filePath) {
        this.filePath = filePath;
        loadUsers();
    }

    // For the first time (call once) only:
    private void loadUsers() {
        userList.clear();
        try (BufferedReader input = new BufferedReader(new FileReader(filePath))) {
            String line;
            ArrayList<String> buffer = new ArrayList<>();

            while ((line = input.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines.

                buffer.add(line.trim()); // Add line to temporary list.

                if (buffer.size() == 3) {
                    userList.add(new User(buffer.get(0), buffer.get(1), buffer.get(2)));
                    buffer.clear(); // Empty buffer for the next person.
                }
            }
        } catch (IOException e) {
            System.out.println("User file not found. A new one will be created upon registration.");
        }
    }

    // For GUI:
    public User login(String email, String passwordInput) {
        for (User user : userList) {
            if (user.getEmail().equals(email)) {
                String storedPassword = user.getPassword();

                if (storedPassword.equals(passwordInput)) {
                    return user;
                }

                String hashedInput = hashPassword(passwordInput);
                if (storedPassword.equals(hashedInput)) {
                    return user;
                }
            }
        }
        return null;
    }

    // For GUI:
    public boolean register(String email, String name, String password) {
        if (email.length() < 19 || !email.startsWith("s10020") || !email.endsWith("@student.fop")) {
            return false;
        }

        for (User user : userList) {
            if (user.getEmail().equals(email)) {
                return false;
            }
        }

        String finalPassword = hashPassword(password);

        try (PrintWriter output = new PrintWriter(new FileOutputStream(filePath, true))) {
            output.println(email);
            output.println(name);
            output.println(finalPassword);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        userList.add(new User(email, name, finalPassword));
        return true;
    }

    private String hashPassword(String originalPassword) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            // Create a variable named digest that holds a MessageDigest tool,
            // and fill it by asking the Security Factory to give us the SHA-256 model.

            byte[] encodedhash = digest.digest(originalPassword.getBytes());
            // 1. originalPassword.getBytes() takes the letters 'a', 'p', 'p', 'l', 'e' and turns them into their
            // numeric codes (ASCII number).
            // 2. digest.digest() acts like a blender, it changes the numeric code into totally different and random number.
            // Result: encodedhash is an array of raw numbers. It is NOT text yet.

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                // 1. The Problem: In Java, a byte is a tiny number that can be NEGATIVE.
                // 2. The Fix (0xff & b): This is a "Bitwise Mask" to treat it as positive because
                // Integer.toHexString(...): This only takes POSITIVE number and writes it in "Base-16" language.

                if (hex.length() == 1) hexString.append('0');
                // Check for consistency. We want "0a", not "a" (need two characters).

                hexString.append(hex);
                // append means "Add to the end."
            }
            return hexString.toString();

        } catch (Exception e) {
            return originalPassword;
        }
    }
}
