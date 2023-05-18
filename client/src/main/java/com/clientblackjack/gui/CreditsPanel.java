package com.clientblackjack.gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class CreditsPanel {
    private JPanel panel = new JPanel();

    public CreditsPanel(ActionListener listener) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/images/mainmenu.jpg"));
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        JLabel jLabelOne = new JLabel("Credit to Kyle Garrett for primary development, Game UI design, and debugging\n\n");
        JLabel jLabelTwo = new JLabel("Credit to Arthur Santamaria for development, UI design, and debugging.\n\n");
        JLabel jLabelThree = new JLabel("Credit to Andrew Coviello for secondary development, debug and final revision");
        JButton backButton = new JButton("Back");
        Font labelFont = new Font("Verdana", Font.BOLD, 20);
        Color labelColor = Color.WHITE;

        jLabelOne.setFont(labelFont);
        jLabelOne.setForeground(labelColor);
        jLabelTwo.setFont(labelFont);
        jLabelTwo.setForeground(labelColor);
        jLabelThree.setFont(labelFont);
        jLabelThree.setForeground(labelColor);

        panel.add(jLabelOne);
        panel.add(jLabelTwo);
        panel.add(jLabelThree);

        backButton.addActionListener(listener);
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        backButton.setName("back");
        backButton.setContentAreaFilled(false); // Make the button transparent
        backButton.setForeground(Color.WHITE); // Set the text color to white
        backButton.setFont(labelFont); // Set the font for the button text

        panel.add(Box.createVerticalGlue()); // Add vertical glue to push the button to the bottom
        panel.add(backButton);

        panel.setBorder(new LineBorder(Color.BLACK));
        panel.setSize(400, 400);
        panel.setVisible(true);
    }

    public JPanel getPanel() {
        return this.panel;
    }
}
