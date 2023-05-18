package com.serverblackjack;

import java.util.ArrayList;
import java.util.Random;

public class Lobby {
    private int id_;
    private String name_;
    private int maxPlayers_;
    private int state_;                                                 // -1, no players, 0, waiting for players to bet (30secs), 1 dealing, 2 player turn, 3 dealer turn, 4 post-game/calculation
    private Random rand_ = new Random();
    private ArrayList<Player> players_ = new ArrayList<Player>();       // players array - cards, wager, status(out, in, stand, etc..)

    private Dealer dealer_;                                             // dealer object cards (some might be hidden), status
    private int playerTurnStage_;

    
    public Lobby(int id, String name, int maxPlayers) {
        this.id_ = id;
        this.name_ = name;
        this.maxPlayers_ = maxPlayers;
        this.dealer_ = new Dealer();
        this.state_ = -1;

        //test code
        //this.generateTestingData();
        //System.out.println(this.getData());
        // testing threading
    }
    /* 
    public void run() {
        System.out.println("This code is running in a thread");
        try {
            while (true) {
                switch (this.state_) {
                    case -1:
                        System.out.println(players_.size());
                        if (players_.size() > 0) {
                            System.out.println("We have players! Starting game");
                            for (int i = 0; i < players_.size(); i++) {
                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
                                LocalDateTime now = LocalDateTime.now();
                                players_.get(i).getConnection().sendMessage(dtf.format(now) + "|-!-" + ":" + "update-data:" + this.getUpdateData() + "-!-|");
                                }
                                this.state_ = 0;
                            }
                            break;
                    case 0:
                        for (int i = 0; i < players_.size(); i++) {
                            
                            
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
                            LocalDateTime now = LocalDateTime.now();
                            players_.get(i).getConnection().sendMessage(dtf.format(now) + "|-!-" + ":" + "update-data:" + this.getUpdateData() + "-!-|");
                            String response = players_.get(i).getConnection().receiveMessage();
                            this.server_.parseMessage(response, players_.get(i).getConnection());
                        }
                    Thread.sleep(500);
                } 
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    */
    public int getPlayerTurnStage() {
        return playerTurnStage_;
    }
    public void setPlayerTurnStage(int playerTurnStage_) {
        this.playerTurnStage_ = playerTurnStage_;
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
    public Dealer getDealer() {
        return dealer_;
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

    public void addPlayer(String name, Connection cnt) {
        Player player = new Player(name, cnt); 
        this.players_.add(player);
    }

    // For initial data, Returns servers-data:dealer-data:players-data
    public String getData() {
        String s = "server-data:" + this.getName() + ":" + this.getState() + ":" + dealer_.getData();
        for (int j = 0; j < this.players_.size(); j++) {
            s += this.players_.get(j).getData();
        }
        return s;
    }

    // For streamed data
    public String getUpdateData() {
        String s = "update-data:" + this.getName() + ":" + this.getState() + ":" + dealer_.getData();
        for (int j = 0; j < this.players_.size(); j++) {
            s += this.players_.get(j).getData();
        }
        return s;
    }

    // deals to each player & dealer
    public void deal() {
        for (int i = 0; i < this.players_.size(); i++) { //player 1x
            this.players_.get(i).getHand().deal();
        }
        this.dealer_.getHand().deal();                  // dealer 1x
        for (int i = 0; i < this.players_.size(); i++) { //player 2x
            this.players_.get(i).getHand().deal();
        }
        this.dealer_.getHand().deal();                  // dealer 1x
        
    }

    public void dealDealer() {
        this.dealer_.getHand().deal();                  // dealer 1x
    }

    public ArrayList<Player> getPlayers() {
        return players_;
    }
    public void setPlayers(ArrayList<Player> players_) {
        this.players_ = players_;
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
    public void setState(int i) {
        this.state_ = i;
    }
}
