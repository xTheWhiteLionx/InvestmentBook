package gui.platformController;

import gui.FeeType;
import gui.Helper;
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
import logic.platform.AbsolutePlatform;
import logic.platform.MixedPlatform;
import logic.platform.PercentPlatform;
import logic.platform.Platform;

import java.net.URL;
import java.util.ResourceBundle;

import static gui.FeeType.*;

public class NewPlatformController implements Initializable {

    @FXML
    private TextField nameTxtFld;
    @FXML
    private ChoiceBox<FeeType> feeTypeChcBx;
    @FXML
    private TextField feeTxtFld;
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
     * Creates and adds a changeListener to the controller
     * items of the investment to regular the accessibility of the apply button
     */
    private void initializeApplyListener() {
        //Listener of the add investment attributes to make the btnAddInvestment enable/disable
        ChangeListener<Object> FieldValidityListener = (observable, oldValue, newValue) -> {
            boolean someInputIsInvalid = nameTxtFld.getText().isEmpty()
                    || !Helper.isValidDouble(feeTxtFld)
                    || !Helper.isValidDouble(minTxtFld);

            btnApply.setDisable(someInputIsInvalid);
        };

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
        Style.setCurrenciesForLbls(
                minFeeCurrencyLbl
        );
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
     * the (new) {@link AbsolutePlatform} attributes
     */
    @FXML
    //TODO outsource to a methode in investmentBook
    private void handleApply() {
        String platformName = nameTxtFld.getText();
        double fee = Helper.doubleOfTextField(feeTxtFld);

        Platform newPlatform = switch (feeTypeChcBx.getValue()) {
            case PERCENT -> new PercentPlatform(platformName, fee);
            case ABSOLUTE -> new AbsolutePlatform(platformName, fee);
            case MIXED -> new MixedPlatform(platformName, fee, Helper.doubleOfTextField(minTxtFld));
        };
        investmentBook.add(newPlatform);
        handleCancel();
    }
}
