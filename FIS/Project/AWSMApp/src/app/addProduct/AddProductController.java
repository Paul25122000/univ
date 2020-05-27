package app.addProduct;

import app.components.Component;
import app.services.APIHandler;
import app.services.ProductsLists;
import app.systems.Systems;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AddProductController {


    public CheckBox isPaid;
    public CheckBox isDelivered;
    public Label providerLabel;
    public Label warrantyLabel;
    @FXML
    private RadioButton isSystem;
    @FXML
    private RadioButton isComponent;
    @FXML
    private ChoiceBox<String> categorySelect;
    @FXML
    private TextField nameInput;
    @FXML
    private TextField providerInput;
    @FXML
    private Spinner<Double> amountInput;
    @FXML
    private Spinner<Double> priceInput;
    @FXML
    private Spinner<Double> warrantyInput;
    @FXML
    private HBox warrantyHBOX;
    @FXML
    private HBox statusHBOX;
    @FXML
    private HBox providerHBOX;
    @FXML
    private HBox deliveryCommentsHBOX;
    @FXML
    private TextArea deliveryComments;
    @FXML
    private Button addBtn;
    @FXML
    private Label statusText;
    @FXML
    private Label deliveryCommentsText;

    ArrayList<String> systemsCategories = new ArrayList<>();
    ArrayList<String> componentsCategories = new ArrayList<>();

    public AddProductController() {

        int systemsAmount = ProductsLists.getSystemsAmount();
        int componentsAmount = ProductsLists.getComponentsAmount();
        String categoryName;

        for (int i = 0; i < systemsAmount; i++) {
            categoryName = ProductsLists.getSystems(i).categoryName;
            if(!systemsCategories.contains(categoryName)) {
                systemsCategories.add(categoryName);
            }
        }
        for (int i = 0; i < componentsAmount; i++) {
            categoryName = ProductsLists.getComponent(i).categoryName;
            if(!componentsCategories.contains(categoryName)) {
                componentsCategories.add(categoryName);
            }
        }
    }

    @FXML
    void addNewProduct(ActionEvent event) throws IOException, JSONException {
        if (isComponent.isSelected()) {
            int categoryId = ProductsLists.getComponentCategoryId(categorySelect.getValue());
            final String POST_PARAMS = "{\n" +
                    "    \"categoryId\": " + categoryId + ",\r\n" +
                    "    \"name\": \"" + nameInput.getText() + "\",\r\n" +
                    "    \"provider\": \"" + providerInput.getText() + "\",\r\n" +
                    "    \"amount\": " + (int) (Math.round(amountInput.getValue())) + ",\r\n" +
                    "    \"price\": " + (int) (Math.round(priceInput.getValue())) + ",\r\n" +
                    "    \"paid\": " + isPaid.isSelected() + ",\r\n" +
                    "    \"delivered\": " + isDelivered.isSelected() + ",\r\n" +
                    "    \"comments\": \"" + deliveryComments.getText() + "\"\n}";

            String response =
                    APIHandler.makeRequest("PUT", "components", POST_PARAMS).toString();
            JSONObject record = new JSONObject(response);
            ProductsLists.push(new Component(
                    record.getInt("id"),
                    record.getInt("categoryId"),
                    record.getString("categoryName"),
                    record.getString("name"),
                    record.getInt("amount"),
                    record.getInt("price"),
                    record.getBoolean("paid"),
                    record.getString("date"),
                    record.getString("image"),
                    record.getString("provider"),
                    record.getBoolean("delivered"),
                    record.getString("comments"),
                    record.getInt("complaints")
            ));

        } else if (isSystem.isSelected()) {
            int categoryId = ProductsLists.getSystemCategoryId(categorySelect.getValue());
            final String POST_PARAMS = "{\n" +
                    "    \"categoryId\": " + categoryId + ",\r\n" +
                    "    \"name\": \"" + nameInput.getText() + "\",\r\n" +
                    "    \"amount\": " + (int) (Math.round(amountInput.getValue())) + ",\r\n" +
                    "    \"price\": " + (int) (Math.round(priceInput.getValue())) + ",\r\n" +
                    "    \"paid\": " + isPaid.isSelected() + ",\r\n" +
                    "    \"warranty\": " + (int) (Math.round(warrantyInput.getValue())) + "\n}";

            String response =
                    APIHandler.makeRequest("PUT", "systems", POST_PARAMS).toString();
            JSONObject record = new JSONObject(response);
            ProductsLists.push(new Systems(
                    record.getInt("id"),
                    record.getInt("categoryId"),
                    record.getString("categoryName"),
                    record.getString("name"),
                    record.getInt("amount"),
                    record.getInt("price"),
                    record.getBoolean("paid"),
                    record.getString("date"),
                    record.getString("image"),
                    record.getInt("orders"),
                    record.getInt("delivers"),
                    record.getInt("warranty"),
                    record.getString("categoryParent"),
                    record.getString("components")
            ));
        }
    }

    @FXML
    public void initialize() {

        amountInput.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 0, 1));
        amountInput.setEditable(true);
        priceInput.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 10000, 0, 1));
        priceInput.setEditable(true);
        warrantyInput.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 48, 0, 1));
        warrantyInput.setEditable(true);
        warrantyHBOX.getChildren().clear();
        categorySelect.setItems(FXCollections.observableArrayList(componentsCategories));
    }


    @FXML
    private void switchProductCategory() {
        if (isComponent.isSelected()) {
            categorySelect.setItems(FXCollections.observableArrayList(componentsCategories));
            categorySelect.setStyle("-fx-background-color: FFFFFF;-fx-effect: dropshadow(gaussian,rgba(8,88,207,0.08),7,0,0,5 ); -fx-font-family: 'Arial';-fx-font-size: 13;-fx-text-fill: #bebebe");
            statusHBOX.getChildren().add(isDelivered);
            deliveryCommentsHBOX.getChildren().addAll(deliveryCommentsText, deliveryComments);
            providerHBOX.getChildren().addAll(providerLabel, providerInput);
            warrantyHBOX.getChildren().clear();
        } else if (isSystem.isSelected()) {
            categorySelect.setItems(FXCollections.observableArrayList(systemsCategories));
            categorySelect.setStyle("-fx-background-color: FFFFFF;-fx-effect: dropshadow(gaussian,rgba(8,88,207,0.08),7,0,0,5 );-fx-font-family: 'Arial';-fx-font-size: 13; -fx-text-fill: #bebebe");
            statusHBOX.getChildren().remove(isDelivered);
            deliveryCommentsHBOX.getChildren().clear();
            providerHBOX.getChildren().clear();
            warrantyHBOX.getChildren().addAll(warrantyLabel, warrantyInput);
        }
    }
}
