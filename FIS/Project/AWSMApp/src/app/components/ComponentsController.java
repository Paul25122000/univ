package app.components;

import app.services.ProductsLists;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import app.components.card.ComponentCardController;
import org.json.JSONException;

import java.io.*;
import java.util.*;

public class ComponentsController {

    @FXML
    private AnchorPane wrapper;
    @FXML
    private ScrollPane scroll;
    @FXML
    private TextField searchBar;
    @FXML
    private ComboBox categoryBox;
    @FXML
    private Button showAll;

    Set<String> categories = new HashSet<String>();

    @FXML
    public void appendTemplate(ComponentCardController component,
                               int layoutX, int layoutY) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/components/card/ComponentCard.fxml"));
        loader.setController(component);
        Pane componentCard = loader.load();
        componentCard.setLayoutX(layoutX);
        componentCard.setLayoutY(layoutY);

        wrapper.getChildren().add(componentCard);
        scroll.setContent(wrapper);
    }

    @FXML
    private void search(ActionEvent event) throws IOException {
        renderView(((TextField) event.getSource()).getText());
    }

    @FXML
    public void filter(ActionEvent event) throws IOException {
        renderView(categoryBox.getValue().toString());
    }

    @FXML
    void renderView(String filter) throws IOException {
        wrapper.getChildren().clear();
        int layoutY = 30;

        int componentsAmount = ProductsLists.getComponentsAmount();
        int occurrences = 0;
        for (int i = 0; i < componentsAmount; i++) {

            Component component = ProductsLists.getComponent(i);
            categories.add(String.valueOf(component.categoryName));

            int layoutX = 50;
            if (occurrences % 3 == 1) {
                layoutX = 270;
            } else if (occurrences % 3 == 2) {
                layoutX = 480;
            }

            if (filter.equals("none")
                    || filter.equals(component.categoryName)
                    || component.name.toLowerCase().contains(filter.toLowerCase())) {

                appendTemplate(new ComponentCardController(component, categories, this), layoutX, layoutY);
                layoutY += (occurrences + 1) % 3 != 0 ? 0 : 270;
                occurrences++;
            }
        }
    }

    @FXML
    public void renderAll() throws IOException, JSONException {
        renderView("none");
    }

    @FXML
    public void initialize() throws IOException, JSONException {
        renderView("none");

        categoryBox.setItems(FXCollections.observableArrayList(categories));
        categoryBox.getSelectionModel().selectFirst();
    }
}
