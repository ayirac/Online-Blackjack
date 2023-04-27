package com.clientblackjack.gui;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.border.LineBorder;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
	    jLabelOne.setFont(new Font("Verdana",1,20));
	    // i can only seem to add this label
	    panel.add(jLabelOne);
	    
	    /*
	    jLabelTwo.setFont(new Font("Verdana",1,20));
	    panel.add(jLabelTwo);
	    jLabelThree.setFont(new Font("Verdana",1,20));
	    panel.add(jLabelThree);
	    */
	    // border established for panel within frame
	    panel.setBorder(new LineBorder(Color.BLACK));
	    // border added to frame
	    // border size set to 400x400
	    panel.setSize(400, 400);
	    // frame location not set relative to anything
	    //panel.setLocationRelativeTo(null);
	    // default close operation (x button top right)
	    //panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    // make sure you can actually see it
	    panel.setVisible(true);
	}
	public JPanel getPanel() {
        return this.panel;
    }
}
