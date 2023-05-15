package com.serverblackjack;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
	// establish a connection to the SQL server via variable con
    private java.sql.Connection con;
    // database method throws an SQL exception, con gets connection
    public Database(String url, String user, String password) throws SQLException {
        con = DriverManager.getConnection(url, user, password);
    }


    // Checks if the user:password combination exists in the database
    public boolean validateUser(String userName, String password) throws SQLException {
        // SQL command string, designed to be used automatically in the db
    	String sqlCmd = "SELECT COUNT(*) FROM users WHERE username=\"" + userName + "\" AND password=\"" + password + "\";";
        // prepared SQL statement is prepared for execution
    	PreparedStatement sqlStatement = con.prepareStatement(sqlCmd);
    	// result set variable is the result of the executed SQL query
        ResultSet rs = sqlStatement.executeQuery();
        // iterate recursively, if true then this is good
        rs.next();
        // c becomes the integer that was in the result
        int c = rs.getInt(1);
        rs.close();
        sqlStatement.close();
        // simple if else statement checking to see if c, the rs int, is 0
        return c == 0 ? false : true; 
    }
}
