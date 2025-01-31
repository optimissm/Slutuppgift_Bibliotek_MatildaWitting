package org.example;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// okej.
// detta är mitt bibliotekskort där vi har våra olika användare
// här har vi getters och setters så att vi kan
// hämta och lämna namn, epost, användarnamn och lösenord
// till vår main
// men nu behöver jag då koppla in klassen till main
public class LibraryCard {

    protected int id = -1;
    protected String name = "";
    protected String username = "";
    protected String email = "";
    protected String password = "";

    // en "tom" constructor för att kunna skapa nytt objekt from scratch
    public LibraryCard() {}

    // constructor för att komma åt redan skapade objekt med matchande id
    // innehåller också ett SQLException om i fall att
    // något får snett med databasanrop
    public LibraryCard(int id) throws SQLException {
        // här hämtar vi kolumnerna från LibraryCard i databasen
        // String sql = "SELECT id, name, username, email, password FROM libraryCard WHERE id = ? ";
        String sql = "SELECT * FROM libraryCard WHERE id = ?";
        PreparedStatement pst = LibraryDB.getConnection().prepareStatement(sql);

        pst.setInt(1, id);

        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            this.id = rs.getInt("id");
            this.name = rs.getString("name");
            this.username = rs.getString("username");
            this.email = rs.getString("email");
            this.password = rs.getString("password");

        }

    }


    // constructor som hämtar objekt via namn
    public LibraryCard (String name) throws SQLException {
        // String sql = "SELECT id, name, username, email, password FROM libraryCard where name = ? ";
        String sql = "SELECT * FROM libraryCard WHERE name = ?";
        PreparedStatement pst = LibraryDB.getConnection().prepareStatement(sql);

        pst.setString(1, name);

        init(pst);

    }

    // en metod för att köra och bearbeta SQL frågan med init
    private void init(PreparedStatement pst) throws SQLException {
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            id = rs.getInt("id");
            name = rs.getString("name");
            username = rs.getString("username");
            email = rs.getString("email");
            password = rs.getString("password");

        }
    }

    // nu sparar vi informationen som vi vill skicka in till databasen
    public int save() throws SQLException {
        // såhär ser uppbyggnaden av frågan ut i SQL
        String sql = "name = ?, " +
                "username = ?, " +
                "email = ?, " +
                "password = ?";

        // frågan lämnar plats till uppdatering
        if (id > 0) {
            sql = "UPDATE libraryCard SET" + sql + " WHERE id = ?";

        } else {
            // eller ett helt nytt objekt
            sql = "INSERT INTO libraryCard" + sql;
        }

       PreparedStatement pst = LibraryDB.getConnection().prepareStatement(sql,
               PreparedStatement.RETURN_GENERATED_KEYS);

        pst.setString(1, name);
        pst.setString(2, username);
        pst.setString(3, email);
        pst.setString(4, password);

        if (id > 0) {
            pst.setInt(4, id);

        }

        // behöver jag denna på flera ställen också...?
        int numberOfChangedRows = pst.executeUpdate();

        if (id < 0) {
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        return 0;

    }

    // samma här igen
    // de enda som behövs är väl de blåa?
    public int getId() {
        return id;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername(){
        return username;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }

    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return password;
    }




}
