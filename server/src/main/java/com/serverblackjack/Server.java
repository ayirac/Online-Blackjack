package com.serverblackjack;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port_;
    private Connection[] connections_ = new Connection[100];
    private int totalConnections_ = 0;
    private ServerSocket serverSocket_;
    private ExecutorService clientProcessingPool_;
    private Database db_;

    public Server(int port) {
        this.port_ = port;
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
        
        switch (args[0]) {
            case "login":
                String username = args[1];
                String password = args[2];
                System.out.println("Extracted " + username + " and " + password);
                try { // Send response, |-!-login:valid:-!-|
                    String res;
                    if (db_.validateUser(username, password))
                        res = "valid";
                    else
                        res = "not valid";
                    System.out.println(res);  
                    cnt.sendMessage("|-!" + "login:" + res + "-!-|");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
                
                // Check if user exists in DB
                // Check if password matches
                // If match, send success.
                // else, send failure
        }
    }

    private class ClientTask implements Runnable {
        private final Socket clientSocket;
        private ClientTask(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            System.out.println("Got a client! " + clientSocket.getInetAddress());
            
            try {
                connections_[totalConnections_] = new Connection(clientSocket);
                String msg = connections_[totalConnections_].receiveMessage();
                parseMessage(msg, connections_[totalConnections_]);
                totalConnections_++;
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
