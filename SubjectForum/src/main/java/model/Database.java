package model;

import java.sql.*;

import java.util.*;
import model.Post;


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
    public static void InsertQueryForumPost(Post post)
    {
        int u_id = 0, c_id = 0;
        try {
            ResultSet r = Query("SELECT ID FROM users WHERE Username = '" + post.getAuthor() + "'");

            r.first();
            u_id = r.getInt("ID");
            ResultSet r2 = Query("SELECT ID FROM classes WHERE Code = '" + post.getSubject() +"'");
            r2.first();
            c_id = r2.getInt("ID");

            String title = post.getTitle();
            String text = post.getContent();
            String query = " Insert into forum_post (title , text, User_id, class_id)" + "values (?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, title);
            pstmt.setString(2, text);
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
    public static void InsertQueryReply(Reply reply)
    {
        try {
            ResultSet r = Query("SELECT ID FROM users WHERE Username = '" + reply.getUser() + "'");
            int u_id = 0, c_id = 0;
            while(r.next())
            {
                u_id = r.getInt("ID");
            }
            String query = " Insert into forum_post (Title , Text, User_id, post_id)" + "values (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, u_id);
            pstmt.setString(2, reply.getContent());
            pstmt.setInt(3, reply.getPostID());

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
    public static ArrayList<String> QueryGetPost(String adat, String classId)
    {
        ArrayList<String> list = new ArrayList<>();
        try {

            ResultSet r = Query("SELECT "+ adat +" FROM forum_post WHERE class_id = '" + classId +"'");
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

    public static ArrayList<Post> GetListofPost(String className)
    {
        ArrayList<Post> list = new ArrayList<Post>();
        try
        {
            int cid = 0;
            ResultSet cclass = Query("SELECT ID FROM classes WHERE Name = '" + className +"'");
            while(cclass.next())
            {
                cid = cclass.getInt("ID");
            }
            ResultSet r = Query("SELECT * FROM forum_post WHERE Class_id = " + cid);
            while(r.next())
            {
                ResultSet userNameSet = Query("SELECT Username FROM users WHERE ID = " + r.getInt("User_id"));
                String userName = "";
                while(userNameSet.next())
                {
                    userName = userNameSet.getString("Username");
                }
                Post p = new Post();
                p.setAuthor(userName);
                p.setContent(r.getString("Text"));
                p.setSubject(className);
                p.setTitle(r.getString("Title"));
                list.add(p);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<Reply> GetListofReply(Post post)
    {
        ArrayList<Reply> list = new ArrayList<Reply>();
        try
        {
            ResultSet r = Query("SELECT * FROM replies WHERE Post_id = " + post.getID());
            while(r.next())
            {
                Reply p = new Reply();
                p.setId(r.getInt("ID"));
                p.setContent(r.getString("reply"));
                ResultSet userNameSet = Query("SELECT Username FROM users WHERE ID = " + r.getInt("User_id"));
                String userName = "";
                while(userNameSet.next())
                {
                    userName = userNameSet.getString("Username");
                }
                p.setUser(userName);
                p.setPostID(r.getInt("Post_id"));
                list.add(p);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }



    //Ez visszaadja a listát ami tartalmazza a Postokat és a hozzátartozó Replyokat
    //Mappel van megoldva

    public static ArrayList<Map<Post,ArrayList<Reply>>> QueryGetEverything(String className)
    {
        ArrayList<Map<Post,ArrayList<Reply>>> array = new ArrayList<Map<Post,ArrayList<Reply>>>();
        try
        {
            ArrayList<Post> postarray = GetListofPost(className);
            for (Post post : postarray)
            {
                Map<Post,ArrayList<Reply>> mapp = new HashMap<Post, ArrayList<Reply>>();
                ArrayList<Reply> replyarray = GetListofReply(post);
                mapp.put(post, replyarray);
                array.add(mapp);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return array;
    }

}
