package com.clientblackjack.gui;

import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;

public class InstructionsPanel {
    public static final String NEXT = "next";
    public static final String PREVIOUS = "previous";
    public static final String[] BUTTONS = {NEXT, PREVIOUS};
    
    private JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();

    public JLabel instructionsLabel = new JLabel("Instructions");
    public JButton nextBtn = new JButton("Next");
    public JButton previousBtn = new JButton("Previous");

    public InstructionsPanel(ActionListener listener) {
        nextBtn.setName(NEXT);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);
        // adding login label to the login panel
        instructionsLabel.setHorizontalAlignment(JLabel.CENTER);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(instructionsLabel, constraints);
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        panel.add(nextBtn, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        panel.add(previousBtn, constraints);
        // enabling the login button to accept clicks
        nextBtn.addActionListener(listener);
        nextBtn.addActionListener(listener);
    }

    public JPanel getPanel() {
        return this.panel;
    }
}
