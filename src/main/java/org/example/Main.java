package org.example;

import java.util.Date;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

import static org.example.LibraryDB.SQLExceptionPrint;

// det är denna som är komplett

public class Main {
    public static void main(String[] args) {

        // så att vi kan skanna in ny info från användaren
        Scanner scan = new Scanner(System.in);

        // hittils har ingen loggat in
        int loggedInId = -1;

        // kopplar in LibraryCard, Shelf och BorrowHistory
        LibraryCard card = new LibraryCard();
        // fast dessa är inte aktiva...?
        // Shelf shelf = new Shelf();
        // BorrowHistory history = new BorrowHistory();

        // och startar en koppling till min databas som vi använder throughout
        Connection conn = LibraryDB.getConnection();

        // Här börjar själva appen
        // där vi börjar med att antingen logga in
        // eller skaffa nytt bibliotekskort
        System.out.println("Välkommen till Fulköpings bibliotek");
        System.out.println("1. Skaffa bibliotekskort");
        System.out.println("2. Logga in");

        int select = scan.nextInt();
        scan.nextLine();

        // Skaffa bibliotekskort/skapa konto
        if (select == 1) {
            System.out.println("Jag vill skaffa ett bibliotekskort");

            System.out.println("Då behöver vi dina uppgifter.");
            System.out.println("Vad heter du? ");
            String name = scan.nextLine();

            System.out.println("Vad är din email? ");
            String email = scan.nextLine();

            System.out.println("Välj ett användarnamn: ");
            String user = scan.nextLine();

            System.out.println("Välj lösenord: ");
            String password = scan.nextLine();

            System.out.println("Detta är dina uppgifter: " +
                    "\n Namn: " + name +
                    "\n Email: " + email +
                    "\n Användarnamn: " + user +
                    "\n Password: " + password);

            card.setName(name);
            card.setEmail(email);
            card.setUsername(user);
            card.setPassword(password);

            try {
                Statement stmtLC = conn.createStatement();

                // sql metoden för att få in användaren i databasen
                // med sql syntax
                String sql = "INSERT INTO LibraryCard (";
                sql += "name, email, username, password";
                sql += ") VALUES ('";
                sql += card.getName() + "','";
                sql += card.getEmail() + "','";
                sql += card.getUsername() + "','";
                sql += card.getPassword() + "')";

                // utför ändringen
                stmtLC.executeUpdate(sql);

                System.out.println("Välkommen till Fulköping " + name);

                // efter att ha skaffat kort så går vi vidare till
                // låna/lämna tillbaka böcker

            } catch (SQLException e) {
                SQLExceptionPrint(e);
            }

        // Logga in
        } else if (select == 2) {
            System.out.println("Logga in");

            System.out.println("Ange ditt användarnamn: ");
            String user = scan.nextLine();

            System.out.println("Ditt lösenord: ");
            String password = scan.nextLine();

            // funktion för att spara den inloggade användaren
            // så hen kan låna objekt senare
            loggedInId = login(user, password);

            // med det användarnamn och lösenord som du matat in
            // körs login funktionen (längre ner)
            // för att se så att användarnamn finns
            // och matchar till inmatat lösen
            if (loggedInId != -1) {
                System.out.println("Välkommen in i vårt bibliotek!");

                System.out.println("Här är alla tillgängliga titlar. ");
                try {
                    Statement stmtShelf = conn.createStatement();

                    // här kommer du in i biblioteket och får då se all tillgänglig
                    // media på en och samma gång
                    // (ÄNDRAT DENNA NYLIGEN => SE OM DEN FUNKAR)
                    String queryShelf = "SELECT * FROM Shelf WHERE isBorrowed = false";
                    ResultSet resultShelf = stmtShelf.executeQuery(queryShelf);

                    while (resultShelf.next()) {
                        // kanske inte behöver se id heller...
                        // int id = resultShelf.getInt("id");
                        String title = resultShelf.getString("title");
                        String author = resultShelf.getString("author");
                        String typeOfMedia = resultShelf.getString("typeOfMedia");

                        boolean isBorrowed = resultShelf.getBoolean("isBorrowed");

                        // tänker att man inte behöver få ut info om vem som har boken atm
//                    long borrowedBy = resultShelf.getLong("borrowedBy");
//                    Date borrowedUntil = resultShelf.getDate("borrowedUntil");

                        System.out.println(
                                "Title: " + title +
                                        ", Author: " + author +
                                        ", Type Of Media: " + typeOfMedia
                                        // denna såg inte snygg ut att ha med
                                        // och nu är ändå litstan ändrad till "tillgängliga"
                                        // + ", This is unavailable: " + isBorrowed
                        );
                    }
                }  catch (SQLException e) {
                    SQLExceptionPrint(e);
                }

            } else {
                System.out.println("Fel användarnamn eller lösenord");
                return;
            }

            // Här slutar login

        }

        System.out.println("Vad vill du göra nu? ");
        System.out.println("1. Låna böcker" +
                "\n2. Lämna tillbaka böcker" +
                "\n3. Upptadera profilinfo" +
                "\n4. Logga ut");

        int choice = scan.nextInt();

        // Låna böcker
        if (choice == 1) {
            // här är då hur man vill sortera media
            // för att hitta det du vill låna
            System.out.println("Vill du: ");
            System.out.println("1. Låna bok efter titel" +
                    "\n2. Efter författare" +
                    "\n3. Sortera efter vilken typ av media");

            int sort = scan.nextInt();

            // Låna bok efter titel
            if (sort == 1) {
                System.out.println("Vad är titeln på boken du letar efter? ");
                // lägger in en extra scan.nextLine för att neutralisera scan.nextInt
                scan.nextLine();
                // så att vi kan söka på titel
                String findTitle = scan.nextLine();

                // denna metoden hittar vi i Shelf
                try {
                    Shelf shelfTitle = new Shelf(findTitle);

                    // om den är utlånad
                    if (shelfTitle.isBorrowed()) {
                        System.out.println(shelfTitle.getTitle() + "Är tyvärr utlånad...");
                    } else {
                        System.out.println("Vill du låna denna? (Ja/Nej)");
                        String loan = scan.nextLine();

                        if (loan.equalsIgnoreCase("Ja")) {

                            System.out.println(shelfTitle.getId());

                            // så vill vi använda oss av vår "låna media funktion"
                            // och då behöver vi variablarna för
                            // vem som är inloggad så vi kan lägga boken på deras kort
                            // och vilken typ av media det är så vi vet hur länge den kan lånas
                            borrowMedia(loggedInId, shelfTitle.getId(), shelfTitle.getTypeOfMedia());
                            System.out.println("Nu har du lånat " + shelfTitle.getTitle() + " på ditt kort!");

                        }
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                // Här slutar sök efter titel

            // Sök på författare
            } else if (sort == 2) {
                // denna har jag lite problem med fortfarande...
                // Men for now får vi nöja oss med att låna bok efter titel funkar...

                System.out.println("Vad heter författaren du letar efter? ");

                scan.nextLine();
                String findAuthor = scan.nextLine();

                // Shelf.getByAuthor(findAuthor);
                // Shelf shelfAuthor = new Shelf(findAuthor);

                // SELECT * FROM Shelf WHERE author = ?

                System.out.println("Vill du låna en bok av denna författaren? (Ja/Nej)");
                scan.nextLine();
                String YN = scan.nextLine();

                if (YN.equalsIgnoreCase("Ja")) {
                    System.out.println("Vad är titeln på objektet du vill låna? ");
                    String auTitle = scan.nextLine();

                    try {
                        Shelf shelfTitle = new Shelf(auTitle);

                        // om den är utlånad
                        if (shelfTitle.isBorrowed()) {
                            System.out.println(shelfTitle.getTitle() + "Är tyvärr utlånad...");
                        } else {
                            System.out.println("Vill du låna denna? (Ja/Nej)");
                            String loan = scan.nextLine();

                            if (loan.equalsIgnoreCase("Ja")) {

                                borrowMedia(loggedInId, shelfTitle.getId(), shelfTitle.getTypeOfMedia());
                                System.out.println("Nu har du " + shelfTitle.getTitle() + "på ditt kort!");

                            }
                        }

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    return;
                }

            // Sortera efter media
            } else if (sort == 3) {
                System.out.println("Vill du söka efter böcker, filmer eller tidningar " +
                        "\n(Book/Movie/Magazine)");

                scan.nextLine();
                String mediaType = scan.nextLine();

                try {
                    List<Shelf> sortedShelf = Shelf.getMediaType(mediaType);

                    if (!sortedShelf.isEmpty()) {
                        // ska få ut böcker/filmer/tidningar
                        System.out.println("Du sökte på: " + mediaType);

                        for (Shelf shelfMedia : sortedShelf) {
                            System.out.println("Titel: " + shelfMedia.title +
                                    ", Författare: " + shelfMedia.author);
                        }

                        System.out.println("Hittade du någonting du är intresserad av att låna? (Ja/Nej) ");
                        scan.nextLine();
                        String JN = scan.nextLine();

                        if (JN.equalsIgnoreCase("Ja")) {

                            System.out.println("Vad är titeln på den du vill låna?");
                            String mtTitle = scan.nextLine();

                            try {
                                Shelf shelfTitle = new Shelf(mtTitle);

                                // om den är utlånad
                                if (shelfTitle.isBorrowed()) {
                                    System.out.println(shelfTitle.getTitle() + "Är tyvärr utlånad...");
                                } else {
                                    System.out.println("Vill du låna denna? (Ja/Nej)");
                                    String loan = scan.nextLine();

                                    if (loan.equalsIgnoreCase("Ja")) {

                                        borrowMedia(loggedInId, shelfTitle.getId(), shelfTitle.getTypeOfMedia());
                                        System.out.println("Nu har du " + shelfTitle.getTitle() + " på ditt kort!");

                                    }
                                }

                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }



                        }


                    } else {
                        System.out.println("Kontrollera att du skrivit in rätt mediatyp.");
                    }

                    // okej... Det konstiga är nu att
                    // efter att jag fått ut de titlar som stämmer in på vad jag sökte efter
                    // så får jag ut ytterligare en sträng...
                    // och sen ber den mig att repetera på vilket sätt jag vill söka efter
                    // objekt igen...

                    // men nu är nästa steg att lägga in en metod för att låna/lämna tillnaka

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            // Här slutar låna böcker

        // Lämna tillbaka böcker
        } else if (choice == 2) {
            System.out.println("Vilken titel vill du lämna tillbaka?");
            scan.nextLine();

            String returnTitle = scan.nextLine();

            try {
                Shelf shelfToReturn = new Shelf(returnTitle);

                if (!shelfToReturn.isBorrowed()) {
                    System.out.println("Denna titel är inte utlånad");
                } else if (shelfToReturn.getBorrowedBy() != loggedInId) {
                    System.out.println("Du har inte lånat denna titel");
                } else {
                    returnMedia(loggedInId, shelfToReturn.getId(), shelfToReturn.getTypeOfMedia());
                    System.out.println("Du har nu lämnat tillbaka: " + shelfToReturn.getTitle());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


        // Uppdatera profil
        } else if (choice == 3) {

        // Logga ut
        } else if (choice == 4) {

        }

        // stänger scanner när appen är slut
        scan.close();


    }

    // bara inloggningsmetoden
    private static int login(String user, String password) {

        // där vi tar ut användarnamn och lösenord för att logga in
        try {
            Connection conn = LibraryDB.getConnection();
            String queryLog = "SELECT * FROM LibraryCard " +
                    "WHERE username = ? " +
                    "AND password = ?";

            PreparedStatement psLog = conn.prepareStatement(queryLog);
            psLog.setString(1, user);
            psLog.setString(2, password);

            ResultSet rsLog = psLog.executeQuery();

            // om inloggningen lyckas så sparar vi id
            if (rsLog.next()) {
                return rsLog.getInt("id");
            }

        } catch (SQLException e) {
            SQLExceptionPrint(e);
        }

        // lyckas vi inte så behåller vi -1
        return -1;

    }

    // Låna media metoden
    private static void borrowMedia(int userId, int mediaId, String mediaType) throws SQLException {
        Connection conn = LibraryDB.getConnection();

        // är det en bok 30 dagar, annars 10
        int borrowDays = mediaType.equals("Book") ? 30 : 10;
        Date borrowDate = new Date();
//        Timestamp sqlBorrowDate = new Timestamp(borrowDate.getTime());
//        long returnTime = borrowDate.getTime();
        Date returnDate = new Date(borrowDate.getTime() + (borrowDays * 86400000L));

        // gör en ändring i Shelf så att median registreras som utlånad med sql syntax
        String updateShelf = "UPDATE Shelf SET " +
                "isBorrowed = ?, borrowedBy = ?, borrowedUntil = ? " +
                "WHERE id = ?";

        PreparedStatement pstShelf = conn.prepareStatement(updateShelf);
        pstShelf.setBoolean(1, true);
        pstShelf.setInt(2, userId);
        pstShelf.setDate(3, new java.sql.Date(returnDate.getTime()));
        pstShelf.setInt(4, mediaId);

        pstShelf.executeUpdate();


        // och så sätter vi in infon i BorrowHistory med SQL också
        String insertBorrow = "INSERT INTO BorrowHistory" +
                "(userId, bookId, borrowDate, returnDate)" +
                "VALUES (?, ?, ?, ?)";

        PreparedStatement pstBorrow = conn.prepareStatement(insertBorrow);
        pstBorrow.setInt(1, userId);
        pstBorrow.setInt(2, mediaId);
        pstBorrow.setDate(3, new java.sql.Date(borrowDate.getTime()));
        pstBorrow.setDate(4, new java.sql.Date(returnDate.getTime()));

        pstBorrow.executeUpdate();


    }

    // och för att lämna tillbaka
    private static void returnMedia (int userId, int mediaId, String mediaType) throws SQLException {
        Connection conn = LibraryDB.getConnection();

        String check =
                // "SELECT * FROM Shelf WHERE userId = ?, bookId = ?, returnDate IS NULL";
                "SELECT * FROM Shelf WHERE borrowedBy = ? AND borrowedUntil IS NOT NULL";

        PreparedStatement pstReturn = conn.prepareStatement(check);
        pstReturn.setInt(1, userId);
        pstReturn.setInt(2, mediaId);

        ResultSet rs = pstReturn.executeQuery();

        if (rs.next()) {
            String updateShelf = "UPDATE Shelf SET" +
                    "isBorrowed = ?, borrowedBy = NULL, borrowedUntil = NULL " +
                    "WHERE id = ?";

            PreparedStatement pstShelf = conn.prepareStatement(updateShelf);
            pstShelf.setBoolean(1, false);
            pstShelf.setInt(2, mediaId);

            pstShelf.executeUpdate();

            // System.out.println("Tack för att du lämnade tillbaka " + Shelf.getTitle);


        } else {
            System.out.println("Du har inte lånat denna, så kan därför inte heller lämna tillbaka den. ");
        }

    }

//    } catch (SQLException e) {
//        e.printStackTrace();
//    }

}