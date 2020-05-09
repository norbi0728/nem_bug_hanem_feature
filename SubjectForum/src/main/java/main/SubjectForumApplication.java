package main;

import controller.Controller;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Database;
import model.Post;

import javax.naming.ldap.Control;
import javax.xml.crypto.Data;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.util.*;


public class SubjectForumApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        primaryStage.setTitle("SubjectForum");
        primaryStage.setScene(new Scene(root, 744, 583));
        primaryStage.show();

        Database d = new Database();

        //Ez egy teszteset
        ArrayList<String> v = new ArrayList<String>();
        v = Database.QueryGetClassName("Name");
        for(int i = 0; i < v.size(); i++)
        {
            System.out.println(v.get(i));
        }
        //Database.InsertQueryUsers("EzegyUsername","EzegyJelszo","Minta","Péter");
        //System.out.println(Database.LoginQuery("EzegyUsername","EzegyJelszo"));
//        ResultSet rs = Database.Query("SELECT * FROM users WHERE USERNAME="+
//                "'norbi'");
//        rs.first();
//        System.out.println(rs.getString("Firstname")+" " +rs.getString("Lastname"));
        //new String("Szoftverfejlesztés".getBytes(Charset.forName("utf-8")))
        //Database.InsertQueryForumPost(new Post("asd", "norbi", "asdasd", new String("Szoftverfejlesztés".getBytes(Charset.forName("utf-8")))));

    }

    public static void main(String[] args) {
        launch(args);
    }
}
