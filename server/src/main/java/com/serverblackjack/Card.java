package com.serverblackjack;

import java.util.Random;

public class Card {
    public enum Rank {
        ACE("ace", 1), TWO("two", 2), THREE("three", 3), FOUR("four", 4), FIVE("five", 5), SIX("six", 6), SEVEN("seven", 7),
        EIGHT("eight", 8), NINE("nine", 9), TEN("ten", 10), JACK("jack", 10), QUEEN("queen", 10), KING("king", 10);
    
        private final String name;
        private final int value;
        private Rank(String name, int v) {
            this.name = name;
            this.value = v;
        }
        public String getName() {
            return this.name;
        }
        public int getValue() {
            return this.value;
        }
    }
    

    public enum Suit {
        CLUBS("clubs"), DIAMONDS("diamonds"), HEARTS("hearts"), SPADES("spades");

        private final String name;
        private Suit (String name) {
            this.name = name;
        }
        public String getName() {
            return this.name;
        }
    }

    private final Rank rank_;
    private final Suit suit_;


    public Card(Rank rank, Suit suit) {
        rank_ = rank;
        suit_ = suit;
    }
    public Rank getRank() {
        return rank_;
    }
    public Suit getSuit() {
        return suit_;
    }
    public String getType() {
        return this.suit_.getName() + "_" + this.rank_.getName();
    }

    public static Card generateRandomCard() {
        Random random = new Random();
        Card.Rank[] ranks = Card.Rank.values();
        Card.Suit[] suits = Card.Suit.values();
    
        Card.Rank randomRank = ranks[random.nextInt(ranks.length)];
        Card.Suit randomSuit = suits[random.nextInt(suits.length)];
        return new Card(randomRank, randomSuit);
    }
    
}