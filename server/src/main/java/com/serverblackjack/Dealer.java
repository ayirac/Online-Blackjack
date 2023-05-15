package com.serverblackjack;

public class Dealer extends Actor {
    @Override
    public String getData() {
        String s = "dealer" + ":" + this.getState() +  ":" + "hand" + ":";
        for (int j = 0; j < this.getHand().getCards().size(); j++) {
            s += this.getHand().getCards().get(j).getType() + ":";
        }
        return s;
    }
}
