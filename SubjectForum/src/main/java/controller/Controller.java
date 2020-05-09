package controller;

import alert.AlertBox;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import graphics.PostCellFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import logic.PostHandler;
import logic.authentication.Authentication;
import logic.neptun_data.NeptunDataHandlerThread;
import model.Database;
import model.Post;
import model.Reply;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


public class Controller implements Initializable {
    private String currentSubject;
    @FXML
    private TextField familyNameField;
    @FXML
    private TextField givenNameField;
    @FXML
    private TextField regUserNameField;
    @FXML
    private TextField regPasswordField;
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
    private Label nameLabel;
    @FXML
    private Pane loginPane;
    @FXML
    private Pane statusPane;
    @FXML
    private Pane regPane;
    @FXML
    private Pane subjectPane;
    @FXML
    private AnchorPane secondRoot;
    @FXML
    private Pane neptunPane;
    @FXML
    private ListView<String> subjectList;
    @FXML
    private Label subjectName;
    @FXML
    private ListView<Post> postDisplay;
    @FXML
    private ListView<String> lv;
    @FXML
    private StackPane stackPane;
    @FXML
    private TextArea postContent;
    @FXML
    private TextField titleField;

    Map<String, String> classCodeNames;

    private static final String REST_URI = "http://localhost:5000/";
    private static Client client = ClientBuilder.newClient();
    int counter = 0;
    private TextAreaSkin skin;
    private String selectedWord;

