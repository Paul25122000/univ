package app.components.dialog;

import app.components.Component;
import app.components.ComponentsController;
import app.services.ProductsLists;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import app.services.APIHandler;
import org.json.JSONException;

import java.io.IOException;
import java.util.Set;

public class ComponentDialogController extends Component {

    @FXML
    private Button updateBtn;
    @FXML
    private Pane deleteBtn;
    @FXML
    private Pane saveBtn;
    @FXML
    private Pane cancelBtn;
    @FXML
    private ComboBox<?> categoryCombo;
    @FXML
    private TextField providerInput;
    @FXML
    private TextField amountInput;
    @FXML
    private TextField nameInput;
    @FXML
    private Pane imageHolder;
    @FXML
    private ComboBox<?> componentsCombo;
    @FXML
    private Pane closeBtn;
    @FXML
    private CheckBox deliveredCheckbox;
    @FXML
    private CheckBox paidCheckbox;
    @FXML
    private TextField priceInput;
    @FXML
    private TextArea commentsInput;

    public Set recordCategories;
    Component updatedComponent = null;
    ComponentsController componentsController = null;
    public ComponentDialogController(Component component, Set categories,
                                     ComponentsController componentsController) {
        super(component);
        this.recordCategories = categories;
        this.componentsController = componentsController;
        this.updatedComponent = component;
        System.out.println(component.complaints);
    }

    private void setDisabled(Boolean isDisabled){
        nameInput.setDisable(isDisabled);
        categoryCombo.setDisable(isDisabled);
        providerInput.setDisable(isDisabled);
        amountInput.setDisable(isDisabled);
        priceInput.setDisable(isDisabled);
        deliveredCheckbox.setDisable(isDisabled);
        paidCheckbox.setDisable(isDisabled);
        commentsInput.setDisable(isDisabled);

        saveBtn.setVisible(!isDisabled);
        cancelBtn.setVisible(!isDisabled);
    }

    @FXML
    public void initialize() {
        nameInput.setText(name);
        categoryCombo.setItems(FXCollections.observableArrayList(recordCategories));
        amountInput.setText(String.valueOf(amount));
        providerInput.setText(provider);
        paidCheckbox.setSelected(paid);
        deliveredCheckbox.setSelected(delivered);
        priceInput.setText(String.valueOf(price));
        commentsInput.setText(comments);
        imageHolder.setStyle(String.format("-fx-background-image: url(%s);", image));

        setDisabled(true);

        var iterator = recordCategories.iterator();
        int categoryIndex = 0;
        while (iterator.hasNext()) {
            if (iterator.next().equals(categoryName))
                break;
            categoryIndex++;
        }
        categoryCombo.getSelectionModel().select(categoryIndex);

        //modifica
        updateBtn.setOnMouseClicked(mouseEvent -> {
            setDisabled(false);
        });

        //save changes
        saveBtn.setOnMouseClicked(mouseEvent -> {
            setDisabled(true);

            updatedComponent.categoryId = ProductsLists.getComponentCategoryId(
                    (String) categoryCombo.getValue());
            updatedComponent.categoryName = (String) categoryCombo.getValue();
            updatedComponent.name = nameInput.getText();
            updatedComponent.provider = providerInput.getText();
            updatedComponent.amount = Integer.parseInt(amountInput.getText());
            updatedComponent.price = Integer.parseInt(priceInput.getText());
            updatedComponent.paid = paidCheckbox.isSelected();
            updatedComponent.delivered = deliveredCheckbox.isSelected();
            updatedComponent.comments = commentsInput.getText();


            final String UPDATE_PARAMS = "{\n" +
                    "    \"id\": " + id + ",\r\n" +
                    "    \"categoryId\": " + updatedComponent.categoryId + ",\r\n" +
                    "    \"name\": \"" + updatedComponent.name + "\",\r\n" +
                    "    \"provider\": \"" + updatedComponent.provider + "\",\r\n" +
                    "    \"amount\": " + updatedComponent.amount + ",\r\n" +
                    "    \"price\": " + updatedComponent.price + ",\r\n" +
                    "    \"paid\": " + updatedComponent.paid + ",\r\n" +
                    "    \"delivered\": " + updatedComponent.delivered + ",\r\n" +
                    "    \"comments\": \"" + updatedComponent.comments + "\"\n}";
            try {
                APIHandler.makeRequest("UPDATE", "components", UPDATE_PARAMS);
                if(ProductsLists.updateComponent(updatedComponent) != 0) {
                    componentsController.renderAll();
                } else {
                    System.out.println("Was unable to update");
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
        //cancel btn
        cancelBtn.setOnMouseClicked(mouseEvent -> {
            setDisabled(true);
        });
        //delete btn
        deleteBtn.setOnMouseClicked(mouseEvent -> {
            try {
                APIHandler.makeRequest("DELETE", "components", super.getDeleteJSON());

                try {
                    if(ProductsLists.deleteComponent(id) != 0) {
                        componentsController.renderAll();
                    } else {
                        System.out.println("Was unable to delete");
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            // do what you have to do
            stage.close();
        });
        closeBtn.setOnMouseClicked(mouseEvent -> {
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            // do what you have to do
            stage.close();
        });

    }
}

