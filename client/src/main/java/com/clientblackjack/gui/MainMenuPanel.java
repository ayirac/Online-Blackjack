package com.clientblackjack.gui;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MainMenuPanel {
    public static final String MAINMENU = "main-menu";
    public static final String PLAYGAME = "play-game";
    public static final String INSTRUCTIONS = "instructions";
    public static final String LEADERBOARD = "leaderboard";
    public static final String QUITGAME = "quit";
    public static final String[] BUTTONS = {MAINMENU, PLAYGAME, INSTRUCTIONS, LEADERBOARD, QUITGAME};
    private JPanel panel = new JPanel();
    private BoxLayout bLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);

    public JButton playGameBtn = new JButton("Play Game");
    public JButton instructionsBtn = new JButton("Instructions");
    public JButton leaderboardBtn = new JButton("Leaderboard");
    public JButton quitBtn = new JButton("Quit Game");
    


    public MainMenuPanel(ActionListener listener) {
        playGameBtn.setName(PLAYGAME);
        instructionsBtn.setName(INSTRUCTIONS);
        instructionsBtn.setName(INSTRUCTIONS);
        leaderboardBtn.setName(LEADERBOARD);
        quitBtn.setName(QUITGAME);

        panel.setLayout(bLayout);
        playGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(10));
        panel.add(playGameBtn);
        panel.add(Box.createVerticalStrut(5));
        panel.add(instructionsBtn);
        panel.add(Box.createVerticalStrut(5));
        panel.add(leaderboardBtn);
        panel.add(Box.createVerticalStrut(5));
        panel.add(quitBtn);
        panel.add(Box.createVerticalStrut(5));

        playGameBtn.addActionListener(listener);
        instructionsBtn.addActionListener(listener);
        leaderboardBtn.addActionListener(listener);
        quitBtn.addActionListener(listener);

    }

    public JPanel getPanel() {
        return this.panel;
    }
}
