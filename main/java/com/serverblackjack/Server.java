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
	// specified server port integer
    private int port_;
    // established connections between clients and clients/servers
    private Connection[] connections_ = new Connection[100];
    // number of connections happening right now
    private int totalConnections_ = 0;
    // server socket, that bridges clients and servers
    private ServerSocket serverSocket_;
    // client processing pool as an executor service, used to process clients
    private ExecutorService clientProcessingPool_;
    // database holds player/server data
    private Database db_;
    // lobbies for players to queue in
    private Lobby[] lobbies_ = new Lobby[20];
    // number of lobbies existing right now
    private int totalLobbies_ = 0;
    
    // establishment of a server where players connect and interact
    public Server(int port) {
    	// every server has a port, initialized here
        this.port_ = port;

        // fill lobbies_ with data from a .json file, trying for exception
        try {
        	// not sure what the string does
            String content = new String(Files.readAllBytes(Paths.get("server/src/main/resources/servers.json")));
            // new JSON object using the content string
            JSONObject json = new JSONObject(content);
            // new JSON array using "servers" string from content (?)
            JSONArray serversJson = json.getJSONArray("servers");
            
            // Could we make this recursive?
            for (int i = 0; i < serversJson.length(); i++) {
            	// get each JSON object and set it equal to serversJson
                JSONObject serverJson = serversJson.getJSONObject(i);
                // This loop most likely establishes server names
                String name = serverJson.getString("name");
                // max players per server
                int maxPlayers = serverJson.getInt("maxPlayers");
                // servername has a max player count of n players
                System.out.println(name + " has a max player count of " + maxPlayers);
                // establish a new lobby for the server
                lobbies_[totalLobbies_] = new Lobby(totalLobbies_++, name, maxPlayers);
            }
            // exception e is general and prints a stack trace, most likely mem exception
            // looking for leaks
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Starts the server
    public void startServer() {
    	// sets client processing pool to a new fixed thread pool with capacity 10
        clientProcessingPool_ = Executors.newFixedThreadPool(10);
        // tries for an SQL exception, printing stack trace if thrown
        try {
        	// new database established for server game
            db_ = new Database("jdbc:mysql://localhost/oblackjack", "admin", "12345");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
       
        // Runnable variable established for running the server
        Runnable serverTask = new Runnable() {
        	@Override
        	// method called when running the server
            public void run() {
        		// trying for an IO exception when unable to process requests
                try {
                	// new server socket established with the given port
                    serverSocket_ = new ServerSocket(port_);
                    // gives this waiting text in console
                    System.out.println("Waiting for clients to connect...");
                    // this loop serves as a time.sleep() of sorts, recur it maybe?
                    while (true) {
                    	// the client socket is hard wired to accept
                        Socket clientSocket = serverSocket_.accept();
                        // as a result, a new client task is submitted to the pool
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
            // overrides previous declaration of run
            public void run() {
                try {
                	// close the server
                    System.out.println("Closing server");
                    serverSocket_.close();
                    clientProcessingPool_.shutdown();
                    // if we somehow throw an io exception during this we just print the stack trace
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        // new server thread is established and started
        Thread serverThread = new Thread(serverTask);
        serverThread.start();

    }

    // Types of messages that the user might send
    // |-!-login:Joe:pass#-!-| 
    // |-!-getServers-!-|
    // Parses a message & sends a response or logs an error.
    public void parseMessage(String msg, Connection cnt) {
    	// data is substring of given message
        String data = msg.substring(msg.indexOf("|-!-")+4, msg.indexOf("-!-|")); // Get text inbetween start/end
        // establish incrementation integer and previ which represents previous, as well as e
        int i = 0, previ = 0, e = 0;
        // arguments string array with capacity 10
        String[] args = new String[10];
        // while data is valid, essentially, because you can't have -1 (essentially 0)
        while ((i = data.indexOf(":", previ)) != -1) {
            // cmd is the data substring from character number previ to character number i
            String cmd = data.substring(previ, i);
            System.out.println(cmd);
            // move previ up the data string
            previ = i+1;
            // set args at e + 1, never to exceed 10, equal to cmd
            args[e++] = cmd;
            // this might be a point of failure (?)
        }
        // switch statement, is it wiser to use if else or just keep?
        switch (args[0]) { // cont, server needs to send server data!
            case "login":
            	// first argument entered is the username
                String username = args[1];
                // second argument entered is the password
                String password = args[2];
                try { // Send response, |-!-login:valid:-!-|
                    String res;
                    // if the validateUser boolean comes back true
                    if (db_.validateUser(username, password))
                    	// then the response back is valid
                        res = "valid";
                    else
                    	// if it doesn't the response back is invalid
                        res = "invalid";
                    // string prints result
                    System.out.println("User tried connecting, result: " + res);  
                    // message sent with result to determine next step
                    cnt.sendMessage("|-!-" + "login:" + res + ":-!-|");
                } catch (SQLException ex) {
                	// SQL exception, print stack trace for SQL mem view
                    ex.printStackTrace();
                }
                // break case, best practice
                break;
                // if server list is the string
            case "server-list":
                // Should we uncomment this? VVVV
                //String res = "US West 1:5/8:40:US West 2:1/8:35:US East:4/8:87:";
                StringBuilder builder = new StringBuilder();
                // new int counter parsing through all established lobbies
                for (int f = 0; f < totalLobbies_-1; f++) {
                	// append the current lobby's name, current players and ping to its string
                    builder.append(lobbies_[f].getName()).append(":")
                        .append(lobbies_[f].getCurrentPlayers()).append(":")
                        .append(lobbies_[f].getPing()).append(":"); // for some reason this isnt working & its skips?
                }
                // res is the result of putting all those things together
                String res = builder.toString();

                System.out.println(res);
                // try sleep for 1000ms (1 second)
                try {
                    Thread.sleep(1000);
                    // basically looking to see if we catch an interrupted exception
                } catch (InterruptedException ev) {
                    ev.printStackTrace();
                }
                // send server list of lobbies, then break
                cnt.sendMessage("|-!-" + "server-list:" + res + "-!-|");
                break;
                // in the case of connecting a client to a server
            case "connect": // iterate through current servers, check if the one im looking for in arg[1] is present. if it is, send client Dealer data, and Player Data, tmrw :)
                for (int f = 0; f < totalLobbies_-1; f++) {
                	// if the lobby names match
                    if (lobbies_[f].getName().equals(args[1])) {
                    	// print "lobbyA is found!" or something like that
                        System.out.println(lobbies_[f].getName() + "is founds"); // basically, send them all of lobbies_ data...
                        // send message to the lobby in question
                        cnt.sendMessage("|-!-" + lobbies_[f].getData() + "-!-|");
                    }
                }
        }
    }

    private class ClientTask implements Runnable { // runnable from earlier
        private final Socket clientSocket; // client socket
        private Connection connection; // connection
    
        private ClientTask(Socket clientSocket) { // client task using client socket
            // the method uses the given client socket as the socket
        	this.clientSocket = clientSocket;
        }
    
        @Override
        // overrides previous definitions of run (2, total 3)
        public void run() {
        	// successful console declaration of got a client
            System.out.println("Got a client! " + clientSocket.getInetAddress());

            try { // try to connect while running, without exception
                connection = new Connection(clientSocket); // new connection with the client socket
                while (true) { // this might be a safe infinite loop
                    String msg = connection.receiveMessage(); // message is received
                    parseMessage(msg, connection); // message is parsed
                }
                // catch I/O exception and print stack trace
            } catch (IOException e) {
                e.printStackTrace();
                // conclusive action
            } finally {
                try {
                	// try closing without exception
                    connection.close();
                    // catch I/O exception and print stack trace
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }    
}