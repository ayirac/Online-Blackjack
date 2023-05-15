package com.clientblackjack;

import com.clientblackjack.gui.MainFrame;


public class Application 
{
    public static void main( String[] args )
    {
        MainFrame mainFrame = new MainFrame();
        mainFrame.swapMainMenu();
        //mainFrame.swapGame();
        mainFrame.run(1280, 720);
    }
}
