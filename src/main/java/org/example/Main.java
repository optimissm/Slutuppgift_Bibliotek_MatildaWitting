package org.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.example.LibraryDB.SQLExceptionPrint;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");


        try {
            Connection conn = LibraryDB.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SHOW TABLES");

            while (result.next()) {
                System.out.println(result.getString("Tables_in_Library"));
            }

            String sql = "INSERT INTO LibraryCard (";
            sql += "name, email, password";
            sql += ") VALUES (";
            sql += "'Matilda Witting', ";
            sql += "'matildawitting@gmail.com', ";
            sql += "'password'";
            sql += "); ";

            System.out.println(sql);
            int changeRows = stmt.executeUpdate(sql);
            System.out.println("We changed " + changeRows + " rows in database");


            String sqlT = "INSERT INTO Shelf (";
            sqlT += "title, author, typeOfMedia, status";
            sqlT += ") VALUES (";
            sqlT += "'Java For Dummies', ";
            sqlT += "'Barry Burd PhD', ";
            sqlT += "'Book', ";
            sqlT += "'Available' ";
            sqlT += ") ";

            System.out.println(sqlT);
            int changedRows = stmt.executeUpdate(sqlT);
            System.out.println("We changed " + changedRows + " rows in database");



        } catch (SQLException e) {
            SQLExceptionPrint(e, true);
        }

//        public void createPokemon (String name, String type, String gender) {
//            String url = "jdbc:mysql:localhost:3306/PokedexDB";
//            String user = "root";
//            String password = "rootpassword";
//
//            String sql = "INSERT INTO Pokemon (name, type, gender) " +
//                    "VALUES (?, ?, ?) ";
//
//            try (Connection conn = DriverManager.getConnection(url, user, password);
//                 PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//                stmt.setString(1, name);
//                stmt.setString(2, type);
//                stmt.setString(3, gender);
//
//                int changedRows = stmt.executeUpdate();
//                System.out.println("we changed " + changedRows + " rows in database");
//
//
//            } catch (SQLException e) {
//                SQLExceptionPrint(e, true);
//            }



    }
}  