package app.components.card;

import app.components.Component;
import app.components.ComponentsController;
import app.services.DialogHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import app.components.dialog.ComponentDialogController;
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
    @FXML
    private Text productAmount;


    @FXML
    private Pane complaintFlag;

    Set recordCategories;
    Component component = null;
    ComponentsController componentsController = null;
    public ComponentCardController(Component component, Set categories,
                                   ComponentsController componentsController) {
        super(component);
        this.component = component;
        this.recordCategories = categories;
        this.componentsController = componentsController;
    }

    @FXML
    public Pane initialize() {
        productName.setText(name);
        imagePane.setStyle(String.format("-fx-background-image: url(%s);", image));
        productAmount.setText("Stock: "+amount);
        complaintFlag.setVisible(complaints > 0);
        complaintFlag.toFront();

        paneContainer.setOnMouseClicked(mouseEvent -> {
            ComponentDialogController componentDialogController =
                    new ComponentDialogController(component, recordCategories, componentsController);
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/app/components/dialog/ComponentDialog.fxml"));
            loader.setController(componentDialogController);
            DialogHelper.loadDialog(mouseEvent, loader);
        });
        return paneContainer;
    }
}
