package com.serverblackjack;

import java.util.ArrayList;
import java.util.Random;

public class Hand {
	// hand of cards, literal representation
    private ArrayList<Card> cards;
    // hand function
    public Hand() {
        cards = new ArrayList<>();
    }
    // add a card
    public void add(Card c) {
        cards.add(c);
    }
    // get the cards
    public ArrayList<Card> getCards() {
        return cards;
    }
    // recursively calls add in an iterative function, maybe reconsider?
    public void populate() {
        // populating with 2, if needed add as argument
        for (int i = 0; i < 2; i++) {
            cards.add(new Card(Card.Rank.values()[new Random().nextInt(Card.Rank.values().length)], Card.Suit.values()[new Random().nextInt(Card.Suit.values().length)]));
        }
    }
}