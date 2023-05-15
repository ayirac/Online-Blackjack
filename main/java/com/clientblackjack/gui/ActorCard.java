package com.clientblackjack.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ActorCard extends JPanel {
    public Hand hand_ = new Hand();
    private Avatar avatar_ = new Avatar("test_avatar");
    private JLabel name_;
    private int state;

    ActorCard(String actName) {
        GridBagConstraints constraints = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        Font font = new Font("Arial", Font.BOLD, 20);  // create new font
        this.name_ = new JLabel(actName, SwingConstants.CENTER);
        this.name_.setFont(font);                          // set font for the name label
        this.name_.setPreferredSize(new Dimension(200, 50)); // set size for the name label
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;              // Avatar
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 85, 0, 0);
        this.add(this.avatar_, constraints);

        constraints.gridx = 1;              // Name
        constraints.gridy = 0;
        constraints.insets = new Insets(0, -50, 0, 0);
        constraints.anchor = GridBagConstraints.WEST;
        this.add(this.name_, constraints);
        

        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.gridx = 0;              // Hand/Cards
        constraints.gridy = 1;
        constraints.gridwidth = 5;
        this.add(hand_, constraints);
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
    
}
