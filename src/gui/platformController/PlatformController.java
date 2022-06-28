package gui.platformController;

import gui.DialogWindow;
import gui.calculator.FeeCalculatorController;
import logic.platform.Platform;

/**
 * Controller of the graphical platform interface.
 *
 * @author xthe_white_lionx
 */
public interface PlatformController {

    /**
     * Handles the "fee calculator" button and
     * opens a FeeCalculator Window. Also gives the
     * Controller the current platforms.
     */
    default void handleFeeCalculator(Platform platform) {
        FeeCalculatorController feeCalculator =
                DialogWindow.createStage("calculator/FeeCalculatorController.fxml",
                        "Fee Calculator",
                        400,
                        200
                );
        feeCalculator.setPlatform(platform);
    }

    /**
     *
     * @param platform
     */
    void setDisplay(Platform platform);
}