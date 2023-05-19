package com.clientblackjack.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class MainMenuPanel { // main menu class
    private Clip backgroundMusic; // background music playing at all times
    public static final String MAINMENU = "main-menu"; // main menu string
    public static final String PLAYGAME = "play-game"; // play game string
    public static final String INSTRUCTIONS = "instructions"; // instructions string
    public static final String LEADERBOARD = "leaderboard"; // leaderboard string
    public static final String CREDITS = "credits"; // credits string
    public static final String QUITGAME = "quit"; // quit string - all these strings are immutable
    public static final String[] BUTTONS = { MAINMENU, PLAYGAME, INSTRUCTIONS, LEADERBOARD, QUITGAME, QUITGAME }; // all string buttons are immutable/static 
    private JPanel panel = new JPanel() { // new jpanel 
        @Override
        protected void paintComponent(Graphics g) { // paint component using graphics g as parameter
            super.paintComponent(g);
            // gets image, already resized to 1280 x 720
            ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/images/mainmenu.jpg"));
            // displays the background image
            g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    };
    private BoxLayout bLayout = new BoxLayout(panel, BoxLayout.Y_AXIS); // box layout creates new private box layout along the y axis
    
    public JButton playGameBtn = new JButton("Play Game"); // play game button created
    public JButton instructionsBtn = new JButton("Instructions"); // instructions button created
    public JButton leaderboardBtn = new JButton("Leaderboard"); // leaderboard button created
    public JButton creditsBtn = new JButton("Credits"); // credits button created
    public JButton quitBtn = new JButton("Quit Game"); // quit game button created
    public JButton muteBtn = new JButton("Mute/Unmute"); // mute/unmute button created

    public MainMenuPanel(ActionListener listener) { // main menu panel with action listener as param
        panel.setAlignmentX(Component.CENTER_ALIGNMENT); // set alignment of the panel to the center of the horizontal (X)
        panel.setLayout(bLayout); // set the panel's layout to the box layout
        playGameBtn.setName(PLAYGAME); // play game button name set to PLAYGAME
        instructionsBtn.setName(INSTRUCTIONS); // set instructions button name to INSTRUCTIONS
        instructionsBtn.setName(INSTRUCTIONS);
        leaderboardBtn.setName(LEADERBOARD); // set leaderboard button name to LEADERBOARD
        creditsBtn.setName(CREDITS); // set credits button name to CREDITS
        quitBtn.setName(QUITGAME); // set quit game button name to QUITGAME
        muteBtn.setName("Mute/Unmute"); // set mute/unmute button name to Mute/Unmute
        
        playGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT); // Play game button is aligned in the center horizontal of the screen
        instructionsBtn.setAlignmentX(Component.CENTER_ALIGNMENT); // Instructions button is aligned in the center horizontal of the screen
        leaderboardBtn.setAlignmentX(Component.CENTER_ALIGNMENT); // Leaderboard button is aligned in the center horizontal of the screen
        quitBtn.setAlignmentX(Component.CENTER_ALIGNMENT); // Quit button is aligned in the center horizontal of the screen
        creditsBtn.setAlignmentX(Component.CENTER_ALIGNMENT); // Credits button is aligned in the center horizontal of the screen
        muteBtn.setAlignmentX(Component.CENTER_ALIGNMENT); // Mute button is aligned in the center horizontal of the screen

        panel.add(Box.createVerticalStrut(25)); // Box added to panel
        //buttoninsets will change the button size

        Insets buttonInsets = new Insets(15, 15, 15, 15); 
        Font buttonFont = new Font("Copperplate Gothic", Font.BOLD, 18); // Specify the desired font size
        
        //Playgame Button
        playGameBtn.setMargin(buttonInsets); // set margin using button insets
        playGameBtn.setOpaque(true); // it is opaque
        playGameBtn.setContentAreaFilled(false); // content area is not filled
        playGameBtn.setBorderPainted(true); // border is painted
        playGameBtn.setForeground(Color.WHITE); // Set the text color to white
        playGameBtn.setFont(buttonFont); // button font, predefined, is set
        panel.add(playGameBtn); // play game button added to panel
        panel.add(Box.createVerticalStrut(25)); // box added to panel

        //Instructions Button
        instructionsBtn.setMargin(buttonInsets); // Margin set based on predefined button insets variable
        instructionsBtn.setOpaque(true); // Button is set to opaque
        instructionsBtn.setContentAreaFilled(false); // Content area not filled
        instructionsBtn.setBorderPainted(true); // border is painted
        instructionsBtn.setForeground(Color.WHITE); // Set the text color to white
        instructionsBtn.setFont(buttonFont); // button font preset is the font of the button
        panel.add(instructionsBtn); // instructions button added to panel
        panel.add(Box.createVerticalStrut(25)); // box added to panel

        //Leaderboard Button
        leaderboardBtn.setMargin(buttonInsets); // Margin set based on predefined button insets variable
        leaderboardBtn.setOpaque(true); // Button is set to opaque
        leaderboardBtn.setContentAreaFilled(false); // Content area not filled
        leaderboardBtn.setBorderPainted(true); // border is painted
        leaderboardBtn.setForeground(Color.WHITE); // Set the text color to white
        leaderboardBtn.setFont(buttonFont); // button font preset is the font of the button
        panel.add(leaderboardBtn); // leaderboard button added to panel 
        panel.add(Box.createVerticalStrut(25));

        //Credits Button
        creditsBtn.setMargin(buttonInsets); // Margin set based on predefined button insets variable
        creditsBtn.setOpaque(true); // Button is set to opaque
        creditsBtn.setContentAreaFilled(false); // Content area not filled
        creditsBtn.setBorderPainted(true); // border is painted
        creditsBtn.setForeground(Color.WHITE); // Set the text color to white
        creditsBtn.setFont(buttonFont); // button font preset is the font of the button
        panel.add(creditsBtn); // credits button added to panel
        panel.add(Box.createVerticalStrut(25));

        //Mute Button 
        muteBtn.setMargin(buttonInsets); // Margin set based on predefined button insets variable
        muteBtn.setOpaque(true); // Button is set to opaque
        muteBtn.setContentAreaFilled(false); // Content area not filled
        muteBtn.setBorderPainted(true); // border is painted
        muteBtn.setForeground(Color.WHITE); // Set the text color to white
        muteBtn.setFont(buttonFont); // button font preset is the font of the button
        panel.add(muteBtn); // mute button added to panel
        panel.add(Box.createVerticalStrut(25));
        
        //Quit Button 
        quitBtn.setMargin(buttonInsets); // Margin set based on predefined button insets variable
        quitBtn.setOpaque(true); // Button is set to opaque
        quitBtn.setContentAreaFilled(false); // Content area not filled
        quitBtn.setBorderPainted(true); // border is painted
        quitBtn.setForeground(Color.WHITE); // Set the text color to white
        quitBtn.setFont(buttonFont); // button font preset is the font of the button
        panel.add(quitBtn); // quit button added to panel
        panel.add(Box.createVerticalStrut(25));

        
        //adds actionlistener to main buttons
        playGameBtn.addActionListener(listener); // added action listener for play game button
        instructionsBtn.addActionListener(listener); // added action listener for instructions button
        leaderboardBtn.addActionListener(listener); // added action listener for leaderboard button
        creditsBtn.addActionListener(listener); // added action listener for credits button
        quitBtn.addActionListener(listener); // added action listener for quit button
        
        // Loads and plays the background music
        try { // look for unsuppoted audio files, unavailable lines or bad data
            URL musicUrl = getClass().getResource("/music/mainmenusong.wav"); // establishes URL object for main menu song
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicUrl); // establishes audio input stream from returned audio input stream
            backgroundMusic = AudioSystem.getClip(); // background music is set to the clip gotten by the getClip command 
            backgroundMusic.open(audioInputStream); // open audio input stream to bring the music to you!

            // Lower the volume since it was too loud at the start
            FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN); // gain control var established to control volume
            int volume = -25; // Decrease the volume by 25 decibels
            gainControl.setValue(volume); // set value to now decreased value so it isn't as loud

            // Keeps looping throughout the menus the moment it's run
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // I'm confused as to why you couldn't use a while true loop tbh
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace(); // print a stack trace if something ends up going screwy
        }

        //Mute button
        muteBtn.addActionListener(new ActionListener() { // mute button action listener method
            @Override
            public void actionPerformed(ActionEvent e) { // uses action event e as parameter to take the user clicking the mute button
                if (backgroundMusic.isRunning()) { // if the background music is running
                    backgroundMusic.stop(); // stops the background music
                } else {
                    backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // loops the music clip continuously
                }
            }
        });

    }

    public JPanel getPanel() { // returns the panel we want to access
        return this.panel;
    }
}
