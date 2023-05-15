package com.clientblackjack.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;

public class PlayerCard extends ActorCard {
    PlayerCard(String name) {
        super(name);
        wagerLabel.setText("0");
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;              // Avatar
        constraints.gridy = 0;
        //constraints.insets = new Insets(0, 20, 0, 0);
        this.add(this.wagerLabel, constraints);
    }

    private JLabel wagerLabel = new JLabel();

    public void setWager(int wager) {
        wagerLabel.setText(Integer.toString(wager));
    }

    public void clearWager() {
        wagerLabel.setText("0");
    }
}
