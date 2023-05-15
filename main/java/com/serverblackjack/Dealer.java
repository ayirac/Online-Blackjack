package com.serverblackjack;

public class Dealer extends Actor {
	// overrides previous (assumedly actor) declaration of get data
	@Override
    public String getData() {
        // returns the cards in the dealer's deck
		String s = "dealer" + ":" + this.getState() +  ":" + "hand" + ":";
        for (int j = 0; j < this.getHand().getCards().size(); j++) {
            s += this.getHand().getCards().get(j).getType() + ":";
        }
        return s;
    }
}
