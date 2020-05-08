package main;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Database;
import model.Post;
import model.Reply;

import javax.xml.crypto.Data;

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



        //Ezzel a módszerrel lehet lekérni a getQueryEVeríthinget
        //Javába nincs nagyon olyan jó szerkezet erre a megoldásra, szval MAP funkcionalitását egy kicsit másra
        // használjuk mint kéne ezért ilyen ronda lesz a szerkezete
        ArrayList<Map<Post,ArrayList<Reply>>> array = new ArrayList<Map<Post,ArrayList<Reply>>>();
        array = Database.QueryGetEverything("Szoftverfejlesztes");

        for(int i = 0; i < array.size(); i++)
        {
            for( Map.Entry<Post,ArrayList<Reply>> asd : array.get(i).entrySet())
            {
                System.out.println(asd.getKey().getAuthor());
                for(int j = 0; j < asd.getValue().size(); j++)
                {
                    System.out.println(asd.getValue().get(j).getContent());
                }
            }
        }


    }

    public static void main(String[] args) {
        launch(args);
    }
}
