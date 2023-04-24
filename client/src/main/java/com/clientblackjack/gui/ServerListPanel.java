package com.clientblackjack.gui;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.*;

public class ServerListPanel {
    public static final String BACKTOMENU = "back-to-menu";
    public static final String CONNECT = "connect";
    public static final String[] BUTTONS = {BACKTOMENU, CONNECT};
    private JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();

    public JButton connectBtn = new JButton("Connect");
    public JButton backBtn = new JButton("Back");

    public JLabel listPlaceholderLabel = new JLabel("Server List");
    public JTable serverList;

    public ServerListPanel(ActionListener listener) {
        String[] columnHeaders = {"Name", "Players", "Ping"};

        DefaultTableColumnModel columnMod = new DefaultTableColumnModel();
        for (int i = 0; i < columnHeaders.length; i++) {
            TableColumn c = new TableColumn(i);
            c.setHeaderValue(columnHeaders[i]);
            columnMod.addColumn(c);
        }   
        columnMod.getColumn(0).setPreferredWidth(400);
        columnMod.getColumn(1).setPreferredWidth(100);
        columnMod.getColumn(2).setPreferredWidth(100);

        DefaultTableModel tableMod = new DefaultTableModel(columnHeaders, 13);
        serverList = new JTable(tableMod, columnMod);
        serverList.setRowHeight(50);
        
        JScrollPane scrollPane = new JScrollPane(serverList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(200, serverList.getSize().height));
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        
        constraints.insets = new Insets(5, 5, 10, 10);
        scrollPane.setSize(300, 700);

        listPlaceholderLabel.setHorizontalAlignment(JLabel.CENTER);
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(listPlaceholderLabel, constraints);

        constraints.weighty = 1.0;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(scrollPane, constraints); 

        constraints.weighty = 0.0;
        constraints.insets = new Insets(5, 30, 5, 30);
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(backBtn, constraints); 

        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(connectBtn, constraints); 
    }

    public JPanel getPanel() {
        return this.panel;
    }

    public void loadServers(String[][] servers) {
        DefaultTableModel tableMod = (DefaultTableModel) serverList.getModel();
        int c = 0;
        for (int i = 0; servers[i][0] != null; i++) {
            System.out.println(servers[i][0] + servers[i][1] + servers[i][2]);
            tableMod.setValueAt(servers[i][c], i, c++);
            tableMod.setValueAt(servers[i][c], i, c++);
            tableMod.setValueAt(servers[i][c], i, c++);
            c %= 3;
        }
    }
}
