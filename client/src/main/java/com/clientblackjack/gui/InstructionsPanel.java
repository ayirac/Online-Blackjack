package com.clientblackjack.gui;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.*;

public class InstructionsPanel {

    private JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();

    public JLabel instructionsLabel = new JLabel("Instructions");
    public JTextArea instructionsText = new JTextArea();

    public InstructionsPanel(ActionListener listener) {
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);

        // Adding instructions label to the panel
        instructionsLabel.setHorizontalAlignment(JLabel.CENTER);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(instructionsLabel, constraints);

        // Adding instructions text area to the panel
        instructionsText.setEditable(false);
        instructionsText.setLineWrap(true);
        instructionsText.setWrapStyleWord(true);
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
                + "Blackjack: If the player's initial hand consists of an Ace and a 10-value card (10, Jack, Queen, or King), it's called a Blackjack. A Blackjack is an automatic win unless the dealer also has a Blackjack. In that case, it's a push.\n"
                + "Insurance: If the dealer's face-up card is an Ace, players have the option to take insurance. This is a separate side bet that pays 2:1 if the dealer has a Blackjack.\n\n"
                + "Options:\n"
                + "Hit: To request another card.\n"
                + "Stand: To stop taking cards and end the player's turn.\n"
                + "Double Down: To double the initial bet and receive one additional card.\n"
                + "Split: If the player's initial two cards have the same value, they can choose to split them into two separate hands. This requires placing an additional bet equal to the initial bet.");
                constraints.gridx = 0;
                constraints.gridy = 1;
                constraints.gridwidth = 2;
                constraints.weightx = 1.0; // Set the weightx to 1.0 to allow horizontal expansion
                constraints.fill = GridBagConstraints.BOTH; // Allow both horizontal and vertical expansion
                panel.add(instructionsText, constraints);

                JButton backButton = new JButton("Back");
                backButton.setName("back"); // change to const/enum later
                backButton.addActionListener(listener);
                constraints.gridx = 0;
                constraints.gridy = 3;
                panel.add(backButton, constraints);
            }
        




            public JPanel getPanel() {
                return this.panel;
            }
        }
        