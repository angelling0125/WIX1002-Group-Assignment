import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Login {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String correctEmail;
        while (true) {
            System.out.print("Enter your email: ");
            String email = sc.nextLine();
            if (email.length() < 19 || !email.startsWith("s10020") || !email.endsWith("@student.fop")) {
                System.out.println("Invalid email. Please try again");
            } else {
                correctEmail = email;
                break;
            }
        }

        System.out.print("Enter your username: ");
        String username = sc.nextLine();

        String finalPassword;
        while (true) {
            System.out.print("Enter your password: ");
            String password = sc.nextLine();
            if (password.length() < 5) {
                System.out.println("Password is too short. Please try again");
            } else {
                finalPassword = hashPassword(password);
                break;
            }
        }

        try {
            PrintWriter output = new PrintWriter(new FileOutputStream("./UserData.txt", true));

            output.println(correctEmail);
            output.println(username);
            output.println(finalPassword);
            output.close();
            System.out.println("Registration successful! Account created.");

        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<User> userList = new ArrayList<>();

        try {
            BufferedReader input = new BufferedReader(new FileReader("./UserData.txt"));
            String line;

            while ((line = input.readLine()) != null) {
                String email = line;
                String name = input.readLine();
                String pass = input.readLine();

                if (name != null && pass != null) {
                    userList.add(new User(email, name, pass));
                }
            }
            input.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\nCurrent Registered Users:\n");
        for (User user : userList) {
            System.out.println("Username: " + user.getUsername() + " | Email: " + user.getEmail());
            System.out.println("Hashed Password: " + user.getPassword());
            System.out.println("--------------------------------");
        }
    }

    private static String hashPassword(String originalPassword) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            // Create a variable named digest that holds a MessageDigest tool,
            // and fill it by asking the Security Factory to give us the SHA-256 model.

            byte[] encodedhash = digest.digest(originalPassword.getBytes());
            // 1. originalPassword.getBytes() takes the letters 'a', 'p', 'p', 'l', 'e' and turns them into their
            // numeric codes: [97, 112, 112, 108, 101].
            //
            // 2. digest.digest() acts like a blender. It takes those simple numbers, runs complex math on them,
            // and spits out a new array of completely different and random numbers (e.g., [-14, 55, 120, -5...]).
            //
            // Result: encodedhash is an array of raw numbers. It is NOT text yet.

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                // 1. The Problem: In Java, a byte is a tiny number that can be NEGATIVE (from -128 to 127).
                // Example: The blender (digest.digest) might give us the number -50.
                //
                // 2. The Goal: Hexadecimal codes (like color codes #FF0000) don't use negative signs. They treat everything as positive.
                // Integer.toHexString(...): This takes that POSITIVE number and writes it in "Base-16" language.
                // e.g. 206 becomes "ce", 10 becomes "a".
                //
                // 3. The Fix (0xff & b): This is a "Bitwise Mask".
                // Imagine 0xff is a filter or a stencil.
                // When we combine it with & (AND), it mathematically cuts off the "negative" part of the number and
                // forces it to look like a positive integer (0 to 255).
                // It turns the computer's internal representation of -50 into the positive number 206.

                if (hex.length() == 1) hexString.append('0');
                // 1. The Reason: Consistency.
                // We want every byte to always produce TWO characters of text.
                // If the number is big (like 255), we get "ff" (2 chars). Perfect.
                // If the number is small (like 10), we get "a" (1 char). Problem.
                //
                // 2. The Fix: The if condition checks: "Is this only 1 character long?"
                // If YES: We manually add a '0' in front.
                // Result: "a" becomes "0a".

                hexString.append(hex);
                // Answer: append means "Add to the end."
                // We are building the long chain.
                // First loop: append("3a") -> Notebook has "3a"
                // Second loop: append("7b") -> Notebook has "3a7b"
                // Third loop: append("d3") -> Notebook has "3a7bd3"
            }
            return hexString.toString();
            // Inside the loop, we created tiny Strings ("3a", "7b"), but we dumped them all into the StringBuilder (the Sketchpad).
            // The variable hexString is still a StringBuilder object, NOT a String.
            // Other parts of your program (like the file saver) expect a real String. They don't know how to handle a StringBuilder.
            // .toString() is the final step: It takes the content of the Sketchpad, laminates it, and hands you the final,
            // unchangeable String object.

        } catch (Exception e) {
            return originalPassword;
        }
    }
}import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Login {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String correctEmail;
        while (true) {
            System.out.print("Enter your email: ");
            String email = sc.nextLine();
            if (email.length() < 19 || !email.startsWith("s10020") || !email.endsWith("@student.fop")) {
                System.out.println("Invalid email. Please try again");
            } else {
                correctEmail = email;
                break;
            }
        }

        System.out.print("Enter your username: ");
        String username = sc.nextLine();

        String finalPassword;
        while (true) {
            System.out.print("Enter your password: ");
            String password = sc.nextLine();
            if (password.length() < 5) {
                System.out.println("Password is too short. Please try again");
            } else {
                finalPassword = hashPassword(password);
                break;
            }
        }

        try {
            PrintWriter output = new PrintWriter(new FileOutputStream("./UserData.txt", true));

            output.println(correctEmail);
            output.println(username);
            output.println(finalPassword);
            output.close();
            System.out.println("Registration successful! Account created.");

        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<User> userList = new ArrayList<>();

        try {
            BufferedReader input = new BufferedReader(new FileReader("./UserData.txt"));
            String line;

            while ((line = input.readLine()) != null) {
                String email = line;
                String name = input.readLine();
                String pass = input.readLine();

                if (name != null && pass != null) {
                    userList.add(new User(email, name, pass));
                }
            }
            input.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\nCurrent Registered Users:\n");
        for (User user : userList) {
            System.out.println("Username: " + user.getUsername() + " | Email: " + user.getEmail());
            System.out.println("Hashed Password: " + user.getPassword());
            System.out.println("--------------------------------");
        }
    }

    private static String hashPassword(String originalPassword) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            // Create a variable named digest that holds a MessageDigest tool,
            // and fill it by asking the Security Factory to give us the SHA-256 model.

            byte[] encodedhash = digest.digest(originalPassword.getBytes());
            // 1. originalPassword.getBytes() takes the letters 'a', 'p', 'p', 'l', 'e' and turns them into their
            // numeric codes: [97, 112, 112, 108, 101].
            //
            // 2. digest.digest() acts like a blender. It takes those simple numbers, runs complex math on them,
            // and spits out a new array of completely different and random numbers (e.g., [-14, 55, 120, -5...]).
            //
            // Result: encodedhash is an array of raw numbers. It is NOT text yet.

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                // 1. The Problem: In Java, a byte is a tiny number that can be NEGATIVE (from -128 to 127).
                // Example: The blender (digest.digest) might give us the number -50.
                //
                // 2. The Goal: Hexadecimal codes (like color codes #FF0000) don't use negative signs. They treat everything as positive.
                // Integer.toHexString(...): This takes that POSITIVE number and writes it in "Base-16" language.
                // e.g. 206 becomes "ce", 10 becomes "a".
                //
                // 3. The Fix (0xff & b): This is a "Bitwise Mask".
                // Imagine 0xff is a filter or a stencil.
                // When we combine it with & (AND), it mathematically cuts off the "negative" part of the number and
                // forces it to look like a positive integer (0 to 255).
                // It turns the computer's internal representation of -50 into the positive number 206.

                if (hex.length() == 1) hexString.append('0');
                // 1. The Reason: Consistency.
                // We want every byte to always produce TWO characters of text.
                // If the number is big (like 255), we get "ff" (2 chars). Perfect.
                // If the number is small (like 10), we get "a" (1 char). Problem.
                //
                // 2. The Fix: The if condition checks: "Is this only 1 character long?"
                // If YES: We manually add a '0' in front.
                // Result: "a" becomes "0a".

                hexString.append(hex);
                // Answer: append means "Add to the end."
                // We are building the long chain.
                // First loop: append("3a") -> Notebook has "3a"
                // Second loop: append("7b") -> Notebook has "3a7b"
                // Third loop: append("d3") -> Notebook has "3a7bd3"
            }
            return hexString.toString();
            // Inside the loop, we created tiny Strings ("3a", "7b"), but we dumped them all into the StringBuilder (the Sketchpad).
            // The variable hexString is still a StringBuilder object, NOT a String.
            // Other parts of your program (like the file saver) expect a real String. They don't know how to handle a StringBuilder.
            // .toString() is the final step: It takes the content of the Sketchpad, laminates it, and hands you the final,
            // unchangeable String object.

        } catch (Exception e) {
            return originalPassword;
        }
    }
}
