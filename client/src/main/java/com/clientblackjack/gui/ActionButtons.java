package com.clientblackjack.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionButtons extends JPanel {
    public static final String HIT = "hit";
    public static final String STAND = "stand";
    public static final String INPUTWAGER = "input-wager";
    public static final String PLACEWAGER = "place-wager";
    public static final String[] BUTTONS = {HIT, STAND, INPUTWAGER, PLACEWAGER};

    private JButton hitButton;
    private JButton standButton;
    private JTextField wagerInput;
    private JButton wagerButton;

    public ActionButtons(ActionListener listener) {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);

        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");
        wagerInput = new JTextField(10);
        wagerButton = new JButton("Place Wager");

        hitButton.addActionListener(listener);
        standButton.addActionListener(listener);
        wagerInput.addActionListener(listener);
        wagerButton.addActionListener(listener);

        hitButton.setName(HIT);
        standButton.setName(STAND);
        wagerInput.setName(INPUTWAGER);
        wagerButton.setName(PLACEWAGER);

        // Add components to the panel using GridBagConstraints
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(hitButton, constraints);

        constraints.gridx = 1;
        add(standButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        add(wagerInput, constraints);

        constraints.gridy = 2;
        constraints.gridwidth = 2;
        add(wagerButton, constraints);

        toggleButtonVisibility(false);
        toggleWagerVisibility(false);
    }

    public String getWagerInput() {
        return wagerInput.getText();
    }

    public void toggleButtonVisibility(boolean show) {
        hitButton.setVisible(show);
        standButton.setVisible(show);
    }

    public void toggleWagerVisibility(boolean show) {
        wagerButton.setVisible(show);
        wagerInput.setVisible(show);
    }

    public int getWager() {
        return Integer.parseInt(this.wagerInput.getText());
    }
    
}
