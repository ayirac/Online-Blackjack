package com.serverblackjack;

import java.util.ArrayList;
import java.util.Random;

public class Lobby {
    private int id_; // lobby ID integer
    private String name_; // lobby name string
    private int maxPlayers_; // the maximum amount of players for the lobby
    private int state_;                     // state in game, in-game... waitng on p1 to hit.. timeout, 30 seconds between rounds to bet, collecitng wagers..
    private Random rand_ = new Random(); // mathematical random aspect - not "real" random
    private ArrayList<Player> players_ = new ArrayList<Player>();       // players array - cards, wager, status(out, in, stand, etc..)
    private Dealer dealer_;                  // dealer object cards (some might be hidden), status

    public Lobby(int id, String name, int maxPlayers) {
    	// lobby needs an id, name, and max players
        this.id_ = id;
        this.name_ = name;
        this.maxPlayers_ = maxPlayers; // they are established here

        //test code
        this.generateTestingData();
        // prints out the id, name and max players
        System.out.println(this.getData());
    }
    // function which returns lobby ID
    public int getId() {
        return id_;
    }
 // function which returns max amount of players allowed in the lobby
    public int getMaxPlayers() {
        return maxPlayers_;
    }
    // function which sets max amount of players allowed in the lobby
    public void setMaxPlayers(int maxPlayers_) {
        this.maxPlayers_ = maxPlayers_;
    }
    // function which returns name of the lobby
    public String getName() {
        return name_;
    }
    // function which establishes name of the lobby
    public void setName(String name_) {
        this.name_ = name_;
    }
    // function which returns ping of the lobby
    public int getPing() {
        return rand_.nextInt(10) + 1;
    }
    // function which returns current amount of players in the lobby
    public String getCurrentPlayers() {
        return players_.size() + "/" + this.maxPlayers_;
    }
    // function which returns state of the lobby
    public int getState() { return this.state_;}

    // Returns servers-data:dealer-data:players-data
    public String getData() {
    	// adding the dealer data
        String s = "server-data:" + this.getName() + ":" + Integer.toString(this.getState()) + ":" + dealer_.getData();
        for (int j = 0; j < this.players_.size(); j++) {
            // adding the players data
        	s += this.players_.get(j).getData();
        }
        return s;
    }

    public void generateTestingData() {
        // Populate players array with random players
        String[] names = {"Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Heidi", "Ivan", "Judy"};
        // number of players is randomized
        int numPlayers = rand_.nextInt(3) + 1;
        // for each player
        for (int i = 0; i < numPlayers; i++) {
            // random name
        	Player player = new Player(names[rand_.nextInt(names.length)]); 
            // random wager amount
        	player.setWager(new Random().nextInt(500));
            // populated hand of cards
        	player.getHand().populate();
        	// added to the table
            this.players_.add(player);
        }
        // Randomize dealer
        this.dealer_ = new Dealer();
        this.dealer_.getHand().populate();

        // Randomize state
        this.state_ = 5;
    }
}