package de.gabik21.hospitalcore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.math3.util.Pair;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class MySQL {

    public static String host;
    public static String port;
    public static String database;
    public static String username;
    public static String password;
    public static Connection con;

    public static void connect() {

	if (!isConnected()) {
	    try {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUser(username);
		dataSource.setPassword(password);
		dataSource.setDatabaseName(database);
		dataSource.setServerName(host);
		con = dataSource.getConnection();
		System.out.println("[Hospital] MySQL connection established!");
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

    }

    public static void disconnect() {

	try {
	    con.close();
	    System.out.println("[Hospital] MySQL connection closed");
	} catch (SQLException e) {
	    e.printStackTrace();
	}

    }

    public static boolean isConnected() {

	if (con == null)
	    return false;

	try {
	    if (!con.isValid(1000))
		return false;
	} catch (SQLException e) {
	    e.printStackTrace();
	}

	return true;

    }

    public static int executeUpdate(String s) {

	int i = -1;

	try {
	    PreparedStatement ps = getConnection().prepareStatement(s);
	    i = ps.executeUpdate();
	    ps.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}

	return i;
    }

    public static Pair<ResultSet, PreparedStatement> executeQuery(String s) {

	ResultSet result = null;
	PreparedStatement ps = null;

	try {
	    ps = getConnection().prepareStatement(s);
	    result = ps.executeQuery();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return new Pair<>(result, ps);

    }

    public static Connection getConnection() {

	if (!isConnected())
	    connect();

	return con;
    }

}
