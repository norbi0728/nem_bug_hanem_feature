package model;

import java.sql.*;

import java.util.*;


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
        ResultSet result;
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


    //Not Tested Yet
    public static void InsertQueryForumPost(String Title, String Text, String userName , String classCode)
    {
        try {
            ResultSet r = Query("SELECT ID FROM users WHERE Username = '" + userName + "'");
            ResultSet r2 = Query("SELECT ID FROM classes WHERE Code = '" + classCode +"'");
            int u_id = 0, c_id = 0;
            while(r.next())
            {
                u_id = r.getInt("ID");
            }
            while(r2.next())
            {
                c_id = r2.getInt("ID");
            }
            String query = " Insert into forum_post (Title , Text, User_id, class_id)" + "values (?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, Title);
            pstmt.setString(2, Text);
            pstmt.setInt(3, u_id);
            pstmt.setInt(4, c_id);

            pstmt.execute();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }

    //Not Tested yet
    public static void InsertQueryReply(String Reply, String userName , int post_id)
    {
        try {
            ResultSet r = Query("SELECT ID FROM users WHERE Username = '" + userName + "'");
            int u_id = 0, c_id = 0;
            while(r.next())
            {
                u_id = r.getInt("ID");
            }
            String query = " Insert into forum_post (Title , Text, User_id, post_id)" + "values (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, u_id);
            pstmt.setString(2, Reply);
            pstmt.setInt(3, post_id);

            pstmt.execute();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }

    //lekéri az összes classot egy listába
    //Kéri hogy melyik oszlopbol kell adat
    //Lehet Code = Tárgy kódjával, Name = Tárgy neve, ID = ID, Credit = Creditszám
    // ez az összes class-t kéri le
    public static ArrayList<String> QueryGetClassName(String adat)
    {
        ArrayList<String> list = new ArrayList<>();
        try {
            ResultSet r = Query("SELECT "+ adat  +" FROM classes");
            while (r.next()) {
                list.add(r.getString(adat));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return list;
    }

    //lekéri az összes Postot egy listába a megadott classon belül
    //Kéri hogy melyik oszlopbol kell adat
    //Lehet ID = ID, Title = cím, Text = post üzenete, User_id = postoló id-ja, Class_id = posthoz tartozó óra id-ja
    public static ArrayList<String> QueryGetPost(String adat, String className)
    {
        ArrayList<String> list = new ArrayList<>();
        try {

            ResultSet r = Query("SELECT "+ adat +" FROM forum_post WHERE class_id = '" + className +"'");
            while (r.next()) {
                list.add(r.getString(adat));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return list;
    }


    //lekéri az összes Reply-t egy listába a megadott poston belül
    //Kéri hogy melyik oszlopbol kell adat
    //Lehet ID = ID,  User_id = válaszoló id-ja, Post_id = Melyik posthoz tartozik, reply = az üzenet tartalma
    public static ArrayList<String> QueryGetReply(String adat, int id)
    {
        ArrayList<String> list = new ArrayList<>();
        try {
            ResultSet r = Query("SELECT "+ adat +" FROM reply WHERE Post_id = '" + id +"'");
            while (r.next()) {
                list.add(r.getString(adat));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return list;
    }


}
