package gui;

import javafx.application.Application;
import javafx.stage.Stage;

import static logic.GeneralMethods.createStage;

/**
 * This class contains the main.
 *
 * @author Fabian Hardt
 */
public class Main extends Application {

    /**
     * Width for the creating stage
     */
    private final int WIDTH = 300;

    //TODO JavaDoc
    private final int HEIGHT = 300;

    @Override
    public void start(Stage primaryStage) {
        createStage("FileController.fxml", "Investment Book", WIDTH, HEIGHT);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
