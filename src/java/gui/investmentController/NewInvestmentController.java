package gui.investmentController;

import gui.DoubleUtil;
import gui.Style;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.Investment;
import logic.State;
import logic.investmentBook.InvestmentBook;
import logic.platform.Platform;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class NewInvestmentController implements Initializable {

    @FXML
    private DatePicker creationDatePicker;
    @FXML
    public ChoiceBox<State> stateChcBx;
    @FXML
    private ChoiceBox<Platform> platformChcBx;
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
    private DatePicker sellingDatePicker;
    @FXML
    private Button btnApply;
    @FXML
    private Button btnCancel;

    /**
     * the current {@link InvestmentBook}
     */
    private InvestmentBook investmentBook;

    /**
     * Sets the current {@link InvestmentBook}
     *
     * @param investmentBook the over handed investment
     */
    public void setInvestmentBook(InvestmentBook investmentBook) {
        assert investmentBook != null;

        this.investmentBook = investmentBook;
        stateChcBx.getItems().addAll(State.values());
        stateChcBx.setValue(stateChcBx.getItems().get(0));

        if (!investmentBook.getPlatforms().isEmpty()) {
            platformChcBx.getItems().addAll(investmentBook.getPlatforms());
            platformChcBx.setValue(platformChcBx.getItems().get(0));
        }
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

        stockNameTxtFld.textProperty().addListener(FieldValidityListener);
        exchangeRateTxtFld.textProperty().addListener(FieldValidityListener);
        capitalTxtFld.textProperty().addListener(FieldValidityListener);
        sellingPriceTxtFld.textProperty().addListener(FieldValidityListener);
        sellingDatePicker.valueProperty().addListener(FieldValidityListener);
        sellingDatePicker.setDayCellFactory(d -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isBefore(creationDatePicker.getValue()));
            }
        });
    }

    /**
     * Creates and adds a changeListener to the add investment items
     * to regular the accessibility of the add platform button
     */
    private void initializeAddInvestmentListener() {
        //Listener to regular the accessibility of the add investment button
        ChangeListener<Object> FieldValidityListener = (observable, oldValue, newValue) -> {
            boolean someInputIsInvalid = creationDatePicker == null
                    || platformChcBx.getValue() == null
                    || !DoubleUtil.isValidDouble(exchangeRateTxtFld)
                    || !DoubleUtil.isValidDouble(capitalTxtFld)
                    || stockNameTxtFld.getText().isEmpty()
                    //To be a valid investment the sellingDate and sellingPrice has to be both
                    // invalid or both attributes has to be valid
                    || (sellingDatePicker.getValue() != null ^ DoubleUtil.isValidDouble(sellingPriceTxtFld));

            btnApply.setDisable(someInputIsInvalid);
        };

        creationDatePicker.valueProperty().addListener(FieldValidityListener);
        platformChcBx.valueProperty().addListener(FieldValidityListener);
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
        creationDatePicker.setValue(LocalDate.now());

        initializeApplyListener();
        initializeAddInvestmentListener();
    }

    /**
     * Closes the Window.
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private boolean isValid(TextField textField) {
        if (!textField.getText().isEmpty()) {
            return DoubleUtil.parse(sellingPriceTxtFld.getText()) > 0;
        }
        return false;
    }

    /**
     * Handles the "Apply" Button and hands over
     * the (new) {@link Investment} attributes
     */
    @FXML
    //TODO outsource to a methode in investmentBook
    private void handleApply() {
        LocalDate sellingDate = sellingDatePicker.getValue();

        Investment newInvestment;
        if (isValid(sellingPriceTxtFld) && sellingDate != null) {
            newInvestment = new Investment(creationDatePicker.getValue(),
                    platformChcBx.getValue(),
                    stockNameTxtFld.getText(),
                    DoubleUtil.parse(exchangeRateTxtFld.getText()),
                    DoubleUtil.parse(capitalTxtFld.getText()),
                    DoubleUtil.parse(sellingPriceTxtFld.getText()),
                    sellingDate);
        } else {
            newInvestment = new Investment(creationDatePicker.getValue(),
                    platformChcBx.getValue(),
                    stockNameTxtFld.getText(),
                    DoubleUtil.parse(exchangeRateTxtFld.getText()),
                    DoubleUtil.parse(capitalTxtFld.getText()));
        }
        investmentBook.add(newInvestment);
        handleCancel();
    }

}
