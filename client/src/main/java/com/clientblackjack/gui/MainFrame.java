package com.clientblackjack.gui;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.clientblackjack.Connection;

import javax.swing.JButton;

public class MainFrame {
    private CardLayout cLayout = new CardLayout();
    private JPanel cards = new JPanel(cLayout);
    private MainMenuPanel mainMenuPanel;
    private LoginPanel loginPanel;
    private ServerListPanel serverListPanel;
    private CreditsPanel creditsPanel;
    private Connection serverConnection_;

    public MainFrame() {
        // Listeners
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String buttonName = ((JButton) e.getSource()).getName();
                switch (buttonName) {
                    case MainMenuPanel.PLAYGAME:
                        swapPlayGame();
                        break;
                    case MainMenuPanel.CREDITS:
                        swapCredits();
                        break;
                    case LoginPanel.SUBMIT:
                        System.out.print("Attempting to login as " + loginPanel.getUsername() + ":" + loginPanel.getPasswordHash());

                        try {
                            // basic test connection
                            serverConnection_ = new Connection(new Socket("localhost", 5012));
                            serverConnection_.sendMessage("|-!-login:" + loginPanel.getUsername() + ":" + loginPanel.getPasswordHash() + ":-!-|");
                            String response = serverConnection_.receiveMessage();
                            String loginResult = parseMessage(response).str;
                            if (loginResult.equals("valid")) {
                                serverConnection_.sendMessage("|-!-server-list:-!-|");
                                response = serverConnection_.receiveMessage();
                                String[][] servers = parseMessage(response).arr;
                                swapServerList(servers);
                            } else if (loginResult.equals("invalid")) {
                                System.out.println("Invalid login! Popup box to be implemented");
                            } else {
                                System.out.println("Error: Incorrect message passing");
                            }
                            //serverConnection_.close();
                        } catch (IOException ev) {
                            ev.printStackTrace();
                        }
                    
                        
                }
            }
        };
        mainMenuPanel = new MainMenuPanel(actionListener);
        loginPanel = new LoginPanel(actionListener);
        serverListPanel = new ServerListPanel(actionListener);
        creditsPanel = new CreditsPanel(actionListener);

        cards.add(mainMenuPanel.getPanel(), MainMenuPanel.MAINMENU);
        cards.add(loginPanel.getPanel(), MainMenuPanel.PLAYGAME);
        cards.add(creditsPanel.getPanel(), MainMenuPanel.CREDITS);
        cards.add(serverListPanel.getPanel(), "server-list");
    }

    public void run(int x, int y) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(cards);
        frame.setSize(x, y);
        frame.setVisible(true);
    }

    public void swapMainMenu() {
        cLayout.show(cards, MainMenuPanel.MAINMENU);
    }

    public void swapPlayGame() {
        cLayout.show(cards, MainMenuPanel.PLAYGAME);
    }

    public void swapServerList(String[][] servers) {
        serverListPanel.loadServers(servers);
        cLayout.show(cards, "server-list");
    }

    public void swapInstructions() {
        cLayout.show(cards, MainMenuPanel.INSTRUCTIONS);
    }

    public void swapLeaderboard() {
        cLayout.show(cards, MainMenuPanel.LEADERBOARD);
    }

    public void swapCredits() {
        cLayout.show(cards, MainMenuPanel.CREDITS);
    }

    public void quitGame() {
        System.exit(0);
    }

    private class Data {
        public String str;
        public String[][] arr;

        public Data(String s, String[][] a) {
            this.str = s;
            this.arr = a;
        }
        
        public Data(String s) {
            this.str = s;
            this.arr = new String[0][];
        } 

        public Data(String[][] a) {
            this.str = "";
            this.arr = a;
        } 
    }

     // Types of messages that the server might send
    // |-!-login:invalid-!-| 
    // |-!-getServers:US West 1:7/8:50-!-|
    // Parses a message & sends a response or logs an error.
    public Data parseMessage(String msg) {
        String data = msg.substring(msg.indexOf("|-!-")+4, msg.indexOf("-!-|")); // Get text inbetween start/end
        System.out.println("parsing  " + msg);
        int i = 0, previ = 0, e = 0;
        String[] args = new String[50];

        while ((i = data.indexOf(":", previ)) != -1) {
            String cmd = data.substring(previ, i);
            previ = i+1;
            args[e++] = cmd;
        }
        
        switch (args[0]) {
            case "login":
                Data d = new Data(args[1]);
                System.out.println(d.str);
                return d;
            case "server-list":
                // name:players:ping
                int rows = (args.length-2)/3;
                String [][] servers = new String[rows][3];
                int c = 0, j = 1;
                for (int r = 0; r < rows; r++) {
                    servers[r][c++] = args[j++];
                    servers[r][c++] = args[j++];
                    servers[r][c++] = args[j++];
                    c %= 3;
                }
                return new Data(servers);
        }
        return new Data("");
    }
}
