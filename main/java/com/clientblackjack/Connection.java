package com.clientblackjack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {
    private Socket socket_;
    private BufferedReader input_;
    private PrintWriter output_;

    public Connection(Socket sock) throws IOException {
        this.socket_ = sock;
        this.input_ = new BufferedReader(new InputStreamReader(this.socket_.getInputStream()));
        this.output_ = new PrintWriter(this.socket_.getOutputStream(), true);
    }

    public String receiveMessage() throws IOException {
        String msg = input_.readLine();
        System.out.println("Received from client: " + msg);
        return msg;
    }

    public void sendMessage(String message) {
        System.out.println("Sending to client: " + message);
        output_.println(message);
    }

    public void close() throws IOException {
        input_.close();
        output_.close();
        socket_.close();
    }
}
