package graphics;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.util.Date;

public class ReplyGraphics extends VBox {
    private Label user;
    private Label date;
    private Label content;

    public ReplyGraphics(String user, Date date, String content){
        super(new Label());
        setSpacing(10);
        getChildren().remove(0);
        HBox hb = new HBox(new Label(user), new Label(date.toString()));
        hb.setSpacing(10);
        getChildren().addAll(hb, new Label(content), new Line(this.getLayoutX(), this.getLayoutY(), this.getLayoutX() + 100, this.getLayoutY()));

    }
}
