package gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.Optional;

/**
 * This class contains the dialog window methods.
 *
 * @author xthe_white_lionx
 */
public class DialogWindow {

    /**
     * The directory of the package of the json files
     */
    public static final String DIRECTORY = "books/";

    /**
     *
     * @return
     */
    //TODO JavaDoc
    private static FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        //ensure the dialog opens in the correct directory
        fileChooser.setInitialDirectory(new File(DIRECTORY));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json")
        );
        return fileChooser;
    }

    /**
     *
     * @param window
     * @return
     */
    //TODO JavaDoc
    public static File openDialogFile(Window window) {
        assert window != null;

        FileChooser fileChooser = createFileChooser();
        fileChooser.setTitle("Open JSON Graph-File");
        return fileChooser.showOpenDialog(window);
    }

    /**
     *
     * @param window
     * @return
     */
    //TODO JavaDoc
    public static File saveDialogFile(Window window) {
        assert window != null;

        FileChooser fileChooser = createFileChooser();
        fileChooser.setTitle("Save JSON Graph-File");
        return fileChooser.showSaveDialog(window);
    }

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
    public static void exceptionAlert(Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(alert.getAlertType().name().toLowerCase());
        alert.setContentText(exception.getMessage());
        ButtonType btnOk = new ButtonType("ok");
        ButtonType btnCancel = new ButtonType("cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnOk, btnCancel);
        alert.showAndWait();
    }
}
