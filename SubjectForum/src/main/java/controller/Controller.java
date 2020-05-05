package controller;

import alert.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import logic.PostHandler;
import logic.authentication.Authentication;
import logic.neptun_data.NeptunDataHandlerThread;
import model.Post;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;


public class Controller implements Initializable {
    @FXML
    private TextField inputLastname;
    @FXML
    private TextField inputFirstname;
    @FXML
    private TextField inputUsername;
    @FXML
    private TextField inputPassword;
    @FXML
    private TextField inputUsernameLog;
    @FXML
    private TextField inputPasswordLog;
    @FXML
    private TextField inputNeptuncode;
    @FXML
    private TextField inputNeptunpassword;



    @FXML
    private Button regButton;
    @FXML
    private Button toRegButton;
    @FXML
    private Button loginButton;
    @FXML
    private Button backButton;
    @FXML
    private Button subjectButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button profilButton;
    @FXML
    private Button neptunButton;
    @FXML
    private Button loginNeptunButton;

    @FXML
    private ProgressBar neptunSubjectGetterIndicator;

    @FXML
    private Pane loginPane;
    @FXML
    private Pane registrationPane;
    @FXML
    private AnchorPane subjectPane;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Pane neptunPane;
    @FXML
    private ListView<String> subjectList;

    @FXML
    private void handleButtonAction(ActionEvent event) throws InterruptedException {
        if(event.getSource().equals(toRegButton)){
            registrationPane.setVisible(true);
            loginPane.setVisible(false);
        }
        if(event.getSource().equals(regButton)){
            //regisztracio fv hivas
            Authentication.getAuthentication().register( inputUsername.getText(), inputPassword.getText(), inputFirstname.getText(), inputLastname.getText());
            registrationPane.setVisible(false);
            loginPane.setVisible(true);
        }
        if(event.getSource().equals(loginButton)){
            //login fv hivasa
            //valtas a kovi pane-re
            Authentication.getAuthentication().login(inputUsernameLog.getText(), inputPasswordLog.getText());
            loadSecond(event);
        }
        if(event.getSource().equals(backButton)){
            registrationPane.setVisible(false);
            loginPane.setVisible(true);
        }
        if(event.getSource().equals(exitButton)){
            System.exit(0);
        }
        if(event.getSource().equals(neptunButton)){
            //neptunPane.setVisible(true);
            //subjectPane.setVisible(false);
            // plusz az osszes tobbi : .setVisible(false);
        }
        if(event.getSource().equals(subjectButton)){
//            neptunPane.setVisible(false);
//            subjectPane.setVisible(true);
            if(!subjectList.isVisible()) {
                subjectList.setLayoutX(subjectButton.getLayoutX());
                subjectList.setLayoutY(subjectButton.getLayoutY() + 50);
                subjectList.setVisible(true);
            }
            else{
                subjectList.setVisible(false);
            }
            // plusz az osszes tobbi : .setVisible(false);
        }
        if(event.getSource().equals(loginNeptunButton)){
            NeptunDataHandlerThread neptunDataHandlerThread = new NeptunDataHandlerThread(
                    inputNeptuncode.getText(), inputNeptunpassword.getText());

            neptunDataHandlerThread.start();

            AlertBox.display("Figyelem", "Kerlek varj, amig betoltjuk a tantargyaid! Mindjart ertesitunk ennek allapotarol!");

            neptunDataHandlerThread.join();

            if(neptunDataHandlerThread.getSubjectNames() != null){
                AlertBox.display("Figyelem", "Tantargyak sikeresen betoltve!");
                subjectList.getItems().addAll(neptunDataHandlerThread.getSubjectNames());

                AtomicReference<List<Post>> posts = null;
                subjectList.setOnMouseClicked(event1 ->{
                    System.out.println(subjectList.getSelectionModel().getSelectedItem());
                    posts.set(PostHandler.getPostHandler().getPosts("Szoftverfejlesztes"));
                        });
//                for (Post p: posts.get()){
//                    System.out.println(p.getContent());
//                }

            }
            else {
                AlertBox.display("Figyelem", "Tantargyak betoltese sikertelen! Lehet, hogy elirtad a neptunkodod, vagy a jelszavad. Kerlek probald meg ujbol!");
            }


        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void loadSecond(ActionEvent event){
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/oldal.fxml"));
            rootPane.getChildren().setAll(pane);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}


