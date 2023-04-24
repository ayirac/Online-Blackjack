[33mcommit 2979b8f9a96dc5405d078c72c8221219c4199074[m[33m ([m[1;36mHEAD -> [m[1;32mmain[m[33m, [m[1;31morigin/main[m[33m, [m[1;31morigin/HEAD[m[33m)[m
Author: ayirac <ayirac@cougars.csusm.edu>
Date:   Sun Apr 23 15:01:22 2023 -0700

    Added serverlist functionality for client/server

[1mdiff --git a/client/src/main/java/com/clientblackjack/Application.java b/client/src/main/java/com/clientblackjack/Application.java[m
[1mindex e6cf9f5..d9bb039 100644[m
[1m--- a/client/src/main/java/com/clientblackjack/Application.java[m
[1m+++ b/client/src/main/java/com/clientblackjack/Application.java[m
[36m@@ -8,7 +8,7 @@[m [mpublic class Application[m
     public static void main( String[] args )[m
     {[m
         MainFrame mainFrame = new MainFrame();[m
[31m-        mainFrame.swapMainMenu();[m
[31m-        mainFrame.run();[m
[32m+[m[32m        mainFrame.swapMainMenu();;[m
[32m+[m[32m        mainFrame.run(1280, 720);[m
     }[m
 }[m
[1mdiff --git a/client/src/main/java/com/clientblackjack/gui/MainFrame.java b/client/src/main/java/com/clientblackjack/gui/MainFrame.java[m
[1mindex 6befa9e..4ecd06c 100644[m
[1m--- a/client/src/main/java/com/clientblackjack/gui/MainFrame.java[m
[1m+++ b/client/src/main/java/com/clientblackjack/gui/MainFrame.java[m
[36m@@ -38,9 +38,18 @@[m [mpublic class MainFrame {[m
                             // basic test connection[m
                             serverConnection_ = new Connection(new Socket("localhost", 5012));[m
                             serverConnection_.sendMessage("|-!-login:" + loginPanel.getUsername() + ":" + loginPanel.getPasswordHash() + ":-!-|");[m
[31m-                            String m = serverConnection_.receiveMessage();[m
[31m-                            System.out.println(m);[m
[31m-                            swapServerList();[m
[32m+[m[32m                            String response = serverConnection_.receiveMessage();[m
[32m+[m[32m                            String loginResult = parseMessage(response).str;[m
[32m+[m[32m                            if (loginResult.equals("valid")) {[m
[32m+[m[32m                                serverConnection_.sendMessage("|-!-server-list:-!-|");[m
[32m+[m[32m                                response = serverConnection_.receiveMessage();[m
[32m+[m[32m                                String[][] servers = parseMessage(response).arr;[m
[32m+[m[32m                                swapServerList(servers);[m
[32m+[m[32m                            } else if (loginResult.equals("invalid")) {[m
[32m+[m[32m                                System.out.println("Invalid login! Popup box to be implemented");[m
[32m+[m[32m                            } else {[m
[32m+[m[32m                                System.out.println("Error: Incorrect message passing");[m
[32m+[m[32m                            }[m
                             //serverConnection_.close();[m
                         } catch (IOException ev) {[m
                             ev.printStackTrace();[m
[36m@@ -58,11 +67,11 @@[m [mpublic class MainFrame {[m
         cards.add(serverListPanel.getPanel(), "server-list");[m
     }[m
 [m
[31m-    public void run() {[m
[32m+[m[32m    public void run(int x, int y) {[m
         JFrame frame = new JFrame();[m
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);[m
         frame.add(cards);[m
[31m-        frame.setSize(300, 200);[m
[32m+[m[32m        frame.setSize(x, y);[m
         frame.setVisible(true);[m
     }[m
 [m
[36m@@ -74,7 +83,8 @@[m [mpublic class MainFrame {[m
         cLayout.show(cards, MainMenuPanel.PLAYGAME);[m
     }[m
 [m
[31m-    public void swapServerList() {[m
[32m+[m[32m    public void swapServerList(String[][] servers) {[m
[32m+[m[32m        serverListPanel.loadServers(servers);[m
         cLayout.show(cards, "server-list");[m
     }[m
 [m
[36m@@ -89,4 +99,61 @@[m [mpublic class MainFrame {[m
     public void quitGame() {[m
         System.exit(0);[m
     }[m
[32m+[m
[32m+[m[32m    private class Data {[m
[32m+[m[32m        public String str;[m
[32m+[m[32m        public String[][] arr;[m
[32m+[m
[32m+[m[32m        public Data(String s, String[][] a) {[m
[32m+[m[32m            this.str = s;[m
[32m+[m[32m            this.arr = a;[m
[32m+[m[32m        }[m
[32m+[m[41m        [m
[32m+[m[32m        public Data(String s) {[m
[32m+[m[32m            this.str = s;[m
[32m+[m[32m            this.arr = new String[0][];[m
[32m+[m[32m        }[m[41m [m
[32m+[m
[32m+[m[32m        public Data(String[][] a) {[m
[32m+[m[32m            this.str = "";[m
[32m+[m[32m            this.arr = a;[m
[32m+[m[32m        }[m[41m [m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m     // Types of messages that the server might send[m
[32m+[m[32m    // |-!-login:invalid-!-|[m[41m [m
[32m+[m[32m    // |-!-getServers:US West 1:7/8:50-!-|[m
[32m+[m[32m    // Parses a message & sends a response or logs an error.[m
[32m+[m[32m    public Data parseMessage(String msg) {[m
[32m+[m[32m        String data = msg.substring(msg.indexOf("|-!-")+4, msg.indexOf("-!-|")); // Get text inbetween start/end[m
[32m+[m[32m        System.out.println("parsing  " + msg);[m
[32m+[m[32m        int i = 0, previ = 0, e = 0;[m
[32m+[m[32m        String[] args = new String[10];[m
[32m+[m
[32m+[m[32m        while ((i = data.indexOf(":", previ)) != -1) {[m
[32m+[m[32m            String cmd = data.substring(previ, i);[m
[32m+[m[32m            previ = i+1;[m
[32m+[m[32m            args[e++] = cmd;[m
[32m+[m[32m        }[m
[32m+[m[41m        [m
[32m+[m[32m        switch (args[0]) {[m
[32m+[m[32m            case "login":[m
[32m+[m[32m                Data d = new Data(args[1]);[m
[32m+[m[32m                System.out.println(d.str);[m
[32m+[m[32m                return d;[m
[32m+[m[32m            case "server-list":[m
[32m+[m[32m                // name:players:ping[m
[32m+[m[32m                int rows = (args.length-2)/3;[m
[32m+[m[32m                String [][] servers = new String[rows][3];[m
[32m+[m[32m                int c = 0, j = 1;[m
[32m+[m[32m                for (int r = 0; r < rows; r++) {[m
[32m+[m[32m                    servers[r][c++] = args[j++];[m
[32m+[m[32m                    servers[r][c++] = args[j++];[m
[32m+[m[32m                    servers[r][c++] = args[j++];[m
[32m+[m[32m                    c %= 3;[m
[32m+[m[32m                }[m
[32m+[m[32m                return new Data(servers);[m
[32m+[m[32m        }[m
[32m+[m[32m        return new Data("");[m
[32m+[m[32m    }[m
 }[m
[1mdiff --git a/client/src/main/java/com/clientblackjack/gui/ServerListPanel.java b/client/src/main/java/com/clientblackjack/gui/ServerListPanel.java[m
[1mindex 6c99936..afcd3d5 100644[m
[1m--- a/client/src/main/java/com/clientblackjack/gui/ServerListPanel.java[m
[1m+++ b/client/src/main/java/com/clientblackjack/gui/ServerListPanel.java[m
[36m@@ -1,29 +1,94 @@[m
 package com.clientblackjack.gui;[m
 [m
 import java.awt.event.ActionListener;[m
[32m+[m
[32m+[m[32mimport javax.swing.JButton;[m
 import javax.swing.JLabel;[m
 import javax.swing.JPanel;[m
[32m+[m[32mimport javax.swing.JScrollPane;[m
[32m+[m[32mimport javax.swing.JTable;[m
[32m+[m[32mimport javax.swing.table.DefaultTableColumnModel;[m
[32m+[m[32mimport javax.swing.table.DefaultTableModel;[m
[32m+[m[32mimport javax.swing.table.TableColumn;[m
[32m+[m
 import java.awt.*;[m
 [m
 public class ServerListPanel {[m
[32m+[m[32m    public static final String BACKTOMENU = "back-to-menu";[m
[32m+[m[32m    public static final String CONNECT = "connect";[m
[32m+[m[32m    public static final String[] BUTTONS = {BACKTOMENU, CONNECT};[m
     private JPanel panel = new JPanel(new GridBagLayout());[m
     GridBagConstraints constraints = new GridBagConstraints();[m
 [m
[31m-    public JLabel listPlaceholderLabel = new JLabel("list placeholder");[m
[32m+[m[32m    public JButton connectBtn = new JButton("Connect");[m
[32m+[m[32m    public JButton backBtn = new JButton("Back");[m
[32m+[m
[32m+[m[32m    public JLabel listPlaceholderLabel = new JLabel("Server List");[m
[32m+[m[32m    public JTable serverList;[m
 [m
     public ServerListPanel(ActionListener listener) {[m
[31m-        constraints.fill = GridBagConstraints.HORIZONTAL;[m
[31m-        constraints.insets = new Insets(5, 5, 5, 5);[m
[32m+[m[32m        String[] columnHeaders = {"Name", "Players", "Ping"};[m
[32m+[m
[32m+[m[32m        DefaultTableColumnModel columnMod = new DefaultTableColumnModel();[m
[32m+[m[32m        for (int i = 0; i < columnHeaders.length; i++) {[m
[32m+[m[32m            TableColumn c = new TableColumn(i);[m
[32m+[m[32m            c.setHeaderValue(columnHeaders[i]);[m
[32m+[m[32m            columnMod.addColumn(c);[m
[32m+[m[32m        }[m[41m   [m
[32m+[m[32m        columnMod.getColumn(0).setPreferredWidth(400);[m
[32m+[m[32m        columnMod.getColumn(1).setPreferredWidth(100);[m
[32m+[m[32m        columnMod.getColumn(2).setPreferredWidth(100);[m
[32m+[m
[32m+[m[32m        DefaultTableModel tableMod = new DefaultTableModel(columnHeaders, 13);[m
[32m+[m[32m        serverList = new JTable(tableMod, columnMod);[m
[32m+[m[32m        serverList.setRowHeight(50);[m
[32m+[m[41m        [m
[32m+[m[32m        JScrollPane scrollPane = new JScrollPane(serverList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);[m
[32m+[m[32m        scrollPane.setPreferredSize(new Dimension(200, serverList.getSize().height));[m
[32m+[m[32m        constraints.fill = GridBagConstraints.BOTH;[m
[32m+[m[32m        constraints.weightx = 1.0;[m
[32m+[m[41m        [m
[32m+[m[32m        constraints.insets = new Insets(5, 5, 10, 10);[m
[32m+[m[32m        scrollPane.setSize(300, 700);[m
 [m
         listPlaceholderLabel.setHorizontalAlignment(JLabel.CENTER);[m
[32m+[m[32m        constraints.anchor = GridBagConstraints.NORTH;[m
         constraints.gridx = 0;[m
         constraints.gridy = 0;[m
[32m+[m[32m        constraints.gridwidth = 2;[m
         panel.add(listPlaceholderLabel, constraints);[m
 [m
[31m-       [m
[32m+[m[32m        constraints.weighty = 1.0;[m
[32m+[m[32m        constraints.gridwidth = 2;[m
[32m+[m[32m        constraints.gridx = 0;[m
[32m+[m[32m        constraints.gridy = 1;[m
[32m+[m[32m        panel.add(scrollPane, constraints);[m[41m [m
[32m+[m
[32m+[m[32m        constraints.weighty = 0.0;[m
[32m+[m[32m        constraints.insets = new Insets(5, 30, 5, 30);[m
[32m+[m[32m        constraints.gridwidth = 1;[m
[32m+[m[32m        constraints.gridx = 0;[m
[32m+[m[32m        constraints.gridy = 2;[m
[32m+[m[32m        panel.add(backBtn, constraints);[m[41m [m
[32m+[m
[32m+[m[32m        constraints.gridx = 1;[m
[32m+[m[32m        constraints.gridy = 2;[m
[32m+[m[32m        panel.add(connectBtn, constraints);[m[41m [m
     }[m
 [m
     public JPanel getPanel() {[m
         return this.panel;[m
     }[m
[32m+[m
[32m+[m[32m    public void loadServers(String[][] servers) {[m
[32m+[m[32m        DefaultTableModel tableMod = (DefaultTableModel) serverList.getModel();[m
[32m+[m[32m        int c = 0;[m
[32m+[m[32m        for (int i = 0; i < servers.length; i++) {[m
[32m+[m[32m            System.out.println(servers[i][0] + servers[i][1] + servers[i][2]);[m
[32m+[m[32m            tableMod.setValueAt(servers[i][c], i, c++);[m
[32m+[m[32m            tableMod.setValueAt(servers[i][c], i, c++);[m
[32m+[m[32m            tableMod.setValueAt(servers[i][c], i, c++);[m
[32m+[m[32m            c %= 3;[m
[32m+[m[32m        }[m
[32m+[m[32m    }[m
 }[m
[1mdiff --git a/client/target/classes/com/clientblackjack/Application.class b/client/target/classes/com/clientblackjack/Application.class[m
[1mindex 17b4911..50494cc 100644[m
Binary files a/client/target/classes/com/clientblackjack/Application.class and b/client/target/classes/com/clientblackjack/Application.class differ
[1mdiff --git a/client/target/classes/com/clientblackjack/gui/MainFrame$1.class b/client/target/classes/com/clientblackjack/gui/MainFrame$1.class[m
[1mindex 689e701..0b63fee 100644[m
Binary files a/client/target/classes/com/clientblackjack/gui/MainFrame$1.class and b/client/target/classes/com/clientblackjack/gui/MainFrame$1.class differ
[1mdiff --git a/client/target/classes/com/clientblackjack/gui/MainFrame$Data.class b/client/target/classes/com/clientblackjack/gui/MainFrame$Data.class[m
[1mnew file mode 100644[m
[1mindex 0000000..a56d936[m
Binary files /dev/null and b/client/target/classes/com/clientblackjack/gui/MainFrame$Data.class differ
[1mdiff --git a/client/target/classes/com/clientblackjack/gui/MainFrame.class b/client/target/classes/com/clientblackjack/gui/MainFrame.class[m
[1mindex 1753366..0a99698 100644[m
Binary files a/client/target/classes/com/clientblackjack/gui/MainFrame.class and b/client/target/classes/com/clientblackjack/gui/MainFrame.class differ
[1mdiff --git a/client/target/classes/com/clientblackjack/gui/ServerListPanel.class b/client/target/classes/com/clientblackjack/gui/ServerListPanel.class[m
[1mindex 3cedcee..34e65c3 100644[m
Binary files a/client/target/classes/com/clientblackjack/gui/ServerListPanel.class and b/client/target/classes/com/clientblackjack/gui/ServerListPanel.class differ
[1mdiff --git a/server/src/main/java/com/serverblackjack/Server.java b/server/src/main/java/com/serverblackjack/Server.java[m
[1mindex 0363507..88059e6 100644[m
[1m--- a/server/src/main/java/com/serverblackjack/Server.java[m
[1m+++ b/server/src/main/java/com/serverblackjack/Server.java[m
[36m@@ -81,7 +81,7 @@[m [mpublic class Server {[m
             args[e++] = cmd;[m
         }[m
         [m
[31m-        switch (args[0]) {[m
[32m+[m[32m        switch (args[0]) { // cont, server needs to send server data![m
             case "login":[m
                 String username = args[1];[m
                 String password = args[2];[m
[36m@@ -91,40 +91,49 @@[m [mpublic class Server {[m
                     if (db_.validateUser(username, password))[m
                         res = "valid";[m
                     else[m
[31m-                        res = "not valid";[m
[32m+[m[32m                        res = "invalid";[m
                     System.out.println(res);  [m
[31m-                    cnt.sendMessage("|-!" + "login:" + res + "-!-|");[m
[32m+[m[32m                    cnt.sendMessage("|-!" + "login:" + res + ":-!-|");[m
                 } catch (SQLException ex) {[m
                     ex.printStackTrace();[m
                 }[m
                 break;[m
[31m-                [m
[31m-                // Check if user exists in DB[m
[31m-                // Check if password matches[m
[31m-                // If match, send success.[m
[31m-                // else, send failure[m
[32m+[m[32m            case "server-list":[m
[32m+[m[32m                // placeholder[m
[32m+[m[32m                String res = "US West 1:5/8:40:US West 2:1/8:35:US East:4/8:87:";[m
[32m+[m[32m                cnt.sendMessage("|-!" + "server-list:" + res + "-!-|");[m
[32m+[m[32m                break;[m
         }[m
     }[m
 [m
     private class ClientTask implements Runnable {[m
         private final Socket clientSocket;[m
[32m+[m[32m        private Connection connection;[m
[32m+[m[41m    [m
         private ClientTask(Socket clientSocket) {[m
             this.clientSocket = clientSocket;[m
         }[m
[31m-[m
[32m+[m[41m    [m
         @Override[m
         public void run() {[m
             System.out.println("Got a client! " + clientSocket.getInetAddress());[m
[31m-            [m
[32m+[m[41m    [m
             try {[m
[31m-                connections_[totalConnections_] = new Connection(clientSocket);[m
[31m-                String msg = connections_[totalConnections_].receiveMessage();[m
[31m-                parseMessage(msg, connections_[totalConnections_]);[m
[31m-                totalConnections_++;[m
[31m-                clientSocket.close();[m
[32m+[m[32m                connection = new Connection(clientSocket);[m
[32m+[m[32m                while (true) {[m
[32m+[m[32m                    String msg = connection.receiveMessage();[m
[32m+[m[32m                    parseMessage(msg, connection);[m
[32m+[m[32m                }[m
             } catch (IOException e) {[m
                 e.printStackTrace();[m
[32m+[m[32m            } finally {[m
[32m+[m[32m                try {[m
[32m+[m[32m                    connection.close();[m
[32m+[m[32m                } catch (IOException e) {[m
[32m+[m[32m                    e.printStackTrace();[m
[32m+[m[32m                }[m
             }[m
         }[m
     }[m
[32m+[m[41m    [m
 }[m
[1mdiff --git a/server/target/classes/com/serverblackjack/Server$1.class b/server/target/classes/com/serverblackjack/Server$1.class[m
[1mindex 98857a8..f440d96 100644[m
Binary files a/server/target/classes/com/serverblackjack/Server$1.class and b/server/target/classes/com/serverblackjack/Server$1.class differ
[1mdiff --git a/server/target/classes/com/serverblackjack/Server$2.class b/server/target/classes/com/serverblackjack/Server$2.class[m
[1mindex a613668..cc71d26 100644[m
Binary files a/server/target/classes/com/serverblackjack/Server$2.class and b/server/target/classes/com/serverblackjack/Server$2.class differ
[1mdiff --git a/server/target/classes/com/serverblackjack/Server$ClientTask.class b/server/target/classes/com/serverblackjack/Server$ClientTask.class[m
[1mindex 7f8f7b4..4b7610d 100644[m
Binary files a/server/target/classes/com/serverblackjack/Server$ClientTask.class and b/server/target/classes/com/serverblackjack/Server$ClientTask.class differ
[1mdiff --git a/server/target/classes/com/serverblackjack/Server.class b/server/target/classes/com/serverblackjack/Server.class[m
[1mindex dcae4da..70e3dc3 100644[m
Binary files a/server/target/classes/com/serverblackjack/Server.class and b/server/target/classes/com/serverblackjack/Server.class differ
