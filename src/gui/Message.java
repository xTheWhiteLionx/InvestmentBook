package gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * This class contains the Messages
 *
 * @author xthe_white_lionx
 */
public class Message {
    /**
     * creates a confirmation alert with the given content
     *
     * @return true if ...
     */
    //TODO implement
    //TODO JavaDoc
    public static boolean acceptedDeleteAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(alert.getAlertType().name().toLowerCase());
        alert.setContentText("Are your sure you want to delete the selected item?");
        ButtonType btnYes = new ButtonType("yes");
        ButtonType btnNo = new ButtonType("no");
        ButtonType btnCancel = new ButtonType("cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnYes, btnNo, btnCancel);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == btnYes;
    }

    /**
     * creates an error alert with the given
     * exception as content
     *
     * @param exception which should be displayed
     */
    //TODO JavaDoc
    public static void ExceptionAlert(Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(alert.getAlertType().name().toLowerCase());
        alert.setContentText(exception.getMessage());
        ButtonType btnOk = new ButtonType("ok");
        ButtonType btnCancel = new ButtonType("cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnOk, btnCancel);
        alert.showAndWait();
    }
}
