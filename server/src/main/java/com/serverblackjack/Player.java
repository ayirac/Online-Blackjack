package com.serverblackjack;

// Child class, A player might have a -Name, Hand, -Wager, State (actions available to them)
public class Player extends Actor{
    private String name;
    private int wager;

    public Player(String n) {
        this.name = n;
        this.wager = 0;
    }

    public String getName() {return this.name;}
    public int getWager() {return this.wager;}
    public void setWager(int w) {this.wager = w;}
    // output data as such: name:state:wager:hand:cDIA5:cSPAJ:
    @Override
    public String getData() {
        String s = this.getName() + ":" + this.getState() + ":" + Integer.toString(this.getWager()) + ":" + "hand" + ":";
        for (int j = 0; j < this.getHand().getCards().size(); j++) {
            s += this.getHand().getCards().get(j).getType() + ":";
        }
        return s;
    }
}
