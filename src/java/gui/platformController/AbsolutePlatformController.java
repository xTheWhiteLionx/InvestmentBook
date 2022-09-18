package gui.platformController;

import gui.DoubleUtil;
import gui.Style;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.platform.AbsolutePlatform;
import logic.platform.Platform;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller of the graphical file interface.
 *
 * @author xthe_white_lionx
 */
public class AbsolutePlatformController implements Initializable, PlatformController {

    @FXML
    private TextField nameTxtFld;
    @FXML
    private Label typLbl;
    @FXML
    private TextField feeTxtFld;
    @FXML
    private Label feeCurrencyLbl;
    @FXML
    private Button btnApply;
    @FXML
    private Button btnCancel;

    /**
     * The current controlled platform
     */
    private AbsolutePlatform currPlatform;

    /**
     * Creates and adds a changeListener to the controller
     * items of the investment to regular the accessibility of the apply button
     */
    private void initializeApplyListener() {
        //Listener of the add investment attributes to make the btnAddInvestment enable/disable
        ChangeListener<Object> FieldValidityListener = (observable, oldValue, newValue) -> {
            boolean someInputIsInvalid = nameTxtFld.getText().isEmpty()
                    || !DoubleUtil.isValidDouble(feeTxtFld);

            btnApply.setDisable(someInputIsInvalid);
        };

        nameTxtFld.textProperty().addListener(FieldValidityListener);
        feeTxtFld.textProperty().addListener(FieldValidityListener);
    }

    /**
     * Initializes the application.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Style.setCurrenciesForLbls(feeCurrencyLbl);
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
        currPlatform.setFee(DoubleUtil.parse(feeTxtFld.getText()));
        handleCancel();
    }

    @Override
    public void display(Platform platform) {
        this.currPlatform = (AbsolutePlatform) platform;
        nameTxtFld.setText(currPlatform.getName());
        typLbl.setText(currPlatform.getType().getName());
        feeTxtFld.setText(DoubleUtil.format(currPlatform.getFee(new BigDecimal(100))));
    }
}
