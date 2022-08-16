package gui.calculator;

import gui.DoubleUtil;
import gui.Style;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.platform.Platform;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import static gui.DoubleUtil.isValidDouble;

/**
 * Controller of the graphical fee calculator.
 *
 * @author Fabian Hardt
 */
public class FeeCalculatorController implements Initializable {

    @FXML
    private Label platformName;
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

    private Platform platform;

    /**
     *
     * @param platforms
     */
    //TODO JavaDoc
    public void setPlatformChoiceBox(Set<Platform> platforms) {
    }

    public void setPlatform(Platform currPlatform) {

    }

    /**
     * Initializes the application.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Style.setCurrenciesForLbls(capitalCurrencyLbl,
                feeCurrencyLbl);
        applyBtn.setDisable(true);
        capitalTxtField.textProperty().addListener(
                (observableValue, oldValue, newValue) -> applyBtn.setDisable(
                        !isValidDouble(newValue)));
    }

    /**
     * Handles the "Apply" Button and display
     * the calculated fee.
     */
    //TODO JavaDoc
    @FXML
    private void handleCalculate() {
        double capital = platform.getFee(DoubleUtil.parse(capitalTxtField.getText()));
        feeLbl.setText(DoubleUtil.formatMoney(capital));
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
