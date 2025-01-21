package org.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import static org.example.LibraryDB.SQLExceptionPrint;

public class Main {
    public static void main(String[] args) {

        // så att vi kan skanna in ny info från användaren
        Scanner scan  = new Scanner(System.in);

        // så vi börjar om lite och
        // kopplar in LibraryCard
        LibraryCard card = new LibraryCard();
        // skapa ett nytt kort
        System.out.println("Vad heter du? ");
        String name = scan.nextLine();
        card.setName(name);
        // System.out.println("Så du heter: " + name);

        System.out.println("Vad är din email? ");
        String email = scan.nextLine();
        card.setEmail(email);

        System.out.println("Välj lösenord: ");
        String password = scan.nextLine();
        card.setPassword(password);

        System.out.println("Stämmer dina uppgifter? " +
                "\n Namn: " + name +
                "\n Email: " + email +
                "\n Password: " + password);

        // detta borde vara en bra formula nu för att sätta ny användare
        // Jag får ju åtminstone med mig alla uppgifter

        // bara att sätta ID kvar då.
        // och hur gör jag det om det ska autogenereras?
        // har för mig att jag inte behöver sätta något alls då...

        // card.setId()

        System.out.println("Välkommen till Fulköping");




//        Scanner scan  = new Scanner(System.in);
//
//
//            System.out.println("Välkommen till Fulköpings bibliotek!");
//            System.out.println("Vill du 1. Logga in eller 2. Skaffa ett bibliotekskort? ");
//
//            int choice = scan.nextInt();
//
//            if (choice == 1) {
//                System.out.println("Logga in. Ange namn och lösenord: ");
//                System.out.println("Namn: ");
//                String name = scan.nextLine();
//
////                if (name == existerar i databasen) {
////                    System.out.println("Lösenord: ");
////                    String password = scan.nextLine();
////
////                    if (password == matchar till namnet) {
////                        // så loggas man in
////                    }
////
////                } else {
////                    System.out.println("Vi kan tyvärr inte hitta någon med det namnet. ");
////                    // och så "börja om från vill du 1 eller 2?"
////
////                }
//
//
//
//
//                // mata in namn
//                // mata in lösenord
//                // kontrollera med databasen så att
//                // det finns någon med detta namn
//                // och så att namnet överensstämmer med lösenord
//
//            } else if (choice == 2) {
//                System.out.println("Skaffa nytt bibliotekskort");
//
//                // scanna in namn, epost, lösenord
//                // spara denna data i databasen
//
//            } else {
//                System.out.println("Ogiltigt val. Välj 1 eller 2");
//            }
//
//
//            /*
//        try {
//            Connection conn = LibraryDB.getConnection();
//            Statement stmt = conn.createStatement();
//            ResultSet result = stmt.executeQuery("SHOW TABLES");
//
//
//
//            while (result.next()) {
//                System.out.println(result.getString("Tables_in_Library"));
//            }
//
//            String sql = "INSERT INTO LibraryCard (";
//            sql += "name, email, password";
//            sql += ") VALUES (";
//            sql += "'Matilda Witting', ";
//            sql += "'matildawitting@gmail.com', ";
//            sql += "'password'";
//            sql += "); ";
//
////            String sql = "INSERT INTO LibraryCard (";
////            sql += "name, email, password";
////            sql += ") VALUES (";
////            sql += "'Matilda Witting', ";
////            sql += "'matildawitting@gmail.com', ";
////            sql += "'password'";
////            sql += "); ";
//
//            System.out.println(sql);
//            int changeRows = stmt.executeUpdate(sql);
//            System.out.println("We changed " + changeRows + " rows in database");
//
//
//            String sqlT = "INSERT INTO Shelf (";
//            sqlT += "title, author, typeOfMedia, status";
//            sqlT += ") VALUES (";
//            sqlT += "'Java For Dummies', ";
//            sqlT += "'Barry Burd PhD', ";
//            sqlT += "'Book', ";
//            sqlT += "'Available' ";
//            sqlT += ") ";
//
//            System.out.println(sqlT);
//            int changedRows = stmt.executeUpdate(sqlT);
//            System.out.println("We changed " + changedRows + " rows in database");
//
//
//
//        } catch (SQLException e) {
//            SQLExceptionPrint(e, true);
//        }
//
////        public void createPokemon (String name, String type, String gender) {
////            String url = "jdbc:mysql:localhost:3306/PokedexDB";
////            String user = "root";
////            String password = "rootpassword";
////
////            String sql = "INSERT INTO Pokemon (name, type, gender) " +
////                    "VALUES (?, ?, ?) ";
////
////            try (Connection conn = DriverManager.getConnection(url, user, password);
////                 PreparedStatement stmt = conn.prepareStatement(sql)) {
////
////                stmt.setString(1, name);
////                stmt.setString(2, type);
////                stmt.setString(3, gender);
////
////                int changedRows = stmt.executeUpdate();
////                System.out.println("we changed " + changedRows + " rows in database");
////
////
////            } catch (SQLException e) {
////                SQLExceptionPrint(e, true);
////            }
//
//
//             */
//
        scan.close();

    }
}  