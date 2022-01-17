package gui;

import gui.calculator.FeeCalculatorController;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import logic.Investment;
import logic.InvestmentBook;
import logic.InvestmentBookView;
import logic.enums.FeeTypes;
import logic.enums.Quarter;
import logic.enums.Status;
import logic.platform.Platform;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;

import static gui.DialogWindow.saveDialogFile;
import static javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import static logic.GeneralMethods.*;
import static logic.enums.FeeTypes.*;
import static logic.enums.Quarter.getQuarterByMonth;


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
    @FXML
    public ProgressBar progressBar;


    /**
     * content of the platform Tab
     */
    @FXML
    private ChoiceBox<FeeTypes> feeTypesChcBox;
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
    private ListView<Platform> platformListView;

    /**
     * content of the investment Tab
     */
    //filter attributes
    @FXML
    private ChoiceBox<Status> statusChoiceBox;
    @FXML
    private ChoiceBox<Platform> platformChoiceBox;
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

    //investment attributes for a new investment
    @FXML
    private GridPane addInvestmentGrdPn;
    @FXML
    private DatePicker creationDatePicker;
    @FXML
    private ChoiceBox<Platform> platformChoiceBox2;
    @FXML
    private TextField stockNameTxtFld;
    @FXML
    private TextField exchangeRateTxtFld;
    @FXML
    private Label exchangeRateCurrencyLbl;
    @FXML
    private TextField capitalTxtFld;
    @FXML
    private Label capitalCurrencyLbl;
    @FXML
    private TextField sellingPriceTxtFld;
    @FXML
    private Label sellingPriceCurrencyLbl;
    @FXML
    private DatePicker sellingDatePicker;
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

//    private final Label[] CurrencyLbls = {
//            minCurrencyLbl,
//            exchangeRateCurrencyLbl,
//            capitalCurrencyLbl,
//            sellingPriceCurrencyLbl,
//            totalPerformanceCurrencyLbl
//    };

    /**
     * The current {@link InvestmentBookView}
     */
    private InvestmentBookView investmentBookVw;

    /**
     * The current file (name)
     */
    //TODO delete and pull it to the FileController
    //TODO create FileController/LoginController
