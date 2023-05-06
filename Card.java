package com.clientblackjack.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

    private final Rank rank_;
    private final Suit suit_;

    public Card(Rank rank, Suit suit) {
        rank_ = rank;
        suit_ = suit;
    }

    public BufferedImage getImage() throws IOException {
        String filePath = "resources/images/" + rank_.toString().toLowerCase() + "_" + suit_.toString().toLowerCase() + ".png";
        File file = new File(filePath);
        BufferedImage img = ImageIO.read(file);
        return img;
    }
}
