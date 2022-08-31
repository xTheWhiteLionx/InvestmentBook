package gui.calculator;

import gui.ApplicationMain;
import gui.JarMain;
import gui.Settings;
import gui.Style;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.BigDecimalUtils;
import logic.Investment;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.DialogWindow.displayError;
import static gui.DoubleUtil.isValidDouble;

//TODO JavaDoc
public class SellingPriceCalculatorController implements Initializable {

    @FXML
    private TextField targetPerformance;
    @FXML
    private Label targetPerformanceCurrency;
    @FXML
    private Label sellingExchangeRateLbl;
    @FXML
    private Button btnCalculate;
    @FXML
    private Button btnCancel;

    /**
     * The current controlled investment
     */
    private Investment investment;

    /**
     *
     * @param investment
     */
    public static void loadSellingPriceCalculatorController(Investment investment) {
        SellingPriceCalculatorController sellingPriceCalculatorController =
                createSellingPriceCalculatorController(investment);
        sellingPriceCalculatorController.investment = investment;
    }

    /**
     *
     * @param investment
     * @return
     */
    public static SellingPriceCalculatorController createSellingPriceCalculatorController(
            Investment investment) {
        final Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("calculator/" +
                        "SellingPriceCalculatorController.fxml"));

        // Icon/logo of the application
        newStage.getIcons().add(new Image("gui/textures/investmentBookIcon.png"));
        newStage.setTitle("selling price calculator: " + investment.getStockName());
        newStage.setMinWidth(350D);
        newStage.setMinHeight(200D);
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
     * Initializes the application.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Style.setCurrenciesForLbls(targetPerformanceCurrency);
        btnCalculate.setDisable(true);
        targetPerformance.textProperty().addListener(
                (observableValue, oldValue, newValue) -> btnCalculate.setDisable(
                        !isValidDouble(newValue)));
    }

    /**
     * Handles the "Apply" Button and hands over the Initializations.
     */
    @FXML
    //TODO JavaDoc
    private void handleCalculate() {
        BigDecimal sellingExchangeRate =
                investment.getPlatform().calcSellingExchangeRate(investment,
                BigDecimalUtils.parse(targetPerformance.getText()));
        sellingExchangeRateLbl.setText(BigDecimalUtils.formatMoney(sellingExchangeRate));
    }

    /**
     * Close the Window.
     */
    @FXML
    //TODO JavaDoc
    private void handleCancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
