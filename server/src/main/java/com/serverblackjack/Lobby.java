package com.serverblackjack;

import java.util.Random;

public class Lobby {
    private int id_;
    private String name_;
    private int maxPlayers_;
    private Random rand_ = new Random();

    public Lobby(int id, String name, int maxPlayers) {
        this.id_ = id;
        this.name_ = name;
        this.maxPlayers_ = maxPlayers;
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
        return (rand_.nextInt(8 - 0 + 1) + 1) + "/" + this.maxPlayers_;
    }
}
