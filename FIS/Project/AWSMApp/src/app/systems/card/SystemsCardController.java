package app.systems.card;

import app.promotions.Promotion;
import app.services.DialogHelper;
import app.services.ProductsLists;
import app.systems.Systems;
import app.systems.SystemsController;
import app.systems.dialog.SystemsDialogController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.HashSet;
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
    @FXML
    private Text productOrders;

    Set recordCategories;
    @FXML
    private Pane promotionHolder;
    Promotion promotion = null;
    Systems systems = null;
    SystemsController systemsController = null;
    public SystemsCardController(Systems systems, Set categories,
                                 SystemsController systemsController) {
        super(systems);
        this.systems = systems;
        this.recordCategories = categories;
        this.systemsController = systemsController;
        promotion = ProductsLists.getPromotion(id);
    }

    @FXML
    public Pane initialize() {
        promotionHolder.setVisible(promotion != null);
        productName.setText(name);
        productOrders.setText("Comenzi: "+orders);
        imagePane.setStyle(String.format("-fx-background-image: url(%s);", image));

        paneContainer.setOnMouseClicked(mouseEvent -> {
            SystemsDialogController sysDialog =
                    new SystemsDialogController(systems, recordCategories, promotion, systemsController);
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/app/systems/dialog/SystemsDialog.fxml"));
            loader.setController(sysDialog);
            DialogHelper.loadDialog(mouseEvent, loader);
        });
        return paneContainer;
    }
}
