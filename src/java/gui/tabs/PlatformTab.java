package gui.tabs;

import gui.ApplicationMain;
import gui.DoubleUtil;
import gui.calculator.FeeCalculatorController;
import gui.platformController.NewPlatformController;
import gui.platformController.PlatformController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.investmentBook.InvestmentBook;
import logic.platform.FeeType;
import logic.platform.MixedPlatform;
import logic.platform.PercentPlatform;
import logic.platform.Platform;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.DialogWindow.*;
import static gui.UserInterfaceController.isDoubleClicked;

public class PlatformTab implements Initializable {

    @FXML
    private TextField platformSearchbarTxtFld;
    @FXML
    public Button platformCleanSearchBar;
    @FXML
    private ChoiceBox<FeeType> feeTypeChcBx;
    @FXML
    private Button btnApplyPlatformFilter;
    @FXML
    private Button btnResetPlatformFilter;
    @FXML
    private TableView<Platform> platformTableView;
    @FXML
    private VBox wrapVBoxPlatforms;
    @FXML
    private Label platformNameLbl;

    // TODO: 18.09.2022 include to the fxml
    @FXML
    private Label platformTypLbl;
    @FXML
    private Label platformFeeLbl;
    @FXML
    private Label platformMinFeeLbl;
    @FXML
    private Button btnEditPlatform;
    @FXML
    private Button btnFeeCalculator;
    @FXML
    private Button btnDeletePlatform;

    /**
     * The current {@link InvestmentBook}
     */
    private InvestmentBook investmentBook;

    /**
     *
     */
    private Platform currentPlatform;

    /**
     *
     */
    private Tab tab;

    /**
     * @return
     */
    public static PlatformTab createPlatformTab() {
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("tabs/PlatformTab.fxml"));

        Tab tab;
        try {
            tab = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PlatformTab platformTab = loader.getController();
        platformTab.tab = tab;
        tab.setText("Platforms");
        tab.setClosable(false);

        return platformTab;
    }

    /**
     * @return
     */
    public Tab getTab() {
        return tab;
    }

    /**
     * @return
     */
    public TableView<Platform> getPlatformTableView() {
        return platformTableView;
    }

    /**
     * @param investmentBook
     */
    public void setInvestmentBook(InvestmentBook investmentBook) {
        this.investmentBook = investmentBook;
    }

    /**
     * Initialize the investment tab, the Tableview and
     * sets the default values
     */
    private void initializeTblVw() {
        ObservableList<TableColumn<Platform, ?>> columns = platformTableView.getColumns();

        TableColumn<Platform, String> typColumn = new TableColumn<>("fee type");
        typColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        columns.add(typColumn);
        TableColumn<Platform, String> nameColumn = new TableColumn<>("name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        columns.add(nameColumn);

        platformTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        platformTableView.prefHeightProperty().bind(wrapVBoxPlatforms.heightProperty());

        platformTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    currentPlatform = newValue;
                    showPlatformDetails(newValue);

                    boolean isNull = newValue == null;
                    btnEditPlatform.setDisable(isNull);
                    btnFeeCalculator.setDisable(isNull);
                    btnDeletePlatform.setDisable(isNull);
                }
        );