//    private File currFile = new File("books/Investments.json");
    private File currFile;

    public void setFile(File selectedFile) {
        if (selectedFile == null) {
            selectedFile = new File(DIRECTORY + "newBook.json");
        }
        currFile = selectedFile;
        createInvestmentBookView(currFile);
    }

    //TODO JavaDoc
    private void initializeAddPlatformListener() {
        //Listener of the add platform attributes to make the btnAddPlatform enable or disable
        ChangeListener<Object> FieldValidityListener = (observable, oldValue, newValue) -> {
            FeeTypes feeType = feeTypesChcBox.getValue();
            boolean isNotMixedPlatform = feeType != MIXED;
            boolean nameAndFeeAreInvalid =
                    platformNameTxtFld.getText().isEmpty() || !isValidDouble(feeTxtFld);


            if (isNotMixedPlatform) {
                minTxtFld.setText("");
                btnAddPlatform.setDisable(nameAndFeeAreInvalid);
            } else {
                // examines if all fields have invalid input
                btnAddPlatform.setDisable(nameAndFeeAreInvalid || !isValidDouble(minTxtFld));
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

    //TODO JavaDoc
    private void initializePlatformTab() {

        feeTypesChcBox.getItems().addAll(FeeTypes.values());
        feeTypesChcBox.setValue(PERCENT);
        feeTypeSymbolLbl.setText("%");
        initializeAddPlatformListener();

        //TODO rename
        ChangeListener<Platform> selectedPlatformListener = (observable, oldValue, newValue) ->
                btnDeletePlatform.setDisable(newValue == null);

        platformListView.getSelectionModel().selectedItemProperty().addListener(
                selectedPlatformListener
        );
    }

    //TODO JavaDoc
    private void initializeAddInvestmentListener() {

//        Listener of the add investment attributes to make the btnAddInvestment enable/disable
        ChangeListener<Object> FieldValidityListener = (observable, oldValue, newValue) -> {
            boolean someInputIsInvalid = creationDatePicker == null
                    || platformChoiceBox2.getValue() == null
                    || !isValidDouble(exchangeRateTxtFld)
                    || !isValidDouble(capitalTxtFld)
                    || stockNameTxtFld.getText().isEmpty()
                    //To be a valid investment the sellingDate and sellingPrice has to be both
                    // invalid or both attributes has to be valid
                    || (sellingDatePicker.getValue() != null ^ isValidDouble(sellingPriceTxtFld));

            btnAddInvestment.setDisable(someInputIsInvalid);
        };

//        ChangeListener<Object> FieldValidityListener = createChangeListener(
//                creationDatePicker,
//                stockNameTxtFld,
//                exchangeRateTxtFld,
//                capitalTxtFld,
//                sellingPriceTxtFld,
//                sellingDatePicker,
//                btnAddInvestment);

        creationDatePicker.valueProperty().addListener(FieldValidityListener);
        platformChoiceBox2.valueProperty().addListener(FieldValidityListener);
        stockNameTxtFld.textProperty().addListener(FieldValidityListener);
        exchangeRateTxtFld.textProperty().addListener(FieldValidityListener);
        capitalTxtFld.textProperty().addListener(FieldValidityListener);
        sellingPriceTxtFld.textProperty().addListener(FieldValidityListener);
        sellingDatePicker.valueProperty().addListener(FieldValidityListener);
    }

    //TODO JavaDoc
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
                quarterChcBox.setValue(getQuarterByMonth(currMonth));
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
    }

    //TODO JavaDoc
    private void initializeInvestmentTab() {
        TableColumn<Investment, LocalDate> creationDateColumn = new TableColumn<>("creationDate");
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        TableColumn<Investment, Status> statusColumn = new TableColumn<>("status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<Investment, Platform> platformColumn = new TableColumn<>("platform");
        platformColumn.setCellValueFactory(new PropertyValueFactory<>("platform"));
        TableColumn<Investment, String> stockNameColumn = new TableColumn<>("stock name");
        stockNameColumn.setCellValueFactory(new PropertyValueFactory<>("stockName"));
        TableColumn<Investment, String> exchangeRateColumn = new TableColumn<>("exchange Rate" +
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

        statusChoiceBox.getItems().addAll(Status.values());
        monthChcBox.getItems().addAll(Month.values());
        quarterChcBox.getItems().addAll(Quarter.values());

        initializeAddInvestmentListener();
        initializeFilterListener();

        //TODO implement
        ChangeListener<Investment> c = (observable, oldValue, newValue) -> btnDeleteInvestment.setDisable(newValue == null);

        investmentTblVw.getSelectionModel().selectedItemProperty().addListener(c);
    }

//    //TODO JavaDoc
//    private void initializeCurrenciesLbl() {
//        Label[] CurrencyLbls = {
//                minCurrencyLbl,
//                exchangeRateCurrencyLbl,
//                capitalCurrencyLbl,
//                sellingPriceCurrencyLbl,
//                totalPerformanceCurrencyLbl
//        };
//
//        for (Label lbl : CurrencyLbls) {
//            System.out.println(lbl);
//            lbl.setText(SYMBOL_OF_CURRENCY);
//        }
//    }

    /**
     * Cleans the options of the filter interface to their default values
     */
    private void cleanFilter() {
        statusChoiceBox.setValue(null);
        platformChoiceBox.setValue(null);
        filterStockNameTxtFld.setText("");
        monthChcBox.setValue(null);
        quarterChcBox.setValue(null);

        rdBtnFilterMonth.setSelected(false);
        rdBtnFilterQuarter.setSelected(false);
        yearCheckBox.setSelected(false);
        monthChcBox.setDisable(true);
        quarterChcBox.setDisable(true);
        SpinnerValueFactory<Integer> valueFactory =
                new IntegerSpinnerValueFactory(0, 0, 0);
        yearSpinner.setValueFactory(valueFactory);
        yearSpinner.setDisable(true);
    }

    /**
     * Handles the "reset" Button for the add investment interface and
     * cleans the options to their default values
     */
    @FXML
    private void handleResetAddInvestment() {
        creationDatePicker.setValue(LocalDate.now());
        stockNameTxtFld.setText("");
        exchangeRateTxtFld.setText("");
        capitalTxtFld.setText("");
        sellingPriceTxtFld.setText("");
        sellingDatePicker.setValue(null);
        btnAddInvestment.setDisable(true);
    }

    /**
     * Handles the "reset" Button for the add platform interface and
     * cleans the options to their default values
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
     * Loads the {@link InvestmentBook} from the given file name.
     *
     * @param file the given file name
     * @return investmentBook the loaded investmentBook or
     * a new created investmentBook
     */
    private InvestmentBook loadInvestmentBook(File file) {
        if (file.exists()) {
            return readInvestmentBookFromJson(file);
        } else {
            return new InvestmentBook(new HashSet<>(), new ArrayList<>());
        }
    }

    //TODO JavaDoc
    private void createInvestmentBookView(File file) {
        //TODO implement
//        progressBar.progressProperty().bind(loadTask.progressProperty());
        this.investmentBookVw = new InvestmentBookView(
                loadInvestmentBook(file),
                new JavaFXGUI(
                        investmentTblVw,
                        platformListView,
                        platformChoiceBox,
                        platformChoiceBox2,
                        totalPerformanceLbl,
                        status)
        );
        status.setText("File loaded: " + file.getName());
        fileNameLbl.setText(file.getName());
        btnDeletePlatform.setDisable(true);
        btnDeleteInvestment.setDisable(true);
        handleResetAddPlatform();
        cleanFilter();
        handleResetAddInvestment();
    }

    /**
     * Initializes the application.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializePlatformTab();
        initializeInvestmentTab();
        Label[] currencyLbls = {
                minCurrencyLbl,
                exchangeRateCurrencyLbl,
                capitalCurrencyLbl,
                sellingPriceCurrencyLbl,
                totalPerformanceCurrencyLbl
        };
        setCurrenciesForLbls(currencyLbls);
    }

    /**
     * Handles the "Add" Button and hands over the Initializations.
     */
    @FXML
    //TODO JavaDoc
    private void handleAddPlatform() {
        investmentBookVw.addPlatform(
                feeTypesChcBox.getValue(),
                platformNameTxtFld.getText(),
                doubleOfTextField(feeTxtFld),
                minTxtFld.getText().isEmpty() ? 0 : doubleOfTextField(minTxtFld)
        );
        handleResetAddPlatform();
    }

    /**
     * Examines if the given mouseEvent where clicked double.
     * Returns true if the given mouseEvent where double-clicked
     * otherwise false.
     *
     * @param mouseEvent the given mouseEvent
     * @return true, if the given mouseEvent
     */
    private boolean isDoubleClicked(MouseEvent mouseEvent) {
        return mouseEvent.getClickCount() == 2;
    }

    @FXML
    //TODO JavaDoc
    private void clickPlatform(MouseEvent event) {
        //Checking double click
        if (isDoubleClicked(event)) {
            Platform currentPlatform =
                    platformListView.getSelectionModel().getSelectedItem();
            PlatformController platformController = createStage("PlatformController.fxml",
                    "Platform: " + currentPlatform.getName(),
                    650,
                    600
            );
            platformController.setCurrPlatform(currentPlatform);
            platformController.setInvestmentBookView(investmentBookVw);
        }
    }

    /**
     * Handles the "start new Game" button and opens a LoginInterface Window.
     */
    @FXML
    //TODO JavaDoc
    private void handleNewBook() {
        createInvestmentBookView(new File("newBook"));
    }

    /**
     * Handles the "load game" button.
     */
    @FXML
    //TODO JavaDoc
    private void handleLoadBook() {
        System.out.println("program is loading");
        saveDialogFile(investmentTblVw.getScene().getWindow());
        createInvestmentBookView(currFile);
    }

    /**
     * Handles the "save" button and
     * saves the current {@link InvestmentBookView}.
     */
    @FXML
    private void handleSaveBook() {
        writeInvestmentBookToJson(investmentBookVw.getInvestmentBook(), currFile);
    }

    /**
     * Handles the "save as" button by
     * calling the handleSaveBook methode.
     */
    @FXML
    private void handleSaveBookAs() {
        saveDialogFile(investmentTblVw.getScene().getWindow());
        handleSaveBook();
    }

    /**
     * Handles the "fee calculator" button and
     * opens a FeeCalculator Window. Also gives the
     * Controller the current platforms.
     */
    @FXML
    //TODO JavaDoc
    private void handleFeeCalculator() {
        FeeCalculatorController feeCalculator =
                createStage("calculator/FeeCalculatorController.fxml",
                        "Fee Calculator",
                        400,
                        200
                );
        feeCalculator.setPlatformChoiceBox(investmentBookVw.getPlatforms());
    }

    /**
     * Handles the "Add" Button and hands over the Initializations.
     */
    @FXML
    //TODO JavaDoc
    private void handleApplyFilter() {
        investmentBookVw.filter(
                statusChoiceBox.getValue(),
                platformChoiceBox.getValue(),
                filterStockNameTxtFld.getText(),
                monthChcBox.getValue(),
                quarterChcBox.getValue(),
                yearSpinner.getValue()
        );
    }

    /**
     * Handles the "Add" Button and hands over the Initializations.
     */
    //TODO JavaDoc
    @FXML
    private void handleResetFilter() {
        cleanFilter();
        investmentBookVw.updateInvestmentList();
    }

    @FXML
    //TODO JavaDoc
    private void clickInvestment(MouseEvent event) {
        //Checking double click
        if (isDoubleClicked(event)) {
            Investment currentInvestment =
                    investmentTblVw.getSelectionModel().getSelectedItem();
            InvestmentController investController = createStage("InvestmentController.fxml",
                    "Investment: " + currentInvestment.getStockName(),
                    650,
                    600
            );
            investController.setCurrInvestment(currentInvestment);
            investController.setInvestmentBookView(investmentBookVw);
        }
    }

    /**
     * Handles the "Add" Button and hands over the Initializations.
     */
    @FXML
    //TODO JavaDoc
    private void handleAddInvestment() {
        System.out.println(sellingPriceTxtFld.getText());
        double sellingPrice = !isValidDouble(sellingPriceTxtFld) ? 0 :
                doubleOfTextField(sellingPriceTxtFld);

        investmentBookVw.addInvestment(
                creationDatePicker.getValue(),
                platformChoiceBox2.getValue(),
                stockNameTxtFld.getText(),
                doubleOfTextField(exchangeRateTxtFld),
                doubleOfTextField(capitalTxtFld),
                sellingPrice,
                sellingDatePicker.getValue()
        );
        handleResetAddInvestment();
    }

    /**
     * creates a confirmation alert with the given content
     *
     * @param content of the alert
     * @return alert
     */
    //TODO note more alerts!
    private Alert createAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(alert.getAlertType().name().toLowerCase());
        alert.setContentText(content);
        return alert;
    }

    @FXML
    //TODO JavaDoc
    private void handleDeleteInvestment() {
        Alert alert = createAlert("Are your sure you want to delete the selected item?");
        ButtonType btnYes = new ButtonType("yes");
        ButtonType btnNo = new ButtonType("no");
        ButtonType btnCancel = new ButtonType("cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnYes, btnNo, btnCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnYes) {
            Investment selectedInvestment =
                    investmentTblVw.getSelectionModel().getSelectedItem();
            investmentBookVw.deleteInvestment(selectedInvestment);
        }
    }

    @FXML
    //TODO implement
    //TODO JavaDoc
    private void handleDeletePlatform() {
        Alert alert = createAlert("Are your sure you want to delete the selected item?");
        ButtonType btnYes = new ButtonType("yes");
        ButtonType btnNo = new ButtonType("no");
        ButtonType btnCancel = new ButtonType("cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnYes, btnNo, btnCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnYes) {
            Platform selectedPlatform =
                    platformListView.getSelectionModel().getSelectedItem();
            investmentBookVw.deletePlatform(selectedPlatform);
        }
    }

    /**
     * Handles the "exit" button.
     */
    @FXML
    //TODO JavaDoc
    private void handleExit() {
        //TODO AutoSave?

        if (autoSave.isSelected()) {
            handleSaveBook();
        }
        Stage stage = (Stage) investmentTblVw.getScene().getWindow();
        stage.close();
    }
}


