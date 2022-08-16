package gui.platformController;

import gui.DoubleUtil;
import gui.Style;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.investmentBook.InvestmentBook;
import logic.platform.FeeType;
import logic.platform.Platform;

import java.net.URL;
import java.util.ResourceBundle;

import static gui.Style.SYMBOL_OF_CURRENCY;
import static logic.platform.FeeType.ABSOLUTE;
import static logic.platform.FeeType.MIXED;

public class NewPlatformController implements Initializable {

    @FXML
    private TextField nameTxtFld;
    @FXML
    private ChoiceBox<FeeType> feeTypeChcBx;
    @FXML
    private TextField feeTxtFld;
    @FXML
    private Label feeTypeSymbolLbl;
    @FXML
    private TextField minTxtFld;
    @FXML
    private Label minFeeCurrencyLbl;
    @FXML
    private Button btnApply;
    @FXML
    private Button btnCancel;

    private InvestmentBook investmentBook;

    /**
     *
     * @param investmentBook
     */
    // TODO: 27.06.2022 JavaDoc
    public void setInvestmentBook(InvestmentBook investmentBook){
        this.investmentBook = investmentBook;
    }

    /**
     * Creates and adds a changeListener to the controller items
     * to regular the accessibility of the apply button
     */
    private void initializeListener() {
        //Listener of the add platform attributes to make the btnAddPlatform enable or disable
        ChangeListener<Object> FieldValidityListener = (observable, oldValue, newValue) -> {
            FeeType feeType = feeTypeChcBx.getValue();
            boolean isNotMixedPlatform = feeType != MIXED;
            boolean nameAndFeeAreInvalid =
                    nameTxtFld.getText().isEmpty() || !DoubleUtil.isValidDouble(feeTxtFld);

            if (isNotMixedPlatform) {
                minTxtFld.setText("");
                btnApply.setDisable(nameAndFeeAreInvalid);
            } else {
                // examines if all fields have invalid input
                btnApply.setDisable(nameAndFeeAreInvalid || !DoubleUtil.isValidDouble(minTxtFld));
            }
            minTxtFld.setDisable(isNotMixedPlatform);
            feeTypeSymbolLbl.setText(feeType == ABSOLUTE ? SYMBOL_OF_CURRENCY : "%");
        };

        feeTypeChcBx.getSelectionModel().selectedItemProperty().addListener(
                FieldValidityListener
        );
        nameTxtFld.textProperty().addListener(FieldValidityListener);
        feeTxtFld.textProperty().addListener(FieldValidityListener);
        minTxtFld.textProperty().addListener(FieldValidityListener);
    }

    /**
     * Initializes the application.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeListener();
        feeTypeChcBx.getItems().addAll(FeeType.values());
        feeTypeChcBx.setValue(FeeType.values()[0]);
        feeTypeSymbolLbl.setText("%");
        btnApply.setDisable(true);

        Style.setCurrenciesForLbls(
                minFeeCurrencyLbl
        );
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
     * the (new) {@link Platform}
     */
    @FXML
    private void handleApply() {
        Platform newPlatform = Platform.create(feeTypeChcBx.getValue(),
                nameTxtFld.getText(),
                DoubleUtil.parse(feeTxtFld.getText()),
                DoubleUtil.parse(minTxtFld.getText()));
        investmentBook.add(newPlatform);
        handleCancel();
    }
}
