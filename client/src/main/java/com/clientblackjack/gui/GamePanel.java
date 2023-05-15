package com.clientblackjack.gui;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.clientblackjack.gui.Card.Suit;

import java.awt.*;

public class GamePanel {
    // Panel init
    private JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();

    // Elements
    /*private ActorCard p1 = new ActorCard("p1");
    private ActorCard p2 = new ActorCard("p2");
    private ActorCard p3 = new ActorCard("p3");
    private ActorCard p4 = new ActorCard("p4");
*/
    // logic
    private String lobbyName;
    private int state;
    private DealerCard dealer = new DealerCard();
    private ArrayList<PlayerCard> players = new ArrayList<PlayerCard>();

    public GamePanel(ActionListener listener) {
       
    }

    public JPanel getPanel() {
        return this.panel;
    }

    public void setState(int s) {
        this.state = s;
    }

    public DealerCard getDealer() {
        return dealer;
    }

    public ArrayList<PlayerCard> getPlayers() {
        return players;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public void init() {
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 4;
        panel.add(dealer.getPanel(), constraints);      // Dealer
        
        for (int i = 0; i < players.size(); i++) {      // Player
            constraints.gridx = i;
            constraints.gridy = 1;
            constraints.gridwidth = 1;
            panel.add(players.get(i).getPanel(), constraints);
        }
    }

}
