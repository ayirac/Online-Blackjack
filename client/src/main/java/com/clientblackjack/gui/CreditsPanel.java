package com.clientblackjack.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.LineBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CreditsPanel {
    private JPanel panel = new JPanel();

	public CreditsPanel(ActionListener listener) {
		panel.setLayout(new GridBagLayout());
	
		JLabel jLabelOne = new JLabel("Credit to Kyle Garrett for preliminary development and debug");
		JLabel jLabelTwo = new JLabel("Credit to Arthur Santamaria for intermediate code review and debug");
		JLabel jLabelThree = new JLabel("Credit to Andrew Coviello for secondary development, debug and final revision");
	
		GridBagConstraints constraints = new GridBagConstraints();
	
		jLabelOne.setFont(new Font("Verdana", 1, 20));
		constraints.gridx = 0;
		constraints.gridy = 0;
		panel.add(jLabelOne, constraints);
	
		jLabelTwo.setFont(new Font("Verdana", 1, 20));
		constraints.gridx = 0;
		constraints.gridy = 1;
		panel.add(jLabelTwo, constraints);
	
		jLabelThree.setFont(new Font("Verdana", 1, 20));
		constraints.gridx = 0;
		constraints.gridy = 2;
		panel.add(jLabelThree, constraints);
	
		//Back button made here with actionlistener
		JButton backButton = new JButton("Back");
		final ActionListener finalListener = listener;
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Handle back button action here
				finalListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Back"));
			}
		});
		constraints.gridx = 0;
		constraints.gridy = 3;
		panel.add(backButton, constraints);
		//End of backbutton


		panel.setBorder(new LineBorder(Color.BLACK));
		panel.setSize(400, 400);
		panel.setVisible(true);
	}

    public JPanel getPanel() {
        return this.panel;
    }
}
