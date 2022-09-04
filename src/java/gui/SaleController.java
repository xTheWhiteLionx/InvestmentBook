package gui;

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
import logic.investmentBook.InvestmentBook;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static gui.DialogWindow.displayError;
import static gui.DoubleUtil.isValidDouble;

public class SaleController implements Initializable {

    @FXML
    private TextField sellingPriceTxtField;
    @FXML
    private Label sellingPriceLblCurrency;
    @FXML
    private DatePicker sellingDatePicker;
    @FXML
    private Button btnApply;
    @FXML
    private Button btnCancel;

    /**
     *
     */
    private InvestmentBook investmentBook;
    /**
     * The current controlled investment
     */
    private Investment currInvestment;

    /**
     *
     * @param investment
     */
    public static void loadSaleController(Investment investment, InvestmentBook investmentBook){
        SaleController saleController = createSaleController();
        saleController.currInvestment = investment;
        saleController.investmentBook = investmentBook;
    }

    /**
     *
     * @return
     */
    public static SaleController createSaleController() {
        final Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("SaleController.fxml"));

        // Icon/logo of the application
        newStage.getIcons().add(new Image("gui/textures/investmentBookIcon.png"));
        newStage.setTitle("sale");
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
        Style.setCurrenciesForLbls(sellingPriceLblCurrency);
        btnApply.setDisable(true);
        sellingDatePicker.setValue(LocalDate.now());
        sellingDatePicker.setDayCellFactory(d -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isBefore(currInvestment.getCreationDate()));
            }
        });
        sellingPriceTxtField.textProperty().addListener(
                (observableValue, oldValue, newValue) -> btnApply.setDisable(
                        !isValidDouble(newValue)));
    }

    @FXML
    private void handleApply() {
        BigDecimal sellingPrice = BigDecimalUtils.parse(sellingPriceTxtField.getText());
        currInvestment.closeInvestment(sellingDatePicker.getValue(), sellingPrice);
        investmentBook.displayInvestments();
        handleCancel();
    }

    @FXML
    private void handleCancel() {
     Stage stage = (Stage) btnCancel.getScene().getWindow();
     stage.close();
    }
}
