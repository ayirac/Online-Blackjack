package com.clientblackjack.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.LineBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CreditsPanel {
    private JPanel panel = new JPanel(); // panel which will open and where text will be displayed, not set to visible(true) yet

	public CreditsPanel(ActionListener listener) { // credits panel method takes action listener as param
		panel.setLayout(new GridBagLayout()); // using a grid bag layout to set the layout for the panel
	
		JLabel jLabelOne = new JLabel("Credit to Kyle Garrett for preliminary development and debug"); // 1st credit label for Kyle Garrett
		JLabel jLabelTwo = new JLabel("Credit to Arthur Santamaria for intermediate code review and debug"); // 2nd credit label for Arthur Santamaria
		JLabel jLabelThree = new JLabel("Credit to Andrew Coviello for secondary development, debug and final revision"); // 3rd credit label for Andrew Coviello
		// Kyle Arthur Andrew - KAA
		GridBagConstraints constraints = new GridBagConstraints(); // Constraints for the grid bag layout
	
		jLabelOne.setFont(new Font("Verdana", 1, 20)); // Font for first JLabel (Kyle Garrett), will be same size and font for all 3
		constraints.gridx = 0; // That should put this in the middle
		constraints.gridy = 0; // Highest up
		panel.add(jLabelOne, constraints); // Added label 1 with constraints to the panel
	
		jLabelTwo.setFont(new Font("Verdana", 1, 20));
		constraints.gridx = 0; 
		constraints.gridy = 1; // Middle highest up
		panel.add(jLabelTwo, constraints); // Added label 2 with constraints to the panel
	
		jLabelThree.setFont(new Font("Verdana", 1, 20));
		constraints.gridx = 0;
		constraints.gridy = 2; // Between middle label and back button
		panel.add(jLabelThree, constraints); // Added label 3 with constraints to the panel
	
		//Back button made here with actionlistener
		JButton backButton = new JButton("Back");
		backButton.addActionListener(listener); // Listener from before is being used in back button
		constraints.gridx = 0; 
		constraints.gridy = 3; // Lowest
		panel.add(backButton, constraints); // Back button, with constraints, is added to the bottom of the panel
		backButton.setName("back"); // Name for button is set
		//End of backbutton


		panel.setBorder(new LineBorder(Color.BLACK)); // Black border for panel
		panel.setSize(400, 400); // 400x400 panel
		panel.setVisible(true); // now when we click the button we can actually see the panel pop up
	}

    public JPanel getPanel() { // returning the panel we just made
        return this.panel;
    }
}
