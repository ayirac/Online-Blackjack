package com.clientblackjack.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlayerCard extends ActorCard {
    PlayerCard(String name, boolean isPlay) {
        super(name, isPlay);
        wagerLabel.setText("0");
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridx = 0;              // Avatar
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 0, 0, 0);
        this.add(this.avatar_, constraints);



        if (isPlay) {
            JPanel panel = new JPanel(new GridBagLayout());
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.SOUTHEAST;
            panel.add(wagerLabel, constraints);

            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.EAST;
            panel.add(moneyLabel, constraints);
            constraints.gridx = 0;
            constraints.gridy = 2;
            constraints.anchor = GridBagConstraints.NORTHEAST;
            panel.add(resultLabel, constraints);

            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.EAST;
            this.add(panel, constraints);
        }
        }
        

    private JLabel wagerLabel = new JLabel();
    private JLabel moneyLabel = new JLabel();
    private JLabel resultLabel = new JLabel();

    public void setWager(int wager) {
        wagerLabel.setText("Wager: " + Integer.toString(wager));
    }

    public void clearWager() {
        wagerLabel.setText("0");
    }

    public void setMoney(int money) {
        moneyLabel.setText("Money: " + Integer.toString(money));
    }

    public void clearMoney() {
        moneyLabel.setText("0");
    }

    public void setResult(String string) {
        resultLabel.setText(string);
    }


}
