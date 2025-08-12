package org.example;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


// detta är då klassen för all media
public class Shelf {
    // so basically ska jag typ ish kopiera det jag gjorde i LibraryCard
    protected int id = -1;
    protected String title = "";
    protected String author = "";
    protected String typeOfMedia = "";
    protected boolean isBorrowed = false;
    protected Long borrowedBy = null;
    protected Date borrowedUntil = null;

    public Shelf(){}

    // konstruktor för att söka efter objekt med id
    // (Vilket jag undrar om det behövs...?)
    // ev förbättring här skulle kunna vara att lägga till ett
    // "Inga resultat med det ID:t"
    public Shelf(int id) throws SQLException {
        String sql = "SELECT * FROM shelf WHERE id = ?";
        PreparedStatement pst = LibraryDB.getConnection().prepareStatement(sql);

        pst.setInt(1, id);

        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            this.id = rs.getInt("id");
            this.title = rs.getString("title");
            this.author = rs.getString("author");
            this.typeOfMedia = rs.getString("typeOfMedia");
            this.isBorrowed = rs.getBoolean("isBorrowed");
            this.borrowedBy = rs.getLong("borrowedBy");
            this.borrowedUntil = rs.getDate("borrowedUntil");

        }
    }



    // sök efter titel
    // Förbättra ev att returnera alla titlar med samma namn inte bara en
    public Shelf (String title) throws SQLException {
        String sql = "SELECT * FROM Shelf WHERE title = ? ";
        PreparedStatement pst = LibraryDB.getConnection().prepareStatement(sql);

        pst.setString(1, title);

        initTitle(pst);

    }
    private void initTitle(PreparedStatement pst) throws SQLException {
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            this.id = rs.getInt("id");
            this.title = rs.getString("title");
            this.author = rs.getString("author");
            this.typeOfMedia = rs.getString("typeOfMedia");
            this.isBorrowed = rs.getBoolean("isBorrowed");

            long borrowedByValue = rs.getLong("borrowedBy");
            this.borrowedBy = rs.wasNull() ? null : borrowedByValue;

            this.borrowedUntil = rs.getDate("borrowedUntil");

        } else {
            System.out.println("Ingen matchning hittades i Shelf.");
        }
    }


    // efter författare
    // och om författare har mer än ett resultat
    // den körs bara om den författare du sökt på finns
