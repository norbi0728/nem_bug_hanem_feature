package model;

import java.sql.*;

public class Database {

    private static Connection con;
    private static Statement stmt;

    public Database()
    {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String url, user, pwd;
            url = "jdbc:mysql://sql2.freemysqlhosting.net:3306/sql2332780";
            user = "sql2332780";
            pwd = "dZ4%zW3*";
            con = DriverManager.getConnection(url, user, pwd);
            System.out.println("Connection: " + con);
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //Ez egy lekérdezés eredményét adja vissza a ResultSetbe.
    //Ha ebből az adatot szeretnéd lekérdezni a szintaxis: result.getString("ID"); ahoé au ID az adatbázisban lévő oszlop neve
    public static ResultSet Query(String query)
    {
        ResultSet result = null;
        try {
            result = stmt.executeQuery(query);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //Selectben található eredméynből megnézi hogy található-e az adott String
    public static boolean ExistQuery(String query, String keres, String keresett)
    {
        ResultSet result = null;
        boolean valt = false;
        try
        {
            result = stmt.executeQuery(query);
            while(result.next())
            {
                if(result.getString(keresett).equals(keres))
                {
                    valt = true;
                    break;
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return valt;
    }


    //Users táblába való beillesztés
    public static void InsertQueryUsers(String userName, String Password, String firstName , String lastName)
    {
        try {
            String query = " Insert into users (Username , Password, Firstname, Lastname)" + "values (?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, userName);
            pstmt.setString(2, Password);
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);

            pstmt.execute();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }

    public static boolean LoginQuery(String userName, String Password)
    {
        boolean valt = false;
        try {
            ResultSet r = Query("SELECT Username FROM users");
            while (r.next()) {
                if (r.getString("Username").equals(userName))
                {
                    ResultSet rpwd = Query("Select Password FROM users WHERE Username = '"+ userName+"'");
                    while(rpwd.next()) {
                        if (rpwd.getString("Password").equals(Password)) {
                            valt = true;
                            break;
                        }
                    }
                    break;
                }

            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return valt;
    }
}