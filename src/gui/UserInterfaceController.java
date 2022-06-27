package gui;

import gui.calculator.FeeCalculatorController;
import gui.investmentController.InvestmentController;
import gui.investmentController.NewInvestmentController;
import gui.platformController.PlatformController;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logic.Investment;
import logic.Quarter;
import logic.Status;
import logic.investmentBook.InvestmentBook;
import logic.investmentBook.InvestmentBookData;
import logic.platform.AbsolutePlatform;
import logic.platform.MixedPlatform;
import logic.platform.PercentPlatform;
import logic.platform.Platform;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

import static gui.DialogWindow.*;
import static gui.FeeType.*;
import static gui.Style.SYMBOL_OF_CURRENCY;
import static javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import static logic.Quarter.getQuarterOfMonth;


/**
 * Controller of the graphical user interface.
 *
 * @author xthewhitelionx
 */
public class UserInterfaceController implements Initializable {

    @FXML
    private Label fileNameLbl;
    @FXML
    private CheckMenuItem autoSave;

    //TODO implement
    @FXML
    public Label status;
    //TODO implement
    @FXML
    public ProgressBar progressBar;


    /**
     * content of the platform Tab
     */
    @FXML
    private ChoiceBox<FeeType> feeTypesChcBox;
    @FXML
    private TextField platformNameTxtFld;
    @FXML
    private TextField feeTxtFld;
    @FXML
    private Label feeTypeSymbolLbl;
    @FXML
    private TextField minTxtFld;
    @FXML
    private Label minCurrencyLbl;
    @FXML
    private Button btnAddPlatform;
    @FXML
    private Button btnDeletePlatform;
    @FXML
    private ListView<Platform> platformLstVw;

    /**
     * content of the investment Tab
     */
    //filter attributes
    @FXML
    private ChoiceBox<Status> statusChoiceBox;
    @FXML
    private ChoiceBox<Platform> platformChcBx;
    @FXML
    private TextField filterStockNameTxtFld;
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
    private Button btnAddInvestment;
    @FXML
    private Button btnDeleteInvestment;

    //Table
    @FXML
    private TableView<Investment> investmentTblVw;
    @FXML
    private Label totalPerformanceLbl;
    @FXML
    private Label totalPerformanceCurrencyLbl;

    /**
     *
     */
    //TODO JavaDoc
    private ChoiceBox[] platformChcBxs;

    /**
     *
     */
    //TODO JavaDoc
    private Label[] currencyLbls;

    /**
     * The current {@link InvestmentBook}
     */
    private InvestmentBook investmentBook;

    /**
     * The current file
     */
    private File currFile;

    /**
     * @param selectedFile
     */
    //TODO JavaDoc
    public void setFile(File selectedFile) {
        currFile = selectedFile;
        initInvestmentBook();
    }

    /**
     * Creates and adds a changeListener to the add platform items
     * to regular the accessibility of the add platform button
     */
    private void initializeAddPlatformListener() {
        //Listener of the add platform attributes to make the btnAddPlatform enable or disable
        ChangeListener<Object> FieldValidityListener = (observable, oldValue, newValue) -> {
            FeeType feeType = feeTypesChcBox.getValue();
            boolean isNotMixedPlatform = feeType != MIXED;
            boolean nameAndFeeAreInvalid =
                    platformNameTxtFld.getText().isEmpty() || !Helper.isValidDouble(feeTxtFld);

            if (isNotMixedPlatform) {
                minTxtFld.setText("");
                btnAddPlatform.setDisable(nameAndFeeAreInvalid);
            } else {
                // examines if all fields have invalid input
                btnAddPlatform.setDisable(nameAndFeeAreInvalid || !Helper.isValidDouble(minTxtFld));
            }
            minTxtFld.setDisable(isNotMixedPlatform);
            feeTypeSymbolLbl.setText(feeType == ABSOLUTE ? SYMBOL_OF_CURRENCY : "%");
        };

        feeTypesChcBox.getSelectionModel().selectedItemProperty().addListener(
                FieldValidityListener
        );
        platformNameTxtFld.textProperty().addListener(FieldValidityListener);
        feeTxtFld.textProperty().addListener(FieldValidityListener);
        minTxtFld.textProperty().addListener(FieldValidityListener);
    }

