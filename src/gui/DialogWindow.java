package gui;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

import static logic.GeneralMethods.DIRECTORY;

/**
 * This class contains the dialog window methods.
 *
 * @author xthe_white_lionx
 */
public class DialogWindow {

    //TODO JavaDoc
    private static FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        //ensure the dialog opens in the correct directory
        fileChooser.setInitialDirectory(new File(DIRECTORY));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json")
        );
        return fileChooser;
    }

    //TODO JavaDoc
    public static File openDialogFile(Window window) {
        assert window != null;

        FileChooser fileChooser = createFileChooser();
        fileChooser.setTitle("Open JSON Graph-File");
        return fileChooser.showOpenDialog(window);
    }

    //TODO JavaDoc
    public static File saveDialogFile(Window window) {
        assert window != null;

        FileChooser fileChooser = createFileChooser();
        fileChooser.setTitle("Save JSON Graph-File");
        return fileChooser.showSaveDialog(window);
    }

}
