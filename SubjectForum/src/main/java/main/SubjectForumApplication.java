package main;

import javafx.application.Application;

import java.sql.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Database;
import logic.authentication.Authentication;


public class SubjectForumApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("SubjectForum");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
        Database d = new Database();

        //EZek csak teszt esetek, a regisztráció és loginra
        //System.out.println( Authentication.register("VintageMan", "asd123asd", "Nimbus", "Potter"));
        //System.out.println(Authentication.login("VintageMan", "asd123asd"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
