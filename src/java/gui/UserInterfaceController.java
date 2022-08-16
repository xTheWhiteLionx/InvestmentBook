package gui;

import gui.investmentController.NewInvestmentController;
import gui.platformController.NewPlatformController;
import gui.platformController.PlatformController;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.GUIConnector;
import logic.Investment;
import logic.Quarter;
import logic.State;
import logic.investmentBook.InvestmentBook;
import logic.investmentBook.InvestmentBookData;
import logic.platform.FeeType;
import logic.platform.Platform;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;

import static gui.DialogWindow.*;
import static gui.Style.SYMBOL_OF_CURRENCY;
import static gui.investmentController.EditInvestmentController.loadEditInvestmentController;
import static javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import static logic.Quarter.getQuarterOfMonth;


/**
 * Controller of the graphical user interface.
 *
 * @author xthewhitelionx
 */
public class UserInterfaceController implements Initializable {
    @FXML
    private CheckMenuItem autoSave;
    @FXML
    private Menu themesMenu;

    //TODO implement
    @FXML
    private Label status;
    //TODO implement
    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextField platformNameTxtFld;
    @FXML
    private ChoiceBox<FeeType> feeTypeChcBx;
    @FXML
    private Button btnApplyPlatformFilter;
    @FXML
    private Button btnResetPlatformFilter;
    @FXML
    private Button btnDeletePlatform;
    @FXML
    private ListView<Platform> platformLstVw;

    /**
     * Content of the investment Tab
     */
    //investment filter attributes
    @FXML
    private ChoiceBox<State> statusChoiceBox;
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
    private Button btnApplyInvestmentFilter;
    @FXML
    private Button btnResetInvestmentFilter;

    //Tab Investment
    @FXML
    private TableView<Investment> investmentTblVw;
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
    private Label exchangeRateCurrencyLbl;
    @FXML
    private Label capitalLbl;
    @FXML
    private Label capitalCurrencyLbl;
    @FXML
    private Label sellingPriceLbl;
    @FXML
    private Label sellingPriceCurrencyLbl;
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
    private Button btnNewInvestment;
    @FXML
    private Button btnEditInvestment;
    @FXML
    private Button btnDeleteInvestment;

    /**
     *
     */
    //TODO JavaDoc
    private ChoiceBox[] platformChcBxs;

    /**
     * The current {@link InvestmentBook}
     */
    private InvestmentBook investmentBook;

    /**
     * The current {@link InvestmentBook}
     */
    private Investment currentInvestment;

    /**
     * The current file
     */
    private File currFile;

    /**
     * Initialize a new game with the specified arguments by using the
     * methode {@link #loadGameController()}
     *
     * @param involved    array of {@code boolean}s true if someone plays otherwise false
     * @param names       name of each player in an array
     * @param playerTypes typ of each player in an array
     * @throws NullPointerException if involved, names or playerTypes is null
     */
    public static void loadUserInterfaceController() {
        UserInterfaceController controller = createUserInterfaceController("newBook.json");

        controller.setCurrFile(new File(DIRECTORY + "/newBook.json"));
        controller.investmentBook = new InvestmentBook(controller.createJavaFXGUI());
    }

    /**
     * Initialize a new game with the specified arguments by using the
     * methode {@link #loadGameController()}
     *
     * @param involved    array of {@code boolean}s true if someone plays otherwise false
     * @param names       name of each player in an array
     * @param playerTypes typ of each player in an array
     * @throws NullPointerException if involved, names or playerTypes is null
     */
    public static void loadUserInterfaceController(File selectedFile) throws IOException {
        if (selectedFile == null) {
            throw new NullPointerException("selectedFile == null");
        }

        InvestmentBookData investmentBookData = InvestmentBookData.fromJson(selectedFile);
        UserInterfaceController controller = createUserInterfaceController(selectedFile.getName());

        controller.setCurrFile(selectedFile);
        controller.investmentBook = new InvestmentBook(investmentBookData, controller.createJavaFXGUI());
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

        initializeThemeMenu();
        initializePlatformTab();
        initializeInvestmentTab();
        toDefault();
    }

    /**
     * closes the current stage/window and creates a new {@link UserInterfaceController}
     * also it hands over the chosen file
     *
     * @param selectedFile the chosen file which should be
     *                     transmitted to the created UserInterfaceController
     */
    private static UserInterfaceController createUserInterfaceController(String fileName) {
        final Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("UserInterfaceController.fxml"));

        newStage.setMaximized(true);
        // Icon/logo of the application
        newStage.getIcons().add(new Image("gui/textures/investmentBookIcon.png"));
        newStage.setMinWidth(1200D);
        newStage.setMinHeight(650D);
        newStage.initModality(Modality.WINDOW_MODAL);
        try {
            newStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            displayError(e);
        }
        newStage.show();

