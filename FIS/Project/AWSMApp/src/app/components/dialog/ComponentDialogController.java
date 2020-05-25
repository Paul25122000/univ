package app.components.dialog;

import app.components.Component;
import app.promotions.Promotion;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
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
    private ComboBox<?> componentsEditCombo;
    @FXML
    private TextField providerInput;
    @FXML
    private TextField amountInput;
    @FXML
    private TextField nameInput;
    @FXML
    private Pane imageHolder;
    @FXML
    private Text reportBtn;
    @FXML
    private Pane reportArea;
    @FXML
    private Text alegeComponenta;
    @FXML
    private ComboBox<?> componentsCombo;
    @FXML
    private Pane feedbackHolder;
    @FXML
    private Pane renuntaBtn;
    @FXML
    private Pane closeBtn;
    @FXML
    private CheckBox deliveredCheckbox;
    @FXML
    private CheckBox paidCheckbox;
    @FXML
    private TextField priceInput;
    @FXML
    private Pane giftPane;
    @FXML
    private Pane promotionHolder;
    @FXML
    private Pane closeGift;
    @FXML
    private Pane giftHolder;
    public Set recordCategories;
    @FXML
    private Label promotionName;
    @FXML
    private Button addPromotionBtn;
    Promotion promotion = null;

    public ComponentDialogController(Component component, Set categories) throws IOException, JSONException {
        super(component);
        this.recordCategories = categories;
    }

    @FXML
    public void initialize() throws IOException, JSONException {


        //componente pentru garantie
        componentsCombo.setItems(FXCollections.observableArrayList(recordCategories));
        componentsCombo.getSelectionModel().selectFirst();

        reportArea.getChildren().clear();

        nameInput.setText(name);
        nameInput.setDisable(true);

        componentsEditCombo.setItems(FXCollections.observableArrayList(recordCategories));
        recordCategories.forEach(System.out::println);

        componentsEditCombo.getSelectionModel().selectFirst();
        componentsEditCombo.setDisable(true);

        amountInput.setText(String.valueOf(amount));
        amountInput.setDisable(true);

        providerInput.setText(provider);
        providerInput.setDisable(true);

        saveBtn.setVisible(false);
        cancelBtn.setVisible(false);

        paidCheckbox.setSelected(paid);
        paidCheckbox.setDisable(true);

        deliveredCheckbox.setSelected(delivered);
        deliveredCheckbox.setDisable(true);
        priceInput.setText(String.valueOf(price));

        imageHolder.setStyle("-fx-background-radius: 15" +
                ";-fx-background-image: url(" + image + ");" +
                "-fx-background-position: center center;" +
                " -fx-background-repeat: no-repeat;" +
                "-fx-background-size: cover; ");

        //modifica
        updateBtn.setOnMouseClicked(mouseEvent -> {
            nameInput.setDisable(false);
            componentsEditCombo.setDisable(false);
            amountInput.setDisable(false);
            saveBtn.setVisible(true);
            cancelBtn.setVisible(true);
            providerInput.setDisable(false);
            paidCheckbox.setDisable(false);
            deliveredCheckbox.setDisable(false);
        });
        //save changes
        saveBtn.setOnMouseClicked(mouseEvent -> {
            nameInput.setDisable(true);
            componentsEditCombo.setDisable(true);
            providerInput.setDisable(true);
            amountInput.setDisable(true);
            saveBtn.setVisible(false);
            paidCheckbox.setDisable(true);
            deliveredCheckbox.setDisable(true);
            cancelBtn.setVisible(false);

            final String UPDATE_PARAMS = "{\n" +
                    "    \"id\": " + id + ",\r\n" +
                    "    \"category\": \"" + componentsEditCombo.getValue() + "\",\r\n" +
                    "    \"name\": \"" + nameInput.getText() + "\",\r\n" +
                    "    \"amount\": " + Integer.valueOf(amountInput.getText()) + ",\r\n" +
                    "    \"price\": " + Integer.valueOf(priceInput.getText()) + ",\r\n" +
                    "    \"provider\": \"" + providerInput.getText() + "\",\r\n" +
                    "    \"paid\": " + paidCheckbox.isSelected() + ",\r\n" +
                    "    \"delivered\": " + deliveredCheckbox.isSelected() + ",\r\n" +
                    "    \"comments\": \"" + comments + "\"\n}";
            try {
                APIHandler.makeRequest("UPDATE", "components", UPDATE_PARAMS);
//                ProductsLists.push(new Component(
//                        id,
//                        Integer.parseInt(componentsEditCombo.getValue()),
//                        componentsEditCombo.getValue(),
//                        nameInput.getText(),
//                        Integer.parseInt(amountInput.getText()),
//                        Integer.parseInt(priceInput.getText()),
//                        paidCheckbox.isSelected(),
//                        date,
//                        image,
//                        providerInput.getText(),
//                        deliveredCheckbox.isSelected(),
//                        comments));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //cancel btn
        cancelBtn.setOnMouseClicked(mouseEvent -> {
            nameInput.setDisable(true);
            componentsEditCombo.setDisable(true);
            providerInput.setDisable(true);
            amountInput.setDisable(true);
            saveBtn.setVisible(false);
            cancelBtn.setVisible(false);
            paidCheckbox.setDisable(false);
            deliveredCheckbox.setDisable(false);
        });
        //delete btn
        deleteBtn.setOnMouseClicked(mouseEvent -> {
            try {
                APIHandler.makeRequest("DELETE", "components", super.getDeleteJSON());
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

        reportBtn.setOnMouseClicked(mouseEvent -> {
            reportArea.getChildren().addAll(alegeComponenta, componentsCombo, feedbackHolder);
        });

        renuntaBtn.setOnMouseClicked(mouseEvent -> {
            reportArea.getChildren().clear();
        });

    }
}

