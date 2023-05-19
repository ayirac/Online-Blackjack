package com.clientblackjack.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlayerCard extends ActorCard { // player card inherits from actor card
    PlayerCard(String name, boolean isPlay) { // player card constructo takes name str and isPlay bool
        super(name, isPlay); 
        wagerLabel.setText("0"); // set wager label text to 0 cuz no wager is placed yet
        GridBagConstraints constraints = new GridBagConstraints(); // constraints is new grid bag constraints

        constraints.anchor = GridBagConstraints.EAST; // eastward anchor established
        constraints.gridx = 0;              // Avatar
        constraints.gridy = 0;              // No vertical skew
        constraints.insets = new Insets(0, 0, 0, 0); // new insets for constraints
        this.add(this.avatar_, constraints); // add this avatar with the given constraints



        if (isPlay) { // if it is play
            JPanel panel = new JPanel(new GridBagLayout()); // panel is new j panel with a new grid bag layout
            constraints.gridx = 0; // no horizontal skew
            constraints.gridy = 0; // no vertical skew
            constraints.anchor = GridBagConstraints.SOUTHEAST; // southeast anchor established
            panel.add(wagerLabel, constraints); // wager label w/ constraints added to panel

            constraints.gridx = 0; // no horizontal skew
            constraints.gridy = 1; // downward vertical skew
            constraints.anchor = GridBagConstraints.EAST; // eastward anchor established
            panel.add(moneyLabel, constraints); // money label with constraints added to panel
            constraints.gridx = 0; // no horizontal skew
            constraints.gridy = 2; // heavy downward vertical skew
            constraints.anchor = GridBagConstraints.NORTHEAST; // northeast anchor established
            panel.add(resultLabel, constraints); // result label with constraints added to panel

            constraints.gridx = 0; // no horizontal skew
            constraints.gridy = 1; // downward vertical skew
            constraints.anchor = GridBagConstraints.EAST; // eastward anchor established
            this.add(panel, constraints); // panel added with given constraints
        }
        }
        

    private JLabel wagerLabel = new JLabel(); // wager label initialized 
    private JLabel moneyLabel = new JLabel(); // money label initialized
    private JLabel resultLabel = new JLabel(); // result label initialized

    public void setWager(int wager) { // set wager using wager as int param
        wagerLabel.setText("Wager: " + Integer.toString(wager)); // wager label text initialized
    }

    public void clearWager() { // clear wager method
        wagerLabel.setText("0"); // set from wager to 0
    }

    public void setMoney(int money) { // set money method
        moneyLabel.setText("Money: " + Integer.toString(money)); // the amount of money that the player has is established via a visible label
    }

    public void clearMoney() { // clear money method
        moneyLabel.setText("0"); // set from money to 0
    }

    public void setResult(String string) { // set result method
        resultLabel.setText(string); // publish the result given the string that contains it
    }
}
