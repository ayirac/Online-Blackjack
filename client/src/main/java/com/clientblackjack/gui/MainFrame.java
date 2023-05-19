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

public class MainFrame { // mainframe class uses a JFrame
    private JFrame frame;
    public enum Panel { // enumerated panel with main menu, login, instructions, leaderboard, credits, server list and game
        MAINMENU("Main Menu"),
        LOGIN("Login"),
        INSTRUCTIONS("Instructions"),
        LEADERBOARD("Leaderboard"),
        CREDITS("Credits"),
        SERVERLIST("Server List"),
        GAME("Game");
    
        private final String name; // immutable name string
        Panel(String name) { // panel constructor which takes name and assigns it
            this.name = name;
        }
        public String getName() { // name accessor
            return name;
        }
    }

    private class DataThread extends Thread { // data thread class which takes from imported thread class
        @Override
        public void run() { // run method 
            while (true) { // while it's running
                try { // try to catch an interrupted or io exception (bad data/abrupt program stop)
                    serverConnection_.sendMessage("|-!-update-data:" + loggedName_ + ":" +loggedServer_ + ":-!-|"); // server connection object sends a message
                    String response = serverConnection_.receiveMessage(); // response is what happens when we receive this message
                    parseMessage(response); // parse the response
                    Thread.sleep(1000); // just wait for some time
                } catch (IOException | InterruptedException e) { // print a stack trace if we get something bad
                    e.printStackTrace(); // so we can see what went wrong and where
                }
            }
        }
    }
    
    private CardLayout cLayout = new CardLayout(); // card layout
    private JPanel cards = new JPanel(cLayout); // cards panel which uses card layout
    // Panels to be used as cards in a cardlayout, add more as needed
    private MainMenuPanel mainMenuPanel; // main menu panel
    private LoginPanel loginPanel; // login panel
    private ServerListPanel serverListPanel; // server list panel
    private InstructionsPanel instructionsPanel; // instructions panel
    private GamePanel gamePanel; // game panel
    private Connection serverConnection_; // connection object which connects to server
    private CreditsPanel creditsPanel_; // credits panel
    private String loggedName_; // the name that was logged in the data repo
    private String loggedServer_; // the server that was logged in the data repo
    private boolean activatedResultsLabel; // activated results label

    private DataThread dataThread; // private data thread object


