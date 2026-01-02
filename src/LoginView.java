import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Login fields
    private JTextField loginEmail;
    private JPasswordField loginPassword;
    private JLabel loginMsg;

    // Register fields
    private JTextField regEmail;
    private JTextField regName;
    private JPasswordField regPassword;
    private JLabel regMsg;

    private Login loginService;

    public LoginView(Login loginService) {
        this.loginService = loginService;

        setTitle("Smart Journal");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "LOGIN");
        mainPanel.add(createRegisterPanel(), "REGISTER");

        add(mainPanel);
        setVisible(true);
    }

    /* ======================
       LOGIN PANEL
       ====================== */
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("Smart Journal", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        panel.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(6, 1, 8, 5));

        form.add(new JLabel(""));

        form.add(new JLabel("Email"));
        loginEmail = new JTextField();
        form.add(loginEmail);

        form.add(new JLabel("Password"));
        loginPassword = new JPasswordField();
        form.add(loginPassword);

        JCheckBox showLoginPassword = new JCheckBox("Show password");

        showLoginPassword.addActionListener(e -> {
            if (showLoginPassword.isSelected()) {
                loginPassword.setEchoChar((char) 0); // show text
            } else {
                loginPassword.setEchoChar('*'); // hide text
            }
        });
        form.add(showLoginPassword);

        panel.add(form, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));

        JButton loginBtn = new JButton("Login");
        JButton goRegisterBtn = new JButton("Create Account");

        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        goRegisterBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginMsg = new JLabel(" ");
        loginMsg.setForeground(Color.RED);
        loginMsg.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottom.add(Box.createVerticalStrut(10));
        bottom.add(loginBtn);
        bottom.add(Box.createVerticalStrut(8));
        bottom.add(goRegisterBtn);
        bottom.add(Box.createVerticalStrut(10));
        bottom.add(loginMsg);

        panel.add(bottom, BorderLayout.SOUTH);

        // EVENTS
        loginBtn.addActionListener(e -> handleLogin());
        goRegisterBtn.addActionListener(e -> cardLayout.show(mainPanel, "REGISTER"));

        return panel;
    }

    /* ======================
       REGISTER PANEL
       ====================== */
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("Register Account", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(7, 1, 8, 5));

        form.add(new JLabel("Email"));
        regEmail = new JTextField();
        form.add(regEmail);

        form.add(new JLabel("Name"));
        regName = new JTextField();
        form.add(regName);

        form.add(new JLabel("Password"));
        regPassword = new JPasswordField();
        form.add(regPassword);

        JCheckBox showRegPassword = new JCheckBox("Show password");

        showRegPassword.addActionListener(e -> {
            if (showRegPassword.isSelected()) {
                regPassword.setEchoChar((char) 0); // show text
            } else {
                regPassword.setEchoChar('*'); // hide text
            }
        });
        form.add(showRegPassword);

        panel.add(form, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));

        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back to Login");

        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        regMsg = new JLabel(" ");
        regMsg.setForeground(Color.RED);
        regMsg.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottom.add(Box.createVerticalStrut(10));
        bottom.add(registerBtn);
        bottom.add(Box.createVerticalStrut(8));
        bottom.add(backBtn);
        bottom.add(Box.createVerticalStrut(10));
        bottom.add(regMsg);

        panel.add(bottom, BorderLayout.SOUTH);

        // EVENTS
        registerBtn.addActionListener(e -> handleRegister());
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));

        return panel;
    }

    /* ======================
       UI → LOGIC CALLS
       ====================== */
    private void handleLogin() {
        String email = loginEmail.getText();
        String password = new String(loginPassword.getPassword());

        User user = loginService.login(email, password);

        if (user != null) {
            // Successful login
            WelcomeFrame welcomeFrame = new WelcomeFrame(user.getEmail(), user.getName());
            this.dispose();
        } else {
            loginMsg.setText("Invalid email or password");
        }
    }

    private void handleRegister() {
        String email = regEmail.getText();
        String name = regName.getText();
        String password = new String(regPassword.getPassword());

        if (email.isEmpty() || name.isEmpty() || password.isEmpty()) {
            regMsg.setText("All fields are required");
            return;
        }

        boolean success = loginService.register(email, name, password);

        if (success) {
            JOptionPane.showMessageDialog(this, "Registration successful!");
            cardLayout.show(mainPanel, "LOGIN");
        } else {
            regMsg.setText("Email already exists");
        }
    }
}
