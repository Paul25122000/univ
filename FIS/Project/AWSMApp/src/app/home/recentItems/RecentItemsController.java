package app.home.recentItems;

import app.components.Component;
import app.systems.Systems;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RecentItemsController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label amountLabel;

    @FXML
    private Label dateLabel;

    public  String nameRecord,
                    dateRecord;
    public int amountRecord;
    public RecentItemsController(Systems system, Component component){
        nameRecord=component.name;
        dateRecord=component.date;
        amountRecord=component.amount;
    }
    @FXML
    public void initialize(){
        nameLabel.setText(nameRecord);
        amountLabel.setText(String.valueOf(amountRecord));
        dateLabel.setText(dateRecord);
    }
}
