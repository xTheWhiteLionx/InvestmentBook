package gui.calculator;

import gui.ApplicationMain;
import gui.DoubleUtil;
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
import logic.platform.Platform;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.DialogWindow.displayError;
import static gui.DoubleUtil.isValidDouble;
import static logic.BigDecimalUtils.parse;

/**
 * Controller of the graphical fee calculator.
 *
 * @author Fabian Hardt
 */
public class FeeCalculatorController implements Initializable {

    @FXML
    private TextField capitalTxtField;
    @FXML
    private Label capitalCurrencyLbl;
    @FXML
    private Label feeLbl;
    @FXML
    public Button applyBtn;
    @FXML
    private Button cancelBtn;

    private Platform currPlatform;

    public static void loadFeeCalculatorController(Platform platform) {
        FeeCalculatorController feeCalculator = createFeeCalculatorController(platform);
        feeCalculator.currPlatform = platform;
    }

    private static FeeCalculatorController createFeeCalculatorController(Platform currPlatform) {
        final Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("calculator/FeeCalculatorController.fxml"));

        // Icon/logo of the application
        newStage.getIcons().add(new Image("gui/textures/investmentBookIcon.png"));
        newStage.setTitle(currPlatform.getName() + " Fee Calculator");
        newStage.setMinWidth(400D);
        newStage.setMinHeight(200D);
        newStage.setResizable(false);
        newStage.initModality(Modality.WINDOW_MODAL);
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
        Style.setCurrenciesForLbls(capitalCurrencyLbl);
        applyBtn.setDisable(true);
        capitalTxtField.textProperty().addListener(
                (observableValue, oldValue, newValue) -> applyBtn.setDisable(
                        !isValidDouble(newValue)));
    }

    /**
     * Handles the "Apply" Button and display
     * the calculated fee.
     */
    //TODO JavaDoc
    @FXML
    private void handleCalculate() {
        BigDecimal capital = parse(capitalTxtField.getText());
        double fee = currPlatform.getFee(capital);
        feeLbl.setText(DoubleUtil.formatMoney(fee));
    }

    /**
     * Close the Window.
     */
    //TODO JavaDoc
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }
}
