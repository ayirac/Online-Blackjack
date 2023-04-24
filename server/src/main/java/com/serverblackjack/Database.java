package com.serverblackjack;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    private java.sql.Connection con;
    public Database(String url, String user, String password) throws SQLException {
        con = DriverManager.getConnection(url, user, password);
    }


    // Checks if the user:password combination exists in the database
    public boolean validateUser(String userName, String password) throws SQLException {
        String sqlCmd = "SELECT COUNT(*) FROM users WHERE username=\"" + userName + "\" AND password=\"" + password + "\";";
        PreparedStatement sqlStatement = con.prepareStatement(sqlCmd);
        ResultSet rs = sqlStatement.executeQuery();
        rs.next();
        int c = rs.getInt(1);
        rs.close();
        sqlStatement.close();
        return c == 0 ? false : true; 
    }
}
