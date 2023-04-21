package com.clientblackjack.gui;

import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;

public class ServerListPanel {
    private JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();

    public JLabel listPlaceholderLabel = new JLabel("list placeholder");

    public ServerListPanel(ActionListener listener) {
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);

        listPlaceholderLabel.setHorizontalAlignment(JLabel.CENTER);
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(listPlaceholderLabel, constraints);

       
    }

    public JPanel getPanel() {
        return this.panel;
    }
}
