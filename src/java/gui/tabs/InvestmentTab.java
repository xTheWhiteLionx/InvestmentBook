package gui.tabs;

import gui.ApplicationMain;
import gui.Style;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.BigDecimalUtils;
import logic.Investment;
import logic.Quarter;
import logic.State;
import logic.investmentBook.InvestmentBook;
import logic.platform.Platform;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;

import static gui.DialogWindow.ICON;
import static gui.DialogWindow.acceptedDeleteAlert;
import static gui.SaleController.loadSaleController;
import static gui.Style.SYMBOL_OF_CURRENCY;
import static gui.UserInterfaceController.isDoubleClicked;
import static gui.calculator.PerformanceCalculatorController.loadPerformanceCalculatorController;
import static gui.calculator.SellingPriceCalculatorController.loadSellingPriceCalculatorController;
import static gui.investmentController.EditInvestmentController.loadEditInvestmentController;
import static gui.investmentController.NewInvestmentController.loadNewInvestmentController;
import static logic.Quarter.getQuarterOfMonth;

public class InvestmentTab implements Initializable {

    /**
     * Content of the investment Tab
     */
    //investment filter attributes
    @FXML
    private ChoiceBox<State> statusChoiceBox;
    @FXML
    private ChoiceBox<Platform> platformChcBx;
    @FXML
    private TextField investmentSearchbarTxtFld;
    @FXML
    private Button investmentCleanSearchBar;
    @FXML
    private ChoiceBox<Month> monthChcBox;
    @FXML
    private RadioButton rdBtnFilterMonth;
    @FXML
    private ChoiceBox<Quarter> quarterChcBox;
    @FXML
    private RadioButton rdBtnFilterQuarter;
    @FXML
    private Spinner<Integer> yearSpinner;
    @FXML
    private CheckBox yearCheckBox;

    @FXML
    private Button btnApplyInvestmentFilter;
    @FXML
    private Button btnResetInvestmentFilter;

    //Tab Investment
    @FXML
    private TableView<Investment> investmentTblVw;
    @FXML
    private VBox wrapVBoxInvestments;
    @FXML
    private Label totalPerformanceLbl;

    @FXML
    private Label creationDateLbl;
    @FXML
    private Label statusLbl;
    @FXML
    private Label platformLbl;
    @FXML
    private Label stockNameLbl;
    @FXML
    private Label exchangeRateLbl;
    @FXML
    private Label capitalLbl;
    @FXML
    private Label sellingPriceLbl;
    @FXML
    private Label performanceLbl;
    @FXML
    private Label percentPerformanceLbl;
    @FXML
    private Label costLbl;
    @FXML
    private Label sellingDateLbl;
    @FXML
    private Label holdingPeriodLbl;
    @FXML
    private Button btnEditInvestment;
    @FXML
    private Button btnSaleInvestment;
    @FXML
    private Button btnSellingPriceCalculator;
    @FXML
    private Button btnPerformanceCalculator;
    @FXML
    private Button btnDeleteInvestment;

    /**
     * The current {@link InvestmentBook}
     */
    private InvestmentBook investmentBook;

    /**
     * The current {@link InvestmentBook}
     */
    private Investment currentInvestment;

    /**
     *
     */
    private Tab tab;

    private BorderPane borderPane;

    /**
     *
     * @return
     */
    public static InvestmentTab createInvestmentTab(){
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("tabs/InvestmentTab.fxml"));

