package org.example;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class BorrowHistory {

    protected int id = -1;
    protected int userId;
    protected int bookId;
    protected Date borrowDate;
    protected Date returnDate = null;

    public BorrowHistory() {}

    public BorrowHistory(int id) throws SQLException {
        String sql = "SELECT * FROM BorrowHistory WHERE id = ?";
        PreparedStatement pst = LibraryDB.getConnection().prepareStatement(sql);
        pst.setInt(1, id);

        init(pst);


    }

    public BorrowHistory(int userId, int bookId) throws SQLException {
        String sql = "SELECT * FROM BorrowHistory WHERE userId = ? AND bookId = ?";
        PreparedStatement pst = LibraryDB.getConnection().prepareStatement(sql);

        pst.setInt(1, userId);
        pst.setInt(2, bookId);

        init(pst);

    }

    private void init(PreparedStatement pst) throws SQLException {
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            id = rs.getInt("id");
            userId = rs.getInt("userId");
            bookId = rs.getInt("bookId");
            borrowDate = rs.getDate("borrowDate");
            returnDate = rs.getDate("returnDate");

        }
    }

    // göra det möjligt att spara
    public int save() throws SQLException {
        String sql;

        // för att uppdatera
        if (id > 0) {
            sql = "UPDATE BorrowHistory SET userId = ?, borrowDate = ?, returnDate = ? WHERE id = ?";

        } else {
            // för att göra en ny
            sql = "INSERT INTO BorrowHistory (userId, bookId, borrowDate, returnDate) VALUES (?, ?, ?, ?)";
        }

        PreparedStatement pst = LibraryDB.getConnection().
                prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        pst.setInt(1, userId);
        pst.setInt(2, bookId);
        pst.setDate(3, new java.sql.Date(borrowDate.getTime()));

        if (returnDate != null) {
            pst.setDate(4, new java.sql.Date(returnDate.getTime()));

        } else {
            pst.setNull(4, java.sql.Types.DATE);
        }

        if (id > 0) {
            pst.setInt(5, id);
        }

        int rowsAffected = pst.executeUpdate();

        if (id < 0) {
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        return rowsAffected;

    }

    // för att göra det möjligt att radera
    public void delete() throws SQLException {
        if (id > 0) {
            String sql = "DELETE FROM BorrowHistory WHERE id = ?";
            PreparedStatement pst = LibraryDB.getConnection().prepareStatement(sql);

            pst.setInt(1, id);
            pst.executeUpdate();

        }
    }



    // Setters och getters
    public int getId(){
        return id;
    }
    // setter kanske inte behövs här...
    // ska ju inte ändra id
    public void setId (){
        this.id = id;
    }

    public int userId() {
        return userId;
    }
    public void setUserId() {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }
    public void setBookId() {
        this.bookId = bookId;
    }

    public Date getBorrowDate(){
        return borrowDate;
    }
    public void setBorrowDate() {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }
    public void setReturnDate() {
        this.returnDate = returnDate;
    }




}
