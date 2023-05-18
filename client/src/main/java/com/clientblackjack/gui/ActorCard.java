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
    public Hand hand_ = new Hand();
    protected Avatar avatar_ = new Avatar("test_avatar");
    protected String name;
    protected JLabel nameLabel;
    protected int state;

    ActorCard(String actName, boolean play) {
        GridBagConstraints constraints = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        Font font = new Font("Arial", Font.BOLD, 20);  // create new font
        this.nameLabel = new JLabel(actName, SwingConstants.CENTER);
        this.name = actName;
        this.nameLabel.setFont(font);                          // set font for the name label
        this.nameLabel.setPreferredSize(new Dimension(200, 50)); // set size for the name label
        constraints.fill = GridBagConstraints.HORIZONTAL;

        

        constraints.gridx = 1;              // Name
        constraints.gridy = 0;
        constraints.insets = new Insets(0, -50, 0, 0);
        constraints.anchor = GridBagConstraints.WEST;
        this.add(this.nameLabel, constraints);
        

        constraints.insets = new Insets(0, 0, 0, 0);
        if (!play) 
            constraints.gridx = 0;              // Hand/Cards, make room for player's wager
        else {
            constraints.gridx = 1;
            constraints.insets = new Insets(0, 0, 0, 0);
        }
        constraints.gridy = 1;
        constraints.gridwidth = 5;
        this.add(hand_, constraints);
    }

    public void setHand(Hand recievedHand) throws IOException {
        this.hand_.clear();
        for (Card card : recievedHand.getCards()) {
            this.hand_.addCard(new Card(card.getRank(), card.getSuit()));  
        }
    }

    public Component getPanel() {
        return this;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Hand getHand() {
        return this.hand_;
    }
    
    public String getNamed() {
        return name;
    }

    public void setNamed(String name) {
        this.name = name;
    }
}
