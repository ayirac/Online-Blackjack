package com.serverblackjack;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port_;
    private Connection[] connections_ = new Connection[100];
    private int totalConnections_ = 0;
    private ServerSocket serverSocket_;
    private ExecutorService clientProcessingPool_;

    public Server(int port) {
        this.port_ = port;
    }

    public void startServer() {
        clientProcessingPool_ = Executors.newFixedThreadPool(10);

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
                connections_[totalConnections_].receiveMessage();
                totalConnections_++;
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
