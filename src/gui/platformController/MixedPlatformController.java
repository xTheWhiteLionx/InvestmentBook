package gui.platformController;

import gui.Helper;
import gui.Style;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.platform.AbsolutePlatform;
import logic.platform.MixedPlatform;
import logic.platform.Platform;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller of the graphical file interface.
 *
 * @author xthe_white_lionx
 */
public class MixedPlatformController implements Initializable, PlatformController {

    @FXML
    private TextField nameTxtFld;
    @FXML
    private TextField percentTxtFld;
    @FXML
    private TextField minFeeTxtFld;
    @FXML
    private Label minFeeCurrencyLbl;
    @FXML
    private Button btnApply;
    @FXML
    private Button btnCancel;

    /**
     * The current controlled platform
     */
    private MixedPlatform currPlatform;

    /**
     * Sets and show the current {@link MixedPlatform} with his detailed information
     *
     * @param selectedPlatform the over handed platform
     */
    public void setCurrPlatform(MixedPlatform selectedPlatform) {
        assert selectedPlatform != null;

        this.currPlatform = selectedPlatform;
        nameTxtFld.setText(currPlatform.getName());
        percentTxtFld.setText(Helper.stringOfDouble(currPlatform.getPercent()));
        minFeeTxtFld.setText(Helper.stringOfDouble(currPlatform.getMinFee()));
    }

    /**
     * Creates and adds a changeListener to the controller
     * items of the investment to regular the accessibility of the apply button
     */
    private void initializeApplyListener() {
        //Listener of the add investment attributes to make the btnAddInvestment enable/disable
        ChangeListener<Object> FieldValidityListener = (observable, oldValue, newValue) -> {
            boolean someInputIsInvalid = nameTxtFld.getText().isEmpty()
                    || !Helper.isValidDouble(percentTxtFld)
                    || !Helper.isValidDouble(minFeeTxtFld);

            btnApply.setDisable(someInputIsInvalid);
        };

        nameTxtFld.textProperty().addListener(FieldValidityListener);
        percentTxtFld.textProperty().addListener(FieldValidityListener);
        minFeeTxtFld.textProperty().addListener(FieldValidityListener);
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
        currPlatform.setName(nameTxtFld.getText());
        currPlatform.setPercent(Helper.doubleOfTextField(percentTxtFld));
        currPlatform.setMinFee(Helper.doubleOfTextField(minFeeTxtFld));
        handleCancel();
    }

    @Override
    public void setDisplay(Platform platform) {
        assert platform != null;

        this.currPlatform = (MixedPlatform) platform;
        nameTxtFld.setText(currPlatform.getName());
        percentTxtFld.setText(Helper.stringOfDouble(currPlatform.getPercent()));
        minFeeTxtFld.setText(Helper.stringOfDouble(currPlatform.getMinFee()));
    }
}
