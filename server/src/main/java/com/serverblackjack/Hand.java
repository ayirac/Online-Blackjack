package com.serverblackjack;

import java.util.ArrayList;
import java.util.Random;

public class Hand {
    private ArrayList<Card> cards;
    public Hand() {
        cards = new ArrayList<>();
    }

    public void add(Card c) {
        cards.add(c);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    // Deals a single card
    public void deal() {
        this.cards.add(Card.generateRandomCard());
    }

    public void populate() {
        // populating with 2, if needed add as argument
        for (int i = 0; i < 2; i++) {
            cards.add(new Card(Card.Rank.values()[new Random().nextInt(Card.Rank.values().length)], Card.Suit.values()[new Random().nextInt(Card.Suit.values().length)]));
        }
    }

    public int calculateValue() {
        int value = 0;
        for (Card card : cards) {
            Card.Rank rank = card.getRank();
            value += rank.getValue();
        }
        return value;
    }

    public void clear() {
        cards.clear();
    }
}