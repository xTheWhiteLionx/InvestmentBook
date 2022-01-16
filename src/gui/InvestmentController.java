package gui;

import gui.calculator.PerformanceCalculatorController;
import gui.calculator.SellingPriceCalculatorController;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.Investment;
import logic.InvestmentBookView;
import logic.enums.Status;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static logic.GeneralMethods.*;

//TODO JavaDoc
public class InvestmentController implements Initializable {

    @FXML
    private DatePicker creationDatePicker;
    @FXML
    private Label statusLbl;
    @FXML
    private Label platformLbl;
    @FXML
    private TextField stockNameTxtFld;
    @FXML
    private TextField exchangeRateTxtFld;
    @FXML
    private Label exchangeRateCurrencyLbl;
    @FXML
    private TextField capitalTxtFld;
    @FXML
    private Label capitalCurrencyLbl;
    @FXML
    private TextField sellingPriceTxtFld;
    @FXML
    private Label sellingPriceCurrencyLbl;
    @FXML
    private Label performanceLbl;
    @FXML
    private Label percentPerformanceLbl;
    @FXML
    private Label costLbl;
    @FXML
    private DatePicker sellingDatePicker;
    @FXML
    private Label holdingPeriodLbl;
    @FXML
    private Button btnApply;
    @FXML
    private Button btnCancel;

    private Label[] currencyLbls;

    //TODO JavaDoc
    private Investment currInvestment;

    //TODO JavaDoc
    private InvestmentBookView investmentBookView;

    //TODO JavaDoc
    public void setCurrInvestment(Investment selectedInvestment) {
        this.currInvestment = selectedInvestment;
        Status investStatus = currInvestment.getStatus();
        boolean investIsClosed = investStatus.equals(Status.CLOSED);

        creationDatePicker.setValue(currInvestment.getCreationDate());
        statusLbl.setText(investStatus.name());
        platformLbl.setText(currInvestment.getPlatform().getName());
        stockNameTxtFld.setText(currInvestment.getStockName());
        exchangeRateTxtFld.setText(stringOfDouble(currInvestment.getExchangeRate()));
        capitalTxtFld.setText(stringOfDouble(currInvestment.getCapital()));
        sellingPriceTxtFld.setText(stringOfDouble(currInvestment.getSellingPrice()));
        String performance =
                (currInvestment.getPerformance() + " ").replace(".", ",");
        performanceLbl.setText(performance + SYMBOL_OF_CURRENCY);
        String percentPerformance =
                (currInvestment.getPerformance() + " ").replace(".", ",");
        percentPerformanceLbl.setText(percentPerformance + "%");
        String cost =
                (currInvestment.getPerformance() + " ").replace(".", ",");
        costLbl.setText(cost + " " + SYMBOL_OF_CURRENCY);
        LocalDate investmentSellDate = currInvestment.getSellingDate();
//        sellingDatePicker.setValue(
//                investmentSellDate != null ? investmentSellDate : LocalDate.now()
//        );
        sellingDatePicker.setValue(investmentSellDate);
        holdingPeriodLbl.setText(currInvestment.getHoldingPeriod() + " days");

        stockNameTxtFld.setDisable(investIsClosed);
        exchangeRateTxtFld.setDisable(investIsClosed);
        capitalTxtFld.setDisable(investIsClosed);
        sellingPriceTxtFld.setDisable(investIsClosed);

        setCurrenciesForLbls(currencyLbls);
    }

    //TODO JavaDoc
    public void setInvestmentBookView(InvestmentBookView investmentBookView) {
        this.investmentBookView = investmentBookView;
    }

    private void initializeApplyListener() {
        //Listener of the add investment attributes to make the btnAddInvestment enable/disable
        ChangeListener<Object> FieldValidityListener = (observable, oldValue, newValue) -> {
            boolean someInputIsInvalid = creationDatePicker == null
                    || !isValidDouble(exchangeRateTxtFld)
                    || !isValidDouble(capitalTxtFld)
                    || stockNameTxtFld.getText().isEmpty()
                    //To be a valid investment the sellingDate and sellingPrice has to be both
                    // invalid or both attributes has to be valid
                    || (sellingDatePicker.getValue() != null ^ isValidDouble(sellingPriceTxtFld));

            btnApply.setDisable(someInputIsInvalid);
        };

        creationDatePicker.valueProperty().addListener(FieldValidityListener);
        stockNameTxtFld.textProperty().addListener(FieldValidityListener);
        exchangeRateTxtFld.textProperty().addListener(FieldValidityListener);
        capitalTxtFld.textProperty().addListener(FieldValidityListener);
        sellingPriceTxtFld.textProperty().addListener(FieldValidityListener);
        sellingDatePicker.valueProperty().addListener(FieldValidityListener);
    }

    /**
     * Initializes the application.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currencyLbls = new Label[]{
                exchangeRateCurrencyLbl,
                capitalCurrencyLbl,
                sellingPriceCurrencyLbl
        };
        initializeApplyListener();
    }

    /**
     * Closes the Window.
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles the "Apply" Button and hands over the Initializations.
     */
    @FXML
    //TODO outsource to a methode in investmentBook
    //TODO JavaDoc
    private void handleApply() {
        currInvestment.setCreationDate(creationDatePicker.getValue());
        currInvestment.setStockName(stockNameTxtFld.getText());
        currInvestment.setExchangeRate(doubleOfTextField(exchangeRateTxtFld));
        currInvestment.setCapital(doubleOfTextField(capitalTxtFld));
        //TODO for null
        currInvestment.closeInvestment(sellingDatePicker.getValue(),
                doubleOfTextField(sellingPriceTxtFld)
        );
        investmentBookView.updateInvestmentList();
        handleCancel();
    }

    /**
     * Handles the "Apply" Button and hands over the Initializations.
     */
    @FXML
    //TODO JavaDoc
    private void handleSellingPriceCalculator() {
        SellingPriceCalculatorController investController =
                createStage("calculator/SellingPriceCalculatorController.fxml",
                        "Selling price Calculator: " + currInvestment.getStockName(),
                        350,
                        200
                );
        investController.setInvestment(currInvestment);
    }

    /**
     * Handles the "Apply" Button and hands over the Initializations.
     */
    @FXML
    //TODO JavaDoc
    private void handlePerformanceCalculator() {
        PerformanceCalculatorController investController =
                createStage("calculator/PerformanceCalculatorController.fxml",
                        "Performance Calculator: " + currInvestment.getStockName(),
                        350,
                        200);
        investController.setInvestment(currInvestment);
    }
}
