package com.serverblackjack;

// Child class, A player might have a -Name, Hand, -Wager, State (actions available to them)
public class Player extends Actor{
    private String name;
    private int wager;
    private Connection connection;
    private int money;

    public Player(String n, Connection cnt) {
        this.name = n;
        this.wager = 0;
        this.connection = cnt;
        this.money = 5000;
    }

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
        boolean atleastOne = false;
        String s = this.getName() + ":" + this.getState() + ":" + Integer.toString(this.getWager()) + ":" + Integer.toString(this.getMoney()) + ":"  + "hand" + ":"; // adding money here..
        for (int j = 0; j < this.getHand().getCards().size(); j++) {
            atleastOne = true;
            s += this.getHand().getCards().get(j).getType() + ":";
        }
        if (!atleastOne) {
            s += "n_a" + ":";
        }
        return s;
    }

    public int getMoney() {
        return this.money;
    }

    public void setMoney(int mon) {
        this.money = mon;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
