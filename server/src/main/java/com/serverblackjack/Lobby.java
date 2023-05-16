package com.serverblackjack;

import java.util.ArrayList;
import java.util.Random;

public class Lobby implements Runnable{
    private int id_;
    private String name_;
    private int maxPlayers_;
    private int state_;                     // state in game, in-game... waitng on p1 to hit.. timeout, 30 seconds between rounds to bet, collecitng wagers..
    private Random rand_ = new Random();
    private ArrayList<Player> players_ = new ArrayList<Player>();       // players array - cards, wager, status(out, in, stand, etc..)
    private Dealer dealer_;                  // dealer object cards (some might be hidden), status
    private Thread thread_;

    public Lobby(int id, String name, int maxPlayers) {
        this.id_ = id;
        this.name_ = name;
        this.maxPlayers_ = maxPlayers;

        //test code
        this.generateTestingData();
        System.out.println(this.getData());

        // testing threading
        thread_ = new Thread(this);
        thread_.start();
    }
    public void run() {
        System.out.println("This code is running in a thread");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(this.id_ + " slept for 5s");
    }
    public int getId() {
        return id_;
    }
    public int getMaxPlayers() {
        return maxPlayers_;
    }
    public void setMaxPlayers(int maxPlayers_) {
        this.maxPlayers_ = maxPlayers_;
    }
    public String getName() {
        return name_;
    }
    public void setName(String name_) {
        this.name_ = name_;
    }
    public int getPing() {
        return rand_.nextInt(10) + 1;
    }
    public String getCurrentPlayers() {
        return players_.size() + "/" + this.maxPlayers_;
    }
    public int getState() { return this.state_;}

    // Returns servers-data:dealer-data:players-data
    public String getData() {
        String s = "server-data:" + this.getName() + ":" + Integer.toString(this.getState()) + ":" + dealer_.getData();
        for (int j = 0; j < this.players_.size(); j++) {
            s += this.players_.get(j).getData();
        }
        return s;
    }

    public void generateTestingData() {
        // Populate players array with random players
        String[] names = {"Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Heidi", "Ivan", "Judy"};
        int numPlayers = rand_.nextInt(3) + 1;
        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(names[rand_.nextInt(names.length)]); 
            player.setWager(new Random().nextInt(500));
            player.getHand().populate();
            this.players_.add(player);
        }
        // Randomize dealer
        this.dealer_ = new Dealer();
        this.dealer_.getHand().populate();

        // Randomize state
        this.state_ = 5;
    }
}
