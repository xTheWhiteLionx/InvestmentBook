package gui;

import gui.tabs.InvestmentTab;
import gui.tabs.PlatformTab;
import gui.tabs.PortfolioTab;
import gui.tasks.LoadTask;
import gui.tasks.SaveTask;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.GUIConnector;
import logic.investmentBook.InvestmentBook;
import logic.investmentBook.InvestmentBookData;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import static gui.DialogWindow.*;


/**
 * Controller of the graphical user interface.
 *
 * @author xthewhitelionx
 */
public class UserInterfaceController implements Initializable {
    /**
     *
     */
    public static final Duration LOAD_DURATION = Duration.millis(5);

    /**
     *
     */
    public static final Duration SHOW_DURATION = Duration.seconds(1);

    /**
     *
     */
    @FXML
    private CheckMenuItem autoSaveMenuItem;

    /**
     *
     */
    @FXML
    private ImageView modeImageView;

    /**
     *
     */
    @FXML
    private Label message;

    /**
     *
     */
    @FXML
    private ProgressBar progressBar;

    /**
     * The current {@link InvestmentBook}
     */
    private InvestmentBook investmentBook;

    /**
     *
     */
    private InvestmentTab investmentTab;

    private BorderPane investmentPane;

    /**
     *
     */
    private PlatformTab platformTab;

    private BorderPane platformPane;

    /**
     *
     */
    private PortfolioTab portfolioTab;

    /**
     * The current file
     */
    private File currFile;

    /**
     *
     */
    private boolean isLightMode;
    @FXML
    private ToggleButton btnInvestmentPane;
    @FXML
    private ImageView platformsImageview;
    @FXML
    private ToggleButton btnPlatformPane;
    @FXML
    private ImageView investmentsImageview;
    @FXML
    private BorderPane mainBorderPane;

    /**
     * Initialize a new game with the specified arguments by using the
     * methode {@link #loadGameController()}
     *
     * @param involved    array of {@code boolean}s true if someone plays otherwise false
     * @param names       name of each player in an array
     * @param playerTypes typ of each player in an array
     * @throws NullPointerException if involved, names or playerTypes is null
     */
    public static void initializeUserInterfaceController() {
        UserInterfaceController controller = loadUserInterfaceController("newBook.json");

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
    /**
     * @param selectedFile
     * @throws IOException
     */
    public static void initializeUserInterfaceController(File selectedFile) throws IOException {
        if (selectedFile == null) {
            throw new NullPointerException("selectedFile == null");
        }

        InvestmentBookData investmentBookData = InvestmentBookData.fromJson(selectedFile);
        UserInterfaceController controller = loadUserInterfaceController(selectedFile.getName());

        controller.setCurrFile(selectedFile);
        if (investmentBookData != null) {
            controller.investmentBook = new InvestmentBook(investmentBookData, controller.createJavaFXGUI());
            controller.platformTab.setInvestmentBook(controller.investmentBook);
            controller.investmentTab.setInvestmentBook(controller.investmentBook);
        }
    }


    /**
     * Initializes the application.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        autoSaveMenuItem.setSelected(Settings.isAutoSave());
        autoSaveMenuItem.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            Settings.setAutoSave(t1);
        });

        initializeModeImage();
        initializePlatformTab();
        initializeInvestmentTab();
        initializeSideBar();
        toDefault();
    }

    /**
     * closes the current stage/window and creates a new {@link UserInterfaceController}
     * also it hands over the chosen file
     *
     * @param selectedFile the chosen file which should be
     *                     transmitted to the created UserInterfaceController
     */
    private static UserInterfaceController loadUserInterfaceController(String title) {
        final Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(
                ApplicationMain.class.getResource("UserInterfaceController.fxml"));

        newStage.setMaximized(true);
        // Icon/logo of the application
        newStage.setTitle(title);
        newStage.setMinWidth(1200D);
        newStage.setMinHeight(850D);
        newStage.initModality(Modality.WINDOW_MODAL);
        try {
            newStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            displayError(e);
        }

        System.out.println("UserInterfaceController.loadUserInterfaceController");
        styleStage(newStage);

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
                investmentTab.getInvestmentTblVw(),
                platformTab.getPlatformTableView(),
                investmentTab.getPlatformChcBx(),
                investmentTab.getTotalPerformanceLbl(),
                message);
    }

