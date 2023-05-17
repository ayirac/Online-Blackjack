package com.clientblackjack.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ActorCard extends JPanel {
    public Hand hand_ = new Hand(); // Hand of cards available for manipulation
    private Avatar avatar_ = new Avatar("test_avatar"); // Test avatar for if decide to implement custom later
    private String name; // Name for actor
    private JLabel nameLabel; // Label to display name for actor
    private int state; // State of the actor in the game currently

    ActorCard(String actName) { // Actor card constructor
        GridBagConstraints constraints = new GridBagConstraints(); // Did we make constraints gbc variable in other file?
        this.setLayout(new GridBagLayout()); // Set the layout to a new grid bag layout
        Font font = new Font("Arial", Font.BOLD, 20);  // create new font
        this.nameLabel = new JLabel(actName, SwingConstants.CENTER); // This name label for actName is the new J Label which uses the swing constants to center itself
        this.name = actName; // this name is the previously set actName, I think the choice of placement for lines 26 and 27 may cause display errors
        this.nameLabel.setFont(font);                          // set font for the name label
        this.nameLabel.setPreferredSize(new Dimension(200, 50)); // set size for the name label
        constraints.fill = GridBagConstraints.HORIZONTAL; // The constraints are horizontally corrected

        constraints.gridx = 0;              // Avatar
        constraints.gridy = 0;              // At top
        constraints.insets = new Insets(0, 85, 0, 0); // New insets placed 85 pixels down
        this.add(this.avatar_, constraints); // Add the avatar with the given constraints

        constraints.gridx = 1;              // Name
        constraints.gridy = 0;              // To the right of avatar
        constraints.insets = new Insets(0, -50, 0, 0); // New insets placed 50 pixels up
        constraints.anchor = GridBagConstraints.WEST; // Constraints anchor is to the left (WEST)
        this.add(this.nameLabel, constraints); // Add the name label with the given constraints
        

        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.gridx = 0;              // Hand/Cards
        constraints.gridy = 1;              // Below the avatar
        constraints.gridwidth = 5;          // width of 5 pixels
        this.add(hand_, constraints); // Add the hand with the given constraints
    }

    public void setHand(Hand recievedHand) throws IOException {
        this.hand_.clear(); // setHand takes the old cards out of the hand if there were any, "clearing" the hand
        for (Card card : recievedHand.getCards()) { // for each card gotten from the received hand
            this.hand_.addCard(new Card(card.getRank(), card.getSuit())); // basically add a card, if you had 7 cards before you will end up with only 7 new ones (7 to 0 to 7)
        }
    }

    public Component getPanel() {
        return this; // is this method returning anything? What is the "this" variable?
    }

    public int getState() {
        return state; // gets the state of the actor
    }

    public void setState(int state) {
        this.state = state; // sets the state of the actor
    }

    public Hand getHand() {
        return this.hand_; // gets the hand of the actor
    }
    
    public String getNamed() {
        return name; // gets the name of the actor
    }

    public void setNamed(String name) {
        this.name = name; // sets the name of the actor, also why are these methods ending with Named instead of Name?
    }
}
