package gui.investmentController;

import gui.ApplicationMain;
import gui.DoubleUtil;
import gui.JarMain;
import gui.Settings;
import gui.Style;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.BigDecimalUtils;
import logic.Investment;
import logic.State;
import logic.investmentBook.InvestmentBook;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static gui.DialogWindow.displayError;
import static gui.calculator.PerformanceCalculatorController.loadPerformanceCalculatorController;
import static gui.calculator.SellingPriceCalculatorController.loadSellingPriceCalculatorController;

/**
 * Controller of the graphical investment interface.
 *
 * @author xthe_white_lionx
 */
public class EditInvestmentController implements Initializable {

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
    private Investment investment;

    /**
     * the current {@link InvestmentBook}
     */
    private InvestmentBook investmentBook;

    /**
     * Initialize a new game with the specified arguments by using the
     * methode {@link #loadGameController()}
     *
     * @param involved    array of {@code boolean}s true if someone plays otherwise false
     * @param names       name of each player in an array
     * @param playerTypes typ of each player in an array
     */
    public static void loadEditInvestmentController(Investment selectedInvestment,
                                                    InvestmentBook investmentBook) {
        if (selectedInvestment == null) {
            throw new NullPointerException("selectedInvestment == null");
        }
        if (investmentBook == null) {
            throw new NullPointerException("investmentBook == null");
        }

        EditInvestmentController editInvestmentController = createEditInvestmentController();

        editInvestmentController.investmentBook = investmentBook;
        editInvestmentController.setInvestment(selectedInvestment);
    }

    /**
     * 
     * @return
     */
    private static EditInvestmentController createEditInvestmentController() {
        final Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("investmentController/" +
                        "EditInvestmentController.fxml"));

        // Icon/logo of the application
        newStage.getIcons().add(new Image("gui/textures/investmentBookIcon.png"));
        newStage.setTitle("Edit Investment");
        newStage.setMinWidth(650D);
        newStage.setMinHeight(600D);
        newStage.setResizable(false);
        newStage.initModality(Modality.APPLICATION_MODAL);
        try {
            newStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            displayError(e);
        }

        String css = Settings.getMode();
        if (!css.isEmpty()) {
            newStage.getScene().getStylesheets().add(JarMain.class.getResource(
                    "themes/" + css).toExternalForm());
        } else {
            newStage.getScene().getStylesheets().removeAll();
        }

        newStage.show();

        return loader.getController();
    }

    /**
     * Sets and show the current {@link Investment} with his detailed information
     *
     * @param currInvestment the over handed investment
     */
    private void setInvestment(Investment investment) {
        State investState = investment.getState();

        creationDatePicker.valueProperty().addListener((observableValue, localDate, t1) -> {
            if (t1 != null) {
                sellingDatePicker.setDayCellFactory(d ->
                        new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                setDisable(item.isBefore(t1));
                            }
                        });
            }
        });

        creationDatePicker.setValue(investment.getCreationDate());
        statusLbl.setText(investState.name());
        platformLbl.setText(investment.getPlatform().getName());
        stockNameTxtFld.setText(investment.getStockName());
        exchangeRateTxtFld.setText(BigDecimalUtils.format(investment.getExchangeRate()));
        capitalTxtFld.setText(BigDecimalUtils.format(investment.getCapital()));
        performanceLbl.setText(BigDecimalUtils.format(investment.getPerformance()));
        percentPerformanceLbl.setText(BigDecimalUtils.format(investment.getPercentPerformance()) + "%");
        costLbl.setText(BigDecimalUtils.format(investment.getCost()));
        if (investState == State.CLOSED) {
            sellingPriceTxtFld.setText(BigDecimalUtils.format(investment.getSellingPrice()));
            sellingDatePicker.setValue(investment.getSellingDate());
        }

        holdingPeriodLbl.setText(investment.getHoldingPeriod() + " days");

        this.investment = investment;
    }

    /**
     * Creates and adds a changeListener to the controller
     * items of the investment to regular the accessibility of the apply button
     */
    private void initializeApplyListener() {
        //Listener of the add investment attributes to make the btnAddInvestment enable/disable
        ChangeListener<Object> FieldValidityListener = (observable, oldValue, newValue) -> {
            boolean someInputIsInvalid = creationDatePicker == null
                    || !DoubleUtil.isValidDouble(exchangeRateTxtFld)
                    || !DoubleUtil.isValidDouble(capitalTxtFld)
                    || stockNameTxtFld.getText().isEmpty()
                    //To be a valid investment the sellingDate and sellingPrice has to be both
                    // invalid or both attributes has to be valid
                    || (sellingDatePicker.getValue() != null ^ DoubleUtil.isValidDouble(sellingPriceTxtFld));

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
        investment.setCreationDate(creationDatePicker.getValue());
        investment.setStockName(stockNameTxtFld.getText());
        investment.setExchangeRate(BigDecimalUtils.parse(exchangeRateTxtFld.getText()));
        investment.setCapital(BigDecimalUtils.parse(capitalTxtFld.getText()));
        //TODO for null
        investment.closeInvestment(sellingDatePicker.getValue(),
                BigDecimalUtils.parse(sellingPriceTxtFld.getText())
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
        loadSellingPriceCalculatorController(investment);
    }

    /**
     * Handles the "performance calculator" Button and hands over
     * the current investment.
     */
    @FXML
    private void handlePerformanceCalculator() {
        loadPerformanceCalculatorController(investment);
    }
}
