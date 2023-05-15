package com.clientblackjack.gui;

import java.awt.FlowLayout;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

// Shows the hands as Card IMGs on screen
public class Hand extends JPanel {
    private ArrayList<Card> cards;
    
    public Hand() {
        cards = new ArrayList<>();
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    }
    
    public void addCard(Card card) throws IOException {
        float scale = 0.5f; // scale of card size, refactor later
        Image originalImage = card.getImage();
        int newWidth = (int) (originalImage.getWidth(null) * scale);
        int newHeight = (int) (originalImage.getHeight(null) * scale);
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        add(new JLabel(new ImageIcon(scaledImage)));
        cards.add(card);
        revalidate();
        repaint();
    }
    
    
    public void removeCard(Card card) throws IOException {
        ImageIcon image = new ImageIcon (card.getImage());
        cards.remove(card);
        remove(getComponentZOrder(new JLabel(image)));
        revalidate();
        repaint();
    }
    
    public void clear() {
        cards.clear();
        removeAll();
        revalidate();
        repaint();
    }

    public JPanel getPanel() {
        return this;
    }
}
