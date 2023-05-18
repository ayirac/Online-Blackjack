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
    private ArrayList<JLabel> cardLabels; // Array to store the JLabel instances
    
    public ArrayList<Card> getCards() {
        return cards;
    }

    public Hand() {
        cards = new ArrayList<>();
        cardLabels = new ArrayList<>();
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    }

    public void addCard(Card card) throws IOException {
        float scale = 0.2f;
        Image originalImage = card.getImage();
        int newWidth = (int) (originalImage.getWidth(null) * scale);
        int newHeight = (int) (originalImage.getHeight(null) * scale);
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        JLabel cardLabel = new JLabel(new ImageIcon(scaledImage));
        add(cardLabel);
        cards.add(card);
        cardLabels.add(cardLabel); // Add the JLabel instance to the array
        revalidate();
        repaint();
    }

    public void removeCard(Card card) throws IOException {
        ImageIcon image = new ImageIcon(card.getImage());
        int index = cards.indexOf(card);
        if (index != -1) {
            cards.remove(index);
            JLabel cardLabel = cardLabels.remove(index); // Remove the JLabel instance from the array
            remove(cardLabel);
            revalidate();
            repaint();
        }
    }

    public void clear() {
        cards.clear();
        cardLabels.clear(); // Clear the array of JLabel instances
        removeAll();
        revalidate();
        repaint();
    }

    public JPanel getPanel() {
        return this;
    }

    public void hideSecondCard() throws IOException {
        if (cards.size() >= 2) {
             // Get the second card in the hand
            if (!cards.get(1).isHidden()) {
                // Set the second card's image to the hidden image
                float scale = 0.15f;
                Image originalImage = Card.getHideImage();
                int newWidth = (int) (originalImage.getWidth(null) * scale);
                int newHeight = (int) (originalImage.getHeight(null) * scale);
                Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                ImageIcon hideIcon = new ImageIcon(scaledImage);
                JLabel cardLabel = cardLabels.get(1);
                cardLabel.setIcon(hideIcon);
    
                cards.get(1).setHidden(true); // Mark the second card as hidden
                revalidate();
                repaint();
            }
        }
    }

    public void showSecondCard() throws IOException {
        if (cards.size() >= 2) {
            Card secondCard = cards.get(1); // Get the second card in the hand
            if (secondCard.isHidden()) {
                // Set the second card's image to the original image
                ImageIcon imageIcon = new ImageIcon(secondCard.getImage());
                JLabel cardLabel = cardLabels.get(1); // Get the corresponding JLabel
                cardLabel.setIcon(imageIcon);
    
                secondCard.setHidden(false); // Mark the second card as not hidden
                revalidate();
                repaint();
            }
        }
    }
    
    

    public boolean isEqual(Hand h) {
        if (h.getCards().size() != this.cards.size())
            return false;
        for (int i = 0; i < cards.size(); i++) {
            if (!h.cards.get(i).isEqual(cards.get(i))) {
                return false;
            }
        }
        return true;
    }
}
