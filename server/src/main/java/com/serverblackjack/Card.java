package com.serverblackjack;

import java.util.Random;

public class Card {
    public enum Rank {
        ACE("ace"), TWO("two"), THREE("three"), FOUR("four"), FIVE("five"), SIX("six"), SEVEN("seven"),
        EIGHT("eight"), NINE("nine"), TEN("ten"), JACK("jack"), QUEEN("queen"), KING("king");
    
        private final String name;
        private Rank(String name) {
            this.name = name;
        }
        public String getName() {
            return this.name;
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