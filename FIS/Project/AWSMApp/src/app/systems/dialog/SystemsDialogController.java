package app.systems.dialog;

import app.components.Component;
import app.promotions.Promotion;
import app.services.APIHandler;
import app.services.ProductsLists;
import app.systems.Systems;
import app.systems.SystemsController;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONException;

import java.io.IOException;
import java.util.*;

public class SystemsDialogController extends Systems {

    // update values
    @FXML
    private TextField nameInput;
    @FXML
    private TextField priceInput;
    @FXML
    private TextField amountInput;
    @FXML
    private TextField ordersInput;
    @FXML
    private TextField deliversInput;
    @FXML
    private TextField warrantyInput;
    @FXML
    private CheckBox paidCheckbox;

    @FXML
    private Button updateBtn;
    @FXML
    private Pane deleteBtn;
    @FXML
    private Pane saveBtn;
    @FXML
    private Pane cancelBtn;
    @FXML
    private Pane sendBtn;
    @FXML
    private ComboBox<?> categoryCombo;
    @FXML
    private Pane imageHolder;
    @FXML
    private Text reportBtn;
    @FXML
    private Pane reportArea;
    @FXML
    private Text pickComponent;
    @FXML
    private ComboBox<?> warrantyCombo;
    @FXML
    private Pane feedbackHolder;
    @FXML
    private Pane abortButton;
    @FXML
    private Pane closeBtn;
    @FXML
    private Pane giftPane;
    @FXML
    private Pane promotionHolder;
    @FXML
    private Pane closeGift;
    @FXML
    private Pane giftHolder;
    @FXML
    private Button addPromotionBtn;
    @FXML
    private Label promotionName;
    @FXML
    private Label promotionProvider;
    @FXML
    private Label promotionAmount;
    @FXML
    private Label promotionCategory;
    @FXML
    private Pane promotionImage;

    
    public Set recordCategories;
    public Set systemComponents = new HashSet<Object>();;
    private Component component = null;
    Promotion promotion = null;
    SystemsController systemsController = null;
    Systems updatedSystem = null;

    public SystemsDialogController(Systems systems, Set categories, Promotion promotion,
                                   SystemsController systemsController) {
        super(systems);
        this.recordCategories = categories;
        this.promotion = promotion;
        this.systemsController = systemsController;
        this.updatedSystem = systems;
    }

    private void setDisabled(Boolean isDisabled) {
        categoryCombo.setDisable(isDisabled);
        nameInput.setDisable(isDisabled);
        priceInput.setDisable(isDisabled);
        amountInput.setDisable(isDisabled);
        ordersInput.setDisable(isDisabled);
        deliversInput.setDisable(isDisabled);
        paidCheckbox.setDisable(isDisabled);
        warrantyInput.setDisable(isDisabled);

        saveBtn.setVisible(!isDisabled);
        cancelBtn.setVisible(!isDisabled);
    }

