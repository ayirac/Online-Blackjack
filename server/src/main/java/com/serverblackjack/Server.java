package com.serverblackjack;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Server {
    private int port_;
    private Connection[] connections_ = new Connection[100];
    private int totalConnections_ = 0;
    private ServerSocket serverSocket_;
    private ExecutorService clientProcessingPool_;
    private Database db_;
    private Lobby[] lobbies_ = new Lobby[20];
    private int totalLobbies_ = 0;
    private int playerTurnStage_ = 0;
    private boolean activeTimer = false;

    public Server(int port) {
        this.port_ = port;

        // fill lobbies_ with data from a .json file?
        try {
            String content = new String(Files.readAllBytes(Paths.get("server/src/main/resources/servers.json")));
            JSONObject json = new JSONObject(content);
            JSONArray serversJson = json.getJSONArray("servers");

            for (int i = 0; i < serversJson.length(); i++) {
                JSONObject serverJson = serversJson.getJSONObject(i);
                String name = serverJson.getString("name");
                int maxPlayers = serverJson.getInt("maxPlayers");
                System.out.println(name + " has a max player count of " + maxPlayers);
                lobbies_[totalLobbies_] = new Lobby(totalLobbies_++, name, maxPlayers);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Starts the server
    public void startServer() {
        clientProcessingPool_ = Executors.newFixedThreadPool(10);
        try {
            db_ = new Database("jdbc:mysql://localhost/oblackjack", "admin", "12345");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
       

        Runnable serverTask = new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket_ = new ServerSocket(port_);
                    System.out.println("Waiting for clients to connect...");
                    while (true) {
                        Socket clientSocket = serverSocket_.accept();
                        clientProcessingPool_.submit(new ClientTask(clientSocket));
                    }
                } catch (IOException e) {
                    System.err.println("Unable to process client request");
                    e.printStackTrace();
                }
            }
        };
        Runnable lobbyTask = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        // Scan all lobbies
                        for (int i = 0; i < totalLobbies_; i++) {
                            final Lobby lobby = lobbies_[i];
                            if (lobby.getPlayers().size() > 0 && lobby.getState() == -1) {                                      // state -1 to 0, first player connect
                                System.out.println("Players present in " + lobby.getName() + ", starting game...");
                                lobby.setState(0);
                                if (!activeTimer) {
                                    activeTimer = true;
                                    Timer timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            System.out.println("Timeout stage passed in " + lobby.getName() + ", dealing cards..."); // state 0 to 1, pre-game idle
                                            lobby.setState(1);
                                        }
                                    }, 2000); // 30 seconds ideally, put a const here later! refactor
                                }
                                
                            } else if (lobby.getPlayers().size() > 0 && lobby.getState() == 1) {                                    // state 1 to 2, dealing cards
                                System.out.println("Deal stage passed in " + lobby.getName() + ", dealing cards...");
                                lobby.deal(); // maybe make it so the cards are dealt/sent slowly to add more immersion, waaaay in the future!
                                lobby.setState(2);
                            } else if (lobby.getPlayers().size() > 0 && lobby.getState() == 2) {                                    // state 2, players turn state!
                                if (lobby.getPlayerTurnStage() == 0) {
                                    lobby.setPlayerTurnStage(1);
                                    for (int o = 0; o < lobby.getPlayers().size(); o++) {
                                        lobby.getPlayers().get(o).setState(2);
                                    }
                                }
                                
                            } else if (lobby.getPlayers().size() > 0 && lobby.getState() == 3) {                                    // state 3, dealer turn state!

                            }
                        }
        
                        // Sleep for a while before scanning again
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        
        // Shutdown hook to close the server socket when the program is terminated
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("Closing server");
                    serverSocket_.close();
                    clientProcessingPool_.shutdown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread serverThread = new Thread(serverTask);
        Thread lobbiesThread = new Thread(lobbyTask);
        serverThread.start();
        lobbiesThread.start();

    }

    // Types of messages that the user might send
    // |-!-login:Joe:pass#-!-| 
    // |-!-getServers-!-|
    // Parses a message & sends a response or logs an error.
    public void parseMessage(String msg, Connection cnt) {
        String data = msg.substring(msg.indexOf("|-!-")+4, msg.indexOf("-!-|")); // Get text inbetween start/end

        int i = 0, previ = 0, e = 0;
        String[] args = new String[10];
        while ((i = data.indexOf(":", previ)) != -1) {
            
            String cmd = data.substring(previ, i);
            previ = i+1;
            args[e++] = cmd;
        }
        
        switch (args[0]) { // cont, server needs to send server data!
            case "login":
                String username = args[1];
                String password = args[2];
                try { // Send response, |-!-login:valid:-!-|
                    String res;
                    if (db_.validateUser(username, password))
                        res = "valid";
                    else
                        res = "invalid";
                    System.out.println("User tried connecting, result: " + res);  
                    
                    cnt.sendMessage("|-!-" + "login:" + res + ":-!-|");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "server-list":
                // placeholder
                //String res = "US West 1:5/8:40:US West 2:1/8:35:US East:4/8:87:";
                StringBuilder builder = new StringBuilder();
                for (int f = 0; f < totalLobbies_-1; f++) {
                    builder.append(lobbies_[f].getName()).append(":")
                        .append(lobbies_[f].getCurrentPlayers()).append(":")
                        .append(lobbies_[f].getPing()).append(":"); // for some reason this isnt working & its skips?
                }
                String res = builder.toString();
                System.out.println(res);
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ev) {
                    ev.printStackTrace();
                }

                cnt.sendMessage("|-!-" + "server-list:" + res + "-!-|");
                break;
            case "connect": // iterate through current servers, check if the one im looking for in arg[1] is present. if it is, send client Dealer data, and Player Data, tmrw :)
                String serverName = args[2];
                username = args[1];
                for (int f = 0; f < totalLobbies_-1; f++) {
                    if (lobbies_[f].getName().equals(serverName)) {
                        
                        System.out.println(lobbies_[f].getId() + " has a new player");
                        cnt.setLobbyID(lobbies_[f].getId()); // organize connections by what lobby they're connected to
                        lobbies_[f].addPlayer(username, cnt);
                        cnt.sendMessage("|-!-" + lobbies_[f].getData() + "-!-|");
                        break;
                    }
                }
                break;
            case "update-data":
                for (int f = 0; f < totalLobbies_-1; f++) { 
                    if (lobbies_[f].getName().equals(args[2])) {
                        cnt.sendMessage("|-!-" + this.lobbies_[f].getUpdateData() + "-!-|");
                    }
                }
        }
    }

    private class ClientTask implements Runnable {
        private final Socket clientSocket;
        private Connection connection;
    
        private ClientTask(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
    
        @Override
        public void run() {
            System.out.println("Got a client! " + clientSocket.getInetAddress());
    
            try {
                connection = new Connection(clientSocket);
                while (true) {
                    String msg = connection.receiveMessage();
                    parseMessage(msg, connection);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}
