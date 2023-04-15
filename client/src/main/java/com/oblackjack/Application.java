package com.oblackjack;

import com.oblackjack.gui.MainFrame;


public class Application 
{
    public static void main( String[] args )
    {
        MainFrame mainFrame = new MainFrame();
        mainFrame.swapMainMenu();
        mainFrame.run();
    }
}
