package app.systems;

import app.services.ProductsLists;
import app.systems.card.SystemsCardController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.io.*;
import java.util.*;

public class SystemsController {

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
    public void appendTemplate(SystemsCardController systems,
                               int layoutX, int layoutY) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/systems/card/SystemsCard.fxml"));
        loader.setController(systems);
        Pane systemsCard = loader.load();
        systemsCard.setLayoutX(layoutX);
        systemsCard.setLayoutY(layoutY);

        wrapper.getChildren().add(systemsCard);
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

        int systemsAmount = ProductsLists.getSystemsAmount();
        int occurrences = 0;
        for (int i = 0; i < systemsAmount; i++) {

            Systems systems = ProductsLists.getSystems(i);
            categories.add(String.valueOf(systems.categoryName));

            int layoutX = 50;
            if (occurrences % 3 == 1) {
                layoutX = 270;
            } else if (occurrences % 3 == 2) {
                layoutX = 480;
            }
            if (filter.equals("none")
                    || filter.equals(systems.categoryName)
                    || systems.name.toLowerCase().contains(filter.toLowerCase())) {

                appendTemplate(new SystemsCardController(systems, categories, this), layoutX, layoutY);
                layoutY += (occurrences + 1) % 3 != 0 ? 0 : 270;
                occurrences++;
            }
        }
    }

    @FXML
    public void renderAll() throws IOException {
        renderView("none");
    }

    @FXML
    public void initialize() throws IOException {
        renderView("none");

        categoryBox.setItems(FXCollections.observableArrayList(categories));
        categoryBox.getSelectionModel().selectFirst();
    }
}
