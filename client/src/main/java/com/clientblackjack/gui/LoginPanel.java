package com.clientblackjack.gui;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.*;

public class LoginPanel { // login panel class
    public static final String SUBMIT = "submit"; // submit string (immutable)
    public static final String[] BUTTONS = {SUBMIT}; // submit button (immutable)
    JButton backButton = new JButton("Back"); // back button
    private JPanel panel = new JPanel(new GridBagLayout()); // new JPanel with grid bag layout
    GridBagConstraints constraints = new GridBagConstraints(); // new constraints with grid bag constraints

    public JLabel loginLabel = new JLabel("Login"); // new label "Login" JLabel

    public JLabel usernameLabel = new JLabel("Username"); // new label "Username" label
    public JLabel passwordLabel = new JLabel("Password"); // new label "Password" label
    public JTextField usernameField = new JTextField(); // new JTextField "usernameField" field
    public JPasswordField passwordField = new JPasswordField(); // new JPasswordField "passwordField" field
    public JButton loginBtn = new JButton("Submit"); // new JButton "Submit" button


    public LoginPanel(ActionListener listener) { // login panel with listener as param

        Font font = loginLabel.getFont(); // login label font is the value of the new font object
        Font newFont = font.deriveFont(font.getSize() + 7f); // Increase the size by 5 points
        loginLabel.setFont(newFont); // set font for login label to new font
        loginLabel.setForeground(Color.WHITE); // set white foreground for login label
        usernameLabel.setForeground(Color.WHITE); // set white foreground for username label
        passwordLabel.setForeground(Color.WHITE); // set white foreground for password label
        loginBtn.setName(SUBMIT); // set login button name to SUBMIT
        backButton.setName("back"); // set back button name to back

        panel = new JPanel() { // new jPanel called panel
            @Override
            protected void paintComponent(Graphics g) { // paint component with graphics g param is a protected method that controls how the panel looks
                super.paintComponent(g); // call super recursively
                // gets image, already resized to 1280 x 720
                ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/images/bg_2.jpg")); // call predefined, saved bg_2 image
                // displays the background image
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this); // draw image with given image, no offset, width and height params & this
            }
        };



        constraints.fill = GridBagConstraints.HORIZONTAL; // constraints fill using the horizontal preset
        constraints.insets = new Insets(5, 5, 5, 5); // insets are a perfect square of 5s
        // how big the username and password fields will be
        passwordField.setPreferredSize(new Dimension(200, 20)); // 200 wide, 20 high
        usernameField.setPreferredSize(new Dimension(200, 20)); // 200 wide, 20 high
        // adding login label to the login panel
        loginLabel.setHorizontalAlignment(JLabel.CENTER); // set horizontal alignment to be centered
        constraints.gridx = 0; // no horizontal skew
        constraints.gridy = 0; // no vertical skew
        constraints.gridwidth = 2; // width of 2 units
        panel.add(loginLabel, constraints); // add login label with given constraints to the panel
        // adding username label to the login panel
        constraints.gridx = 0; // no horizontal skew
        constraints.gridy = 1; // 1 unit down
        constraints.gridwidth = 2; // width of 2 units
        panel.add(usernameLabel, constraints); // add username label with given constraints to the panel
        // adding username field to the login panel
        constraints.gridx = 0; // no horizontal skew
        constraints.gridy = 2; // 2 units down
        panel.add(usernameField, constraints); // add username field with given constraints to the panel
        // adding password label to the login panel
        constraints.gridx = 0; // no horizontal skew
        constraints.gridy = 3; // 3 units down
        panel.add(passwordLabel, constraints); // add password label with given constraints to the panel
        // adding password field to the login panel
        constraints.gridx = 0; // no horizontal skew
        constraints.gridy = 4; // 4 units down
        panel.add(passwordField, constraints); // add password field with given constraints to the panel
        // adding Login button to the login panel
        constraints.gridx = 0; // no horizontal skew
        constraints.gridy = 5; // 5 units down
        constraints.gridwidth = 2; // width of 2 units
        panel.add(loginBtn, constraints); // add login button with given constraints to the panel

        //adding back button.
        constraints.gridx = 0; // no horizontal skew
        constraints.gridy = 6; // 6 units down
        constraints.gridwidth = 2; // width of 2 units
        panel.add(backButton, constraints); // add back button with given constraints to the panel

        // enabling the login button to accept clicks
        loginBtn.addActionListener(listener); // add action listener to login button
        backButton.addActionListener(listener); // add action listener to back button

    }

    public JPanel getPanel() { // just returns the panel we made
        return this.panel;
    }

    public String getUsername() { // returns username entered
        return this.usernameField.getText(); 
    }

    public String getPasswordHash() { // returns password hash using a new string that just gets the password
        return new String(this.passwordField.getPassword());
    }
}