    /**
     * Initialize the platform tab and
     * sets the default values
     */
    private void initializePlatformTab() {

        feeTypesChcBox.getItems().addAll(FeeType.values());
        feeTypesChcBox.setValue(PERCENT);
        feeTypeSymbolLbl.setText("%");
        initializeAddPlatformListener();

        //changeListener to regular the accessibility of the delete row button
        platformLstVw.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) ->
                        btnDeletePlatform.setDisable(newValue == null)
        );
    }

    /**
     * Creates and adds the changeListeners to the filter items
     * to regular the accessibility of the filter apply button and
     * the toggle between the quarter and month filter option
     */
    private void initializeFilterListener() {
        Month currMonth = LocalDate.now().getMonth();

        ChangeListener<Boolean> filterMonthListener = (observable, oldValue, newValue) -> {
            if (rdBtnFilterMonth.isSelected()) {
                yearCheckBox.setSelected(true);
                //sets the current month
                monthChcBox.setValue(currMonth);
                rdBtnFilterQuarter.setSelected(false);
            } else {
                monthChcBox.setValue(null);
            }
            monthChcBox.setDisable(!rdBtnFilterMonth.isSelected());
        };

        ChangeListener<Boolean> filterQuarterListener = (observable, oldValue, newValue) -> {
            if (rdBtnFilterQuarter.isSelected()) {
                yearCheckBox.setSelected(true);
                //sets the current quarter by the current month
                quarterChcBox.setValue(getQuarterOfMonth(currMonth));
                rdBtnFilterMonth.setSelected(false);
            } else {
                quarterChcBox.setValue(null);
            }
            quarterChcBox.setDisable(!rdBtnFilterQuarter.isSelected());
        };

        ChangeListener<Boolean> filterYearListener = (observable, oldValue, newValue) -> {
            int currYear = LocalDate.now().getYear();
            SpinnerValueFactory<Integer> valueFactory =
                    new IntegerSpinnerValueFactory(1970, currYear, currYear);

            if (!yearCheckBox.isSelected()) {
                rdBtnFilterMonth.setSelected(false);
                rdBtnFilterQuarter.setSelected(false);
                valueFactory = new IntegerSpinnerValueFactory(0, 0, 0);
            }
            yearSpinner.setValueFactory(valueFactory);
            yearSpinner.setDisable(!yearCheckBox.isSelected());
        };

        rdBtnFilterMonth.selectedProperty().addListener(filterMonthListener);
        rdBtnFilterQuarter.selectedProperty().addListener(filterQuarterListener);
        yearCheckBox.selectedProperty().addListener(filterYearListener);
        yearSpinner.setValueFactory(new IntegerSpinnerValueFactory(0, 0, 0));
    }

    /**
     * Creates and adds the changeListeners to the filter items
     * to regular the accessibility of the filter apply button and
     * the toggle between the quarter and month filter option
     */
    private void initializeFilterOptions() {
        statusChoiceBox.getItems().addAll(Status.values());
        monthChcBox.getItems().addAll(Month.values());
        quarterChcBox.getItems().addAll(Quarter.values());
    }

    /**
     * Initialize the investment tab, the Tableview and
     * sets the default values
     */
    private void initializeInvestmentTab() {
        TableColumn<Investment, LocalDate> creationDateColumn = new TableColumn<>("creationDate");
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        TableColumn<Investment, Status> statusColumn = new TableColumn<>("status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
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
                creationDateColumn,
                statusColumn,
                platformColumn,
                stockNameColumn,
                exchangeRateColumn,
                capitalColumn,
                sellingPriceColumn,
                absolutePerformanceColumn,
                percentPerformanceColumn,
                costColumn
        );
        investmentTblVw.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        initializeFilterOptions();
        initializeFilterListener();

        investmentTblVw.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> btnDeleteInvestment.setDisable(newValue == null)
        );
    }

    /**
     * Cleans the options of the filter items to their default values
     */
    private void cleanFilter() {
        statusChoiceBox.setValue(null);
        platformChcBx.setValue(null);
        filterStockNameTxtFld.setText("");
        monthChcBox.setValue(null);
        quarterChcBox.setValue(null);

        rdBtnFilterMonth.setSelected(false);
        rdBtnFilterQuarter.setSelected(false);
        yearCheckBox.setSelected(false);
        monthChcBox.setDisable(true);
        quarterChcBox.setDisable(true);
        yearSpinner.setDisable(true);
    }

    /**
     * Handles the "reset" Button for the add platform interface and
     * cleans the items to their default values
     */
    @FXML
    private void handleResetAddPlatform() {
        platformNameTxtFld.setText("");
        feeTxtFld.setText("");
        minTxtFld.setText("");
        //handles the ability of the minTxtFld depending on the chosen fee type
        minTxtFld.setDisable(!feeTypesChcBox.getValue().equals(MIXED));
        btnAddPlatform.setDisable(true);
    }

    /**
     * Loads the {@link InvestmentBookData} from the given file name.
     *
     * @param file the given file name
     * @return investmentBook the loaded investmentBook or
     * a new created investmentBook
     */
    private InvestmentBookData loadInvestmentBook(File file) {
        if (file.exists()) {
            try {
                return InvestmentBookData.fromJson(file);
            } catch (IOException e) {
                exceptionAlert(e);
                e.printStackTrace();
            }
        } else {
            return new InvestmentBookData(new HashSet<>(), new ArrayList<>());
        }
        return null;
    }

    //TODO JavaDoc
    private void initInvestmentBook() {
        //TODO implement
//        progressBar.progressProperty().bind(loadTask.progressProperty());
        this.investmentBook = new InvestmentBook(
                loadInvestmentBook(currFile),
                new JavaFXGUI(
                        investmentTblVw,
                        platformLstVw,
                        platformChcBxs,
                        totalPerformanceLbl,
                        currencyLbls,
                        status)
        );
        status.setText("File loaded: " + currFile.getName());
        fileNameLbl.setText(currFile.getName());
        btnDeletePlatform.setDisable(true);
        btnDeleteInvestment.setDisable(true);
        handleResetAddPlatform();
        cleanFilter();
    }

    //TODO JavaDoc
    private void initInvestmentBook2() {
        this.investmentBook = new InvestmentBook(
                new HashSet<>(), new ArrayList<>(),
                new JavaFXGUI(
                        investmentTblVw,
                        platformLstVw,
                        platformChcBxs,
                        totalPerformanceLbl,
                        currencyLbls,
                        status)
        );
        status.setText("File loaded: " + currFile.getName());
        fileNameLbl.setText(currFile.getName());
        btnDeletePlatform.setDisable(true);
        btnDeleteInvestment.setDisable(true);
        handleResetAddPlatform();
        cleanFilter();
    }

    /**
     * Initializes the application.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        platformChcBxs = new ChoiceBox[]{platformChcBx};

        currencyLbls = new Label[]{minCurrencyLbl,
                totalPerformanceCurrencyLbl};

        initializePlatformTab();
        initializeInvestmentTab();
    }

    /**
     * Handles the "Add" Button and hands over the value for
     * a new Platform.
     */
    @FXML
    private void handleAddPlatform() {
        String platformName = platformNameTxtFld.getText();
        double fee = Helper.doubleOfTextField(feeTxtFld);

        Platform newPlatform = switch (feeTypesChcBox.getValue()) {
            case PERCENT -> new PercentPlatform(platformName, fee);
            case ABSOLUTE -> new AbsolutePlatform(platformName, fee);
            case MIXED -> new MixedPlatform(platformName, fee, Helper.doubleOfTextField(minTxtFld));
        };
        investmentBook.add(newPlatform);
        handleResetAddPlatform();
    }

    /**
     * Examines if the given mouseEvent where double-clicked.
     * Returns true if the given mouseEvent where double-clicked
     * otherwise false.
     *
     * @param mouseEvent the given mouseEvent
     * @return true, if the given mouseEvent
     */
    private boolean isDoubleClicked(MouseEvent mouseEvent) {
        return mouseEvent.getClickCount() == 2;
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
            Platform currentPlatform = platformLstVw.getSelectionModel().getSelectedItem();

            if (currentPlatform != null) {
                PlatformController platformController = Helper.createStage(
                        currentPlatform.getFxmlPath(),
                        "Platform: " + currentPlatform.getName(),
                        400,
                        300
                );
                platformController.setDisplay(currentPlatform);
            }
        }
    }

    /**
     * Handles the "new" button and creates a
     * new file and {@link InvestmentBook}
     */
    @FXML
    private void handleNewBook() {
        currFile = new File("newBook");
        initInvestmentBook2();
    }

    /**
     * Handles the "open" button.
     */
    @FXML
    private void handleOpenBook() {
        currFile = openDialogFile(investmentTblVw.getScene().getWindow());
        initInvestmentBook();
    }

    /**
     * Handles the "save" button and
     * saves the current {@link InvestmentBook}.
     */
    @FXML
    private void handleSaveBook() {
        try {
            new InvestmentBookData(investmentBook).toJson(currFile);
        } catch (IOException e) {
            exceptionAlert(e);
            e.printStackTrace();
        }
    }

    /**
     * Handles the "save as" button and opens a save dialog
     */
    @FXML
    private void handleSaveBookAs() {
        File selectedFile = saveDialogFile(investmentTblVw.getScene().getWindow());
        if (selectedFile != null) {
            try {
                new InvestmentBookData(investmentBook).toJson(selectedFile);
            } catch (IOException e) {
                exceptionAlert(e);
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the "fee calculator" button and
     * opens a FeeCalculator Window. Also gives the
     * Controller the current platforms.
     */
    @FXML
    private void handleFeeCalculator() {
        FeeCalculatorController feeCalculator =
                Helper.createStage("calculator/FeeCalculatorController.fxml",
                        "Fee Calculator",
                        400,
                        200
                );
        feeCalculator.setPlatformChoiceBox(investmentBook.getPlatforms());
    }

    /**
     * Handles the "apply" filter Button
     */
    @FXML
    private void handleApplyFilter() {
        investmentBook.filter(
                statusChoiceBox.getValue(),
                platformChcBx.getValue(),
                filterStockNameTxtFld.getText(),
                monthChcBox.getValue(),
                quarterChcBox.getValue(),
                yearSpinner.getValue()
        );
    }

    /**
     * Handles the "reset" filter Button and
     * displays the default investments
     */
    @FXML
    private void handleResetFilter() {
        cleanFilter();
        investmentBook.displayInvestments();
    }

    /**
     * Creates and displays new {@link InvestmentController}
     * also it hands over the selected investment and the current
     * investmentBookView which should be transmitted to the created investmentController
     *
     * @param event mouseEvent to examine a double-click
     */
    @FXML
    private void clickInvestment(MouseEvent event) {
        if (isDoubleClicked(event)) {
            Investment selectedInvestment = investmentTblVw.getSelectionModel().getSelectedItem();
            InvestmentController investController = Helper.createStage(
                    "investmentController/InvestmentController.fxml",
                    "Investment",
                    650,
                    600
            );
            investController.setCurrInvestment(selectedInvestment);
            investController.setInvestmentBookView(investmentBook);
        }
    }

    /**
     * Handles the "add" Button and hands over the value for
     * a new Investment.
     */
    @FXML
    private void handleAddInvestment() {
        NewInvestmentController newInvestmentController = Helper.createStage(
                "investmentController/NewInvestmentController.fxml",
                "new Investment",
                650,
                600
        );
        newInvestmentController.setInvestmentBook(investmentBook);
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
     * Handles the "delete selected row" Button and
     * deletes the selected platform.
     */
    @FXML
    private void handleDeletePlatform() {
        if (acceptedDeleteAlert()) {
            Platform selectedPlatform =
                    platformLstVw.getSelectionModel().getSelectedItem();
            investmentBook.remove(selectedPlatform);
        }
    }

    /**
     * Handles the "exit" button.
     */
    @FXML
    private void handleExit() {
        //TODO AutoSave?
        if (autoSave.isSelected()) {
            handleSaveBook();
        }
        Stage stage = (Stage) investmentTblVw.getScene().getWindow();
        stage.close();
    }
}


