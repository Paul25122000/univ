package app.systems.card;

import app.components.dialog.ComponentDialogController;
import app.promotions.Promotion;
import app.services.APIHandler;
import app.services.ProductsLists;
import app.systems.Systems;
import app.systems.dialog.SystemsDialogController;
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
import org.json.JSONException;

import java.io.IOException;
import java.util.Set;

public class SystemsCardController extends Systems {

    @FXML
    private Pane paneContainer;

    @FXML
    private Pane imagePane;

    @FXML
    private Pane textPane;

    @FXML
    private Text productName;

    Set recordCategories;
    @FXML
    private Pane promotionHolder;
    Promotion promotion = null;
    Systems systems = null;
    public SystemsCardController(Systems systems, Set categories) throws IOException, JSONException {
        super(systems);
        this.systems = systems;
        this.recordCategories = categories;
        promotion = ProductsLists.getPromotion(id);
    }

    @FXML
    public Pane initialize() {
        promotionHolder.setVisible(promotion != null);

        productName.setText(name);
        imagePane.setStyle("-fx-background-image: url(" + image + ");" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-size: cover");
        paneContainer.setOnMouseClicked(mouseEvent -> {
            SystemsDialogController sysDialog = null;
            sysDialog = new SystemsDialogController(systems, recordCategories, promotion);

            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/app/systems/dialog/SystemsDialog.fxml"));
            loader.setController(sysDialog);

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
