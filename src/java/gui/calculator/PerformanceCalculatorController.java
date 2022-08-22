package gui.calculator;

import gui.Style;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.BigDecimalUtils;
import logic.Investment;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.DoubleUtil.isValidDouble;

//TODO JavaDoc
public class PerformanceCalculatorController implements Initializable {

    @FXML
    private TextField sellingPrice;
    @FXML
    private Label sellingPriceCurrencyLbl;
    @FXML
    private Label performanceLbl;
    @FXML
    private Button btnCalculate;
    @FXML
    private Button btnCancel;

    /**
     * The current controlled investment
     */
    private Investment currInvestment;

    /**
     *
     * @param investment
     */
    //TODO JavaDoc
    public void setInvestment(Investment investment) {
        this.currInvestment = investment;
    }

    /**
     * Initializes the application.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Style.setCurrenciesForLbls(sellingPriceCurrencyLbl);
        btnCalculate.setDisable(true);
        sellingPrice.textProperty().addListener(
                (observableValue, oldValue, newValue) -> btnCalculate.setDisable(
                        !isValidDouble(newValue)));
    }

    /**
     * Handles the "Apply" Button and hands over the Initializations.
     */
    @FXML
    //TODO JavaDoc
    private void handleCalculate() {
        BigDecimal performance =
                currInvestment.calcPerformance(BigDecimalUtils.parse(sellingPrice.getText()));
        Style.setAndColorsText(performance, performanceLbl);
    }

    /**
     * Closes the Window.
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
