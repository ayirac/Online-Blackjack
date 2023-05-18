package com.clientblackjack.gui;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.*;

public class InstructionsPanel {

    private JPanel panel = new JPanel(new GridBagLayout()); // new JPanel, panel, with gridbaglayout for consistency throughout the program's various buttons
    GridBagConstraints constraints = new GridBagConstraints(); // new constraints using GridBagConstraints default

    public JLabel instructionsLabel = new JLabel("Instructions"); // instructions text label
    public JTextArea instructionsText = new JTextArea(); // text area to go with it

    public InstructionsPanel(ActionListener listener) { // instructions panel constructor with listener as parameter
        constraints.fill = GridBagConstraints.HORIZONTAL; // grid bag constraints fill the panel horizontally to justify it 
        constraints.insets = new Insets(5, 5, 5, 5); // new insets for constraints, basically a perfect square of 5s

        // Adding instructions label to the panel
        instructionsLabel.setHorizontalAlignment(JLabel.CENTER); // set horizontal alignment to the center
        constraints.gridx = 0; // no horizontal skew
        constraints.gridy = 0; // no vertical skew
        constraints.gridwidth = 2;  // width of 2
        
        panel = new JPanel() { // panel with paint component
            @Override
            protected void paintComponent(Graphics g) { // paint component method
                super.paintComponent(g); // call super on paint component with graphics param g (recursion)
                // gets image, already resized to 1280 x 720
                ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/images/mainmenu.jpg"));
                // displays the background image
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.add(instructionsLabel, constraints); // add instructions label with given constraints to the panel

        // Adding instructions text area to the panel
        instructionsText.setEditable(false); // instructions text is not editable
        instructionsText.setLineWrap(false); // instructions text does not have line wrap
        instructionsText.setWrapStyleWord(true); // instructions text sets wrap style word to true
        instructionsText.setBackground(new Color(0, 0, 0, 150)); // background is set to color of rgb 0, 0, 0, 150
        instructionsText.setFont(new Font("Copperplate Gothic", Font.BOLD, 14)); // bold copperplate gothic used for instructions font
        instructionsText.setForeground(Color.WHITE); // white foreground contrasts dark color
        //instructionsText.setForeground(Color.WHITE); Are you going to be doing anything with this line?
        instructionsText.setText("Objective:\n"
                + "The objective of Blackjack is to beat the dealer's hand without exceeding a total card value of 21.\n\n"
                + "Card Values:\n"
                + "Numbered cards (2-10): Face value (e.g., 2 is worth 2 points, 10 is worth 10 points).\n"
                + "Face cards (Jack, Queen, King): Each worth 10 points.\n"
                + "Ace: Can be worth either 1 or 11 points, depending on which value benefits the player the most.\n\n"
                + "Gameplay:\n"
                + "The game begins with the player placing a bet.\n"
                + "The dealer deals two cards to the player and two cards to themselves. One dealer card is face up, while the other is face down.\n"
                + "The player evaluates their hand and decides whether to hit (take another card) or stand (not take any more cards).\n"
                + "If the player's total card value exceeds 21, they bust and lose the hand.\n"
                + "If the player stands, it's the dealer's turn to play.\n"
                + "The dealer reveals their face-down card.\n"
                + "The dealer must hit until their total card value reaches 17 or higher.\n"
                + "If the dealer's total card value exceeds 21, they bust, and the player wins.\n"
                + "If neither the player nor the dealer busts, the hands are compared, and the one with a higher total card value wins.\n"
                + "If the player's hand value is the same as the dealer's, it's a push (tie), and the player's bet is returned.\n\n"
                + "Special Hands:\n"
                + "Blackjack: If the player's initial hand consists of an Ace and a 10-value card (10, Jack, Queen, or King), it's called a Blackjack. \nA Blackjack is an automatic win unless the dealer also has a Blackjack. In that case, it's a push.\n"
                + "Insurance: If the dealer's face-up card is an Ace, players have the option to take insurance. This is a separate side bet that pays 2:1 if the dealer has a Blackjack.\n\n"
                + "Options:\n"
                + "Hit: To request another card.\n"
                + "Stand: To stop taking cards and end the player's turn.\n"
                + "Double Down: To double the initial bet and receive one additional card.\n"
                + "Split: If the player's initial two cards have the same value, they can choose to split them into two separate hands. This requires placing an additional bet equal to the initial bet.");
                constraints.gridx = 0; // no horizontal skew
                constraints.gridy = 1; // skewed down by 1 unit
                constraints.gridwidth = 2; // width of 2 units
                constraints.weightx = 1.0; // Set the weightx to 1.0 to allow horizontal expansion
                constraints.fill = GridBagConstraints.BOTH; // Allow both horizontal and vertical expansion
                panel.add(instructionsText, constraints); // instructions text and constraints added to panel

                JButton backButton = new JButton("Back"); // back button created
                backButton.setName("back"); // change to const/enum later Did you do this yet?
                backButton.addActionListener(listener); // Action listener added to back button
                constraints.gridx = 0; // No horizontal skew
                constraints.gridy = 3; // All the way down at the bottom
                panel.add(backButton, constraints); // Button added to the panel
            }
        
            public JPanel getPanel() { // returns this panel
                return this.panel;
            }
        }
        
