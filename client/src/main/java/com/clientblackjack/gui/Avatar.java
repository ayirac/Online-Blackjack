package com.clientblackjack.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Avatar extends JPanel {
    ImageIcon img_;
    static Dimension SIZE = new Dimension(150, 150);

    // Avatar class, maybe add border around avatar later
    Avatar(String fp) {
        setLayout(new GridBagLayout());
        InputStream is = getClass().getResourceAsStream("/images/" + fp + ".png");
        try {
            BufferedImage img = ImageIO.read(is);
            img_ = new ImageIcon(img);
            int newWidth = 50;
            int newHeight = 50;
            Image scaledImage = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

            JLabel label = new JLabel(new ImageIcon(scaledImage));
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            add(label, constraints);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
