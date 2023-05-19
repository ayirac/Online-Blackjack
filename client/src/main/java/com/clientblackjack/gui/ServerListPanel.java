package com.clientblackjack.gui;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.*;

public class ServerListPanel {
    public static final String BACKTOMENU = "back-to-menu"; // back to menu immutable string
    public static final String CONNECT = "connect"; // connect immutable string
    public static final String[] BUTTONS = {BACKTOMENU, CONNECT}; // immutable string array with backtomenu, connect buttons inside it
    private JPanel panel = new JPanel(new GridBagLayout()); // panel is new panel using grid bag layout (also new)
    GridBagConstraints constraints = new GridBagConstraints(); // constraints are grid bag constraints using new gridbagconstraints

    public JButton connectBtn = new JButton("Connect"); // connect button = new j button with "Connect" string
    public JButton backBtn = new JButton("Back"); // back button = new j button with "Back" string

    public JLabel listPlaceholderLabel = new JLabel("Server List"); // list placeholder label is new j label with "server list"
    public JTable serverList; // new j table server list initialized

    public ServerListPanel() { // serverlist panel constructor
        connectBtn.setName(CONNECT); // connection button name set to CONNECT
        backBtn.setName(BACKTOMENU); // back button name set to BACKTOMENU
        String[] columnHeaders = {"Name", "Players", "Ping"}; // mutable string array of column headers "Name" "Players" "Ping"

        DefaultTableColumnModel columnMod = new DefaultTableColumnModel(); // default table column model variable established
        for (int i = 0; i < columnHeaders.length; i++) { // for every column header in the table
            TableColumn c = new TableColumn(i); // new column established at position i
            c.setHeaderValue(columnHeaders[i]); // new header value set at column headers index i
            columnMod.addColumn(c); // column added
        }   
        columnMod.getColumn(0).setPreferredWidth(400); // first column set to preferred width of 400, second and third have widths of 100
        columnMod.getColumn(1).setPreferredWidth(100);
        columnMod.getColumn(2).setPreferredWidth(100);

        DefaultTableModel tableMod = new DefaultTableModel(columnHeaders, 13); // table mod variable initialized as a new default table model with params: column headers and 13
        serverList = new JTable(tableMod, columnMod); // serverlist is a new JTable with tablemod and column mod as parameters
        serverList.setRowHeight(50); // row height is 50
        
        JScrollPane scrollPane = new JScrollPane(serverList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // vertical scroll bar established
        scrollPane.setPreferredSize(new Dimension(200, serverList.getSize().height)); // scroll pane preferred size set to new dimension of 200xserver list height
        constraints.fill = GridBagConstraints.BOTH; // fill both vertical and horizontal constraints
        constraints.weightx = 1.0; // weight of constraints is 1.0
        
        constraints.insets = new Insets(5, 5, 10, 10); // insets are rectangular 5, 5, 10, 10
        scrollPane.setSize(300, 700); // scroll pane is 300x700

        listPlaceholderLabel.setHorizontalAlignment(JLabel.CENTER); // 
        constraints.anchor = GridBagConstraints.NORTH; // Anchor
        constraints.gridx = 0; // No horizontal skew
        constraints.gridy = 0; // No vertical skew
        constraints.gridwidth = 2; // Width of 2
        panel.add(listPlaceholderLabel, constraints); // List placeholder label added to panel with constraints

        constraints.weighty = 1.0; // Vertical weight of 1.0 for constraints
        constraints.gridwidth = 2; // Width of 2
        constraints.gridx = 0; // No horizontal skew
        constraints.gridy = 1; // Downward vertical skew
        panel.add(scrollPane, constraints); // Scroll pane added to panel with constraints

        constraints.weighty = 0.0; // No weight for constraints
        constraints.insets = new Insets(5, 30, 5, 30); // Insets for constraints
        constraints.gridwidth = 1; // Width of 1
        constraints.gridx = 0; // No horizontal skew
        constraints.gridy = 2; // Heavy downward vertical skew
        panel.add(backBtn, constraints); // Back button added to panel with constraints

        constraints.gridx = 1; // Rightward horizontal skew
        constraints.gridy = 2; // Heavy downward vertical skew
        panel.add(connectBtn, constraints); // Connect button added to panel with constraints

    }

    public JPanel getPanel() { // just return the panel we are looking for
        return this.panel;
    }

    public void loadServers(String[][] servers) { // load servers using 2 dimensional array as param
        DefaultTableModel tableMod = (DefaultTableModel) serverList.getModel(); // table mod basically takes a default table model casted serverlist's model
        int c = 0; // c initialized to 0
        for (int i = 0; servers[i][0] != null; i++) { // go through all the servers until we hit null
            System.out.println(servers[i][0] + servers[i][1] + servers[i][2]); // print the first three servers to the console
            tableMod.setValueAt(servers[i][c], i, c++); // set value at server 0, column 1
            tableMod.setValueAt(servers[i][c], i, c++); // set value at server 0, column 2
            tableMod.setValueAt(servers[i][c], i, c++); // set value at server 0, column 3
            c %= 3; // mod c by 3 to bring it back again
        }
    }

    public JTable getTable() { // return the table we are looking for
        return this.serverList;
    }
}
