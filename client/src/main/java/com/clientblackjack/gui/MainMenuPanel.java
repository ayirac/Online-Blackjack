package com.clientblackjack.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class MainMenuPanel {
    private Clip backgroundMusic;
    public static final String MAINMENU = "main-menu";
    public static final String PLAYGAME = "play-game";
    public static final String INSTRUCTIONS = "instructions";
    public static final String LEADERBOARD = "leaderboard";
    public static final String CREDITS = "credits";
    public static final String QUITGAME = "quit";
    public static final String[] BUTTONS = { MAINMENU, PLAYGAME, INSTRUCTIONS, LEADERBOARD, QUITGAME, QUITGAME };
    private JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // gets image, already resized to 1280 x 720
            ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/images/mainmenu.jpg"));
            // displays the background image
            g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    };
    private BoxLayout bLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);

    public JButton playGameBtn = new JButton("Play Game");
    public JButton instructionsBtn = new JButton("Instructions");
    public JButton leaderboardBtn = new JButton("Leaderboard");
    public JButton creditsBtn = new JButton("Credits");
    public JButton quitBtn = new JButton("Quit Game");

    public MainMenuPanel(ActionListener listener) {
        playGameBtn.setName(PLAYGAME);
        instructionsBtn.setName(INSTRUCTIONS);
        instructionsBtn.setName(INSTRUCTIONS);
        leaderboardBtn.setName(LEADERBOARD);
        creditsBtn.setName(CREDITS);
        quitBtn.setName(QUITGAME);

        panel.setLayout(bLayout);
        playGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        creditsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(10));
        panel.add(playGameBtn);
        panel.add(Box.createVerticalStrut(5));
        panel.add(instructionsBtn);
        panel.add(Box.createVerticalStrut(5));
        panel.add(leaderboardBtn);
        panel.add(Box.createVerticalStrut(5));
        panel.add(creditsBtn);
        panel.add(Box.createVerticalStrut(5));
        panel.add(quitBtn);
        panel.add(Box.createVerticalStrut(5));

        playGameBtn.addActionListener(listener);
        instructionsBtn.addActionListener(listener);
        leaderboardBtn.addActionListener(listener);
        creditsBtn.addActionListener(listener);
        quitBtn.addActionListener(listener);

// Loads and plays the background music
try {
    URL musicUrl = getClass().getResource("/music/mainmenusong.wav");
    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicUrl);
    backgroundMusic = AudioSystem.getClip();
    backgroundMusic.open(audioInputStream);

    // Lower the volume since it was too loud at the start
    FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
    int volume = -25; // Decrease the volume by 25 decibels
    gainControl.setValue(volume);

    // Keeps looping throughout the menus the moment it's run
    backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
    e.printStackTrace();
}

    }

    public JPanel getPanel() {
        return this.panel;
    }
}
