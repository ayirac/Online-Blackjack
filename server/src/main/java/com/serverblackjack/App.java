package com.serverblackjack;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class App 
{
    public static void main( String[] args )
    {
        Server c1 = new Server(5012);
        c1.startServer();
    }
}
