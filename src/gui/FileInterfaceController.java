package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.DialogWindow.DIRECTORY;
import static gui.DialogWindow.openDialogFile;
import static gui.Helper.createStage;

/**
 * Controller of the graphical file interface.
 *
 * @author xthe_white_lionx
 */
public class FileInterfaceController implements Initializable {

    @FXML
    public Button loadBook;

    /**
     * Initializes the application.
     *
     * @param url            unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * closes the current stage/window and creates a new {@link UserInterfaceController}
     * also it hands over the chosen file
     *
     * @param selectedFile the chosen file which should be
     *                     transmitted to the created UserInterfaceController
     */
    private void createUserInterfaceController(File selectedFile) {
        //closes the window/stage
        Stage stage = (Stage) loadBook.getScene().getWindow();
        stage.close();
        UserInterfaceController userInterfaceController = createStage(
                "UserInterfaceController.fxml",
                "Investment Book",
                1200,
                650);

        userInterfaceController.setFile(selectedFile);
    }

    /**
     * handles the create new Book button
     */
    public void handleCreateNewBook() {
        createUserInterfaceController(new File(DIRECTORY + "newBook.json"));
    }

    /**
     * handles the create new Book button
     */
    public void handleLoadBook() {
        File selectedFile = openDialogFile(loadBook.getScene().getWindow());
        if (selectedFile != null) {
            createUserInterfaceController(selectedFile);
        }
    }
}
