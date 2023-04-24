package com.serverblackjack;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
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
        serverThread.start();

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
            System.out.println(cmd);
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
                    
                    cnt.sendMessage("|-!" + "login:" + res + ":-!-|");
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

                cnt.sendMessage("|-!" + "server-list:" + res + "-!-|");
                break;
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
