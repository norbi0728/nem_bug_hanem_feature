package graphics;

import alert.AlertBox;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logic.authentication.Authentication;
import model.Database;
import model.Post;
import model.Reply;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class PostCell extends ListCell<Post> {
    private static final String REST_URI = "http://89.135.17.104:5000/";;
    private static final Client client = ClientBuilder.newClient();

    @FXML
    Label titleLabel;
    @FXML
    Label authorLabel;
    @FXML
    Label dateLabel;
    @FXML
    Label contentLabel;
    @FXML
    TextArea commentArea;
    @FXML
    Button submitComment;
    @FXML
    VBox commentVBox;
    private boolean firstSetProperty;


    Post currentPost;

    @FXML
    private ListView<String> lv;
    int counter = 0;
    private TextAreaSkin skin;
    private String selectedWord;

    @FXML
    public void print(KeyEvent e) {

        if (e.getCode().isLetterKey() || e.getCode().getName() == "Backspace"){
            skin = (TextAreaSkin) commentArea.getSkin();
            lv.setLayoutY(skin.getCaretBounds().getMaxY() + 165);
            lv.setLayoutX(skin.getCaretBounds().getMinX());
            String response = "";
            try {
                response = client.target(REST_URI + currentWord(commentArea.getText())).request().get(String.class);
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
                commentArea.replaceText((commentArea.getCaretPosition() - currentWord(commentArea.getText()).length())-1, commentArea.getCaretPosition(), selectedWord);
            }
            lv.setVisible(false);
            selectedWord = null;
        }
    }

    public String currentWord(String text){
        String textUntilCaret = text.substring(0, commentArea.getCaretPosition());
        String[] splittedText;
        splittedText = textUntilCaret.split("\\s");
        return splittedText[splittedText.length-1];
    }

    private void setTextAreaProperties() {
        commentArea.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
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

    public PostCell() {
        firstSetProperty = true;
        loadFXML();
    }

    public void comment() throws SQLException, InterruptedException {
        if(!commentArea.getText().isEmpty()) {
            ReplyGraphics reply = new ReplyGraphics(Authentication.getUsername(), new Date(System.currentTimeMillis()),
                    commentArea.getText());
            commentVBox.getChildren().add(reply);
            Database.InsertQueryReply(new Reply(currentPost.getID(),
                    Authentication.getUsername(), commentArea.getText()));
            lv.setVisible(false);
            commentArea.clear();
        }
        else
            AlertBox.display("Figyelem", "A megjegyz�s mez� �res! K�rem t�ltse ki!");
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/postCellGraphics.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(Post post, boolean empty) {
        super.updateItem(post, empty);

        if(empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {
            this.currentPost = post;
            titleLabel.setText(post.getTitle());
            try {
                authorLabel.setText(Authentication.getUsersFullName(post.getAuthor()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            dateLabel.setText(post.getDate().toString());
            contentLabel.setText(post.getContent());
            commentVBox.getChildren().clear();
            for (Reply reply: post.getReplies()){
                try {
                    commentVBox.getChildren().add(new ReplyGraphics(reply.getUser(), reply.getDate(), reply.getContent()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

            commentArea.setOnKeyReleased(event -> print(event));
            commentArea.setPrefHeight(30);
            if(firstSetProperty){
                setTextAreaProperties();
                firstSetProperty = false;
            }
            submitComment.setOnMouseClicked(event -> {
                try {
                    comment();
                } catch (SQLException | InterruptedException throwables) {
                    throwables.printStackTrace();
                }
            });

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