    public MainFrame() { // mainframe method
        
        // Listeners, handle button clicks for all panels here, parse
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // action performed method takes event e as parameter
                String buttonName = ((JButton) e.getSource()).getName(); // button name uses e's name when it is cast as a JButton
                switch (buttonName) { // switch case
                    // MainMenuPanel buttons
                    case MainMenuPanel.PLAYGAME: // play game
                        swapPlayGame(); // swap to the play game panel (dedicated method)
                        break;
                    case MainMenuPanel.INSTRUCTIONS: // instructions
                        swapInstructions(); // swap to the instructions panel (dedicated method)
                        break;
                    case MainMenuPanel.LEADERBOARD: // leaderboard
                        swapLeaderboard(); // swap to the leaderboard panel (dedicated method)
                        break;
                    case MainMenuPanel.CREDITS:
                        swapCredits(); // swap to the credits panel (dedicated method)
                        break;
                    case MainMenuPanel.QUITGAME:
                        System.exit(0); // just kill the entire system
                        break;
                    // LoginPanel buttons 
                    case LoginPanel.SUBMIT: // log in/submit button
                        System.out.print("Attempting to login as " + loginPanel.getUsername() + ":" + loginPanel.getPasswordHash()); // username and password hash -> console
                        try {
                            // basic test connection
                            serverConnection_ = new Connection(new Socket("localhost", 5012)); // use default parameters
                            serverConnection_.sendMessage("|-!-login:" + loginPanel.getUsername() + ":" + loginPanel.getPasswordHash() + ":-!-|"); // send message to server
                            String response = serverConnection_.receiveMessage(); // response is what we get when we receive this message
                            String loginResult = parseMessage(response).str; // login result is the parsed response message but as a string
                            if (loginResult.equals("valid")) { // if it's a valid pair of credentials
                                serverConnection_.sendMessage("|-!-server-list:-!-|"); // send the server list message so client can choose a server
                                response = serverConnection_.receiveMessage(); // response is what happens when we receive that message back
                                String[][] servers = parseMessage(response).arr; // two dimensional array of servers, set to the parsed version of our response as an array
                                setLoggedName(loginPanel.getUsername()); // set the logged name to the username (log the username in our data repo)
                                swapServerList(servers); // swap to the server list panel
                            } else if (loginResult.equals("invalid")) { // if the credentials pair (username and hash) is not valid
                                System.out.println("Invalid login! Popup box to be implemented"); // honestly who cares at this point
                            } else { // if something manages to go terribly wrong
                                System.out.println("Error: Incorrect message passing"); // send a message to the console that something has gone terribly wrong
                            }
                            //serverConnection_.close(); 
                        } catch (IOException ev) { // catch input output exception for bad data, and print a stack trace to see: what went wrong and where
                            ev.printStackTrace();
                        }        
                        break;  
                    case ServerListPanel.CONNECT: // add handling for connecting to specific server later
                        swapGame(); // swap to the game panel
                        break;
                    case "back":                // this back handler works for anything that goes back to mainMenu. refactor: add an enum later somewhere like above
                        swapMainMenu(); // swap to the main menu panel
                        break;
                    // GamePanel Action buttons
                    case GamePanel.HIT: 
                        System.out.println("hit!"); // send "hit action has been selected" notification to console
                        serverConnection_.sendMessage(("|-!-action:" + getLoggedName() + ":" + loggedServer_ + ":" + "hit" + ":-!-|")); // tell the server that the client hit
                        try { // try to catch any bad data in an i/o exception block
                            parseMessage(serverConnection_.receiveMessage()); // parse the message we receive via server connection
                        } catch (IOException e1) { // catch the bad data if we have some, and print a stack trace to see what and where it is
                            e1.printStackTrace();
                        }
                        break;
                    case GamePanel.STAND: // tell the server that the user has decided to stand
                        serverConnection_.sendMessage(("|-!-action:" + getLoggedName() + ":" + loggedServer_ + ":" + "stand" + ":-!-|"));
                        try { // maybe we should've made this into a recursive function
                            parseMessage(serverConnection_.receiveMessage());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        break;
                    case GamePanel.PLACEWAGER: // place wager
                        System.out.println("wager placed!"); // push a wager placed message to the console, line below sends wager message to server
                        serverConnection_.sendMessage(("|-!-action:" + getLoggedName() + ":" + loggedServer_ + ":" + "wager" +  ":" + gamePanel.getWager() + ":-!-|"));
                        try { // look for bad wager data
                            parseMessage(serverConnection_.receiveMessage());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        break;
                }
            }
        };
        // serverlist table listener, listen for attempts to connect to a server
        serverListPanel = new ServerListPanel();

        // Initialize flag and timer
        ListSelectionListener tableListener = new ListSelectionListener() {
            boolean isExecuting = false; // it is not executing at the start
            javax.swing.Timer timer = new javax.swing.Timer(1000, null); // timer created to go for 1000ms
            public void valueChanged(ListSelectionEvent event) { // value changed method uses event as parameter
                if (isExecuting) { 
                    // Ignore event if the method is currently being executed
                    return;
                }
                isExecuting = true; // set is executing to true, because it is executing
                timer.setRepeats(false); // Disable repeat
                timer.addActionListener(new ActionListener() { // add action listener
                    public void actionPerformed(ActionEvent evt) { // action performed method uses action event as parameter
                        // Execute method after the delay
                        try { // set logged server equal to a certain value in the table casted to string
                            loggedServer_ = serverListPanel.getTable().getValueAt(serverListPanel.getTable().getSelectedRow(), 0).toString();
                            serverConnection_.sendMessage(("|-!-connect:" + getLoggedName() + ":" + loggedServer_ + ":-!-|")); // send message that user is connected to server
                            String response = serverConnection_.receiveMessage(); // we receive that message back and response gets assigned to the result
                            parseMessage(response); // parse the response
                            gamePanel.init(); // initialize the game panel
                            swapGame(); // take the user to the game panel
                        } catch (IOException e) { // catch bad data if we have any, print a stack trace to find out what it is and where it is
                            e.printStackTrace();
                        }
                        // Reset flag
                        isExecuting = false; // back to false, not executing anymore
                    }
                }); // so that's where the other parenthesis went
                // Start the timer
                timer.start();
            }
        };

        serverListPanel.getTable().getSelectionModel().addListSelectionListener(tableListener); // add a table listener to the table

        // Initalize panels, pass the above action listener to parse data in MainFrame
        mainMenuPanel = new MainMenuPanel(actionListener);

        //Re-did these panels to have a working back button within each panel.
        loginPanel = new LoginPanel(actionListener); // login panel now has a back button
        creditsPanel_ = new CreditsPanel(actionListener); // credits panel now has a back button
        instructionsPanel = new InstructionsPanel(actionListener); // instructions panel now has a back button
        gamePanel = new GamePanel(actionListener); // game panel now has a back button


        // Add panel to MainFrame's cardlayout
        cards.add(mainMenuPanel.getPanel(), Panel.MAINMENU.getName()); // main menu panel added to mainframe's card layout
        cards.add(loginPanel.getPanel(), Panel.LOGIN.getName()); // login panel added to mainframe's card layout
        cards.add(creditsPanel_.getPanel(), Panel.CREDITS.getName()); // credits panel added to mainframe's card layout
        cards.add(serverListPanel.getPanel(), Panel.SERVERLIST.getName()); // server list panel added to mainframe's card layout
        cards.add(instructionsPanel.getPanel(), Panel.INSTRUCTIONS.getName()); // instructions panel added to mainframe's card layout
        cards.add(gamePanel.getPanel(), Panel.GAME.getName()); // game panel added to mainframe's card layout
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
        cLayout.show(cards, Panel.MAINMENU.getName()); // show the main menu panel
    }

