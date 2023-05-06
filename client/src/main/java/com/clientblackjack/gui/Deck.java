package com.clientblackjack.gui;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Deck {
    //Queue<Card> cards_ = new LinkedList<>();
    private final int deck_size = 52;
    private final int start_hand = 2;
    private final int shuffle_num = 1500;

    //Assigns an array for a deck of size 52
    Card[] deck = new Card[deck_size];
    Random rng = new Random();


    //refill a deck of all suits and ranks
    public void refillDeck() 
    {
        int i = 0;
        for (Card.Suit suit : Card.Suit.values()) 
        {
            for (Card.Rank rank : Card.Rank.values()) 
            {
                deck[i] = new Card(rank, suit);
                i++;
            }
        }
    }

    //randomize deck before playing

    public void shuffle()
    {
        for (int i = 0; i <= shuffle_num; i++)
        {
            //gets random indexes to get first index to swap with second index.
            int random_card = rng.nextInt(deck_size); 
            int random_card_two = rng.nextInt(deck_size);

            //Basic swap from first to second card in order to shuffle the deck
            Card temp = deck[random_card];
            deck[random_card] = deck[random_card_two];
            deck[random_card_two] = temp;
        }
    }


    //This function starts the first deal of 2 cards. needs to be called per
    public Card[] start_deal()
    {
        Card[] hand = new Card[start_hand];

        for (int deck_position = 0; deck_position < 2; deck_position++)
        {
            hand[deck_position] = deck[deck_position];
        }

        return hand;

    }

}
