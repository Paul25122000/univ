package app.productCard.popup;

import app.components.Component;
import app.services.ProductsLists;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import app.services.APIHandler;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Set;

public class PopupController extends Component {

    @FXML
    private Button updateBtn;

    @FXML
    private Pane deleteBtn;

    @FXML
    private Pane saveBtn;

    @FXML
    private Pane cancelBtn;

    @FXML
    private TextField categoryInput;

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
    private Pane ofertaHolder;

    public Set recordCategories;

    public PopupController(int id, String category, String name, int amount, int price, String date, String image,
                           String provider, Boolean paid, Boolean delivered, String comments, Set categories) {

        super(id, category, name, amount, price, date, image, provider, paid, delivered, comments);
        this.recordCategories = categories;
    }
    @FXML
    public void initialize() {


        //componente pentru garantie
        componentsCombo.setItems(FXCollections.observableArrayList(recordCategories));
        componentsCombo.getSelectionModel().selectFirst();

        reportArea.getChildren().clear();

        nameInput.setText(name);
        nameInput.setDisable(true);

        categoryInput.setText(category);
        categoryInput.setDisable(true);

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

        ///gift trasition effect
        TranslateTransition translate = new TranslateTransition();

        imageHolder.setStyle("-fx-background-radius: 15" +
                ";-fx-background-image: url(" + image + ");" +
                "-fx-background-position: center center;" +
                " -fx-background-repeat: no-repeat;" +
                "-fx-background-size: cover; ");

        //modifica
        updateBtn.setOnMouseClicked(mouseEvent -> {
            nameInput.setDisable(false);
            categoryInput.setDisable(false);
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
            categoryInput.setDisable(true);
            providerInput.setDisable(true);
            amountInput.setDisable(true);
            saveBtn.setVisible(false);
            paidCheckbox.setDisable(true);
            deliveredCheckbox.setDisable(true);
            cancelBtn.setVisible(false);
            System.out.println("update elemente: " + id);
            final String UPDATE_PARAMS = "{\n" +
                    "    \"id\": " + id + ",\r\n" +
                    "    \"category\": \"" + categoryInput.getText() + "\",\r\n" +
                    "    \"name\": \"" + nameInput.getText() + "\",\r\n" +
                    "    \"amount\": " + Integer.valueOf(amountInput.getText()) + ",\r\n" +
                    "    \"price\": " + Integer.valueOf(priceInput.getText()) + ",\r\n" +
                    "    \"provider\": \"" + providerInput.getText() + "\",\r\n" +
                    "    \"paid\": " + paidCheckbox.isSelected() + ",\r\n" +
                    "    \"delivered\": " + deliveredCheckbox.isSelected() + ",\r\n" +
                    "    \"comments\": \"" + comments+ "\"\n}";
            try {
                APIHandler.makeRequest("UPDATE", "components", UPDATE_PARAMS);
                ProductsLists.push(new Component(id,categoryInput.getText(), nameInput.getText(),Integer.parseInt(amountInput.getText()),
                        Integer.parseInt(priceInput.getText()),date,image,providerInput.getText(),paidCheckbox.isSelected(),
                        deliveredCheckbox.isSelected(),comments));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //cancel btn
        cancelBtn.setOnMouseClicked(mouseEvent -> {
            nameInput.setDisable(true);
            categoryInput.setDisable(true);
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
            //translate gift card to the bottom of current card
            translate.setByY(200);
            translate.setDuration(Duration.millis(1000));
            translate.setNode(giftPane);
            translate.play();

        });

        renuntaBtn.setOnMouseClicked(mouseEvent -> {
            reportArea.getChildren().clear();
            translate.setByY(-200);
            translate.setDuration(Duration.millis(1000));
            translate.setNode(giftPane);
            translate.play();
        });


    }


}

