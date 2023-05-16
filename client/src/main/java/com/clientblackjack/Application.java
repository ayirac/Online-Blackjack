package com.clientblackjack;
// mainframe import from package gui
import com.clientblackjack.gui.MainFrame;


public class Application 
{
    public static void main( String[] args )
    {
        // establish mainframe from mainframe class which was imported
        MainFrame mainFrame = new MainFrame();
        // swap main menu called on the mainframe object
        mainFrame.swapMainMenu();
        // what is swap game? why is this line commented out
        //mainFrame.swapGame();
        // main frame runs, sets up main menu for when opening obj (1280x720 is a standard full screen type)
        mainFrame.run(1280, 720);
    }
}
