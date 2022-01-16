package gui.calculator;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.platform.Platform;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import static logic.GeneralMethods.*;

/**
 * Controller of the graphical fee calculator.
 *
 * @author Fabian Hardt
 */
public class FeeCalculatorController implements Initializable {

    @FXML
    private ChoiceBox<Platform> platformChoiceBox;
    @FXML
    private TextField capitalTxtField;
    @FXML
    private Label capitalCurrencyLbl;
    @FXML
    private Label feeLbl;
    @FXML
    private Label feeCurrencyLbl;
    @FXML
    public Button applyBtn;
    @FXML
    private Button cancelBtn;

    //TODO JavaDoc
    public void setPlatformChoiceBox(Set<Platform> platforms) {
        if (!platforms.isEmpty()) {
            platformChoiceBox.getItems().addAll(platforms);
            platformChoiceBox.setValue(platformChoiceBox.getItems().get(0));
        }
    }

    /**
     * Initializes the application.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Label[] currencyLbls = {
                capitalCurrencyLbl,
                feeCurrencyLbl
        };
        setCurrenciesForLbls(currencyLbls);
        applyBtn.setDisable(true);
        capitalTxtField.textProperty().addListener(createChangeListener(capitalTxtField, applyBtn));
    }

    /**
     * Handles the "Apply" Button and display
     * the calculated fee.
     */
    //TODO JavaDoc
    @FXML
    private void handleCalculate() {
        double capital = platformChoiceBox.getValue().getFee(doubleOfTextField(capitalTxtField));
        feeLbl.setText(stringOfDouble(capital));
    }

    /**
     * Close the Window.
     */
    //TODO JavaDoc
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }
}