        try {
            BorderPane borderPane = loader.load();
            InvestmentTab investmentTab = loader.getController();
            investmentTab.borderPane = borderPane;
//            tab.setText("Investments");
//            tab.setClosable(false);
            return investmentTab;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @return
     */
    public Tab getTab() {
        return tab;
    }

    /**
     *
     * @return
     */
    public BorderPane getBorderPane() {
        return borderPane;
    }

    /**
     *
     * @return
     */
    public TableView<Investment> getInvestmentTblVw() {
        return investmentTblVw;
    }

    /**
     *
     * @return
     */
    public Label getTotalPerformanceLbl() {
        return totalPerformanceLbl;
    }

    /**
     *
     * @return
     */
    public ChoiceBox<Platform> getPlatformChcBx() {
        return platformChcBx;
    }

    /**
     *
     * @param investmentBook
     */
    public void setInvestmentBook(InvestmentBook investmentBook) {
        this.investmentBook = investmentBook;
    }

    /**
     *
     */
    // TODO: 28.06.2022 JavaDoc
    private void initializeInvestmentSearchBar() {
        investmentSearchbarTxtFld.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                // Compare name of every investment with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                investmentBook.filterInvestmentsByName(lowerCaseFilter);
                investmentCleanSearchBar.setVisible(true);
            } else {
                // If filter text is empty, display all investments.
                handleInvestmentCleanSearchBar();
            }
        });
    }

    /**
     * Creates and adds the changeListeners to the filter items
     * to regular the accessibility of the filter apply button and
     * the toggle between the quarter and month filter option
     */
    private void initializeInvestmentFilter() {
        Month currMonth = LocalDate.now().getMonth();

        statusChoiceBox.getItems().addAll(State.values());
        statusChoiceBox.valueProperty().addListener((observableValue, state, t1) -> {
            btnApplyInvestmentFilter.setDisable(t1 == null);
            btnResetInvestmentFilter.setDisable(t1 == null);
        });

        platformChcBx.valueProperty().addListener((observableValue, state, t1) -> {
            btnApplyInvestmentFilter.setDisable(t1 == null);
            btnResetInvestmentFilter.setDisable(t1 == null);
        });

        monthChcBox.getItems().addAll(Month.values());
        monthChcBox.valueProperty().addListener((observableValue, state, t1) -> {
            btnApplyInvestmentFilter.setDisable(t1 == null && !yearCheckBox.isSelected());
            btnResetInvestmentFilter.setDisable(t1 == null && !yearCheckBox.isSelected());
        });

        quarterChcBox.getItems().addAll(Quarter.values());
        quarterChcBox.valueProperty().addListener((observableValue, state, t1) -> {
            btnApplyInvestmentFilter.setDisable(t1 == null && !yearCheckBox.isSelected());
            btnResetInvestmentFilter.setDisable(t1 == null && !yearCheckBox.isSelected());
        });

        yearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0));

        rdBtnFilterMonth.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (rdBtnFilterMonth.isSelected()) {
                yearCheckBox.setSelected(true);
                //sets the current month
                monthChcBox.setValue(currMonth);
                rdBtnFilterQuarter.setSelected(false);
            } else {
                monthChcBox.setValue(null);
            }
            monthChcBox.setDisable(!rdBtnFilterMonth.isSelected());
        });

        rdBtnFilterQuarter.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (rdBtnFilterQuarter.isSelected()) {
                yearCheckBox.setSelected(true);
                //sets the current quarter by the current month
                quarterChcBox.setValue(getQuarterOfMonth(currMonth));
                rdBtnFilterMonth.setSelected(false);
            } else {
                quarterChcBox.setValue(null);
            }
            quarterChcBox.setDisable(!rdBtnFilterQuarter.isSelected());
        });

        yearCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            int currYear = LocalDate.now().getYear();
            SpinnerValueFactory<Integer> valueFactory =
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(1970, currYear, currYear);

            if (!yearCheckBox.isSelected()) {
                rdBtnFilterMonth.setSelected(false);
                rdBtnFilterQuarter.setSelected(false);
                valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0);
            }
            yearSpinner.setValueFactory(valueFactory);
            yearSpinner.setDisable(!newValue);

            btnApplyInvestmentFilter.setDisable(!newValue);
            btnResetInvestmentFilter.setDisable(!newValue);
        });
    }

    /**
     * Initialize the investment tab, the Tableview and
     * sets the default values
     */
    private void initializeTblVw() {
        ObservableList<TableColumn<Investment, ?>> columns = investmentTblVw.getColumns();

        TableColumn<Investment, LocalDate> creationDateColumn = new TableColumn<>("creationDate");
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        columns.add(creationDateColumn);

        TableColumn<Investment, State> stateColumn = new TableColumn<>("state");
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        columns.add(stateColumn);

        TableColumn<Investment, Platform> platformColumn = new TableColumn<>("platform");
        platformColumn.setCellValueFactory(new PropertyValueFactory<>("platform"));
        TableColumn<Investment, String> stockNameColumn = new TableColumn<>("stock name");
        stockNameColumn.setCellValueFactory(new PropertyValueFactory<>("stockName"));
        TableColumn<Investment, Double> exchangeRateColumn = new TableColumn<>("exchange Rate" +
                " " + SYMBOL_OF_CURRENCY);
        exchangeRateColumn.setCellValueFactory(new PropertyValueFactory<>("exchangeRate"));
        TableColumn<Investment, Double> capitalColumn = new TableColumn<>("capital" + " " +
                SYMBOL_OF_CURRENCY);
        capitalColumn.setCellValueFactory(new PropertyValueFactory<>("capital"));
        TableColumn<Investment, Double> sellingPriceColumn = new TableColumn<>("selling Price" +
                " " + SYMBOL_OF_CURRENCY);
        sellingPriceColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        TableColumn<Investment, Double> absolutePerformanceColumn = new TableColumn<>(
                "performance" + " " + SYMBOL_OF_CURRENCY);
        absolutePerformanceColumn.setCellValueFactory(new PropertyValueFactory<>("performance"));
        TableColumn<Investment, Double> percentPerformanceColumn = new TableColumn<>(
                "performance %");
        percentPerformanceColumn.setCellValueFactory(new PropertyValueFactory<>(
                "percentPerformance"));
        TableColumn<Investment, Double> costColumn = new TableColumn<>("cost" + " " +
                SYMBOL_OF_CURRENCY);
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));

        investmentTblVw.getColumns().addAll(
                platformColumn,
                stockNameColumn,
                exchangeRateColumn,
                capitalColumn,
                costColumn
        );
        investmentTblVw.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        investmentTblVw.prefHeightProperty().bind(wrapVBoxInvestments.heightProperty());

        investmentTblVw.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    currentInvestment = newValue;
                    showInvestmentDetails(currentInvestment);
                    boolean isNull = newValue == null;
                    btnEditInvestment.setDisable(isNull);
                    btnSaleInvestment.setDisable(isNull || !currentInvestment.getState().isOpen());
                    btnPerformanceCalculator.setDisable(isNull);
                    btnSellingPriceCalculator.setDisable(isNull);
                    btnDeleteInvestment.setDisable(isNull);
                }
        );

        investmentTblVw.setOnMouseClicked(mouseEvent -> {
            if (isDoubleClicked(mouseEvent)) {
                Investment investment = investmentTblVw.getSelectionModel().getSelectedItem();
                loadEditInvestmentController(investment, investmentBook);
            }
        });
    }

    /**
     * Handles the "apply" filter Button
     */
    @FXML
    private void handleApplyInvestmentFilter() {
        investmentBook.filter(
                statusChoiceBox.getValue(),
                platformChcBx.getValue(),
                monthChcBox.getValue(),
                quarterChcBox.getValue(),
                yearSpinner.getValue()
        );
    }

    /**
     * Cleans the options of the filter items to their default values
     */
    private void cleanInvestmentFilter() {
        statusChoiceBox.setValue(null);
        platformChcBx.setValue(null);
        monthChcBox.setValue(null);
        quarterChcBox.setValue(null);

        rdBtnFilterMonth.setSelected(false);
        rdBtnFilterQuarter.setSelected(false);
        yearCheckBox.setSelected(false);
        monthChcBox.setDisable(true);
        quarterChcBox.setDisable(true);
        yearSpinner.setDisable(true);

        btnApplyInvestmentFilter.setDisable(true);
        btnResetInvestmentFilter.setDisable(true);
    }

    /**
     * Handles the "reset" filter Button and
     * displays the default investments
     */
    @FXML
    private void handleResetInvestmentFilter() {
        cleanInvestmentFilter();
        investmentBook.displayInvestments();
    }

    /**
     * Fills all text fields to show details about the person.
     * If the specified person is null, all text fields are cleared.
     *
     * @param investment the investment or null
     */
    private void showInvestmentDetails(Investment investment) {
        if (investment != null) {
            State investState = investment.getState();

            creationDateLbl.setText(Style.format(investment.getCreationDate()));
            statusLbl.setText(investState.name());
            platformLbl.setText(investment.getPlatform().getName());
            stockNameLbl.setText(investment.getStockName());
            exchangeRateLbl.setText(BigDecimalUtils.formatMoney(investment.getExchangeRate()));
            capitalLbl.setText(BigDecimalUtils.formatMoney(investment.getCapital()));
            performanceLbl.setText(BigDecimalUtils.formatMoney(investment.getPerformance()));
            percentPerformanceLbl.setText(
                    BigDecimalUtils.format(investment.getPercentPerformance()) + "%");
            costLbl.setText(BigDecimalUtils.formatMoney(investment.getCost()));
            if (investState == State.CLOSED) {
                sellingPriceLbl.setText(BigDecimalUtils.formatMoney(investment.getSellingPrice()));
                sellingDateLbl.setText(Style.format(investment.getSellingDate()));
            } else {
                sellingPriceLbl.setText("");
                sellingDateLbl.setText("");
            }
            holdingPeriodLbl.setText(investment.getHoldingPeriod() + " days");
        } else {
            // Investment is null, remove all the text.
            creationDateLbl.setText("");
            statusLbl.setText("");
            platformLbl.setText("");
            stockNameLbl.setText("");
            exchangeRateLbl.setText("");
            capitalLbl.setText("");
            performanceLbl.setText("");
            percentPerformanceLbl.setText("");
            costLbl.setText("");
            sellingPriceLbl.setText("");
            sellingDateLbl.setText("");
            holdingPeriodLbl.setText("");
        }
    }

    /**
     * Handles the "add" Button and hands over the value for
     * a new Investment.
     */
    @FXML
    private void handleEditInvestment() {
        loadEditInvestmentController(currentInvestment, investmentBook);
    }

    /**
     * Handles the "add" Button and hands over the value for
     * a new Investment.
     */
    @FXML
    public void handleAddInvestment() {
        if (!investmentBook.getPlatforms().isEmpty()) {
            loadNewInvestmentController(investmentBook);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(ICON);
            alert.setHeaderText("Platform needed.\n" +
                    "Please add first a platform");
            alert.setContentText("Each investment needs a platform");
            alert.showAndWait();
        }
    }

    /**
     * Handles the "delete selected row" Button and
     * deletes the selected investment.
     */
    @FXML
    private void handleDeleteInvestment() {
        if (acceptedDeleteAlert()) {
            Investment investment = investmentTblVw.getSelectionModel().getSelectedItem();
            investmentBook.remove(investment);
        }
    }

    /**
     *
     */
    @FXML
    private void handleInvestmentCleanSearchBar(){
        investmentSearchbarTxtFld.setText("");
        investmentBook.displayInvestments();
        investmentCleanSearchBar.setVisible(false);
    }

    /**
     * Handles the "selling price calculator" Button and hands over
     * the current investment.
     */
    @FXML
    private void handleSellingPriceCalculator() {
        loadSellingPriceCalculatorController(currentInvestment);
    }

    /**
     * Handles the "performance calculator" Button and hands over
     * the current investment.
     */
    @FXML
    private void handlePerformanceCalculator() {
        loadPerformanceCalculatorController(currentInvestment);
    }

    /**
     *
     */
    @FXML
    private void handleSaleInvestment() {
        loadSaleController(currentInvestment, investmentBook);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTblVw();
        initializeInvestmentFilter();
        initializeInvestmentSearchBar();
        cleanInvestmentFilter();
    }
}