        return loader.getController();
    }

    /**
     * Creates a new {@link JavaFXGUI} with the fields of this class
     *
     * @return new JavaFXGUI with the data fields of this class
     */
    private GUIConnector createJavaFXGUI() {
        return new JavaFXGUI(
                investmentTblVw,
                platformLstVw,
                platformChcBxs,
                totalPerformanceLbl,
                status);
    }

    /**
     * @param currFile
     */
    private void setCurrFile(File currFile) {
        this.currFile = currFile;
        Stage stage = (Stage) investmentTblVw.getScene().getWindow();
        stage.setTitle(currFile.getName());
    }

    /**
     *
     */
    private void initializeThemeMenu() {
        ToggleGroup toggleGroup = new ToggleGroup();

        RadioMenuItem light_theme = new RadioMenuItem("Light");
        light_theme.setSelected(true);
        themesMenu.getItems().add(light_theme);
        toggleGroup.getToggles().add(light_theme);
        light_theme.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                btnDeletePlatform.getScene().getStylesheets().removeAll();
            }
        });

        File themes = new File("src/resources/gui/themes");
        File[] styleSheets = themes.listFiles();

        if (styleSheets != null) {
            for (File styleSheet : styleSheets) {
                String styleSheetName = styleSheet.getName();
                RadioMenuItem theme = new RadioMenuItem(styleSheetName.substring(0,
                        styleSheetName.indexOf(".")));

                toggleGroup.getToggles().add(theme);
                theme.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
                    if (t1) {
                        System.out.println("styleSheetName = " + getClass().getResource(
                                "themes/" + styleSheetName).toExternalForm());
                        btnDeletePlatform.getScene().getStylesheets().add(getClass().getResource(
                                "themes/" + styleSheetName).toExternalForm());
                    } else {
                        btnDeletePlatform.getScene().getStylesheets().remove(getClass().getResource(
                                "themes/" + styleSheetName).toExternalForm());
                    }
                });
                themesMenu.getItems().add(theme);
            }
        }
    }

    /**
     * Initialize the platform tab and
     * sets the default values
     */
    private void initializePlatformTab() {
        feeTypeChcBx.getItems().addAll(FeeType.values());
        feeTypeChcBx.valueProperty().addListener((observableValue, feeType, t1) -> {
            boolean b = t1 == null;
            btnApplyPlatformFilter.setDisable(b);
            btnResetPlatformFilter.setDisable(b);
        });

        //changeListener to regular the accessibility of the delete row button
        platformLstVw.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) ->
                        btnDeletePlatform.setDisable(newValue == null)
        );

        initializePlatformSearch();
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

        yearSpinner.setValueFactory(new IntegerSpinnerValueFactory(0, 0, 0));

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
                    new IntegerSpinnerValueFactory(1970, currYear, currYear);

            if (!yearCheckBox.isSelected()) {
                rdBtnFilterMonth.setSelected(false);
                rdBtnFilterQuarter.setSelected(false);
                valueFactory = new IntegerSpinnerValueFactory(0, 0, 0);
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
    private void initializeInvestmentTab() {
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
//        columns.add(exchangeRateColumn);
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
//                creationDateColumn,
//                stateColumn,
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

        investmentTblVw.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    currentInvestment = newValue;
                    showInvestmentDetails(currentInvestment);
                    boolean isNull = newValue == null;
                    btnEditInvestment.setDisable(isNull);
                    btnDeleteInvestment.setDisable(isNull);
                }
        );

        initializeInvestmentFilter();
        initializeInvestmentSearch();

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
     * Cleans the options of the filter items to their default values
     */
    private void cleanPlatformFilter() {
        feeTypeChcBx.setValue(null);

        btnApplyPlatformFilter.setDisable(true);
        btnResetPlatformFilter.setDisable(true);
    }

    /**
     * Loads the {@link InvestmentBookData} from the given file name.
     *
     * @param file the given file name
     * @return investmentBook the loaded investmentBook or
     * a new created investmentBook
     */
    //TODO JavaDoc
    private void loadInvestmentBook(File file) {
        Task<InvestmentBookData> loadTask = fileLoaderTask(file);
        progressBar.progressProperty().bind(loadTask.progressProperty());
        loadTask.run();
        try {
            InvestmentBookData investmentBookData = InvestmentBookData.fromJson(file);
            this.currFile = file;
            this.investmentBook = new InvestmentBook(investmentBookData, createJavaFXGUI());
        } catch (IOException e) {
            displayError(e);
        }

        //TODO implement methode
//        progressBar.progressProperty().bind(loadTask.progressProperty());
        status.setText("File loaded: " + currFile.getName());
        toDefault();
    }

    private Task<InvestmentBookData> fileLoaderTask(File file){

        Task<InvestmentBookData> loadFileTask = new Task<>() {
            @Override
            protected InvestmentBookData call() throws Exception {
                updateProgress();
                return InvestmentBookData.fromJson(file);
            }
        };
        return null;
    }

    //TODO JavaDoc
    private void toDefault() {
        btnDeletePlatform.setDisable(true);
        btnEditInvestment.setDisable(true);
        btnDeleteInvestment.setDisable(true);
        cleanInvestmentFilter();
        cleanPlatformFilter();
    }

    /**
     * Handles the "Add" Button and hands over the value for
     * a new Platform.
     */
    @FXML
    private void handleAddPlatform() {
        NewPlatformController newPlatformController = createStage(
                "platformController/NewPlatformController.fxml",
                "new Platform",
                400,
                300
        );
        newPlatformController.setInvestmentBook(investmentBook);
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
                PlatformController platformController = createStage(
                        currentPlatform.getFxmlPath(),
                        "Edit Platform",
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
        setCurrFile(new File(DIRECTORY + "/newBook.json"));
        this.investmentBook = new InvestmentBook(createJavaFXGUI());
        toDefault();
    }

    /**
     * Handles the "open" button.
     */
    @FXML
    private void handleOpenBook() {
        FileChooser fileChooser = createFileChooser();
        fileChooser.setTitle("Open JSON Graph-File");
        File selectedFile = fileChooser.showOpenDialog(investmentTblVw.getScene().getWindow());
        loadInvestmentBook(selectedFile);
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
            displayError(e);
        }
    }

    /**
     * Handles the "save as" button and opens a save dialog
     */
    @FXML
    private void handleSaveBookAs() {
        FileChooser fileChooser = createFileChooser();
        fileChooser.setTitle("Save JSON Graph-File");
        File selectedFile = fileChooser.showSaveDialog(investmentTblVw.getScene().getWindow());
        if (selectedFile != null) {
            try {
                new InvestmentBookData(investmentBook).toJson(selectedFile);
            } catch (IOException e) {
                displayError(e);
            }
        }
    }

    /**
     * Handles the "apply" filter Button
     */
    @FXML
    private void handleApplyFilter2() {
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
     * Handles the "apply" filter Button
     */
    @FXML
    private void handleApplyFilter() {
        investmentBook.filter(
                statusChoiceBox.getValue(),
                platformChcBx.getValue(),
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
    private void handleResetInvestmentFilter() {
        cleanInvestmentFilter();
        investmentBook.displayInvestments();
    }

    /**
     * Fills all text fields to show details about the person.
     * If the specified person is null, all text fields are cleared.
     *
     * @param person the person or null
     */
    private void showInvestmentDetails(Investment investment) {
        if (investment != null) {
            State investState = investment.getState();

            creationDateLbl.setText(Style.format(investment.getCreationDate()));
            statusLbl.setText(investState.name());
            platformLbl.setText(investment.getPlatform().getName());
            stockNameLbl.setText(investment.getStockName());
            exchangeRateLbl.setText(DoubleUtil.formatMoney(investment.getExchangeRate()));
            capitalLbl.setText(DoubleUtil.formatMoney(investment.getCapital()));
            performanceLbl.setText(DoubleUtil.formatMoney(investment.getPerformance()));
            percentPerformanceLbl.setText(
                    DoubleUtil.format(investment.getPercentPerformance()) + "%");
            costLbl.setText(DoubleUtil.formatMoney(investment.getCost()));
            if (investState == State.CLOSED) {
                sellingPriceLbl.setText(DoubleUtil.formatMoney(investment.getSellingPrice()));
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
    private void handleAddInvestment() {
        NewInvestmentController newInvestmentController = createStage(
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
            Platform selectedPlatform = platformLstVw.getSelectionModel().getSelectedItem();
            investmentBook.remove(selectedPlatform);
        }
    }

    /**
     *
     */
    // TODO: 28.06.2022 JavaDoc
    private void initializePlatformSearch() {
        platformNameTxtFld.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                // Compare name of every investment with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                investmentBook.filterPlatformsByName(lowerCaseFilter);
            } else {
                // If filter text is empty, display all platforms.
                investmentBook.displayPlatforms();
            }
        });
    }

    /**
     *
     */
    // TODO: 28.06.2022 JavaDoc
    private void initializeInvestmentSearch() {
        filterStockNameTxtFld.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                // Compare name of every investment with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                investmentBook.filterInvestmentsByName(lowerCaseFilter);
            } else {
                // If filter text is empty, display all investments.
                investmentBook.displayInvestments();
            }
        });
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

