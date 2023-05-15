package com.serverblackjack;

public class Card { // establish card class
    public enum Rank { // enumerated ranks from 1-13
        ACE("ace"), TWO("two"), THREE("three"), FOUR("four"), FIVE("five"), SIX("six"), SEVEN("seven"),
        EIGHT("eight"), NINE("nine"), TEN("ten"), JACK("jack"), QUEEN("queen"), KING("king");
    
        private final String name; // card name
        // rank uses name
        private Rank(String name) {
            this.name = name;
        }
        // get name returns name
        public String getName() {
            return this.name;
        }
    }
    
    // enumerated suit - clubs, diamonds, hearts, spades
    public enum Suit {
        CLUBS("clubs"), DIAMONDS("diamonds"), HEARTS("hearts"), SPADES("spades");
    	// string name not supposed to be changed so is private/final
        private final String name;
        // suit method uses name
        private Suit (String name) {
            this.name = name;
        }
        // return name, should we override?
        public String getName() {
            return this.name;
        }
    }

    private final Rank rank_; // rank of the card
    private final Suit suit_; // suit of the card

    public Card(Rank rank, Suit suit) { // establish individual card
        rank_ = rank; // establish its rank
        suit_ = suit; // establish its suit
    }

    public String getType() { // get type of card ( suit + rank )
        return this.suit_.getName() + "_" + this.rank_.getName();
        // maybe do like an "X of Y" type thing so instead of "_" it's "of"
    }
}