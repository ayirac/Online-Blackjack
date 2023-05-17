package com.clientblackjack.gui;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.clientblackjack.Connection;
import com.clientblackjack.gui.Card.Rank;
import com.clientblackjack.gui.Card.Suit;

import javax.swing.JButton;

public class MainFrame {
    private JFrame frame;
    public enum Panel {
        MAINMENU("Main Menu"),
        LOGIN("Login"),
        INSTRUCTIONS("Instructions"),
        LEADERBOARD("Leaderboard"),
        CREDITS("Credits"),
        SERVERLIST("Server List"),
        GAME("Game");
    
        private final String name;
        Panel(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }

    private class DataThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    serverConnection_.sendMessage("|-!-update-data:" + loggedName_ + ":" +loggedServer_ + ":-!-|");
                    String response = serverConnection_.receiveMessage();
                    parseMessage(response);
                    Thread.sleep(1000);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    
    private CardLayout cLayout = new CardLayout();
    private JPanel cards = new JPanel(cLayout);
    // Panels to be used as cards in a cardlayout, add more as needed
    private MainMenuPanel mainMenuPanel;
    private LoginPanel loginPanel;
    private ServerListPanel serverListPanel;
    private InstructionsPanel instructionsPanel;
    private GamePanel gamePanel;
    private Connection serverConnection_;
    private CreditsPanel creditsPanel_;
    private String loggedName_;
    private String loggedServer_;

    private DataThread dataThread;


    public MainFrame() {
        
        // Listeners, handle button clicks for all panels here, parse
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String buttonName = ((JButton) e.getSource()).getName();
                switch (buttonName) { 
                    // MainMenuPanel buttons
                    case MainMenuPanel.PLAYGAME:
                        swapPlayGame();
                        break;
                    case MainMenuPanel.INSTRUCTIONS:
                        swapInstructions();
                        break;
                    case MainMenuPanel.LEADERBOARD:
                        swapLeaderboard();
                        break;
                    case MainMenuPanel.CREDITS:
                        swapCredits();
                        break;
                    case MainMenuPanel.QUITGAME:
                        System.exit(0);
                        break;
                    // LoginPanel buttons 
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
                                setLoggedName(loginPanel.getUsername());
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
                        break;  
                    case ServerListPanel.CONNECT: // add handling for connecting to specific server later
                        swapGame();
                        break;
                    case "back":                // this back handler works for anything that goes back to mainMenu. refactor: add an enum later somewhere like above
                        swapMainMenu();
                        break;
                }
            }
        };
        // serverlist table listener, listen for attempts to connect to a server
        // serverlist table listener, listen for attempts to connect to a server
        serverListPanel = new ServerListPanel();

