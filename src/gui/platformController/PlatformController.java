package gui.platformController;

import javafx.fxml.Initializable;
import logic.platform.Platform;

/**
 * Controller of the graphical platform interface.
 *
 * @author xthe_white_lionx
 */
public interface PlatformController extends Initializable {

    void setDisplay(Platform platform);
}