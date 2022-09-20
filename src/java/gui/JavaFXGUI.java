package gui;

import javafx.animation.PauseTransition;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import logic.GUIConnector;
import logic.Investment;
import logic.platform.Platform;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static gui.Style.setAndColorsText;


/**
 * This class is responsible for changing the gui when the logic deems it
 * necessary. Created by the gui and then passed as a parameter into the logic.
 *
 * @author xthe_white_lionx
 */
public class JavaFXGUI implements GUIConnector {

    /**
     * All {@link Investment}s in a Tableview
     */
    private final TableView<Investment> investmentTableView;

    /**
     * All {@link Platform}s in a Tableview
     */
    private final TableView<Platform> platformTableView;

    /**
     * ChoiceBox of all platforms for the filter of the investments
     */
    private final ChoiceBox<Platform> platformFilterChoiceBox;

    /**
     * Label to show the total performance
     * of the displayed investments
     */
    private final Label totalPerformanceLabel;

    /**
     * label to show the current status
     */
    private final Label messageBar;

    /**
     * sum of the total performance
     * of the displayed investments
     */
    private BigDecimal totalPerformance;

        /**
     * The constructor. Gets past all components of the gui that may change
     * due to actions in the logic.
     *
     * @param investmentTableView     all investments in a Tableview
     * @param platformTableView        all platforms in a Tableview
     * @param platformFilterChoiceBox choiceBox of all platforms for the filter of the investments
     * @param totalPerformanceLabel   label to change the total performance
     *                                of the displayed investments
     * @param messageBar                  label to change the current status
     */
    public JavaFXGUI(TableView<Investment> investmentTableView, TableView<Platform> platformTableView,
                     ChoiceBox<Platform> platformFilterChoiceBox, Label totalPerformanceLabel,
                     Label messageBar) {
        this.investmentTableView = investmentTableView;
        this.platformTableView = platformTableView;
        this.platformFilterChoiceBox = platformFilterChoiceBox;
        this.totalPerformanceLabel = totalPerformanceLabel;
        this.messageBar = messageBar;
    }

    /**
     * Updates the sum of the total performance and displays it in green,
     * red or black, depending on his value
     */
    private void updateTotalPerformanceLabel() {
        setAndColorsText(totalPerformance, totalPerformanceLabel);
    }

    /**
     * Updates the status and cleans it after
     */
    //TODO status enum?
    private void updateStatus(Message message, String content) {
        this.messageBar.setText(message.formatMessage(content));
        this.messageBar.setVisible(true);
        PauseTransition pt = new PauseTransition(UserInterfaceController.SHOW_DURATION);
        pt.onFinishedProperty().set(actionEvent -> {
            this.messageBar.setVisible(false);
            this.messageBar.setText("");
        });
        pt.play();
    }

    /**
     * Adds the over handed investment to the tableView and
     * calculates the new total performance
     *
     * @param investment the investment to add
     */
    private void addInvestment(Investment investment) {
        investmentTableView.getItems().add(investment);
        totalPerformance = totalPerformance.add(investment.getPerformance());
    }

    @Override
    public void addInvestmentOnDisplay(Investment investment) {
        addInvestment(investment);
        updateTotalPerformanceLabel();

        updateStatus(Message.added, "investment");
    }

    @Override
    public void displayInvestmentList(List<Investment> investmentList) {
        investmentTableView.getItems().clear();
        totalPerformance = BigDecimal.ZERO;
        for (Investment investment : investmentList) {
            addInvestment(investment);
        }
        updateTotalPerformanceLabel();
    }

    @Override
    public void deleteInvestment(Investment investment) {
        investmentTableView.getItems().remove(investment);
        totalPerformance = totalPerformance.subtract(investment.getPerformance());
        updateTotalPerformanceLabel();
        updateStatus(Message.deleted, "platform");
    }

    private void addPlatform(Platform newPlatform) {
        platformTableView.getItems().add(newPlatform);
        platformFilterChoiceBox.getItems().add(newPlatform);
    }

    @Override
    public void addPlatformOnDisplay(Platform newPlatform) {
        assert newPlatform != null;

        addPlatform(newPlatform);
        updateStatus(Message.added, "platform");
    }

    @Override
    public void displayPlatforms(Set<Platform> platforms) {
        platformTableView.getItems().clear();
        platformFilterChoiceBox.getItems().clear();
        for (Platform platform : platforms) {
            addPlatform(platform);
        }
    }

    @Override
    public void deletePlatform(Platform platform) {
        platformTableView.getItems().remove(platform);
        platformFilterChoiceBox.getItems().remove(platform);
        updateStatus(Message.deleted, "platform");
    }
}
