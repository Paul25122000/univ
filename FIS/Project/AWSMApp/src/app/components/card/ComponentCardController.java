package app.components.card;

import app.components.Component;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import app.components.dialog.ComponentDialogController;
import org.json.JSONException;

import java.io.IOException;
import java.util.Set;

public class ComponentCardController extends Component {

    @FXML
    private Pane paneContainer;

    @FXML
    private Pane imagePane;

    @FXML
    private Pane textPane;

    @FXML
    private Text productName;

    Set recordCategories;
    Component component = null;
    public ComponentCardController(Component component, Set categories) throws IOException, JSONException {
        super(component);
        this.component = component;
        this.recordCategories = categories;
    }

    @FXML
    public Pane initialize() {
        productName.setText(name);
        imagePane.setStyle("-fx-background-image: url(" + image + ");" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-size: cover");
        paneContainer.setOnMouseClicked(mouseEvent -> {
            ComponentDialogController componentDialogController = null;
            try {
                componentDialogController = new ComponentDialogController(component, recordCategories);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/app/components/dialog/ComponentDialog.fxml"));
            loader.setController(componentDialogController);

            Stage stage = new Stage();
            Parent dialog = null;
            try {
                dialog = loader.load();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Scene scene = new Scene(dialog);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initOwner(((Node) mouseEvent.getSource()).getScene().getWindow());
            stage.showAndWait();
        });
        return paneContainer;
    }
}
