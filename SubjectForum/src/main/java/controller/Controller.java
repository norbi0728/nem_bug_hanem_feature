package controller;

import alert.AlertBox;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import graphics.PostCellFactory;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import logic.PostHandler;
import logic.authentication.Authentication;
import logic.neptun_data.NeptunDataHandlerThread;
import logic.utility.RabinKarpStringPatternMatching;
import model.Database;
import model.Post;
import model.Reply;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.net.URL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;



public class Controller implements Initializable {
    private String currentSubject = null;
    @FXML
    private TextField familyNameField;
    @FXML
    private TextField givenNameField;
    @FXML
    private TextField regUserNameField;
    @FXML
    private PasswordField regPasswordField;
    @FXML
    private TextField inputUsernameLog;
    @FXML
    private TextField inputPasswordLog;
    @FXML
    private TextField inputNeptuncode;
    @FXML
    private TextField inputNeptunpassword;

    @FXML
    private TextField searchField;
    @FXML
    private RadioButton byTitle;
    @FXML
    private RadioButton byContent;


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
    private Button listButton;

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
    @FXML
    private HBox statusHBox;
    @FXML
    private HBox statusHBox2;
    @FXML
    private VBox statusVBox;
    @FXML
    private VBox searchVBox;
    @FXML
    private Separator first;
    @FXML
    private Separator second;
    @FXML
    private Separator third;
    @FXML
    private Separator fourth;
    private boolean firstOpenFlag;
    private boolean firstOpenFlag2;
    @FXML
    private Label statusLabel;
    @FXML
    private VBox subjectVBox;
    @FXML
    private ProgressBar neptunProgress;
    @FXML
    private ListView<String> listSubjectList;
    private boolean firstSetProperty;

    @FXML
    private TextField subjectSearchField;

    Map<String, String> classCodeNames;
    NeptunDataHandlerThread neptunDataHandlerThread;
    private static final String REST_URI = "http://89.135.17.104:5000/";
    private static final Client client = ClientBuilder.newClient();
    int counter = 0;
    private TextAreaSkin skin;
    private String selectedWord;
    private boolean subjectsDownloaded;

