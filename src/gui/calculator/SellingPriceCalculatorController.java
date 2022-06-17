package gui.calculator;

import gui.Helper;
import gui.Style;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.Investment;

import java.net.URL;
import java.util.ResourceBundle;

//TODO JavaDoc
public class SellingPriceCalculatorController implements Initializable {

    @FXML
    private TextField targetPerformance;
    @FXML
    private Label targetPerformanceCurrency;
    @FXML
    private Label sellingExchangeRateLbl;
    @FXML
    private Label sellingExchangeRateCurrency;
    @FXML
    private Button btnCalculate;
    @FXML
    private Button btnCancel;

    /**
     * The current controlled investment
     */
    private Investment investment;

    /**
     *
     * @param investment
     */
    //TODO JavaDoc
    public void setInvestment(Investment investment) {
        this.investment = investment;
    }

    /**
     * Initializes the application.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Style.setCurrenciesForLbls(targetPerformanceCurrency,
                sellingExchangeRateCurrency);
        btnCalculate.setDisable(true);
        targetPerformance.textProperty().addListener(
                Helper.createChangeListener(targetPerformance,btnCalculate)
        );
    }

    /**
     * Handles the "Apply" Button and hands over the Initializations.
     */
    @FXML
    //TODO JavaDoc
    private void handleCalculate() {
        double sellingExchangeRate =
                investment.getPlatform().calcSellingExchangeRate(investment,
                Helper.doubleOfTextField(targetPerformance)
        );
        sellingExchangeRateLbl.setText(Helper.stringOfDouble(sellingExchangeRate));
    }

    /**
     * Close the Window.
     */
    @FXML
    //TODO JavaDoc
    private void handleCancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