    @FXML
    public void initialize() {
        reportArea.getChildren().clear();

        categoryCombo.setItems(FXCollections.observableArrayList(recordCategories));
        nameInput.setText(name);
        priceInput.setText(String.valueOf(price));
        amountInput.setText(String.valueOf(amount));
        ordersInput.setText(String.valueOf(orders));
        deliversInput.setText(String.valueOf(delivers));
        paidCheckbox.setSelected(paid);
        warrantyInput.setText(String.valueOf(warranty));
        setDisabled(true);


        var iterator = recordCategories.iterator();
        int categoryIndex = 0;
        while (iterator.hasNext()) {
            if (iterator.next().equals(categoryName))
                break;
            categoryIndex++;
        }
        categoryCombo.getSelectionModel().select(categoryIndex);


        components = components.substring(1, components.length() - 1);
        String[] componentsArray = components.split("\\s*,\\s*");
        int j =0;
        for ( String item: componentsArray) {
            component = Objects.requireNonNull(
                    ProductsLists.getComponentById(Integer.parseInt(item)));
            systemComponents.add(component.name);
        }

        //componente pentru garantie
        warrantyCombo.setItems(FXCollections.observableArrayList(systemComponents));
        warrantyCombo.getSelectionModel().selectFirst();

        if (promotion != null) {
            promotionName.setText(promotion.name);
            promotionName.setVisible(true);
            giftHolder.setVisible(true);
            promotionHolder.setVisible(true);
        } else {
            giftHolder.setVisible(false);
            promotionHolder.setVisible(false);
            promotionName.setVisible(false);
        }

        ///gift trasition effect
        TranslateTransition translate = new TranslateTransition();

        imageHolder.setStyle(String.format("-fx-background-image: url(%s);", image));

        //modifica
        updateBtn.setOnMouseClicked(mouseEvent -> {
            setDisabled(false);
        });

        //save changes
        saveBtn.setOnMouseClicked(mouseEvent -> {
            setDisabled(true);

            updatedSystem.categoryId = ProductsLists.getComponentCategoryId(
                    (String) categoryCombo.getValue());
            updatedSystem.categoryName = (String) categoryCombo.getValue();
            updatedSystem.name = nameInput.getText();
            updatedSystem.amount = Integer.parseInt(amountInput.getText());
            updatedSystem.price = Integer.parseInt(priceInput.getText());
            updatedSystem.paid = paidCheckbox.isSelected();
            updatedSystem.orders = Integer.parseInt(ordersInput.getText());
            updatedSystem.delivers = Integer.parseInt(deliversInput.getText());
            updatedSystem.warranty = Integer.parseInt(warrantyInput.getText());

            final String UPDATE_PARAMS = "{\n" +
                    "    \"id\": " + id + ",\r\n" +
                    "    \"categoryId\": " + updatedSystem.categoryId + ",\r\n" +
                    "    \"name\": \"" + updatedSystem.name + "\",\r\n" +
                    "    \"amount\": " + updatedSystem.amount + ",\r\n" +
                    "    \"price\": " + updatedSystem.price + ",\r\n" +
                    "    \"paid\": " + updatedSystem.paid + ",\r\n" +
                    "    \"orders\": " + updatedSystem.orders + ",\r\n" +
                    "    \"delivers\": " + updatedSystem.delivers + ",\r\n" +
                    "    \"warranty\": " + updatedSystem.warranty + "\n}";
            try {
                APIHandler.makeRequest("UPDATE", "systems", UPDATE_PARAMS);
                if(ProductsLists.updateSystem(updatedSystem) != 0) {
                    systemsController.renderAll();
                } else {
                    System.out.println("Was unable to update");
                }
            } catch (IOException e) {
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
                    if( ProductsLists.deleteSystem(id) != 0) {
                        systemsController.renderAll();
                    } else {
                        System.out.println("Was unable to delete");
                    }
                } catch (IOException e) {
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

        reportBtn.setOnMouseClicked(mouseEvent -> {
            reportArea.getChildren().addAll(pickComponent, warrantyCombo, feedbackHolder);

            //translate gift card to the bottom of current card
            translate.setToX(0);
            translate.setToY(170);
            translate.setDuration(Duration.millis(500));
            translate.setNode(giftHolder);
            translate.play();
        });

        abortButton.setOnMouseClicked(mouseEvent -> {
            reportArea.getChildren().clear();
            //translate gift card
            translate.setToX(0);
            translate.setToY(0);
            translate.setDuration(Duration.millis(500));
            translate.setNode(giftHolder);
            translate.play();
        });

        //cand se apasa pe produsul cadou, animatie de popup
        giftPane.setOnMouseClicked(mouseEvent -> {
            giftHolder.toFront();
            translate.setToX(90);
            translate.setToY(50);
            translate.setDuration(Duration.millis(500));
            translate.setNode(giftHolder);
            translate.play();
        });
        closeGift.setOnMouseClicked(mouseEvent -> {
            if (reportArea.getChildren().isEmpty()) {
                translate.setToX(0);
                translate.setToY(0);
                translate.setDuration(Duration.millis(500));
                translate.setNode(giftHolder);
                translate.play();
                giftHolder.toBack();
            } else {
                translate.setToX(0);
                translate.setToY(170);
                translate.setDuration(Duration.millis(500));
                translate.setNode(giftHolder);
                translate.play();
                giftHolder.toBack();
            }
        });

        if (promotion != null) {
            promotionImage.setStyle(String.format("-fx-background-image: url(%s);", promotion.image));
            promotionName.setText(promotion.name);
            promotionCategory.setText("Categorie: " + promotion.category);
            promotionProvider.setText("Producător: " + promotion.provider);
            promotionAmount.setText("Rămase: " + promotion.amount);
        }
        for ( String item: componentsArray) {
            systemComponents.add(Objects.requireNonNull(
                    ProductsLists.getComponentById(Integer.parseInt(item))).name);
        }
        sendBtn.setOnMouseClicked(mouseEvent -> {
            component = null;
            for (String item : componentsArray) {
                component = Objects.requireNonNull(
                        ProductsLists.getComponentById(Integer.parseInt(item)));
                if(component.name.equals(warrantyCombo.getValue().toString())) {
                    System.out.println(component.id+", "+ component.name);
                    final String PUT_PARAMS = "{\n" +
                            "    \"id\": " + component.id + ",\r\n" +
                            "    \"complaints\": " + component.complaints +1 + "\n}";
                    try {
                        APIHandler.makeRequest("PUT", "components/complaints", PUT_PARAMS);
                        ProductsLists.incrementComplaints(component.id);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        });
    }
}

