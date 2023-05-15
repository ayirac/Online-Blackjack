package com.clientblackjack.gui;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.border.LineBorder;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout.Constraints;

public class CreditsPanel {
	private JPanel panel = new JPanel();
	
	public CreditsPanel(ActionListener listener)
	{
		// frame that holds the panel
	    // uses grid bag layout, panels don't show up
	    // if layout set to null (REMEMBER THIS)
	    panel.setLayout(new GridBagLayout());
	    // panel within frame
	    // labels within panel
	    // kyle frame
	    JLabel jLabelOne = new JLabel("Credit to Kyle Garrett for preliminary development and debug");
	    // arthur frame
	    JLabel jLabelTwo = new JLabel("Credit to Arthur Santamaria for intermediate code review and debug");
	    // andrew frame
	    JLabel jLabelThree = new JLabel("Credit to Andrew Coviello for secondary development, debug and final revision");
	    // all labels set to verdana font
	    
	    
		GridBagConstraints constraints = new GridBagConstraints();
	    
		jLabelOne.setFont(new Font("Verdana",1,20));
		constraints.gridx = 0;
		constraints.gridy = 0;
	    panel.add(jLabelOne, constraints);
	    jLabelTwo.setFont(new Font("Verdana",1,20));
		constraints.gridx = 0;
		constraints.gridy = 1;
	    panel.add(jLabelTwo, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
	    jLabelThree.setFont(new Font("Verdana",1,20));
	    panel.add(jLabelThree, constraints);

	
	    
	    // border established for panel within frame
	    panel.setBorder(new LineBorder(Color.BLACK));
	    // border added to frame
	    // border size set to 400x400
	    panel.setSize(400, 400);
	    // frame location not set relative to anything
	    panel.setVisible(true);
	}
	public JPanel getPanel() {
        return this.panel;
    }
}
