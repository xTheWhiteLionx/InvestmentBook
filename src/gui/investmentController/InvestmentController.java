package gui.investmentController;

import gui.DialogWindow;
import helper.Helper;
import gui.Style;
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
import logic.State;
import logic.investmentBook.InvestmentBook;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controller of the graphical investment interface.
 *
 * @author xthe_white_lionx
 */
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

    /**
     * The current controlled investment
     */
    private Investment currInvestment;

    /**
     * the current {@link InvestmentBook}
     */
    private InvestmentBook investmentBook;

    /**
     * Sets and show the current {@link Investment} with his detailed information
     *
     * @param selectedInvestment the over handed investment
     */
    public void setCurrInvestment(Investment selectedInvestment) {
        assert selectedInvestment != null;

        this.currInvestment = selectedInvestment;
        State investState = currInvestment.getStatus();
        boolean investIsClosed = investState == State.CLOSED;

        creationDatePicker.setValue(currInvestment.getCreationDate());
        statusLbl.setText(investState.name());
        platformLbl.setText(currInvestment.getPlatform().getName());
        stockNameTxtFld.setText(currInvestment.getStockName());
        exchangeRateTxtFld.setText(Helper.stringOfDouble(currInvestment.getExchangeRate()));
        capitalTxtFld.setText(Helper.stringOfDouble(currInvestment.getCapital()));
        sellingPriceTxtFld.setText(Helper.stringOfDouble(currInvestment.getSellingPrice()));
        String performance =
                (currInvestment.getPerformance() + " ").replace(".", ",");
        performanceLbl.setText(performance + Style.SYMBOL_OF_CURRENCY);
        String percentPerformance =
                (currInvestment.getPercentPerformance() + " ").replace(".", ",");
        percentPerformanceLbl.setText(percentPerformance + "%");
        String cost =
                (currInvestment.getCost() + " ").replace(".", ",");
        costLbl.setText(cost + " " + Style.SYMBOL_OF_CURRENCY);
        LocalDate investmentSellDate = currInvestment.getSellingDate();
        //TODO?
//        sellingDatePicker.setValue(
//                investmentSellDate != null ? investmentSellDate : LocalDate.now()
//        );
        sellingDatePicker.setValue(investmentSellDate);
        holdingPeriodLbl.setText(currInvestment.getHoldingPeriod() + " days");

        stockNameTxtFld.setDisable(investIsClosed);
        exchangeRateTxtFld.setDisable(investIsClosed);
        capitalTxtFld.setDisable(investIsClosed);
        sellingPriceTxtFld.setDisable(investIsClosed);
    }

    /**
     * Sets the current {@link InvestmentBook}
     *
     * @param investmentBook the over handed investment
     */
    public void setInvestmentBookView(InvestmentBook investmentBook) {
        assert investmentBook != null;

        this.investmentBook = investmentBook;
    }

    /**
     * Creates and adds a changeListener to the controller
     * items of the investment to regular the accessibility of the apply button
     */
    private void initializeApplyListener() {
        //Listener of the add investment attributes to make the btnAddInvestment enable/disable
        ChangeListener<Object> FieldValidityListener = (observable, oldValue, newValue) -> {
            boolean someInputIsInvalid = creationDatePicker == null
                    || !Helper.isValidDouble(exchangeRateTxtFld)
                    || !Helper.isValidDouble(capitalTxtFld)
                    || stockNameTxtFld.getText().isEmpty()
                    //To be a valid investment the sellingDate and sellingPrice has to be both
                    // invalid or both attributes has to be valid
                    || (sellingDatePicker.getValue() != null ^ Helper.isValidDouble(sellingPriceTxtFld));

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
        //all the currency labels as array
        Style.setCurrenciesForLbls(exchangeRateCurrencyLbl, capitalCurrencyLbl,
                sellingPriceCurrencyLbl);
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
     * Handles the "Apply" Button and hands over
     * the (new) {@link Investment} attributes
     */
    @FXML
    //TODO outsource to a methode in investmentBook
    private void handleApply() {
        currInvestment.setCreationDate(creationDatePicker.getValue());
        currInvestment.setStockName(stockNameTxtFld.getText());
        currInvestment.setExchangeRate(Helper.doubleOfTextField(exchangeRateTxtFld));
        currInvestment.setCapital(Helper.doubleOfTextField(capitalTxtFld));
        //TODO for null
        currInvestment.closeInvestment(sellingDatePicker.getValue(),
                Helper.doubleOfTextField(sellingPriceTxtFld)
        );
        investmentBook.displayInvestments();
        handleCancel();
    }

    /**
     * Handles the "selling price calculator" Button and hands over
     * the current investment.
     */
    @FXML
    private void handleSellingPriceCalculator() {
        SellingPriceCalculatorController sellingPriceCalculatorController =
                DialogWindow.createStage("calculator/SellingPriceCalculatorController.fxml",
                        "selling price calculator: " + currInvestment.getStockName(),
                        350,
                        200
                );
        sellingPriceCalculatorController.setInvestment(currInvestment);
    }

    /**
     * Handles the "performance calculator" Button and hands over
     * the current investment.
     */
    @FXML
    private void handlePerformanceCalculator() {
        PerformanceCalculatorController performanceCalculatorController =
                DialogWindow.createStage("calculator/PerformanceCalculatorController.fxml",
                        "performance calculator: " + currInvestment.getStockName(),
                        350,
                        200
                );
        performanceCalculatorController.setInvestment(currInvestment);
    }
}
