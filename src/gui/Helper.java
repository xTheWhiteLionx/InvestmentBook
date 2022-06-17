package gui;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Helper {
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
    public static <T> T createStage(String fxmlPath, String title, int width, int height) {
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
            DialogWindow.ExceptionAlert(e);
            e.printStackTrace();
        }
        newStage.show();

        return loader.getController();
    }

    /**
     * Examines if the given text field matches the regex for
     * a valid double
     *
     * @param txtFld to be examined text field
     * @return true if the text, of the text field, is a valid double
     * otherwise false
     */
    public static boolean isValidDouble(TextField txtFld) {
        assert txtFld != null;

        return txtFld.getText().matches(Style.TXT_FIELD_REGEX);
    }

    /**
     * Creates an changeListener which examines if the given text field is a valid double and
     * depending on this validity, toggles the accessibility of the given button
     *
     * @param txtFld to be examined text field
     * @param btn    the given button
     * @return String changeListener
     */
    public static ChangeListener<String> createChangeListener(TextField txtFld, Button btn) {
        assert txtFld != null;
        assert btn != null;

        return (observable, oldValue, newValue) -> btn.setDisable(!isValidDouble(txtFld));
    }

    /**
     * Returns a double out of the given text field and
     * replace dots with commas
     *
     * @param txtFld the given text field
     * @return double out of text field
     */
    public static double doubleOfTextField(TextField txtFld) {
        assert txtFld != null;
        assert isValidDouble(txtFld);

        return Double.parseDouble(txtFld.getText().replace(",", "."));
    }

    /**
     * Returns a string out of the given double value and
     * replace commas with dots
     *
     * @param value the given double value
     * @return string out of double value
     */
    public static String stringOfDouble(double value) {
        return String.valueOf(value).replace(".", ",");
    }
}
