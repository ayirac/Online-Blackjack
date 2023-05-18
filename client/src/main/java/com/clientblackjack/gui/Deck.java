package com.clientblackjack.gui;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Deck {
    //Queue<Card> cards_ = new LinkedList<>();
    private final int deck_size = 52; // deck has 52 cards in it ranging from aces to kings of all 4 suits--13 x 4 = 52
    private final int start_hand = 2; // starting hand in blackjack has 2 cards in it, standard
    private final int shuffle_num = 1500; // the number of times a card in the deck changes position (bottom to top, top to bottom etc) when shuffling
    //Assigns an array for a deck of size 52
    Card[] deck = new Card[deck_size]; // card array
    Random rng = new Random(); // mathematical randomness simulator established here
    //refill a deck of all suits and ranks
    public void refillDeck() 
    {
        int i = 0; // counting integer
        for (Card.Suit suit : Card.Suit.values()) // for every card suit
        {
            for (Card.Rank rank : Card.Rank.values()) // for every card rank
            {
                deck[i] = new Card(rank, suit); // the deck gets a new card, this loop runs a total of 52 times
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
            int random_card = rng.nextInt(deck_size); // picks a first random card
            int random_card_two = rng.nextInt(deck_size); // picks a second random card
            //Basic swap from first to second card in order to shuffle the deck
            Card temp = deck[random_card]; // this is essentially a variable swap
            deck[random_card] = deck[random_card_two]; // the values from one card are being transferred to the object of another card
            deck[random_card_two] = temp; // the object of one card is receiving, in turn, the values of another card
        }
    }
    //
    //This function starts the first deal of 2 cards. needs to be called per
    public Card[] start_deal() // a card array method that basically starts the dealer's dealing
    {
        Card[] hand = new Card[start_hand]; // an array of cards which is essentially the dealer's hand, which is equal in size to that of the starting hand
        for (int deck_position = 0; deck_position < 2; deck_position++) // deck position is used as the delimiter up to 2 times for the cards a player has
        {
            hand[deck_position] = deck[deck_position]; // a player can have no more than 2 cards, and this loop swaps them from deck to hand
        }
        return hand;
    }
}