        // Initialize flag and timer
        ListSelectionListener tableListener = new ListSelectionListener() {
            boolean isExecuting = false;
            javax.swing.Timer timer = new javax.swing.Timer(1000, null);
            public void valueChanged(ListSelectionEvent event) {
                if (isExecuting) {
                    // Ignore event if the method is currently being executed
                    return;
                }
                isExecuting = true;
                timer.setRepeats(false); // Disable repeat
                timer.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        // Execute method after the delay
                        try {
                            loggedServer_ = serverListPanel.getTable().getValueAt(serverListPanel.getTable().getSelectedRow(), 0).toString();
                            serverConnection_.sendMessage(("|-!-connect:" + getLoggedName() + ":" + loggedServer_ + ":-!-|"));
                            String response = serverConnection_.receiveMessage();
                            parseMessage(response);
                            gamePanel.init();
                            swapGame();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // Reset flag
                        isExecuting = false;
                    }
                });
                // Start the timer
                timer.start();
            }
        };

        serverListPanel.getTable().getSelectionModel().addListSelectionListener(tableListener);

        

        // Initalize panels, pass the above action listener to parse data in MainFrame
        mainMenuPanel = new MainMenuPanel(actionListener);

        
        //Re-did these panels to have a working back button within each panel.
        loginPanel = new LoginPanel(actionListener);
        creditsPanel_ = new CreditsPanel(actionListener);
        instructionsPanel = new InstructionsPanel(actionListener);
        gamePanel = new GamePanel(actionListener);


        // Add panel to MainFrame's cardlayout
        cards.add(mainMenuPanel.getPanel(), Panel.MAINMENU.getName());
        cards.add(loginPanel.getPanel(), Panel.LOGIN.getName());
        cards.add(creditsPanel_.getPanel(), Panel.CREDITS.getName());
        cards.add(serverListPanel.getPanel(), Panel.SERVERLIST.getName());
        cards.add(instructionsPanel.getPanel(), Panel.INSTRUCTIONS.getName());
        cards.add(gamePanel.getPanel(), Panel.GAME.getName());
    }

    public void run(int x, int y) {
        frame = new JFrame();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(cards);
        frame.setSize(x, y);
        frame.setVisible(true);
    }

    public void swapMainMenu() {
        cLayout.show(cards, Panel.MAINMENU.getName());
    }

    public void swapPlayGame() {
        // Future: add check here to see if already logged in & then use that session instead of forcing login each time
        cLayout.show(cards, Panel.LOGIN.getName());
    }

    public void swapServerList(String[][] servers) {
        serverListPanel.loadServers(servers);
        cLayout.show(cards, Panel.SERVERLIST.getName());
    }

    public void swapInstructions() {
        cLayout.show(cards, Panel.INSTRUCTIONS.getName());
    }

    public void swapLeaderboard() {
        cLayout.show(cards, Panel.LEADERBOARD.getName());
    }

    public void swapCredits() {
        cLayout.show(cards, Panel.CREDITS.getName());
    }

    public void swapGame() {
        cLayout.show(cards, Panel.GAME.getName());
    }


    public void quitGame() {
        System.exit(0);
    }

    public String getLoggedName() {
        return loggedName_;
    }

    public void setLoggedName(String loggedNamed_) {
        this.loggedName_ = loggedNamed_;
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
    public Data parseMessage(String msg) throws IOException {
        String data = msg.substring(msg.indexOf("|-!-")+4, msg.indexOf("-!-|")); // Get text inbetween start/end
        System.out.println("parsing  " + msg);
        int i = 0, previ = 0, e = 0;
        String[] args = new String[50];

        while ((i = data.indexOf(":", previ)) != -1) {
            String cmd = data.substring(previ, i);
            previ = i+1;
            args[e++] = cmd;
        }
        args[e] = "\0";
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
                // server-data:US West 1:5:dealer:0:hand:spades_king:diamonds_two:Bob:0:0:hand:clubs_seven:hearts_five:
                // server-data:name:state:dealer:dealerState:hand:suit_rank:suit_rank:playerName:playerWager:playerState:hand:suit_rank:suit_rank:...next player... next player... and so ong
            case "server-data": // >lobby class ->dealer class & player class
                int f = 1;
                this.gamePanel.setLobbyName(args[f++]);
                this.gamePanel.setState(Integer.parseInt(args[f++]));
                f++; // skip dealerTag
                this.gamePanel.getDealer().setState(Integer.parseInt(args[f++]));
                f++; // skip handTag
                boolean validCard = true;                           // DEALER DATA, kinda messy, refactor later
                String[] cardType = {"",""};
                while (validCard) {                             
                    if (args.length > f && args[f].contains("_")) {
                        cardType = args[f++].split("_");
                    } else {
                        validCard = false;
                        break;
                    }
                    Suit suit = Suit.valueOf(cardType[0].toUpperCase());
                    Rank rank = Rank.valueOf(cardType[1].toUpperCase());
                    validCard = Card.isValidCard(suit, rank);
                    if (!validCard)
                        break;
                    this.gamePanel.getDealer().getHand().addCard(new Card(rank, suit));
                }
                // PLAYER DATA
                while (!args[f].equals("\0")) {
                    PlayerCard player = new PlayerCard(args[f++]);
                    player.setState(Integer.parseInt(args[f++]));
                    player.setWager(Integer.parseInt(args[f++]));
                    f++;    // skip handTag
                    validCard = true;                          
                    cardType[0] = "";
                    cardType[1] = "";
                    while (validCard & !args[f].equals("\0")) {          
                        if (args[f].equals("n_a")) {
                            this.gamePanel.getPlayers().add(player);
                            f++;
                            break;
                        }                
                        if (args.length > f && args[f].contains("_")) {
                            cardType = args[f++].split("_");
                        } else {
                            validCard = false;
                            break;
                        }
                        Suit suit = Suit.valueOf(cardType[0].toUpperCase());
                        Rank rank = Rank.valueOf(cardType[1].toUpperCase());
                        validCard = Card.isValidCard(suit, rank);
                        if (!validCard)
                            break;
                        player.getHand().addCard(new Card(rank, suit));
                        this.gamePanel.getPlayers().add(player);
                    }
                    gamePanel.init();
                }
                //Idk best place to put the start of the datastream how about here for now..
                dataThread = new DataThread();
                dataThread.start();    
                break;   
            case "update-data": // >lobby class ->dealer class & player class
                f = 1;
                f++; // skip lobbyName
                this.gamePanel.setState(Integer.parseInt(args[f++]));
                f++; // skip dealerTag
                this.gamePanel.getDealer().setState(Integer.parseInt(args[f++]));
                f++; // skip handTag
                validCard = true;                           // DEALER DATA, kinda messy, refactor later
                String[] cardTyp = {"",""};
                Hand recievedDealerHand = new Hand();    
                while (validCard) {                   
                    if (args.length > f && args[f].contains("_")) {
                        cardTyp = args[f++].split("_");
                    } else {
                        validCard = false;
                        break;
                    }

                        Suit suit = Suit.valueOf(cardTyp[0].toUpperCase());
                        Rank rank = Rank.valueOf(cardTyp[1].toUpperCase());
                        validCard = Card.isValidCard(suit, rank);
                        if (!validCard)
                            break;
                       //this.gamePanel.getDealer().getHand().addCard(new Card(rank, suit));
                       recievedDealerHand.addCard(new Card(rank, suit));
                }
                if (!recievedDealerHand.isEqual(this.gamePanel.getDealer().getHand())) {
                    this.gamePanel.getDealer().setHand(recievedDealerHand);
                }
                // PLAYER DATA
                while (args[f] != "\0") {
                    // locate player
                    String name = args[1];
                    int pos = 0;
                    boolean playersExist = false;
                    if (this.gamePanel.getPlayers().size() != 0) {
                        playersExist = true;
                        for (pos = 0; pos < this.gamePanel.getPlayers().size(); pos++) {
                            if (this.gamePanel.getPlayers().get(pos).getNamed().equals(name)) {
                                break;
                            }
                        }
                    }
                    String playerName = args[f++];
                    int cState = Integer.parseInt(args[f++]);
                    int cWager = Integer.parseInt(args[f++]);
                    f++;    // skip handTag
                    validCard = true;                          
                    cardTyp[0] = "";
                    cardTyp[1] = "";
                    Hand recievedHand = new Hand();
                    while (validCard & !args[f].equals("\0"))  {     
                        if (args[f].equals("n_a")) {
                            f++;
                            break;
                        }                                   
                        if (args.length > f && args[f].contains("_")) {
                            cardTyp = args[f++].split("_");
                        } else {
                            validCard = false;
                            break;
                        }
                        Suit suit = Suit.valueOf(cardTyp[0].toUpperCase());
                        Rank rank = Rank.valueOf(cardTyp[1].toUpperCase());
                        validCard = Card.isValidCard(suit, rank);
                        if (!validCard)
                            break;
                        //player.getHand().addCard(new Card(rank, suit));
                        //recievedHand.addCard(new Card(rank, suit));
                        //if (pos != -1) {
                        //this.gamePanel.getPlayers().get(pos).getHand().addCard(new Card(rank, suit));
                        recievedHand.addCard(new Card(rank, suit));
                        //this.gamePanel.getDealer().setHand(recievedDealerHand);
                    }
                    
                    if (playersExist) { // problem is here...
                        if (!recievedHand.isEqual(this.gamePanel.getPlayers().get(pos-1).getHand())) {
                            //player.setHand(recievedHand);
                            //player.getHand().addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
                            //this.gamePanel.getPlayers().add(player); 
                            System.out.println("Wow they arent equal!!");
                            this.gamePanel.getPlayers().get(pos-1).getHand().addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
                            this.gamePanel.getPlayers().get(pos-1).setHand(recievedHand);
                        }
                    } else {
                        PlayerCard player = new PlayerCard(playerName);
                        player.setState(cState);
                        player.setWager(cWager);
                        this.gamePanel.getPlayers().add(player);
                    }
                    gamePanel.init();
                    
                   /*  if (pos != -1) {
                        
                        gamePanel.init();
                    }*/
                    
                }
                break;       
            default: // handle bad messages here
                break;    
        }
        return new Data("");
    }

}
