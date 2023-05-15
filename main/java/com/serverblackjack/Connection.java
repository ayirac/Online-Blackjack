package com.serverblackjack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection { // connection class
    private Socket socket_; // socket
    private BufferedReader input_; // input buffered reader I
    private PrintWriter output_; // output printwriter O

    public Connection(Socket sock) throws IOException { // connection
        this.socket_ = sock; // uses the given socket sock
        // input uses a new buffered reader, which uses the socket above
        this.input_ = new BufferedReader(new InputStreamReader(this.socket_.getInputStream()));
        // output uses a new print writer, which uses the socket above
        this.output_ = new PrintWriter(this.socket_.getOutputStream(), true);
    }

    public String receiveMessage() throws IOException { // receive msg
    	// msg string reads the input, becomes what it reads
        String msg = input_.readLine();
        // puts the message in console with an indicator
        System.out.println("Received from client: " + msg);
        return msg;
    }
        
    public void sendMessage(String message) { // send message
        System.out.println("Sending to client: " + message); // console
        output_.println(message); // console prints both messages
    }

    // a sort of garbage collection method, works very effectively
    public void close() throws IOException {
    	// Wait I think this overrides, did you check for that?
        input_.close();
        // I would call it closeAll()
        output_.close();
        socket_.close();
    }
}
