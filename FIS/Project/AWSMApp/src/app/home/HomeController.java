package app.home;

import app.components.Component;
import app.promotions.Promotion;
import app.services.APIHandler;
import app.services.ProductsLists;
import app.systems.Systems;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import app.home.recentItems.RecentItemsController;
import app.workspace.WorkspaceController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class HomeController extends WorkspaceController {

    @FXML
    private AnchorPane anchorRoot;

    @FXML
    private Pane timeIconHolder;

    @FXML
    private Text totalSystems;

    @FXML
    private Text lastAdded;

    @FXML
    private Pane timeIconHolder1;

    @FXML
    private Text totalComponents;

    @FXML
    private VBox recentFilesVbox;
    @FXML
    private Pane wrapper;

    public StringBuffer response = new StringBuffer();
    private int recordLimit = 3;

    public HomeController() throws IOException, JSONException {
        if (ProductsLists.getComponentsAmount() == 0) {
            response = APIHandler.getRecords("components", "");
            JSONArray jsonArray = new JSONArray(response.toString());

            int recordAmount = jsonArray.length();
            for (int i = 0; i < recordAmount; i++) {
                JSONObject record = jsonArray.getJSONObject(i);
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
                        record.getString("comments")));
            }
        }
        if (ProductsLists.getSystemsAmount() == 0) {
            response = APIHandler.getRecords("systems", "");
            JSONArray jsonArray = new JSONArray(response.toString());
            int recordAmount = jsonArray.length();
            for (int i = 0; i < recordAmount; i++) {
                JSONObject record = jsonArray.getJSONObject(i);
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
                        record.getString("categoryParent")));
            }
        }
        if (ProductsLists.getPromotionsAmount() == 0) {
            response = APIHandler.getRecords("promotions", "");
            JSONArray jsonArray = new JSONArray(response.toString());

            int recordAmount = jsonArray.length();
            for (int i = 0; i < recordAmount; i++) {
                JSONObject record = jsonArray.getJSONObject(i);
                ProductsLists.push(new Promotion(
                        record.getInt("id"),
                        record.getInt("system_id"),
                        record.getString("category"),
                        record.getString("name"),
                        record.getInt("amount"),
                        record.getString("provider"),
                        record.getString("image")));
            }
        }
    }

    @FXML
    public void initialize() throws IOException {
        recentFilesVbox.setSpacing(20);
        RecentItemsController recentItems;

        int recordAmount = ProductsLists.getComponentsAmount();
        int latest = recordAmount - (recordLimit + 1);

        for (int i = 0; i < recordAmount; i++) {
            Component component = ProductsLists.getComponent(i);
            String name = component.name;
            int amount = component.amount;
            String date = component.date;

            if (i > latest) {
                recentItems = new RecentItemsController(name, amount, date);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/home/recentItems/RecentItems.fxml"));
                loader.setController(recentItems);

                Pane recentPane = loader.load();
                recentFilesVbox.getChildren().addAll(recentPane);
            }
        }
        totalComponents.setText(String.valueOf(recordAmount));
        lastAdded.setText("Created on \n" + ProductsLists.getComponent(recordAmount-1).date);
    }
}
