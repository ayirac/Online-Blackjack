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

    public static boolean isValidCard(Suit suit, Rank rank) {
        try {
            Suit.valueOf(suit.name());
            Rank.valueOf(rank.name());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private final Rank rank_;
    private final Suit suit_;
    private boolean hidden;

    

    public Card(Rank rank, Suit suit) {
        hidden = false;
        rank_ = rank;
        suit_ = suit;
    }

    static public BufferedImage getHideImage() throws IOException {
        InputStream is = DealerCard.class.getResourceAsStream("/images/hide.png");
        BufferedImage img = ImageIO.read(is);
        return img;
    }
    
 
   public BufferedImage getImage() throws IOException {
        InputStream is = getClass().getResourceAsStream("/images/" + rank_.toString().toLowerCase() + "_" + suit_.toString().toLowerCase() + ".png"); // disabled until all images are avail
        //InputStream is = getClass().getResourceAsStream("/images/eight_clubs.png");
        BufferedImage img = ImageIO.read(is);
        return img;
    }

    public boolean isEqual(Card card) {
        if (this.suit_ == card.suit_ && this.rank_ == card.rank_) {
            return true;
        }
        return false;
    }

    public Suit getSuit() {
        return suit_;
    }

    public boolean isHidden() {
        return hidden;
    }

    public Rank getRank() {
        return rank_;
    }

    public String getType() {
        return this.suit_.getName() + "_" + this.rank_.getName();
    }

    public void setHidden(boolean h) {
        this.hidden = h;
    }

    public void setImage(BufferedImage hideImage) {
    }
}
