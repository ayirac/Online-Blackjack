package com.clientblackjack.gui;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.*;

public class LoginPanel {
    public static final String SUBMIT = "submit";
    public static final String[] BUTTONS = {SUBMIT};
    JButton backButton = new JButton("Back");
    private JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();

    public JLabel loginLabel = new JLabel("Login");
    public JLabel usernameLabel = new JLabel("Username");
    public JLabel passwordLabel = new JLabel("Password");
    public JTextField usernameField = new JTextField();
    public JPasswordField passwordField = new JPasswordField();
    public JButton loginBtn = new JButton("Submit");

    public LoginPanel(ActionListener listener) {
        loginBtn.setName(SUBMIT);
        backButton.setName("back");

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);
        // how big the username and password fields will be
        passwordField.setPreferredSize(new Dimension(200, 20));
        usernameField.setPreferredSize(new Dimension(200, 20));
        // adding login label to the login panel
        loginLabel.setHorizontalAlignment(JLabel.CENTER);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(loginLabel, constraints);
        // adding username label to the login panel
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(usernameLabel, constraints);
        // adding username field to the login panel
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(usernameField, constraints);
        // adding password label to the login panel
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(passwordLabel, constraints);
        // adding password field to the login panel
        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(passwordField, constraints);
        // adding Login button to the login panel
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        panel.add(loginBtn, constraints);

        //adding back button.
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        panel.add(backButton, constraints);

        // enabling the login button to accept clicks
        loginBtn.addActionListener(listener);
        backButton.addActionListener(listener);

    }

    public JPanel getPanel() {
        return this.panel;
    }

    public String getUsername() {
        return this.usernameField.getText(); 
    }

    public String getPasswordHash() {
        return new String(this.passwordField.getPassword());
    }
}
