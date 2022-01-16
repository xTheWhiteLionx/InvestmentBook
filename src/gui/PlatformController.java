package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import logic.InvestmentBookView;
import logic.platform.AbsolutePlatform;
import logic.platform.MixedPlatform;
import logic.platform.PercentPlatform;
import logic.platform.Platform;

import java.net.URL;
import java.util.ResourceBundle;

import static logic.GeneralMethods.stringOfDouble;

//TODO JavaDoc
public class PlatformController implements Initializable {

    @FXML
    private Label platform;
    @FXML
    private TextField feeTxtFld;
    @FXML
    private DatePicker sellingDatePicker;
    @FXML
    private TextField exchangeRateTxtField;
    @FXML
    private TextField capitalTxtField;
    @FXML
    private TextField sellingPriceTxtField;
    @FXML
    private Label capitalCurrency;
    @FXML
    private Label sellingPriceCurrency;
    @FXML
    private Label holdingPeriod;
    @FXML
    private Label percentPerformanceLbl;
    @FXML
    private Label performanceLbl;
    @FXML
    private Label exchangeRateCurrency;
    @FXML
    private Label costLbl;
    @FXML
    private Label status;
    @FXML
    private Button btnCancel;

    private InvestmentBookView investmentBookView;

    private Platform currPlatform;

    public void setInvestmentBookView(InvestmentBookView investmentBookView) {
        this.investmentBookView = investmentBookView;
    }

    public void setCurrPlatform(Platform currPlatform) {
        this.currPlatform = currPlatform;

        //TODO implement
        platform.setText(currPlatform.getName());
        double fee = 0;
        if (currPlatform.getClass().getComponentType() == AbsolutePlatform.class) {
            fee = ((AbsolutePlatform) currPlatform).getFee();
        } else if (currPlatform.getClass() == PercentPlatform.class) {
            fee = ((PercentPlatform) currPlatform).getPercent();
        } else {
            fee = ((MixedPlatform) currPlatform).getPercent();
            //TODO
            ((MixedPlatform) currPlatform).getMinFee();
        }
        feeTxtFld.setText(stringOfDouble(fee));
    }

    /**
     * Initializes the application.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    //TODO JavaDoc
    @FXML
    private void handleApply() {
        //TODO implement
    }

    //TODO JavaDoc
    @FXML
    private void handleSellingPriceCalculator() {
        //TODO implement
    }

    //TODO JavaDoc
    @FXML
    private void handleCancel() {
    }
}