        platformTableView.setOnMouseClicked(mouseEvent -> {
            if (isDoubleClicked(mouseEvent)) {
                Platform platform = platformTableView.getSelectionModel().getSelectedItem();
                editPlatform(platform);
            }
        });
    }

    /**
     * Initialize the platform tab and
     * sets the default values
     */
    private void initializeFeeTypeChcBx() {
        feeTypeChcBx.getItems().addAll(FeeType.values());
        feeTypeChcBx.valueProperty().addListener((observableValue, feeType, t1) -> {
            boolean b = t1 == null;
            btnApplyPlatformFilter.setDisable(b);
            btnResetPlatformFilter.setDisable(b);
        });
    }

    /**
     *
     */
    // TODO: 28.06.2022 JavaDoc
    private void initializePlatformSearchBar() {
        platformSearchbarTxtFld.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                // Compare name of every investment with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                investmentBook.filterPlatformsByName(lowerCaseFilter);
                platformCleanSearchBar.setVisible(true);
            } else {
                // If filter text is empty, display all platforms.
                handlePlatformCleanSearchBar();
            }
        });
    }

    /**
     * Fills all text fields to show details about the person.
     * If the specified person is null, all text fields are cleared.
     *
     * @param platform the investment or null
     */
    private void showPlatformDetails(Platform platform) {
        if (platform != null) {
            platformNameLbl.setText(platform.getName());
            platformTypLbl.setText(platform.getType().getName());

            String fee = "";

            if (platform instanceof PercentPlatform percentPlatform) {
                fee = DoubleUtil.format(percentPlatform.getPercent()) + " %";
                platformMinFeeLbl.setText("");
            } else if (platform instanceof MixedPlatform mixedPlatform) {
                fee = DoubleUtil.format(mixedPlatform.getPercent()) + " %";
                platformMinFeeLbl.setText(DoubleUtil.formatMoney(mixedPlatform.getMinFee()));
            } else {
                fee = DoubleUtil.formatMoney(platform.getFee(new BigDecimal(100)));
                platformMinFeeLbl.setText("");
            }
            platformFeeLbl.setText(fee);
        } else {
            // platform is null, remove all the text.
            platformNameLbl.setText("");
            platformFeeLbl.setText("");
            platformMinFeeLbl.setText("");
        }
    }

    @FXML
    public void handlePlatformCleanSearchBar() {
        platformSearchbarTxtFld.setText("");
        investmentBook.displayPlatforms();
        platformCleanSearchBar.setVisible(false);
    }

    /**
     * Cleans the options of the filter items to their default values
     */
    private void cleanPlatformFilter() {
        feeTypeChcBx.setValue(null);

        btnApplyPlatformFilter.setDisable(true);
        btnResetPlatformFilter.setDisable(true);
    }

    /**
     * Creates and displays new {@link PlatformController}
     * also it hands over the selected platform and the current
     * investmentBookView which should be transmitted to the created platformController
     *
     * @param event mouseEvent to examine a double-click
     */
    @FXML
    private void clickPlatform(MouseEvent event) {
        if (isDoubleClicked(event)) {
            handleEditPlatform();
        }
    }

    /**
     * Handles the "apply" filter Button
     */
    @FXML
    private void handleApplyPlatformFilter() {
        investmentBook.filterPlatformsByType(feeTypeChcBx.getValue());
    }

    /**
     * Handles the "reset" filter Button and
     * displays the default investments
     */
    @FXML
    private void handleResetPlatformFilter() {
        cleanPlatformFilter();
        investmentBook.displayPlatforms();
    }

    /**
     * Handles the "Add" Button and hands over the value for
     * a new Platform.
     */
    @FXML
    public void handleAddPlatform() {
        NewPlatformController newPlatformController = createStage(
                "platformController/NewPlatformController.fxml",
                "new Platform",
                400,
                300
        );
        newPlatformController.setInvestmentBook(investmentBook);
    }

    private void editPlatform(Platform platform) {
        if (platform != null) {
            final Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(
                    ApplicationMain.class.getResource(platform.getFxmlPath()));

            // Icon/logo of the application
            newStage.setTitle("Edit Platform");
            newStage.setMinWidth(400D);
            newStage.setMinHeight(300D);
            newStage.setResizable(false);
            newStage.initModality(Modality.APPLICATION_MODAL);
            try {
                newStage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                displayError(e);
            }

            styleStage(newStage);

            newStage.show();

            PlatformController platformController = loader.getController();

            platformController.display(platform);
        }
    }

    @FXML
    public void handleEditPlatform() {
        editPlatform(currentPlatform);
    }

    @FXML
    private void handleFeeCalculator() {
        FeeCalculatorController.loadFeeCalculatorController(currentPlatform);
    }

    /**
     * Handles the "delete selected row" Button and
     * deletes the selected platform.
     */
    @FXML
    private void handleDeletePlatform() {
        if (acceptedDeleteAlert()) {
//            Platform selectedPlatform = platformLstVw.getSelectionModel().getSelectedItem();
            Platform selectedPlatform = platformTableView.getSelectionModel().getSelectedItem();
            investmentBook.remove(selectedPlatform);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTblVw();
//        initializeLstVw();
        initializeFeeTypeChcBx();
        initializePlatformSearchBar();
        cleanPlatformFilter();
    }
}
