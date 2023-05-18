package com.clientblackjack.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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
    public JButton muteBtn = new JButton("Mute/Unmute");

    public MainMenuPanel(ActionListener listener) {
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setLayout(bLayout);
        playGameBtn.setName(PLAYGAME);
        instructionsBtn.setName(INSTRUCTIONS);
        instructionsBtn.setName(INSTRUCTIONS);
        leaderboardBtn.setName(LEADERBOARD);
        creditsBtn.setName(CREDITS);
        quitBtn.setName(QUITGAME);
        muteBtn.setName("Mute/Unmute");
        
        playGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        creditsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        muteBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(25));
        //buttoninsets will change the button size

        Insets buttonInsets = new Insets(15, 15, 15, 15); 
        Font buttonFont = new Font("Copperplate Gothic", Font.BOLD, 18); // Specify the desired font size
        
        //Playgame Button
        playGameBtn.setMargin(buttonInsets);
        playGameBtn.setOpaque(true);
        playGameBtn.setContentAreaFilled(false);
        playGameBtn.setBorderPainted(true);
        playGameBtn.setForeground(Color.WHITE); // Set the text color to white
        playGameBtn.setFont(buttonFont);
        panel.add(playGameBtn);
        panel.add(Box.createVerticalStrut(25));

        //Instructions Button
        instructionsBtn.setMargin(buttonInsets);
        instructionsBtn.setOpaque(true);
        instructionsBtn.setContentAreaFilled(false);
        instructionsBtn.setBorderPainted(true);
        instructionsBtn.setForeground(Color.WHITE); // Set the text color to white
        instructionsBtn.setFont(buttonFont);
        panel.add(instructionsBtn);
        panel.add(Box.createVerticalStrut(25));

        //Leaderboard Button
        leaderboardBtn.setMargin(buttonInsets);
        leaderboardBtn.setOpaque(true);
        leaderboardBtn.setContentAreaFilled(false);
        leaderboardBtn.setBorderPainted(true);
        leaderboardBtn.setForeground(Color.WHITE); // Set the text color to white
        leaderboardBtn.setFont(buttonFont);
        panel.add(leaderboardBtn);
        panel.add(Box.createVerticalStrut(25));

        //Credits Button
        creditsBtn.setMargin(buttonInsets);
        creditsBtn.setOpaque(true);
        creditsBtn.setContentAreaFilled(false);
        creditsBtn.setBorderPainted(true);
        creditsBtn.setForeground(Color.WHITE); // Set the text color to white
        creditsBtn.setFont(buttonFont);
        panel.add(creditsBtn);
        panel.add(Box.createVerticalStrut(25));

        //Mute Button 
        muteBtn.setMargin(buttonInsets);
        muteBtn.setOpaque(true);
        muteBtn.setContentAreaFilled(false);
        muteBtn.setBorderPainted(true);
        muteBtn.setForeground(Color.WHITE); // Set the text color to white
        muteBtn.setFont(buttonFont);
        panel.add(muteBtn);
        panel.add(Box.createVerticalStrut(25));
        //Quit Button 
        quitBtn.setMargin(buttonInsets);
        quitBtn.setOpaque(true);
        quitBtn.setContentAreaFilled(false);
        quitBtn.setBorderPainted(true);
        quitBtn.setForeground(Color.WHITE); // Set the text color to white
        quitBtn.setFont(buttonFont);
        panel.add(quitBtn);
        panel.add(Box.createVerticalStrut(25));

        
        //adds actionlistener to main buttons
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

        //Mute button
        muteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (backgroundMusic.isRunning()) {
                    backgroundMusic.stop();
                } else {
                    backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
                }
            }
        });

    }

    public JPanel getPanel() {
        return this.panel;
    }
}
