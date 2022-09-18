package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

/**
 * This class contains the dialog window methods.
 *
 * @author xthe_white_lionx
 */
public class DialogWindow {

    public static final Image ICON = new Image("gui/textures/investmentBookIcon.png");

    /**
     * The directory of the package of the json files
     */
    static final String DIRECTORY = "books";

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
        newStage.getIcons().add(ICON);
        newStage.setTitle(title);
        newStage.setMinWidth(width);
        newStage.setMinHeight(height);
        newStage.initModality(Modality.APPLICATION_MODAL);
        try {
            newStage.setScene(new Scene(loader.load(), width, height));
        } catch (IOException e) {
            displayError(e);
        }

        styleStage(newStage);

        newStage.show();

        return loader.getController();
    }

    /**
     * Returns a json filtered FileChooser on initialized to this {@link #DIRECTORY}.
     * Creates the DIRECTORY if it does not exist.
     *
     * @return a json filtered FileChooser to this {@link #DIRECTORY}
     */
    public static FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        //ensure the dialog opens in the correct directory
        File theDir = new File(DIRECTORY);
        if (!theDir.exists()) {
            theDir.mkdir();
        }
        fileChooser.setInitialDirectory(theDir);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON files", "*.json")
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
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        styleStage(stage);
        alert.setContentText("Are your sure you want to delete the selected item?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Creates a modality stage/window out of the given fxml path.
     * With the given title, width and height. And returns the loaded controller generic,
     * depending on the given fxml path
     */
    // TODO: 22.06.2022 JavaDoc
    public static void createLoginController() {
        double width = 300D;
        double height = 300D;

        final Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(ApplicationMain.class.getResource("LoginController.fxml"));

        // Icon/logo of the application
        newStage.setTitle("Investment Book");
        newStage.setMinWidth(width);
        newStage.setMinHeight(height);
        newStage.initModality(Modality.APPLICATION_MODAL);
        try {
            newStage.setScene(new Scene(loader.load(), width, height));
        } catch (IOException e) {
            displayError(e);
        }

        styleStage(newStage);
        newStage.show();
    }

    /**
     * Displays an alert with the specified exception as near content
     *
     * @param ex exception which should be displayed
     * @throws NullPointerException if the specified element is null
     */
    public static void displayError(Exception ex) {
        if (ex != null) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            Alert alert = new Alert(Alert.AlertType.ERROR);

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            styleStage(stage);

            ex.printStackTrace(printWriter);
            printWriter.close();

            GridPane gridPane = new GridPane();
            gridPane.add(new Label("Exception stacktrace:"), 0, 0);
            gridPane.add(new TextArea(stringWriter.toString()), 0, 1);

            alert.setContentText(ex.getMessage());
            alert.getDialogPane().setExpandableContent(gridPane);
            alert.showAndWait();
        } else {
            throw new NullPointerException("exception == null");
        }
    }

    /**
     * Displays an alert with the specified exception as near content
     *
     * @param ex exception which should be displayed
     * @throws NullPointerException if the specified element is null
     */
    public static void displayInvalidFile(Exception ex) {
        if (ex != null) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            styleStage(stage);

            alert.setHeaderText("Die ausgewählte Datei ist fehlerhaft!");

            alert.setContentText(ex.getMessage());

            ex.printStackTrace(printWriter);
            printWriter.close();

            GridPane gridPane = new GridPane();
            gridPane.add(new Label("Exception stacktrace:"), 0, 0);
            gridPane.add(new TextArea(stringWriter.toString()), 0, 1);

            alert.getDialogPane().setExpandableContent(gridPane);

            alert.showAndWait();
        } else {
            throw new NullPointerException("exception == null");
        }
    }

    public static void styleStage(Stage stage){
        stage.getIcons().add(ICON);

        if (Settings.isLightMode()) {
            stage.getScene().getStylesheets().clear();
        } else {
            stage.getScene().getStylesheets().add(JarMain.class.getResource(
                    "themes/Darkmode 1.css").toExternalForm());
        }
    }

}
