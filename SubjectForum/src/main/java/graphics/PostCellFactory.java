package graphics;

import graphics.PostCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import model.Post;


public class PostCellFactory implements Callback<ListView<Post>, ListCell<Post>> {
    @Override
    public ListCell<Post> call(ListView<Post> param) {
        return new PostCell();
    }
}