    private void initializeSideBar() {
        btnPlatformPane.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                btnPlatformPane.setStyle("-fx-border-color: GREEN;" +
                        "-fx-border-width: 0px 0px 0px 5px;");
                mainBorderPane.setCenter(platformPane);
            } else {
                btnPlatformPane.setStyle(null);
            }
        });
        btnInvestmentPane.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                btnInvestmentPane.setStyle("-fx-border-color: GREEN;" +
                        "-fx-border-width: 0px 0px 0px 5px;");
                mainBorderPane.setCenter(investmentPane);
            } else {
                btnInvestmentPane.setStyle(null);
            }
        });

        ToggleGroup toggleGroup = new ToggleGroup();
        ObservableList<Toggle> toggles = toggleGroup.getToggles();
        toggles.add(btnPlatformPane);
        toggles.add(btnInvestmentPane);
        btnPlatformPane.setSelected(true);
    }

    private void initializePlatformTab() {
        platformTab = PlatformTab.createPlatformTab();
        platformPane = platformTab.getBorderPane();
    }

    private void initializeInvestmentTab() {
        investmentTab = InvestmentTab.createInvestmentTab();
        investmentPane = investmentTab.getBorderPane();
    }

    private void setImages(boolean isLightMode) {
        if (isLightMode) {
            modeImageView.setImage(new Image("gui/textures/moon_icon_black.png"));
            platformsImageview.setImage(new Image("gui/textures/platform_icon_black.png"));
            investmentsImageview.setImage(new Image("gui/textures/investment_icon_black.png"));
        } else {
            modeImageView.setImage(new Image("gui/textures/sun_icon_white.png"));
            platformsImageview.setImage(new Image("gui/textures/platform_icon_white.png"));
            investmentsImageview.setImage(new Image("gui/textures/investment_icon_white.png"));
        }
    }

    /**
     *
     */
    private void initializeModeImage() {
        isLightMode = Settings.isLightMode();
        setImages(isLightMode);
    }

    /**
     * @param currFile
     */
    private void setCurrFile(File currFile) {
        this.currFile = currFile;
        Stage stage = (Stage) progressBar.getScene().getWindow();
        stage.setTitle(currFile.getName());
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
        Task<InvestmentBookData> loadTask = new LoadTask(file, progressBar, message);
        loadTask.run();
        try {
            this.investmentBook = new InvestmentBook(loadTask.get(), createJavaFXGUI());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        setCurrFile(file);
    }

    /**
     *
     */
    //TODO JavaDoc
    private void toDefault() {
        message.setText("");
        message.setVisible(false);
        progressBar.setVisible(false);
    }

    /**
     * Examines if the given mouseEvent where double-clicked.
     * Returns true if the given mouseEvent where double-clicked
     * otherwise false.
     *
     * @param mouseEvent the given mouseEvent
     * @return true, if the given mouseEvent
     */
    public static boolean isDoubleClicked(MouseEvent mouseEvent) {
        return mouseEvent.getClickCount() == 2;
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
        File selectedFile = fileChooser.showOpenDialog(progressBar.getScene().getWindow());
        if (selectedFile != null) {
            loadInvestmentBook(selectedFile);
        }
    }

    /**
     * @param file
     * @return
     */
    private Task<Void> createSaveTask(File file) {
        return new SaveTask(file, investmentBook, progressBar, message);
    }

    /**
     * Handles the "save" button and
     * saves the current {@link InvestmentBook}.
     */
    @FXML
    private void handleSaveBook() {
        Task<Void> saveTask = createSaveTask(currFile);
        progressBar.progressProperty().bind(saveTask.progressProperty());
        saveTask.run();
    }

    /**
     * Handles the "save as" button and opens a save dialog
     */
    @FXML
    private void handleSaveBookAs() {
        FileChooser fileChooser = createFileChooser();
        File selectedFile = fileChooser.showSaveDialog(progressBar.getScene().getWindow());

        if (selectedFile != null) {
            Task<Void> saveTask = createSaveTask(selectedFile);
            progressBar.progressProperty().bind(saveTask.progressProperty());
            saveTask.run();
        }
    }

    /**
     * Handles the "add" Button and hands over the value for
     * a new Investment.
     */
    @FXML
    private void handleAddInvestment() {
        investmentTab.handleAddInvestment();
    }

    /**
     * Handles the "Add" Button and hands over the value for
     * a new Platform.
     */
    @FXML
    public void handleAddPlatform() {
        platformTab.handleAddPlatform();
    }

    /**
     * Handles the "exit" button.
     */
    @FXML
    private void handleMode() {
        isLightMode = !isLightMode;
        if (isLightMode) {
            progressBar.getScene().getStylesheets().clear();
            setImages(true);
            Settings.setLightMode(true);
        } else {
            progressBar.getScene().getStylesheets().add(getClass().getResource(
                    "themes/Darkmode.css").toExternalForm());
            setImages(false);
            Settings.setLightMode(false);
        }
    }

    /**
     * Handles the "exit" button.
     */
    @FXML
    private void handleExit() {
        //TODO AutoSave?
//        if (autoSave.isSelected()) {
//            handleSaveBook();
//        }
        Stage stage = (Stage) progressBar.getScene().getWindow();
        stage.close();
    }
}


