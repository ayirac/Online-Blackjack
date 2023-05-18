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

public class Avatar extends JPanel { // an avatar uses a little j panel - it's just a smaller j panel
    ImageIcon img_; // establish image icon
    static Dimension SIZE = new Dimension(150, 150); // dimension variable named SIZE is essentially the dimensional size of the avatar

    // Avatar class, maybe add border around avatar later
    Avatar(String fp) {
        setLayout(new GridBagLayout()); // layout is set using default grid bag layout params
        InputStream is = getClass().getResourceAsStream("/images/" + fp + ".png"); // inputstream gets PNG images from the server images file
        try { // can we implement ".jpg" and similar really easy with str?
            BufferedImage img = ImageIO.read(is); // img is read in from the input stream that takes images we saw earlier, just the file path is all we need
            img_ = new ImageIcon(img); // img_ becomes the image icon using img
            int newWidth = 50; // new width is 50 long
            int newHeight = 50; // new height is 50 high so 50x50 box
            Image scaledImage = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH); // we scale the image into the dimensions given above

            JLabel label = new JLabel(new ImageIcon(scaledImage)); // the scaled image is put into a label called label... what's with the variable name reuse?
            GridBagConstraints constraints = new GridBagConstraints(); // constraints to be imposed on label
            constraints.gridx = 0; // middle
            constraints.gridy = 0; // middle
            add(label, constraints); // added label with constraints
        } catch (IOException e) { // since it's a try block we catch IO exceptions for bad output and print a stack trace in that case
            e.printStackTrace();
        }
    }
}
