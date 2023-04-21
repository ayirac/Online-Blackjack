package com.clientblackjack.gui;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.clientblackjack.Connection;

import javax.swing.JButton;

public class MainFrame {
    private CardLayout cLayout = new CardLayout();
    private JPanel cards = new JPanel(cLayout);
    private MainMenuPanel mainMenuPanel;
    private LoginPanel loginPanel;
    private ServerListPanel serverListPanel;
    private Connection serverConnection_;

    public MainFrame() {
        // Listeners
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String buttonName = ((JButton) e.getSource()).getName();
                switch (buttonName) {
                    case MainMenuPanel.PLAYGAME:
                        swapPlayGame();
                        break;
                    case LoginPanel.SUBMIT:
                        System.out.print("Attempting to login as " + loginPanel.getUsername() + ":" + loginPanel.getPasswordHash());

                        try {
                            // basic test connection
                            serverConnection_ = new Connection(new Socket("localhost", 5012));
                            serverConnection_.sendMessage("|-!-login:" + loginPanel.getUsername() + ":" + loginPanel.getPasswordHash() + ":-!-|");
                            String m = serverConnection_.receiveMessage();
                            System.out.println(m);
                            swapServerList();
                            //serverConnection_.close();
                        } catch (IOException ev) {
                            ev.printStackTrace();
                        }
                        
                }
            }
        };
        mainMenuPanel = new MainMenuPanel(actionListener);
        loginPanel = new LoginPanel(actionListener);
        serverListPanel = new ServerListPanel(actionListener);

        cards.add(mainMenuPanel.getPanel(), MainMenuPanel.MAINMENU);
        cards.add(loginPanel.getPanel(), MainMenuPanel.PLAYGAME);
        cards.add(serverListPanel.getPanel(), "server-list");
    }

    public void run() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(cards);
        frame.setSize(300, 200);
        frame.setVisible(true);
    }

    public void swapMainMenu() {
        cLayout.show(cards, MainMenuPanel.MAINMENU);
    }

    public void swapPlayGame() {
        cLayout.show(cards, MainMenuPanel.PLAYGAME);
    }

    public void swapServerList() {
        cLayout.show(cards, "server-list");
    }

    public void swapInstructions() {
        cLayout.show(cards, MainMenuPanel.INSTRUCTIONS);
    }

    public void swapLeaderboard() {
        cLayout.show(cards, MainMenuPanel.LEADERBOARD);
    }

    public void quitGame() {
        System.exit(0);
    }
}