    @FXML
    private void handleButtonAction(ActionEvent event) throws InterruptedException, SQLException {
        if(event.getSource().equals(toRegButton)){
            regPane.setVisible(true);
            loginPane.setVisible(false);
        }
        if(event.getSource().equals(loginButton)){

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

        }
        if(event.getSource().equals(subjectButton)) {

            if (subjectsDownloaded) {
                if (!subjectList.isVisible()) {
                    subjectList.setLayoutX(subjectButton.getLayoutX());
                    subjectList.setLayoutY(subjectButton.getLayoutY() + 50);
                    subjectList.setVisible(true);
                    if (neptunDataHandlerThread.getSubjectNames() != null && firstOpenFlag == false) {
                        firstOpenFlag = true;

                        List<String> classNames = new ArrayList<>();
                        //AlertBox.display("Figyelem", "Tantargyak sikeresen betoltve!");
                        classCodeNames = neptunDataHandlerThread.getSubjectNames();
                        for (Map.Entry<String, String> s : classCodeNames.entrySet()) {
                            classNames.add(s.getKey());
                        }
                        subjectList.getItems().addAll(classNames);

                        final List<Post>[] posts = new List[]{null};
                        subjectList.setOnMouseClicked(event1 -> {
                            statusHBox2.setVisible(false);
                            String selected = subjectList.getSelectionModel().getSelectedItem();
                            this.currentSubject = selected;
                            try {
                                statusHBox.setVisible(true);
                                nameLabel.setText(Authentication.getUsersFullName(Authentication.getUsername()));
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            if (!subjectPane.isVisible())
                                subjectPane.setVisible(true);
                            subjectName.setText(selected);
                            if (neptunPane.isVisible())
                                neptunPane.setVisible(false);
                            subjectPane.prefHeightProperty().bind(secondRoot.heightProperty().divide(1.6));
                            //subjectPane.prefWidthProperty().bind(secondRoot.widthProperty().divide(1.2));
                            //subjectVBox.prefHeightProperty().bind(secondRoot.heightProperty());
                            statusPane.prefWidthProperty().bind(secondRoot.widthProperty());
                            statusVBox.prefWidthProperty().bind(secondRoot.widthProperty());
                            searchVBox.prefWidthProperty().bind(secondRoot.widthProperty());
                            postContent.prefWidthProperty().bind(secondRoot.widthProperty().divide(1.3));
                            titleField.prefWidthProperty().bind(postContent.widthProperty().divide(2));
                            postDisplay.prefWidthProperty().bind(secondRoot.widthProperty().divide(1.3));
                            postDisplay.prefHeightProperty().bind(secondRoot.heightProperty().divide(1.6));
                            first.prefWidthProperty().bind(postDisplay.widthProperty());
                            third.prefWidthProperty().bind(postDisplay.widthProperty());
                            postContent.clear();
                            titleField.clear();
                            lv.setVisible(false);
                            if(firstSetProperty){
                                setTextAreaProperties();
                                firstSetProperty = false;
                            }
                            postDisplay.getItems().clear();
                            postDisplay.setCellFactory(new PostCellFactory());
                            AnchorPane.setBottomAnchor(stackPane, 8.0);
                            AnchorPane.setRightAnchor(stackPane, 8.0);
                            AnchorPane.setTopAnchor(stackPane, 8.0);
                            AnchorPane.setLeftAnchor(stackPane, 8.0);
                            posts[0] = PostHandler.getPostHandler().getPosts(
                                    classCodeNames.get(currentSubject));
                            posts[0].sort(Comparator.comparing(Post::getDate).reversed());
                            for (Post p : posts[0]) {
                                postDisplay.getItems().add(p);
                            }
                            subjectList.setVisible(false);
                        });

                    }
                } else {
                    subjectList.setVisible(false);
                }
            }
            else
                AlertBox.display("Figyelem", "A tárgyai megjelenítéséhez jelentkezzen be a Neptunba!");
        }
        if(event.getSource().equals(loginNeptunButton)){
            boolean finish = false;
            List<String> classNames = new ArrayList<>();
             neptunDataHandlerThread = new NeptunDataHandlerThread(
                    inputNeptuncode.getText(), inputNeptunpassword.getText(), finish);

            neptunDataHandlerThread.start();
            if(statusHBox.isVisible())
                statusHBox.setVisible(false);

            statusHBox2.setVisible(true);
            if(!neptunProgress.isVisible())
                neptunProgress.setVisible(true);
            statusLabel.setText("Tárgyak betöltése folyamatban");
            Thread t = new Thread(){
                @Override
                public void run(){
                    double count = 0;
                    while (!neptunDataHandlerThread.isFinish()){
                        neptunProgress.setProgress(count);
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        count +=0.0025;
                    }
                    neptunProgress.setVisible(false);
                    Platform.runLater(() -> {

                        if (neptunDataHandlerThread.getSubjectNames() != null) {
                            statusLabel.setText("Tárgyak sikeresen betöltve");

                            subjectsDownloaded = true;

                            try {
                                AlertBox.display("Figyelem", "Tárgyak betöltése sikeres!");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            statusLabel.setText("Nem sikerült betölteni a tárgyakat");
                            try {
                                AlertBox.display("Figyelem", "Tárgyak betöltése sikertelen! Kérem próbálja meg megadni újra Neptun belépési adatait!");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            };
            t.start();

            AlertBox.display("Figyelem", "Kérem várjon amíg betöltjük a tantárgyait! A folyamat végén értesítjük!");

        }

    }

    void showSubjectPane(){
        subjectPane.setVisible(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        subjectsDownloaded = false;
        firstOpenFlag = false;
        firstOpenFlag2 = false;
        classCodeNames = new HashMap<>();
        firstSetProperty = true;
    }

    @FXML
    private void loadSecond(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/oldal.fxml"));

            Stage stage = new Stage();
            stage.setTitle("Tantárgyfórum");
            //stage.setMinHeight(900);
            //stage.setMinWidth(1567);
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
            lv.setLayoutY(skin.getCaretBounds().getMaxY() + 100);
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
    public void sendPost(MouseEvent mouseEvent) throws InterruptedException {
        if (!postContent.getText().isEmpty() && !titleField.getText().isEmpty()) {
            Post p = new Post(titleField.getText(), Authentication.getUsername(),
                    postContent.getText(), classCodeNames.get(currentSubject));
            Database.InsertQueryForumPost(p);
            postDisplay.getItems().add(p);
            postDisplay.getItems().sort(Comparator.comparing(Post::getDate).reversed());
            postContent.clear();
            titleField.clear();
            lv.setVisible(false);
        }
        else
            AlertBox.display("Figyelem", "Cím vagy a tartalom üresen maradt, kérem töltse ki!");
    }

    public void update(MouseEvent mouseEvent) throws InterruptedException {
        AlertBox.display("Figyelem", "Frissítés folyamatban!");
        List<Post> posts = PostHandler.getPostHandler().getPosts(
                classCodeNames.get(currentSubject));

        postDisplay.getItems().clear();
        for (Post p: posts){
            postDisplay.getItems().add(p);
        }
        postDisplay.getItems().sort(Comparator.comparing(Post::getDate).reversed());
    }

    public void search(MouseEvent mouseEvent) throws InterruptedException {
        if(!searchField.getText().isEmpty()){
            List<Integer> foundIndices;
            List<Post> posts = PostHandler.getPostHandler().getPosts(
                    classCodeNames.get(currentSubject));
            List<Post> foundPosts = new ArrayList<>();
            if (byContent.isSelected() && !searchField.getText().isEmpty()){
                for (Post p: posts){
                    foundIndices = RabinKarpStringPatternMatching.getRabinKarpStringPatternMatching()
                            .stringPatternMatching(p.getContent(), searchField.getText(), 10,13);
                    if(foundIndices.size() != 0){
                        foundPosts.add(p);
                    }
                }
            }
            else if(byTitle.isSelected() && !searchField.getText().isEmpty()){
                for (Post p: posts){
                    foundIndices = RabinKarpStringPatternMatching.getRabinKarpStringPatternMatching()
                            .stringPatternMatching(p.getTitle(), searchField.getText(), 10,13);
                    if(foundIndices.size() != 0){
                        foundPosts.add(p);
                    }
                }
            }

            postDisplay.getItems().clear();
            for (Post p: foundPosts){
                postDisplay.getItems().add(p);
            }
            postDisplay.getItems().sort(Comparator.comparing(Post::getDate).reversed());
        }
        else
            AlertBox.display("Figyelem", "Kérem töltse ki a keresés mezõt!");
    }

    public void listAllSubject(MouseEvent mouseEvent) throws SQLException {
        if(!listSubjectList.isVisible()) {

            listSubjectList.setLayoutX(listButton.getLayoutX());
            listSubjectList.setLayoutY(listButton.getLayoutY() + 50);
            listSubjectList.setVisible(true);
            ResultSet result = Database.Query("SELECT Code, Name FROM classes");
            while(result.next()){
                classCodeNames.put(result.getString("Name"), result.getString("Code"));
            }
            if(firstOpenFlag2 == false){
                firstOpenFlag2 = true;

                List<String> classNames = new ArrayList<>();
                for(Map.Entry<String,String> s: classCodeNames.entrySet()){
                    classNames.add(s.getKey());
                }
                listSubjectList.getItems().addAll(classNames);


                final List<Post>[] posts = new List[]{null};
                listSubjectList.setOnMouseClicked(event1 ->{
                    listSubjectList.setVisible(false);
                    statusHBox2.setVisible(false);
                    String selected = listSubjectList.getSelectionModel().getSelectedItem();
                    this.currentSubject = selected;
                    try {
                        statusHBox.setVisible(true);
                        nameLabel.setText(Authentication.getUsersFullName(Authentication.getUsername()));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    if (!subjectPane.isVisible())
                        subjectPane.setVisible(true);
                    subjectName.setText(selected);
                    if(neptunPane.isVisible())
                        neptunPane.setVisible(false);
                    subjectPane.prefHeightProperty().bind(secondRoot.heightProperty().divide(1.6));
                    //subjectPane.prefWidthProperty().bind(secondRoot.widthProperty().divide(1.2));
                    //subjectVBox.prefHeightProperty().bind(secondRoot.heightProperty());
                    statusPane.prefWidthProperty().bind(secondRoot.widthProperty());
                    statusVBox.prefWidthProperty().bind(secondRoot.widthProperty());
                    searchVBox.prefWidthProperty().bind(secondRoot.widthProperty());
                    postContent.prefWidthProperty().bind(secondRoot.widthProperty().divide(1.3));
                    titleField.prefWidthProperty().bind(postContent.widthProperty().divide(2));
                    postDisplay.prefWidthProperty().bind(secondRoot.widthProperty().divide(1.3));
                    postDisplay.prefHeightProperty().bind(secondRoot.heightProperty().divide(1.6));
                    first.prefWidthProperty().bind(postDisplay.widthProperty());
                    third.prefWidthProperty().bind(postDisplay.widthProperty());
                    postContent.clear();
                    titleField.clear();
                    lv.setVisible(false);
                    if(firstSetProperty){
                        setTextAreaProperties();
                        firstSetProperty = false;
                    }

                    postDisplay.getItems().clear();
                    postDisplay.setCellFactory(new PostCellFactory());
                    AnchorPane.setBottomAnchor(stackPane, 8.0);
                    AnchorPane.setRightAnchor(stackPane, 8.0);
                    AnchorPane.setTopAnchor(stackPane, 8.0);
                    AnchorPane.setLeftAnchor(stackPane, 8.0);
                    posts[0] = PostHandler.getPostHandler().getPosts(
                            classCodeNames.get(currentSubject));
                    posts[0].sort(Comparator.comparing(Post::getDate).reversed());
                    for (Post p: posts[0]){
                        postDisplay.getItems().add(p);
                    }
                });

            }
        }
        else{
            listSubjectList.setVisible(false);
        }
    }

    public void showSearchedSubject(MouseEvent mouseEvent) throws InterruptedException, SQLException {
        if(!subjectSearchField.getText().isEmpty()){
            String selected = subjectSearchField.getText();
            ResultSet result = Database.Query("SELECT Code FROM classes WHERE Name = '"
            +selected+"'");

            if(result.first()){
                String code = result.getString("Code");
                statusHBox2.setVisible(false);

                this.currentSubject = selected;

                try {
                    statusHBox.setVisible(true);
                    nameLabel.setText(Authentication.getUsersFullName(Authentication.getUsername()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                if (!subjectPane.isVisible())
                    subjectPane.setVisible(true);
                subjectName.setText(selected);
                if(neptunPane.isVisible())
                    neptunPane.setVisible(false);
                subjectPane.prefHeightProperty().bind(secondRoot.heightProperty().divide(1.6));
                //subjectPane.prefWidthProperty().bind(secondRoot.widthProperty().divide(1.2));
                //subjectVBox.prefHeightProperty().bind(secondRoot.heightProperty());
                statusPane.prefWidthProperty().bind(secondRoot.widthProperty());
                statusVBox.prefWidthProperty().bind(secondRoot.widthProperty());
                searchVBox.prefWidthProperty().bind(secondRoot.widthProperty());
                postContent.prefWidthProperty().bind(secondRoot.widthProperty().divide(1.3));
                titleField.prefWidthProperty().bind(postContent.widthProperty().divide(2));
                postDisplay.prefWidthProperty().bind(secondRoot.widthProperty().divide(1.3));
                postDisplay.prefHeightProperty().bind(secondRoot.heightProperty().divide(1.6));
                first.prefWidthProperty().bind(postDisplay.widthProperty());
                third.prefWidthProperty().bind(postDisplay.widthProperty());
                postContent.clear();
                titleField.clear();
                lv.setVisible(false);
                if(firstSetProperty){
                    setTextAreaProperties();
                    firstSetProperty = false;
                }
                postDisplay.getItems().clear();
                postDisplay.setCellFactory(new PostCellFactory());
                AnchorPane.setBottomAnchor(stackPane, 8.0);
                AnchorPane.setRightAnchor(stackPane, 8.0);
                AnchorPane.setTopAnchor(stackPane, 8.0);
                AnchorPane.setLeftAnchor(stackPane, 8.0);
                List<Post> posts = PostHandler.getPostHandler().getPosts(
                        code);
                posts.sort(Comparator.comparing(Post::getDate).reversed());
                for (Post p: posts){
                    postDisplay.getItems().add(p);
                }
            }
            else
                AlertBox.display("Figyelem", "Nincs ilyen tárgy az adatbázisban! Kérem ellenõrizze a keresési mezõt!");
        }
        else
            AlertBox.display("Figyelem", "Kérem töltse ki a keresési mezõt!");
    }

    public void loadUserManual() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/readMe.fxml"));

            Stage stage = new Stage();
            stage.setTitle("A Tantárgyfórum felhasználói kézikönyve");
            stage.setMinHeight(400);
            stage.setMaxHeight(400);
            stage.setMinWidth(600);
            stage.setMaxWidth(600);
            stage.setScene(new Scene(root));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void exitProgram(ActionEvent actionEvent) {
        System.exit(0);
    }
}