    @FXML
    private void handleButtonAction(ActionEvent event) throws InterruptedException {
        if(event.getSource().equals(toRegButton)){
            regPane.setVisible(true);
            loginPane.setVisible(false);
        }
        if(event.getSource().equals(loginButton)){
            //login fv hivasa
            //valtas a kovi pane-re
            String msg = Authentication.getAuthentication().login(inputUsernameLog.getText(), inputPasswordLog.getText());
            AlertBox.display("Figyelem", msg);
            if(msg.equals("Sikeres bejelentkezés!")){
                loadSecond(event);
            }

        }
        if(event.getSource().equals(exitButton)){
            System.exit(0);
        }
        if(event.getSource().equals(neptunButton)){
            neptunPane.setVisible(true);
            subjectPane.setVisible(false);
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
            List<String> classNames = new ArrayList<>();
            NeptunDataHandlerThread neptunDataHandlerThread = new NeptunDataHandlerThread(
                    inputNeptuncode.getText(), inputNeptunpassword.getText());

            neptunDataHandlerThread.start();

            AlertBox.display("Figyelem", "Kerlek varj, amig betoltjuk a tantargyaid! Mindjart ertesitunk ennek allapotarol!");

            neptunDataHandlerThread.join();

            if(neptunDataHandlerThread.getSubjectNames() != null){
                AlertBox.display("Figyelem", "Tantargyak sikeresen betoltve!");
                classCodeNames = neptunDataHandlerThread.getSubjectNames();
                for(Map.Entry<String,String> s: classCodeNames.entrySet()){
                    classNames.add(s.getKey());
                }
                subjectList.getItems().addAll(classNames);

                final List<Post>[] posts = new List[]{null};
                subjectList.setOnMouseClicked(event1 ->{

                    String selected = subjectList.getSelectionModel().getSelectedItem();
                    this.currentSubject = selected;
                    System.out.println(selected);
                    if(!statusPane.isVisible())
                        statusPane.setVisible(true);
                    try {
                        nameLabel.setText(Authentication.getUsersFullName());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    if (!subjectPane.isVisible())
                        subjectPane.setVisible(true);
                    subjectName.setText(selected);
                    if(neptunPane.isVisible())
                        neptunPane.setVisible(false);
                    setTextAreaProperties();
                    postDisplay.getItems().clear();
                    postDisplay.setCellFactory(new PostCellFactory());
                    secondRoot.setBottomAnchor(stackPane, 8.0);
                    secondRoot.setRightAnchor(stackPane, 8.0);
                    secondRoot.setTopAnchor(stackPane, 8.0);
                    secondRoot.setLeftAnchor(stackPane, 8.0);
                    posts[0] = PostHandler.getPostHandler().getPosts(new String(selected.getBytes(Charset.forName("utf-8"))));
                    for (Post p: posts[0]){
                        postDisplay.getItems().add(p);
                    }
                });

            }
            else {
                AlertBox.display("Figyelem", "Tantargyak betoltese sikertelen! Lehet, hogy elirtad a neptunkodod, vagy a jelszavad. Kerlek probald meg ujbol!");
            }
        }

    }

    void showSubjectPane(){
        subjectPane.setVisible(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void loadSecond(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/oldal.fxml"));
//            rootPane.getChildren().setAll(pane);
//            rootPane.setPrefWidth(1600);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void register() throws InterruptedException {
        String msg = Authentication.getAuthentication().register( regUserNameField.getText(), regPasswordField.getText(), givenNameField.getText(), familyNameField.getText());
        AlertBox.display("Figyelem", msg);
        if(msg.equals("Sikeres regisztráció!")){
            regPane.setVisible(false);
            loginPane.setVisible(true);
        }
    }

    public void backToLogin(MouseEvent mouseEvent) {
        regPane.setVisible(false);
        loginPane.setVisible(true);
    }
    @FXML
    public void print(KeyEvent e) {

        if (e.getCode().isLetterKey() || e.getCode().getName() == "Backspace"){
            skin = (TextAreaSkin) postContent.getSkin();
            lv.setLayoutY(skin.getCaretBounds().getMaxY() + 165);
            lv.setLayoutX(skin.getCaretBounds().getMinX());
            String response = "";
            try {
                response = client.target(REST_URI + currentWord(postContent.getText())).request().get(String.class);
            }catch (Exception ex){
                ex.printStackTrace();
            }
            if (response != ""){
                String[] splittedResponse = response.split(" ");
                lv.getItems().clear();
                lv.getItems().addAll(splittedResponse);
                lv.setVisible(true);
                lv.getSelectionModel().select(counter);
                selectedWord = lv.getSelectionModel().getSelectedItem();
            }
        }
        if(e.getCode().getName() == "Space"){
            lv.setVisible(false);
            selectedWord = null;
        }

        if(e.getCode().getName() == "Tab" || e.getCode().getName() == "Enter"){
            if (selectedWord != null){
                postContent.replaceText((postContent.getCaretPosition() - currentWord(postContent.getText()).length())-1, postContent.getCaretPosition(), selectedWord);
            }
            lv.setVisible(false);
            selectedWord = null;
        }
    }

    public String currentWord(String text){
        String textUntilCaret = text.substring(0, postContent.getCaretPosition());
        String[] splittedText;
        splittedText = textUntilCaret.split("\\s");
        return splittedText[splittedText.length-1];
    }

    private void setTextAreaProperties() {
        postContent.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().getName() == "Down" && lv.isVisible()){
                event.consume();
                if (counter < 2){
                    counter++;
                }
                else {
                    counter = 2;
                }
                lv.getSelectionModel().select(counter);
                selectedWord = lv.getSelectionModel().getSelectedItem();
            }
            else if (event.getCode().getName() == "Up" && lv.isVisible()){
                event.consume();
                if (counter > 0){
                    counter--;
                }
                else {
                    counter = 0;
                }
                lv.getSelectionModel().select(counter);
                selectedWord = lv.getSelectionModel().getSelectedItem();
            }
        });
    }
    //public Post(String title, String author, String content, String subject)
    public void sendPost(MouseEvent mouseEvent) {
        Post p = new Post(titleField.getText(), Authentication.getUsername(),
                postContent.getText(), classCodeNames.get(currentSubject));
        Database.InsertQueryForumPost(p);
        postDisplay.getItems().add(p);
        postDisplay.getItems().sort(Comparator.comparing(Post::getDate).reversed());
    }

}


