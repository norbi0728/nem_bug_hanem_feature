package main;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Database;
import javax.xml.crypto.Data;


public class SubjectForumApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        primaryStage.setTitle("SubjectForum");
        primaryStage.setScene(new Scene(root, 744, 583));
        primaryStage.show();

        Database d = new Database();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
