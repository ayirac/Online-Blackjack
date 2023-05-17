package com.clientblackjack.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Card {
    // Enums to simplify things
    
    public enum Rank {
        ACE("ace"), TWO("two"), THREE("three"), FOUR("four"), FIVE("five"), SIX("six"), SEVEN("seven"),
        EIGHT("eight"), NINE("nine"), TEN("ten"), JACK("jack"), QUEEN("queen"), KING("king"); // enums for every single possible number card from 1-13 (ace-king)
    
        private final String name; // immutable name string
        private Rank(String name) { // rank is given name as param
            this.name = name; // this name is the passed-in parameter name
        }
        public String getName() { // return the name given
            return this.name;
        }
    }
    

    public enum Suit { // enums for all four suits of card
        CLUBS("clubs"), DIAMONDS("diamonds"), HEARTS("hearts"), SPADES("spades");

        private final String name; // another immutable name string, should we be using these vars like this?
        private Suit (String name) { // suit does the exact same thing as rank
            this.name = name;
        }
        public String getName() { // even the return method is the same
            return this.name;
        }
    }

    public static boolean isValidCard(Suit suit, Rank rank) { // bool method which, given suit and rank, finds if a card is valid
        try { // valueOf test whether the suit and rank are legal enums
            Suit.valueOf(suit.name());
            Rank.valueOf(rank.name());
            return true;
        } catch (IllegalArgumentException e) { // if they're outside of legal, just return false
            return false;
        }
    }

    private final Rank rank_; // immutable rank variable rank
    private final Suit suit_; // immutable suit variable suit

    public Card(Rank rank, Suit suit) { // a card has a rank and a suit, passed in as parameters, they are assigned
        rank_ = rank;
        suit_ = suit;
    }

    public BufferedImage getImage() throws IOException { // you should uncomment that line below
        //InputStream is = getClass().getResourceAsStream("/images/" + rank_.toString().toLowerCase() + "_" + suit_.toString().toLowerCase() + ".png"); // disabled until all images are avail
        InputStream is = getClass().getResourceAsStream("/images/eight_clubs.png"); // i see you using this in successful test cases, good job getting hit up Kyle
        BufferedImage img = ImageIO.read(is); // buffered image img is read in from is which is gotten using an imageio.read call
        return img;
    }

    public boolean isEqual(Card card) { // is one card equal to another
        if (this.suit_ == card.suit_ && this.rank_ == card.rank_) { // if the rank and suit are equal, return true
            return true;
        } // return false if we manage to get past this block
        return false;
    }

    public Suit getSuit() { // return a given card's suit
        return suit_;
    }

    public Rank getRank() { // return a given card's rank
        return rank_;
    }

    public String getType() { // return a given card's type
        return this.suit_.getName() + "_" + this.rank_.getName(); // ACE_HEARTS e.g.
    }
}
