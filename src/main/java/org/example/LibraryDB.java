package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class LibraryDB {

    private static Connection db = null;

    static Connection getConnection() {
        if (db != null) return db;

        String username = "";
        String password = "";
        String host = "";
        String port = "";
        String name = "";

        Properties props = new Properties();

        try (InputStream input = LibraryDB.class.getResourceAsStream("/LibraryProperties")){

            props.load(input);
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");
            host = props.getProperty("db.host");
            port = props.getProperty("db.port");
            name = props.getProperty("db.name");


        } catch (IOException ex){
            ex.printStackTrace();
            return null;
        }

        try {

            String url = "jdbc:mysql://" + host;
            url += ":" + port;
            url += "/" + name + "?useSSL=false";

            db = DriverManager.getConnection(url, username, password);

        } catch (SQLException e) {
            SQLExceptionPrint(e);
            return null;
        }

        return db;

    }

    static void SQLExceptionPrint(SQLException sqle) {
        SQLExceptionPrint(sqle, false);
    }

    static void SQLExceptionPrint(SQLException sqle, Boolean printStackTrace) {
        while (sqle != null) {
            System.out.println("\n---SQLException Caught---\n");
            System.out.println("SQLState: " + sqle.getSQLState());
            System.out.println("Severity: " + sqle.getErrorCode());
            System.out.println("Message: " + sqle.getMessage());

            if(printStackTrace) sqle.printStackTrace();
            sqle = sqle.getNextException();

        }
    }


}
