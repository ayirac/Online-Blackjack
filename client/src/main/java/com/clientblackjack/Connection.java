package com.clientblackjack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {
    // establish socket, bufferedreader input I and printwriter output O to supply details regarding connection attempts
    private Socket socket_;
    private BufferedReader input_;
    private PrintWriter output_;

    public Connection(Socket sock) throws IOException { // Connection method throws IOException when attempting to connect goes wrong, this method connects clients & servers
        this.socket_ = sock; // param sock is the given socket
        this.input_ = new BufferedReader(new InputStreamReader(this.socket_.getInputStream())); // input and output use the above socket declaration to get info
        this.output_ = new PrintWriter(this.socket_.getOutputStream(), true);
    }

    public String receiveMessage() throws IOException { // receive message throws IO exception if input e.g. message received not valid
        String msg = input_.readLine(); // msg string takes the input
        System.out.println("Received from client: " + msg); // console notification for when message is received from client
        return msg;
    }

    public void sendMessage(String message) { // does not throw IO exception because it will only ever send out valid input
        System.out.println("Sending to client: " + message); // console notification for when message is sent to client
        output_.println(message); // printwriter also displays the message by itself
    }

    public void close() throws IOException { // isn't this overriding without a declaration? Or am I mistaken?
        input_.close(); // I would call this method closeAll() personally
        output_.close();
        socket_.close();
    }
}