    public void swapPlayGame() {
        // Future: add check here to see if already logged in & then use that session instead of forcing login each time
        cLayout.show(cards, Panel.LOGIN.getName()); // show the login panel
    }

    public void swapServerList(String[][] servers) {
        serverListPanel.loadServers(servers); // load the servers
        cLayout.show(cards, Panel.SERVERLIST.getName()); // show the server list panel
    }

    public void swapInstructions() {
        cLayout.show(cards, Panel.INSTRUCTIONS.getName()); // show the instructions panel
    }

    public void swapLeaderboard() {
        cLayout.show(cards, Panel.LEADERBOARD.getName()); // show the leaderboard panel
    }

    public void swapCredits() {
        cLayout.show(cards, Panel.CREDITS.getName()); // show the credits panel
    }

    public void swapGame() {
        cLayout.show(cards, Panel.GAME.getName()); // show the game panel
    }

    public void quitGame() {
        System.exit(0); // just kill the entire system
    }

    public String getLoggedName() {
        return loggedName_; // accessor for the name that was logged
    }

    public void setLoggedName(String loggedNamed_) {
        this.loggedName_ = loggedNamed_; // take logged nameD data and assign it (mutator) to logged name
    }


    private class Data { // data class
        public String str; // string
        public String[][] arr; // two-dimensional array

        public Data(String s, String[][] a) { // constructor
            this.str = s; // this string and array is assigned to the data of the parameter string and array
            this.arr = a;
        }
        
        public Data(String s) {
            this.str = s; // if just the string is given as a parameter, assign it and create new "1-dimensional" 2-dimensional array
            this.arr = new String[0][];
        } 

        public Data(String[][] a) {
            this.str = ""; // if just given array as parameter, assign it and assign this str to blank string
            this.arr = a;
        } 
    }

