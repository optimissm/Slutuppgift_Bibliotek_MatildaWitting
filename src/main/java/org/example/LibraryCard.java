package org.example;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// okej.
// detta är mitt bibliotekskort där vi har våra olika användare
// här har vi getters och setters så att vi kan hämta och lämna namn, epost och lösenord
// till vår main
// men nu ehöver jag då koppla in klassen till main
public class LibraryCard {

    protected int id = -1;
    protected String name = "";
    protected String email = "";
    protected String password = "";

    public LibraryCard() {}

    public LibraryCard(int id) throws SQLException {
        String sql = "Select id, name, email, password FROM libraryCard where id = ? ";
        PreparedStatement pst = LibraryDB.getConnection().prepareStatement(sql);

        pst.setInt(1, id);

        // vad det verkar som så gjorde det ingen skillnad
        // att lägga in denna i smeten...
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            this.id = rs.getInt("id");
            this.name = rs.getString("name");
            this.email = rs.getString("email");
            this.password = rs.getString("password");

        }

    }



    public LibraryCard (String name) throws SQLException {
        String sql = "SELECT id, name, email, password FROM libraryCard where name = ? ";
        PreparedStatement pst = LibraryDB.getConnection().prepareStatement(sql);

        pst.setString(1, name);

        init(pst);

    }

    private void init(PreparedStatement pst) throws SQLException {
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            id = rs.getInt("id");
            name = rs.getString("name");
            email = rs.getString("email");
            password = rs.getString("password");

        }
    }

    public int save() throws SQLException {
        String sql = "name = ?, " +
                "email = ?, " +
                "password = ?";

        if (id > 0) {
            sql = "UPDATE libraryCard SET" + sql + " WHERE id = ?";

        } else {
            sql = "INSERT INTO libraryCard" + sql;
        }

       PreparedStatement pst = LibraryDB.getConnection().prepareStatement(sql,
               PreparedStatement.RETURN_GENERATED_KEYS);

        pst.setString(1, name);
        pst.setString(2, email);
        pst.setString(3, password);

        if (id > 0) {
            pst.setInt(4, id);

        }

        int numberOfChangedRows = pst.executeUpdate();

        if (id < 0) {
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        return 0;

    }


    public int getId() {
        return id;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
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
