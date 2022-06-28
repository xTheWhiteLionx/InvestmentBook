package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
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
    static final String DIRECTORY = "books/";

    /**
     * Creates a modality stage/window out of the given fxml path.
     * With the given title, width and height. And returns the loaded controller generic,
     * depending on the given fxml path
     *
     * @param fxmlPath path of the fxml which should be loaded
     * @param title    of the stage/window
     * @param width    of the stage/window
     * @param height   of the stage/window
     * @param <T>      type of the controller, depending on the given fxml path
     * @return controller, depending on the given fxml path
     */
    public static <T> T createStage(@NotNull String fxmlPath, String title, int width, int height) {
        assert !fxmlPath.isEmpty();
        assert width > 0;
        assert height > 0;

        final Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(ApplicationMain.class.getResource(fxmlPath));

        // Icon/logo of the application
        newStage.getIcons().add(new Image("gui/textures/investmentBookIcon.png"));
        newStage.setTitle(title);
        newStage.setMinWidth(width);
        newStage.setMinHeight(height);
        newStage.initModality(Modality.APPLICATION_MODAL);
        try {
            newStage.setScene(new Scene(loader.load(), width, height));
        } catch (IOException e) {
            ErrorAlert(e);
            e.printStackTrace();
        }
        newStage.show();

        return loader.getController();
    }

    /**
     * Creates a modality stage/window out of the given fxml path.
     * With the given title, width and height. And returns the loaded controller generic,
     * depending on the given fxml path
     */
    // TODO: 22.06.2022 JavaDoc
    static void createFileController() {
        double width = 300D;
        double height = 300D;

        final Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(ApplicationMain.class.getResource("FileInterfaceController.fxml"));

        // Icon/logo of the application
        newStage.getIcons().add(new Image("gui/textures/investmentBookIcon.png"));
        newStage.setTitle("Investment Book");
        newStage.setMinWidth(width);
        newStage.setMinHeight(height);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setResizable(false);
        try {
            newStage.setScene(new Scene(loader.load(), width, height));
        } catch (IOException e) {
            ErrorAlert(e);
            e.printStackTrace();
        }
        newStage.show();
    }

    /**
     *
     * @return
     */
    //TODO JavaDoc
    static FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        //ensure the dialog opens in the correct directory
        fileChooser.setInitialDirectory(new File(DIRECTORY));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json")
        );
        return fileChooser;
    }

    /**
     * creates a confirmation alert with the given content
     *
     * @return true if ...
     */
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
    public static void ErrorAlert(Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(alert.getAlertType().name().toLowerCase());
        alert.setContentText(exception.getMessage());
        ButtonType btnOk = new ButtonType("ok");
        ButtonType btnCancel = new ButtonType("cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnOk, btnCancel);
        alert.showAndWait();
    }
}