//    public Shelf (boolean isAuthor, String author) throws SQLException {
//        if(!isAuthor) return;
//
//        // ta ut alla från Shelf där author är det du sökte på
//        String sql = "SELECT * FROM Shelf WHERE author = ? ";
//        PreparedStatement pst = LibraryDB.getConnection().prepareStatement(sql);
//
//        pst.setString(1, author);
//
//        init(pst);
//
//    }
//    // denna hör till för att få ut media via författare
//    // om författaren har skrivit mer än en bok tror jag...
//    private void init(PreparedStatement pst) throws SQLException {
//        ResultSet rs = pst.executeQuery();
//
//        // vi gör en lista av Shelf så att vi kan få mer än en titel
//        // om författaren du söker på har mer än en bok i vårt bibliotek
//        List<Shelf> mediaShelves = new ArrayList<Shelf>();
//
//        while (rs.next()) {
//            Shelf shelfMedia = new Shelf();
//
//            shelfMedia.id = rs.getInt("id");
//            shelfMedia.title = rs.getString("title");
//            shelfMedia.author = rs.getString("author");
//            shelfMedia.typeOfMedia = rs.getString("typeOfMedia");
//            shelfMedia.isBorrowed = rs.getBoolean("isBorrowed");
//            shelfMedia.borrowedBy = rs.getLong("borrowedBy");
//            shelfMedia.borrowedUntil = rs.getDate("borrowedUntil");
//
//            mediaShelves.add(shelfMedia);
//
//        }
//
//        if (!mediaShelves.isEmpty()) {
//            System.out.println("Titlar som matchar din sökning: ");
//            for (Shelf shelf : mediaShelves) {
//                System.out.println(shelf.title + ", " + shelf.author);
//            }
//        } else {
//            System.out.println("Inga matchningar");
//        }
//
//    }

    // testar en ny för author ist för den över
    public static List<Shelf> searchByAuthor(String author) throws SQLException {
        String sql = "SELECT * FROM SHELF WHERE author = ? ";
        PreparedStatement pst = LibraryDB.getConnection().prepareStatement(sql);
        pst.setString(1, author);

        ResultSet rs = pst.executeQuery();
        List<Shelf> result = new ArrayList<>();

        while (rs.next()) {
            Shelf shelfMedia = new Shelf();
            shelfMedia.id = rs.getInt("id");
            shelfMedia.title = rs.getString("title");
            shelfMedia.author = rs.getString("author");
            shelfMedia.typeOfMedia = rs.getString("typeOfMedia");
            shelfMedia.isBorrowed = rs.getBoolean("isBorrowed");
            shelfMedia.borrowedBy = rs.getLong("borrowedBy");
            shelfMedia.borrowedUntil = rs.getDate("borrowedUntil");
            result.add(shelfMedia);
        }

        return result;
    }



    // söka efter mediatyp och precis som med author så vill jag
    // ha möjligheten att få ut mer än en titel åt gången
    public static List<Shelf> getMediaType(String typeOfMedia) throws SQLException {
        String sql = "SELECT * FROM Shelf WHERE typeOfMedia = ?";
        PreparedStatement pst = LibraryDB.getConnection().prepareStatement(sql);

        pst.setString(1, typeOfMedia);
        ResultSet rs = pst.executeQuery();

        // så gör en lista här också för att få ut mer än en
        List<Shelf> shelves = new ArrayList<Shelf>();

        while (rs.next()) {
            Shelf shelf = new Shelf();

            shelf.id = rs.getInt("id");
            shelf.title = rs.getString("title");
            shelf.author = rs.getString("author");
            shelf.typeOfMedia = rs.getString("typeOfMedia");
            shelf.isBorrowed = rs.getBoolean("isBorrowed");
            shelf.borrowedBy = rs.getLong("borrowedBy");
            shelf.borrowedUntil = rs.getDate("borrowedUntil");

            shelves.add(shelf);

        }

        return shelves;
    }


    // funktion för att kunna spara eller uppdatera objekt i Shelf
    // men osäker på om jag behöver ha med denna
    // då Elsbieta kan göra detta direkt i databasen
    public int save() throws SQLException {
        // såhär ser uppbyggnaden av frågan ut i SQL
        String sql = "title = ?," +
                "author = ?," +
                "typeOfMedia = ?," +
                "isBorrowed= ?," +
                "borrowedBy= ?," +
                "borrowedUntil= ?";

        // frågan lämnar plats till uppdatering
        // Osäker på hur jag behöver det här
        // då enda sättet att uppdatera ska vara via databasen...
        if (id > 0) {
            sql = "UPDATE Shelf SET " + sql + " WHERE id = ?";

        } else {
            // eller ett helt nytt objekt
            // samma fråga här... Behöver jag denna...?
            sql = "INSERT INTO Shelf " +
                    "(title, author, typeOfMedia, " +
                    "isBorrowed, borrowedBy, borrowedUntil) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            // alla frågetecken representerar den nya infpn du vill lägga in
            // för varje fråga
        }

        PreparedStatement pst = LibraryDB.getConnection().prepareStatement(sql,
                PreparedStatement.RETURN_GENERATED_KEYS);

        pst.setString(1, title);
        pst.setString(2, author);
        pst.setString(3, typeOfMedia);
        pst.setBoolean(4, isBorrowed);
        pst.setLong(5, borrowedBy);
        pst.setDate(6, (java.sql.Date) borrowedUntil);


        if (id > 0) {
            pst.setInt(7, id);

        }

        int numberOfChangedRows = pst.executeUpdate();

        // Hämta det nya idt vid ny post
        if (id < 0) {
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        return numberOfChangedRows;

    }


    // getters och setters
    // men återigen så ska ju objekt i Shelf inte kunna ändras av användare
    // det ska ju Elsbieta stå för i databasen
    // men getters behöver vi iallafall för att få ut objekten
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getTypeOfMedia() {
        return typeOfMedia;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public Long getBorrowedBy() {
        return borrowedBy;
    }

    public Date getBorrowedUntil() {
        return borrowedUntil;
    }

}