     // Types of messages that the server might send
    // |-!-login:invalid-!-| 
    // |-!-getServers:US West 1:7/8:50-!-|
    // Parses a message & sends a response or logs an error.
    public Data parseMessage(String msg) throws IOException { // data type method with string parameter "parse message" which throws io exception
        String data = msg.substring(msg.indexOf("|-!-")+4, msg.indexOf("-!-|")); // Get text inbetween start/end
        System.out.println("parsing  " + msg); // send message to console indicating that we are parsing
        int i = 0, previ = 0, e = 0; // i, previ and e all initialized to 0
        String[] args = new String[50]; // args is a string array with 50 indices

        while ((i = data.indexOf(":", previ)) != -1) { // while message data is in valid indices
            String cmd = data.substring(previ, i); // cmd is the substring from previ to i
            previ = i+1; // increase previ
            args[e++] = cmd; // increase e and move through the array
        }
        args[e] = "\0"; // current arg is null
        switch (args[0]) { // first index of the array
            case "login": // if it's login
                Data d = new Data(args[1]); // make new data
                System.out.println(d.str); // print string
                return d; // return data
            case "server-list": // if it's asking for the server list
                // name:players:ping
                int rows = (args.length-2)/3; // define how many rows are in the 2d array
                String [][] servers = new String[rows][3]; // number of servers, pick one server right there at the intersection of rows # and 3
                int c = 0, j = 1; // c is initialized to 0, j is initialized to 1, c represents number of columns
                for (int r = 0; r < rows; r++) { // for each row in the array
                    servers[r][c++] = args[j++]; // the intersection of this current row and c takes the data at args[2]
                    servers[r][c++] = args[j++]; // the intersection of this current row and c takes the data at args[3]
                    servers[r][c++] = args[j++]; // the intersection of this current row and c takes the data at args[4]
                    c %= 3;
                }
                return new Data(servers); // return new data using the servers array as a parameter
                // server-data:US West 1:5:dealer:0:hand:spades_king:diamonds_two:Bob:0:0:hand:clubs_seven:hearts_five:
                // server-data:name:state:dealer:dealerState:hand:suit_rank:suit_rank:playerName:playerWager:playerState:hand:suit_rank:suit_rank:...next player... next player... and so ong
            case "server-data": // >lobby class ->dealer class & player class
                this.activatedResultsLabel = false; // activated results label is not showing
                int f = 1; // integer f initialized to 1
                this.gamePanel.setLobbyName(args[f++]); // set lobby name to whatever's in args[2]
                this.gamePanel.setState(Integer.parseInt(args[f++])); // set state to whatever is in args[2] making sure to cast it as an integer
                f++; // skip dealerTag
                this.gamePanel.getDealer().setState(Integer.parseInt(args[f++])); // set state to whatever is in args[4]
                f++; // skip handTag
                boolean validCard = true;                           // DEALER DATA, kinda messy, refactor later
                String[] cardType = {"",""}; // card type string array is empty
                while (validCard) {                             // while the current card is valid
                    if (args.length > f && args[f].contains("_")) { // if the size of args is greater than f and args @ f contains the "_" character
                        cardType = args[f++].split("_"); // the card type is whatever's at args[f + 1] besides the "_"
                    } else { 
                        validCard = false; // that's not a valid card
                        break;
                    }
                    Suit suit = Suit.valueOf(cardType[0].toUpperCase()); // suit is card array index 0 in all caps
                    Rank rank = Rank.valueOf(cardType[1].toUpperCase()); // rank is card array index 1 in all caps
                    validCard = Card.isValidCard(suit, rank); // compare the truth state against whatever is given for the suit and rank of that card (can't have a 0 of hearts)
                    if (!validCard) // if it's not valid then just stop
                        break;
                    this.gamePanel.getDealer().getHand().addCard(new Card(rank, suit)); // in the default case just add the given card to the dealer's hand
                }
                // PLAYER DATA
                while (!args[f].equals("\0")) { // while the args at index f is not null
                    String nam = args[f++]; // nam is whatever's in args index f + 1
                    boolean isPlayer = false; // it's not a player
                    if (nam.equals(this.loggedName_)) // if nam is equal to the logged name then it is actually a player
                        isPlayer = true;
                    PlayerCard player = new PlayerCard(nam, isPlayer); // player card type variable called player is a new player card with its name and whether it's a player
                    player.setState(Integer.parseInt(args[f++])); // set state to whatever integer is found in (args index (f + 1))
                    player.setWager(Integer.parseInt(args[f++])); // added money here
                    if (isPlayer) // if it's a player (true)
                        player.setMoney(Integer.parseInt(args[f++])); // set money to whatever integer is foudn in (args index (f + 1))
                    else
                        f++; // skip other peoples money, myb fix this in future
                    f++;    // skip handTag
                    validCard = true;                          // it's a valid card
                    cardType[0] = ""; // blank suit
                    cardType[1] = ""; // blank rank
                    while (validCard & !args[f].equals("\0")) {        // while it's a valid card and we're not at a null index  
                        if (args[f].equals("n_a")) { // if args at index f equals "no player" equivalent
                            this.gamePanel.getPlayers().add(player); // add the player 
                            f++; // increase f
                            break;
                        }                
                        if (args.length > f && args[f].contains("_")) { // if the size of args is greater than f and args @ f contains the "_" character
                            cardType = args[f++].split("_"); // the card type is whatever's at args[f + 1] besides the "_"
                        } else {
                            validCard = false; // not a valid card otherwise
                            break;
                        }
                        Suit suit = Suit.valueOf(cardType[0].toUpperCase()); // suit is card array index 0 in all caps
                        Rank rank = Rank.valueOf(cardType[1].toUpperCase()); // rank is card array index 1 in all caps
                        validCard = Card.isValidCard(suit, rank); // set the truth state equal to whatever is given for the suit and rank of that card (can't have a 0 of hearts)
                        if (!validCard) // if it's not a valid card then rank
                            break;
                        player.getHand().addCard(new Card(rank, suit)); // add new card with given rank and suit to the player's hand
                        this.gamePanel.getPlayers().add(player); // add player to the game
                    }
                    gamePanel.init(); // initialize the game panel
                }
                //Idk best place to put the start of the datastream how about here for now..
                dataThread = new DataThread();
                dataThread.start();    // start the data thread
                break;   
            case "update-data": // >lobby class ->dealer class & player class
                f = 1; // f is reset to 1
                f++; // skip lobbyName
                this.gamePanel.setState(Integer.parseInt(args[f++])); // parse int at args[3] and use that to set the state
                f++; // skip dealerTag
                this.gamePanel.getDealer().setState(Integer.parseInt(args[f++])); // parse int at args[5] and use that to set the state that the dealer will use (getDealer)
                f++; // skip handTag
                validCard = true;                           // DEALER DATA, kinda messy, refactor later
                String[] cardTyp = {"",""}; // card type string array is empty
                Hand recievedDealerHand = new Hand();    // the received dealer hand is a new hand
                while (validCard) {                   // while the card is valid
                    if (args.length > f && args[f].contains("_")) { // if the size of args is greater than f and args @ f contains the "_" character
                        cardTyp = args[f++].split("_"); // the card type is whatever's at args[f + 1] besides the "_"
                    } else {
                        validCard = false; // not a valid  card otherwise
                        break;
                    }

                        Suit suit = Suit.valueOf(cardTyp[0].toUpperCase()); // suit is card array index 0 in all caps
                        Rank rank = Rank.valueOf(cardTyp[1].toUpperCase()); // rank is card array index 1 in all caps
                        validCard = Card.isValidCard(suit, rank); // valid card bool is set to whether the card with the given params is valid
                        if (!validCard) // stop outright if it's not
                            break;
                       //this.gamePanel.getDealer().getHand().addCard(new Card(rank, suit));
                       recievedDealerHand.addCard(new Card(rank, suit)); // add this card to the dealer hand
                }
                if (!recievedDealerHand.isEqual(this.gamePanel.getDealer().getHand())) { // if the received dealer hand is not equal to the dealer's hand
                    this.gamePanel.getDealer().setHand(recievedDealerHand); // just make them equal
                }
                // PLAYER DATA
                Boolean[] playersProcessed = new Boolean[12]; // 12 players have false data at start
                for (int n = 0; n < playersProcessed.length; n++) { // loop that runs 12 times
                    playersProcessed[n] = false; // set all to false
                }

                while (args[f] != "\0") { // while args[current] is not empty str
                    // locate player
                    String name = args[f]; // name is that string
                    int pos = 0; // position is 0
                    boolean playersExist = false; // players don't exist yet
                    if (this.gamePanel.getPlayers().size() != 0) { // if the # of players is not 0
                        playersExist = true; // players do exist (what about negative players?)
                        for (pos = 0; pos < this.gamePanel.getPlayers().size(); pos++) { // for position 0 and what I assume to be all 12 players
                            if (playersProcessed[pos] == false) { // if this player has not been processed
                                if (this.gamePanel.getPlayers().get(pos).getNamed().equals(name)) { // process that player, get name
                                    playersProcessed[pos] = true; // processed = true
                                    break;
                                }
                            }
                            
                        }
                    }
                    System.out.println("Handing player #" + Integer.toString((pos))); // send message to console indicating that a player is having cards dealt to them 
                    String playerName = args[f++]; // player name is the string at args index f + 1
                    int cState = Integer.parseInt(args[f++]); // c state is the integer parsed from the string at args index f + 2
                    int cWager = Integer.parseInt(args[f++]); // c wager is the integer parsed from the string at args index f + 3
                    int cMoney = Integer.parseInt(args[f++]); // c money is the integer parsed from the string at args index f + 4
                    if (cState == 0 || cState == 1) { // if the state is 0 or 1
                        this.activatedResultsLabel = false; // lazy way of setting this to false at start of each game
                    }
                    System.out.println(activatedResultsLabel); // print activated results label to the console
                    if (this.loggedName_.equals(playerName)) // if this logged name equals player name, as expected
                        this.gamePanel.getPlayers().get(pos).setMoney(cMoney); // assign player at position to their predefined money integer
                    //int cMoney = Integer.parseInt(args[f++]);

                    // Adjust buttons/actions based on state
                    if (playerName.equals(this.loggedName_)) { // if the player name equals this logged name
                        if (cState == 1) { // if the state is 1
                            this.gamePanel.getActionButtons().toggleWagerVisibility(true); // make wager visible
                        }
                        else if (cState == 2) { // if the state is 2
                            this.gamePanel.getActionButtons().toggleWagerVisibility(false); // make wager invisible
                            this.gamePanel.getActionButtons().toggleButtonVisibility(true); // make buttons visibile
                        } else { // if not then make neither of those things visible
                            this.gamePanel.getActionButtons().toggleWagerVisibility(false);
                            this.gamePanel.getActionButtons().toggleButtonVisibility(false);
                        }
                    }
                    
                    f++;    // skip handTag
                    validCard = true;           // it is a valid card               
                    cardTyp[0] = ""; // type at index 0 is blank
                    cardTyp[1] = ""; // type at index 1 is blank so effectively {"", ""};
                    Hand recievedHand = new Hand(); // received hand is new hand so nothing in it
                    while (validCard & !args[f].equals("\0"))  {      // valid card and non null args at index f
                        if (args[f].equals("n_a")) { // if no player  there yet
                            f++; // increase f
                            break;
                        }                                   
                        if (args.length > f && args[f].contains("_")) { // if args array size is bigger than f integer and args at current position contains _
                            cardTyp = args[f++].split("_"); // split the text away from the _ to get the card type
                        } else {
                            validCard = false; // it's not a valid card otherwise
                            break;
                        }
                        Suit suit = Suit.valueOf(cardTyp[0].toUpperCase()); // suit is card array index 0 in all caps
                        Rank rank = Rank.valueOf(cardTyp[1].toUpperCase()); // rank is card array index 1 in all caps
                        validCard = Card.isValidCard(suit, rank); // valid card boolean state equal to whether the card is a valid card by suit and rank
                        if (!validCard) // if it isn't then just break
                            break;
                        //player.getHand().addCard(new Card(rank, suit));
                        //recievedHand.addCard(new Card(rank, suit));
                        //if (pos != -1) {
                        //this.gamePanel.getPlayers().get(pos).getHand().addCard(new Card(rank, suit));
                        recievedHand.addCard(new Card(rank, suit)); // add a new card of rank rank and suit suit to the received hand
                        //this.gamePanel.getDealer().setHand(recievedDealerHand);
                    }
                    
                    if (playersProcessed[pos]) { // problem is here...
                        if (!recievedHand.isEqual(this.gamePanel.getPlayers().get(pos).getHand())) { // if the received hand is not equal to the hand of the player
                            //player.setHand(recievedHand);
                            //player.getHand().addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
                            //this.gamePanel.getPlayers().add(player); 
                            System.out.println(Integer.toString((pos)) + " has new deck, updating"); // print out that position pos has new deck, updating
                            //this.gamePanel.getPlayers().get(pos-1).getHand().addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
                            this.gamePanel.getPlayers().get(pos).setHand(recievedHand); // set hand to received hand 
                        }
                        if ( this.gamePanel.getPlayers().get(pos).getState() == 4 || this.gamePanel.getPlayers().get(pos).getState() == 6 || this.gamePanel.getPlayers().get(pos).getState() == 7) { // check for victory/bust
                            if ( this.gamePanel.getPlayers().get(pos).getNamed().equals(this.loggedName_)) { // if the player name at this position equals the logged name
                                if (!activatedResultsLabel) { // only then if the activated results label is false set it to true
                                    activatedResultsLabel = true;
                                    if (this.gamePanel.getPlayers().get(pos).getState() == 4 || this.gamePanel.getPlayers().get(pos).getState() == 7) //bust
                                        this.gamePanel.toggle("Bust", this.loggedName_); // toggle bust notification
                                    else if (this.gamePanel.getPlayers().get(pos).getState() == 6) // win
                                        this.gamePanel.toggle("Win", this.loggedName_); // toggle win notification
                                    this.gamePanel.getDealer().setHidden(false); // dealers cards are no longer hidden, the game is over
                                }
                            }
                        } else if (this.gamePanel.getPlayers().get(pos).getState() == 0 || this.gamePanel.getPlayers().get(pos).getState() == 1) {
                            this.gamePanel.toggle("delete", this.loggedName_); // if the state is 0 or 1 for this player, delete the player
                        }
                        
                        this.gamePanel.getPlayers().get(pos).setState(cState); // set state to c state
                        this.gamePanel.getPlayers().get(pos).setWager(cWager); // set wager to c wager
                        //this.gamePanel.getPlayers().get(pos).setMoney(cMoney);
                    } else { // if the state is not 0, 1, 4, 6, or 7
                        PlayerCard player = new PlayerCard(playerName, false); // new player card with player name but the card is not valid yet
                        player.setState(cState); // set state to c state
                        player.setWager(cWager); // set wager to c wager
                        //player.setMoney(cMoney);
                        this.gamePanel.getPlayers().add(player); // add the player
                    }
                    gamePanel.init(); // initialize the game panel

                    if (this.gamePanel.getDealer().getHand().getCards().size() >= 2) { // if the number of cards in the dealer's hand is at least 2
                        if (this.gamePanel.getState() == 1 || this.gamePanel.getState() == 2) { // lazy way of setting dealer card to invis at start
                            try { // try to catch some bad data if there is any
                                if (!this.gamePanel.getDealer().hiddenCard()) // if the card's not hidden
                                    this.gamePanel.getDealer().hideSecondCard(); // hide the second card
                            } catch (IOException ds) { // print stack trace if we find something, see what happened and where
                                ds.printStackTrace();
                            }
                        }   // else if the card is hidden and the state is 1 or 2
                        else if (this.gamePanel.getDealer().hiddenCard() && (this.gamePanel.getState() == 1 || this.gamePanel.getState() == 2)) { // show card 
                            try { // try to catch some bad data again if we ca
                                if (this.gamePanel.getDealer().hiddenCard()) // if the card is hidden
                                    this.gamePanel.getDealer().showSecondCard(); // show the second card
                            } catch (IOException ds) { // print a stack trace if we do encounter some mumbo jumbo - what and where
                                ds.printStackTrace();
                            }
                        }
                    }
                }
                break;       
            default: // handle bad messages here
                break;    
        }
        return new Data(""); // new data with blank string field
    }
}
