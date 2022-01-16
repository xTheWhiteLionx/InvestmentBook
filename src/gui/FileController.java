package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static logic.GeneralMethods.DIRECTORY;
import static logic.GeneralMethods.ICON;

public class FileController implements Initializable {

    @FXML
    public Button loadBook;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    private void createUserInterfaceController(File selectedFile){
        int width = 1200;
        int height = 650;

        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource(
                "UserInterfaceController.fxml")));

        newStage.getIcons().add(ICON);
        newStage.setTitle("Investment Book");
        newStage.setMinWidth(width);
        newStage.setMinHeight(height);
        try {
            newStage.setScene(new Scene(loader.load(), width, height));
        } catch (IOException e) {
            e.printStackTrace();
        }
        newStage.show();
        UserInterfaceController userInterfaceController = loader.getController();
        userInterfaceController.setFile(selectedFile);
        //close the window/stage
        Stage stage = (Stage) loadBook.getScene().getWindow();
        stage.close();
    }

    public void handleCreateNewBook() {
        createUserInterfaceController(null);
    }

    public void handleLoadBook() {
        FileChooser fileChooser = new FileChooser();
        //ensure the dialog opens in the correct directory
        fileChooser.setInitialDirectory(new File(DIRECTORY));
        Window window = loadBook.getScene().getWindow();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        fileChooser.setTitle("Open JSON Graph-File");
        File selectedFile = fileChooser.showOpenDialog(window);
        createUserInterfaceController(selectedFile);
    }
}
