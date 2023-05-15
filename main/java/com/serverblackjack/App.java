package com.serverblackjack;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class App 
{
	// is this main method?
    public static void main( String[] args )
    {
    	// c1 is new server, calls start server method from other class
        Server c1 = new Server(5012);
        c1.startServer();
    }
}
