package app.services;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class DialogHelper {

    public static void loadDialog(MouseEvent mouseEvent, FXMLLoader loader) {
        Stage stage = new Stage();
        Parent dialog = null;
        try {
            dialog = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert dialog != null;
        Scene scene = new Scene(dialog);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initOwner(((Node) mouseEvent.getSource()).getScene().getWindow());
        stage.showAndWait();
    }
}
