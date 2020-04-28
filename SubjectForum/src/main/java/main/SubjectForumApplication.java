package main;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Database;
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
        /*
        //Ez egy teszteset
        ArrayList<String> v = new ArrayList<String>();
        v = Database.QueryGetClassName("Name");
        for(int i = 0; i < v.size(); i++)
        {
            System.out.println(v.get(i));
        }
        */

    }

    public static void main(String[] args) {
        launch(args);
    }
}
