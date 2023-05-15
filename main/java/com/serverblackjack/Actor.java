package com.serverblackjack;

// Base class for Player/Dealer data classes
// A player might have a Name, Hand, Wager, State (actions available to them)
// A dealer might have a Hand (of which some might be 'hidden'), State
public class Actor {
    private Hand hand;
    private int state;

    Actor() {
        this.hand = new Hand();
        this.state = 0;
    }

    public Hand getHand() {return this.hand; }
    public int getState() {return this.state; }
    public void setState(int s) {this.state = s; }
    public String getData() {return "";}
}
