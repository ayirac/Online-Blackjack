package com.clientblackjack.gui;

import java.awt.FlowLayout;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

// Shows the hands as Card IMGs on screen
public class Hand extends JPanel { // hand class extends JPanel
    private ArrayList<Card> cards; // list of cards
    private ArrayList<JLabel> cardLabels; // Array to store the JLabel instances
    
    public ArrayList<Card> getCards() { // array list of cards type method that just returns the cards
        return cards;
    }

    public Hand() { // hand constructor
        cards = new ArrayList<>(); // cards is a new array list
        cardLabels = new ArrayList<>(); // so is card labels
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5)); // the layout becomes a new flow layout centered and 5x5
    }

    public void addCard(Card card) throws IOException { // add card method using a Card variable called card
        float scale = 0.2f; // scale var for adjusting the physical scale (size) of certain things
        Image originalImage = card.getImage(); // original image variable of type image is the card's returned image
        int newWidth = (int) (originalImage.getWidth(null) * scale); // new width is set using scale
        int newHeight = (int) (originalImage.getHeight(null) * scale); // new height is set using scale
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH); // scaled image is a scaled instance of the original image
        // with new width and height
        JLabel cardLabel = new JLabel(new ImageIcon(scaledImage)); // card label is a new JLabel made using the scaled image icon
        add(cardLabel); // add the card label
        cards.add(card); // add the card into the list of cards
        cardLabels.add(cardLabel); // Add the JLabel instance to the array
        revalidate(); 
        repaint();
    }

    public void removeCard(Card card) throws IOException { // remove card method which takes a card as param and sees if valid to remove
        ImageIcon image = new ImageIcon(card.getImage()); // image is the new image icon (card.getImage)
        int index = cards.indexOf(card); // index integer saves the index of the card in cards
        if (index != -1) { // if it's valid I.E. not -1 (maybe should do more than -1)
            cards.remove(index); // remove the card itself
            JLabel cardLabel = cardLabels.remove(index); // Remove the JLabel instance from the array
            remove(cardLabel); // remove the JLabel instance entirely
            revalidate();
            repaint();
        }
    }

    public void clear() { // clears the entire deck of cards
        cards.clear(); // recursion
        cardLabels.clear(); // Clear the array of JLabel instances
        removeAll(); // removes all cards
        revalidate();
        repaint();
    }

    public JPanel getPanel() { // return panel
        return this;
    }

    public void hideSecondCard() throws IOException { // hide second card (method to keep the requirements of blackjack as a game)
        if (cards.size() >= 2) { // if the size of the hand is greater than or equal to 2
             // Get the second card in the hand
            if (!cards.get(1).isHidden()) {
                // Set the second card's image to the hidden image
                float scale = 0.15f; // even smaller scale
                Image originalImage = Card.getHideImage(); // original image is the image hidden so that it stays preserved
                int newWidth = (int) (originalImage.getWidth(null) * scale); // new width is scaled down
                int newHeight = (int) (originalImage.getHeight(null) * scale); // new height is scaled down
                Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH); // scaled image image variable is a scaled instance of the og
                ImageIcon hideIcon = new ImageIcon(scaledImage); // hide icon is a new image icon of the scaled image
                JLabel cardLabel = cardLabels.get(1); // cardlabel jlabel variable is set equal to the output of cardlabels.get, 1 is 2 for computers so we get image 2
                cardLabel.setIcon(hideIcon); // set icon to hide icon, the card is now hidden from an image basis
    
                cards.get(1).setHidden(true); // Mark the second card as hidden
                revalidate();
                repaint();
            }
        }
    }

    public void showSecondCard() throws IOException { // method to show the second card in the hand 
        if (cards.size() >= 2) { // if the card hand size is at least 2
            Card secondCard = cards.get(1); // Get the second card in the hand
            if (secondCard.isHidden()) { // if the second card is hidden as per previous method
                // Set the second card's image to the original image
                ImageIcon imageIcon = new ImageIcon(secondCard.getImage()); // new image icon which is the image for the second card
                JLabel cardLabel = cardLabels.get(1); // Get the corresponding JLabel
                cardLabel.setIcon(imageIcon); // icon set to image icon
    
                secondCard.setHidden(false); // Mark the second card as not hidden
                revalidate();
                repaint();
            }
        }
    }
  
    public boolean isEqual(Hand h) { // are the hands equal
        if (h.getCards().size() != this.cards.size()) // if the size returned by h is not equal to the size in the current cards
            return false;
        for (int i = 0; i < cards.size(); i++) { // for every card in the hnd
            if (!h.cards.get(i).isEqual(cards.get(i))) { // if none of the cards are equal
                return false;
            }
        } // in all other cases return true
        return true;
    }
}
