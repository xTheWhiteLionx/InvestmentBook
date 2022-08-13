package gui;

import javafx.application.Application;
import javafx.stage.Stage;

import static gui.DialogWindow.createLoginController;

/**
 * This class contains the main.
 *
 * @author xthe_white_lionx
 */
public class ApplicationMain extends Application {

    /**
     * Creating the stage and showing it. This is where the initial size and the
     * title of the window are set.
     *
     * @param primaryStage the stage to be shown
     */
    @Override
    public void start(Stage primaryStage) {
        createLoginController();
    }

    /**
     * Main method
     *
     * @param args unused
     */
    public static void main(String[] args) {
        launch(args);
    }
}
