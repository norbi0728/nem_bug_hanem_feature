package main;

import controller.Controller;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Database;
import model.Post;
import model.Reply;

import javax.naming.ldap.Control;
import javax.xml.crypto.Data;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.util.*;


public class SubjectForumApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        primaryStage.setTitle("Tantárgyfórum");
        primaryStage.setScene(new Scene(root, 744, 583));
        primaryStage.show();

        Database d = new Database();


    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
