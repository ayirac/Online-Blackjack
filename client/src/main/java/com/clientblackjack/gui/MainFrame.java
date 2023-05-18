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
    private boolean activatedResultsLabel;

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
                    // GamePanel Action buttons
                    case GamePanel.HIT:
                        System.out.println("hit!");
                        serverConnection_.sendMessage(("|-!-action:" + getLoggedName() + ":" + loggedServer_ + ":" + "hit" + ":-!-|"));
                        try {
                            parseMessage(serverConnection_.receiveMessage());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        break;
                    case GamePanel.STAND:
                        serverConnection_.sendMessage(("|-!-action:" + getLoggedName() + ":" + loggedServer_ + ":" + "stand" + ":-!-|"));
                        try {
                            parseMessage(serverConnection_.receiveMessage());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        break;
                    case GamePanel.PLACEWAGER:
                        System.out.println("wager placed!");
                        serverConnection_.sendMessage(("|-!-action:" + getLoggedName() + ":" + loggedServer_ + ":" + "wager" +  ":" + gamePanel.getWager() + ":-!-|"));
                        try {
                            parseMessage(serverConnection_.receiveMessage());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
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
                this.activatedResultsLabel = false;
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
                    String nam = args[f++];
                    boolean isPlayer = false;
                    if (nam.equals(this.loggedName_))
                        isPlayer = true;
                    PlayerCard player = new PlayerCard(nam, isPlayer);
                    player.setState(Integer.parseInt(args[f++]));
                    player.setWager(Integer.parseInt(args[f++])); // added money here
                    if (isPlayer)
                        player.setMoney(Integer.parseInt(args[f++]));
                    else 
                        f++; // skip other peoples money, myb fix this in future
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
                Boolean[] playersProcessed = new Boolean[12]; // 12 players have false data at start
                for (int n = 0; n < playersProcessed.length; n++) {
                    playersProcessed[n] = false;
                }

                while (args[f] != "\0") {
                    // locate player
                    String name = args[f];
                    int pos = 0;
                    boolean playersExist = false;
                    if (this.gamePanel.getPlayers().size() != 0) {
                        playersExist = true;
                        for (pos = 0; pos < this.gamePanel.getPlayers().size(); pos++) {
                            if (playersProcessed[pos] == false) {
                                if (this.gamePanel.getPlayers().get(pos).getNamed().equals(name)) {
                                    playersProcessed[pos] = true;
                                    break;
                                }
                            }
                            
                        }
                    }
                    System.out.println("Handing player #" + Integer.toString((pos)));
                    String playerName = args[f++];
                    int cState = Integer.parseInt(args[f++]);
                    int cWager = Integer.parseInt(args[f++]);
                    int cMoney = Integer.parseInt(args[f++]);
                    if (cState == 0 || cState == 1) {
                        this.activatedResultsLabel = false; // lazy way of setting this to false at start of each game
                    }
                    System.out.println(activatedResultsLabel);
                    if (this.loggedName_.equals(playerName))
                        this.gamePanel.getPlayers().get(pos).setMoney(cMoney);
                    //int cMoney = Integer.parseInt(args[f++]);

                    // Adjust buttons/actions based on state
                    if (playerName.equals(this.loggedName_)) {
                        if (cState == 1) {
                            this.gamePanel.getActionButtons().toggleWagerVisibility(true);
                        }
                        else if (cState == 2) {
                            this.gamePanel.getActionButtons().toggleWagerVisibility(false);
                            this.gamePanel.getActionButtons().toggleButtonVisibility(true);
                        } else {
                            this.gamePanel.getActionButtons().toggleWagerVisibility(false);
                            this.gamePanel.getActionButtons().toggleButtonVisibility(false);
                        }
                    }
                    
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
                    
                    if (playersProcessed[pos]) { // problem is here...
                        if (!recievedHand.isEqual(this.gamePanel.getPlayers().get(pos).getHand())) {
                            //player.setHand(recievedHand);
                            //player.getHand().addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
                            //this.gamePanel.getPlayers().add(player); 
                            System.out.println(Integer.toString((pos)) + " has new deck, updating");
                            //this.gamePanel.getPlayers().get(pos-1).getHand().addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
                            this.gamePanel.getPlayers().get(pos).setHand(recievedHand);
                        }
                        if ( this.gamePanel.getPlayers().get(pos).getState() == 4 || this.gamePanel.getPlayers().get(pos).getState() == 6 || this.gamePanel.getPlayers().get(pos).getState() == 7) { // check for victory/bust
                            if ( this.gamePanel.getPlayers().get(pos).getNamed().equals(this.loggedName_)) {
                                if (!activatedResultsLabel) {
                                    activatedResultsLabel = true;
                                    if (this.gamePanel.getPlayers().get(pos).getState() == 4 || this.gamePanel.getPlayers().get(pos).getState() == 7) //bust
                                        this.gamePanel.toggle("Bust", this.loggedName_);
                                    else if (this.gamePanel.getPlayers().get(pos).getState() == 6) // win
                                        this.gamePanel.toggle("Win", this.loggedName_);
                                    this.gamePanel.getDealer().setHidden(false);
                                }
                            }
                        } else if (this.gamePanel.getPlayers().get(pos).getState() == 0 || this.gamePanel.getPlayers().get(pos).getState() == 1) {
                            this.gamePanel.toggle("delete", this.loggedName_);
                        }
                        
                        this.gamePanel.getPlayers().get(pos).setState(cState);
                        this.gamePanel.getPlayers().get(pos).setWager(cWager);
                        //this.gamePanel.getPlayers().get(pos).setMoney(cMoney);
                    } else {
                        PlayerCard player = new PlayerCard(playerName, false);
                        player.setState(cState);
                        player.setWager(cWager);
                        //player.setMoney(cMoney);
                        this.gamePanel.getPlayers().add(player);
                    }
                    gamePanel.init();

                    if (this.gamePanel.getDealer().getHand().getCards().size() >= 2) {
                        if (this.gamePanel.getState() == 1 || this.gamePanel.getState() == 2) { // lazy way of setting dealer card to invis at start
                            try {
                                if (!this.gamePanel.getDealer().hiddenCard())
                                    this.gamePanel.getDealer().hideSecondCard();
                            } catch (IOException ds) {
                                ds.printStackTrace();
                            }
                        }   
                        else if (this.gamePanel.getDealer().hiddenCard() && (this.gamePanel.getState() == 1 || this.gamePanel.getState() == 2)) { // show card 
                            try {
                                if (this.gamePanel.getDealer().hiddenCard())
                                    this.gamePanel.getDealer().showSecondCard();
                            } catch (IOException ds) {
                                ds.printStackTrace();
                            }
                        }
                    }
                    
                    
                    
                }
                break;       
            default: // handle bad messages here
                break;    
        }
        return new Data("");
    }

}
