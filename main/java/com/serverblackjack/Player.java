package com.serverblackjack;

// Child class, A player might have a -Name, Hand, -Wager, State (actions available to them)
public class Player extends Actor {
	// name of the player
    private String name;
    // how many in game credits they're betting
    private int wager;

    public Player(String n) { // default player is named default string
        this.name = n;
        this.wager = 0; // has 0 credits to begin with
    }
    // you might need a setname statement
    public String getName() {return this.name;} // get player's name
    public int getWager() {return this.wager;} // get player's wager
    public void setWager(int w) {this.wager = w;} // set player's wager
    // output data as such: name:state:wager:hand:cDIA5:cSPAJ:
    @Override
    public String getData() { // return data for player in string form
        String s = this.getName() + ":" + this.getState() + ":" + Integer.toString(this.getWager()) + ":" + "hand" + ":";
        for (int j = 0; j < this.getHand().getCards().size(); j++) {
            s += this.getHand().getCards().get(j).getType() + ":";
        } // s has the player's name, state, wager, and their cards as well as the types of cards in their hand
        return s;
    }
}
