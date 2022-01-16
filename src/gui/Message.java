package gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class Message {

//TODO
    private void createAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(alert.getAlertType().name().toLowerCase());
        alert.setContentText("Are your sure you want to delete the selected item?");
        ButtonType btnYes = new ButtonType("yes");
        ButtonType btnNo = new ButtonType("no");
        ButtonType btnCancel = new ButtonType("cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnYes, btnNo, btnCancel);
        alert.showAndWait();
    }
}
