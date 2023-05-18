package com.clientblackjack.gui;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.clientblackjack.gui.Card.Suit;

import java.awt.*;

public class GamePanel {
    // Panel init
    private JPanel panel = new JPanel(new GridBagLayout());

    // Elements
    /*private ActorCard p1 = new ActorCard("p1");
    private ActorCard p2 = new ActorCard("p2");
    private ActorCard p3 = new ActorCard("p3");
    private ActorCard p4 = new ActorCard("p4");
*/
    // logic

    public static final String HIT = "hit";
    public static final String STAND = "stand";
    public static final String INPUTWAGER = "input-wager";
    public static final String PLACEWAGER = "place-wager";
    public static final String[] BUTTONS = {HIT, STAND, INPUTWAGER, PLACEWAGER};

    private String lobbyName;
    private int state;
    public int getState() {
        return state;
    }

    private DealerCard dealer = new DealerCard();
    private ActionButtons actionButtons;
    private boolean turnedOn = false;
    
    private ArrayList<PlayerCard> players = new ArrayList<PlayerCard>();

    public GamePanel(ActionListener listener) {
        actionButtons = new ActionButtons(listener);
    }

    public ActionButtons getActionButtons() {
        return actionButtons;
    }

    public int getWager() {
        return this.actionButtons.getWager();
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
        panel.removeAll(); // Clear the panel
        GridBagConstraints cs = new GridBagConstraints();
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 5;
        cs.fill = GridBagConstraints.HORIZONTAL;
        panel.add(dealer.getPanel(), cs);      // Dealer

        cs.gridx = 0;                          // buttons
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(actionButtons, cs);
        
        for (int i = 0; i < players.size(); i++) {      // Player
            cs.gridx = i;
            cs.gridy = 2;
            cs.gridwidth = 1;
            panel.add(players.get(i).getPanel(), cs);
        }
        panel.repaint();
        panel.revalidate();
    }

    public void toggle(String string, String name) {
            for (int i = 0; i < this.players.size(); i++) {
                if (name.equals(this.players.get(i).getNamed())) {
                    if (string.equals("delete")) {
                        this.players.get(i).setResult("");
                        break;
                    }
                    if (!turnedOn) {
                        this.players.get(i).setResult("Result: " + string);
                    } else {
                        this.players.get(i).setResult("");
                    }
                    turnedOn = !turnedOn;
                    
                    break;
                }
            }      
        
    }


}
