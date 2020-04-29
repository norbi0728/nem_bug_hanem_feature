package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import logic.authentication.Authentication;

import java.net.URL;
import java.util.ResourceBundle;


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
    private Pane loginPane;
    @FXML
    private Pane registrationPane;


    @FXML
    private void handleButtonAction(ActionEvent event){
        if(event.getSource().equals(toRegButton)){
            registrationPane.toFront();
        }
        if(event.getSource().equals(regButton)){
            //regisztracio fv hivas
            //Authentication::register( inputUsername.getText(), inputPassword.getText(), inputFirstname.getText(), inputLastname.getText());
            loginPane.toFront();
        }
        if(event.getSource().equals(loginButton)){
            //login fv hivasa
            //valtas a kovi pane-re
            //Authentication::login(inputUsernameLog.getText(), inputPasswordLog.getText());
        }
        if(event.getSource().equals(backButton)){
            loginPane.toFront();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }



}


