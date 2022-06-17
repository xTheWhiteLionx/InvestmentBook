package gui;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

import static gui.Helper.createStage;

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
     * @throws IOException
     */
    @Override
    public void start(Stage primaryStage) {
               createStage("FileInterfaceController.fxml",
                "Investment Book",
                300,
                300
        );
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
