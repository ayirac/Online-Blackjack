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
    private JPanel panel = new JPanel(new GridBagLayout()); // game panel object for the panel containing the game

    // Elements
    /*private ActorCard p1 = new ActorCard("p1");
    private ActorCard p2 = new ActorCard("p2");
    private ActorCard p3 = new ActorCard("p3");
    private ActorCard p4 = new ActorCard("p4");
*/
    // logic

    public static final String HIT = "hit"; // hit string - immutable
    public static final String STAND = "stand"; // stand string - immutable
    public static final String INPUTWAGER = "input-wager"; // input wager string - immutable
    public static final String PLACEWAGER = "place-wager"; // place wager string - immutable
    public static final String[] BUTTONS = {HIT, STAND, INPUTWAGER, PLACEWAGER}; // immutable string array of buttons, most likely for labels

    private String lobbyName; // private lobby name string
    private int state; // private state of game integer
    public int getState() { // get state, public method has no params but is type int just to return state
        return state;
    }

    private DealerCard dealer = new DealerCard(); // the dealer's card is initialized here
    private ActionButtons actionButtons; // actionbuttons var initialized here
    private boolean turnedOn = false; // turned on set to false at first, all three variables private
    
    private ArrayList<PlayerCard> players = new ArrayList<PlayerCard>(); //  arraylist of player cards called players is initialized here

    public GamePanel(ActionListener listener) { // game panel method which takes action listener as param
        actionButtons = new ActionButtons(listener); // actionbuttons is initialized to new actionbuttons with listener
    }

    public ActionButtons getActionButtons() { // just returns action buttons
        return actionButtons;
    }

    public int getWager() { // just returns this actionButtons getWager recursively within itself, maybe a call to another method
        return this.actionButtons.getWager();
    }

    public JPanel getPanel() { // returns this JPanel
        return this.panel;
    }

    public void setState(int s) { // set state given int s as a parameter
        this.state = s; // this state has s as its value
    }

    public DealerCard getDealer() { // get the dealer card
        return dealer;
    }

    public ArrayList<PlayerCard> getPlayers() { // get the list of players
        return players;
    }

    public String getLobbyName() { // get the lobby name
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) { // set the lobby name (basically if changing the lobby name is desired or initializing it initially)
        this.lobbyName = lobbyName;
    }

    public void init() { // initialization method
        panel.removeAll(); // Clear the panel
        GridBagConstraints cs = new GridBagConstraints(); // new grid bag constraints for a new game
        cs.gridx = 0; // no horizontal deviation from middle 
        cs.gridy = 0; // no vertical deviation from middle
        cs.gridwidth = 5; // width of 5
        cs.fill = GridBagConstraints.HORIZONTAL; // the type of fill correction used is equal to the horizontal preset for gridbagconstraints
        panel.add(dealer.getPanel(), cs);      // Dealer
        cs.gridx = 0;                          // buttons
        cs.gridy = 1;                          // slightly down
        cs.gridwidth = 1;                      // grid width of 1
        panel.add(actionButtons, cs); // add action buttons to the panel
        Insets playerInsets = new Insets(10, 10, 10, 10); // Customize the values as needed
    
        for (int i = 0; i < players.size(); i++) {      // Player
            cs.gridx = i; // spreads the players in a horizontal formation 
            cs.gridy = 2; // puts them 2 units below the dealer
            cs.gridwidth = 1; // width of 1
            cs.insets = playerInsets; // Set the insets for player panel
            panel.add(players.get(i).getPanel(), cs); // add player to the panel
        }
        panel.repaint(); // repaint the panel
        panel.revalidate(); // revalidate the panel
    }
    // toggle method takes string named string, string named name
    public void toggle(String string, String name) {
            for (int i = 0; i < this.players.size(); i++) { // for all players in the game
                if (name.equals(this.players.get(i).getNamed())) { // if the player's name matches
                    if (string.equals("delete")) { // if the parameter given is "delete"
                        this.players.get(i).setResult(""); // remove the player data
                        break; // break statement in an if
                    }
                    if (!turnedOn) { // if not turned on
                        this.players.get(i).setResult("Result: " + string); // this string determines player outcomes such as bust
                    } else {
                        this.players.get(i).setResult(""); // blank result
                    }
                    turnedOn = !turnedOn; // set to its opposite(true if false, false if true)
                    
                    break; // break statement in an if else
                }
            }         
    }
}
