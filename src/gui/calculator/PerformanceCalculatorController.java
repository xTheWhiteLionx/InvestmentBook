package gui.calculator;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.Investment;

import java.net.URL;
import java.util.ResourceBundle;

import static logic.GeneralMethods.*;

//TODO JavaDoc
public class PerformanceCalculatorController implements Initializable {

    @FXML
    private TextField sellingPrice;
    @FXML
    private Label sellingPriceCurrencyLbl;
    @FXML
    private Label performanceLbl;
    @FXML
    private Label performanceCurrencyLbl;
    @FXML
    private Button btnCalculate;
    @FXML
    private Button btnCancel;

    //TODO JavaDoc
    private Investment currInvestment;

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
        Label[] currencyLbls = {
                sellingPriceCurrencyLbl,
                performanceCurrencyLbl
        };
        setCurrenciesForLbls(currencyLbls);
        btnCalculate.setDisable(true);
        sellingPrice.textProperty().addListener(createChangeListener(sellingPrice, btnCalculate));
    }

    /**
     * Handles the "Apply" Button and hands over the Initializations.
     */
    @FXML
    //TODO JavaDoc
    private void handleCalculate() {
        double performance = currInvestment.calcPerformance(doubleOfTextField(sellingPrice));
        setAndColorsText(performance, performanceLbl);
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
