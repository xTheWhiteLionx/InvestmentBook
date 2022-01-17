package gui;

import javafx.application.Application;
import javafx.stage.Stage;

import static logic.GeneralMethods.createStage;

/**
 * This class contains the main.
 *
 * @author xtheWhiteLionx
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        createStage("FileInterfaceController.fxml", "Investment Book", 300, 300);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
