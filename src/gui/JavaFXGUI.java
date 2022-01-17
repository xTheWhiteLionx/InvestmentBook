package gui;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import logic.GUIConnector;
import logic.Investment;
import logic.platform.Platform;

import java.util.List;
import java.util.Set;

import static logic.GeneralMethods.setAndColorsText;


/**
 * This class is responsible for changing the gui when the logic deems it
 * necessary. Created by the gui and then passed as a parameter into the logic.
 *
 * @author xtheWhiteLionx
 */
public class JavaFXGUI implements GUIConnector {

    /**
     * All {@link Investment}s in a Tableview
     */
    private final TableView<Investment> INVESTMENTS_TABLEVIEW;

    /**
     * All {@link Platform}s in a Listview
     */
    private final ListView<Platform> platformListView;

    /**
     * ChoiceBox of all platforms for the filter
     */
    private final ChoiceBox<Platform> PLATFORM_FILTER_CHOICE_BOX;

    /**
     * ChoiceBox of all platforms to create a new investment
     */
    private final ChoiceBox<Platform> PLATFORM_CHOICE_BOX;

    /**
     * Label to show the total performance
     * of the displayed investments
     */
    private final Label TOTAL_PERFORMANCE_LABEL;

    /**
     * label to show the current status
     */
    private final Label STATUS;

    /**
     * sum of the total performance
     * of the displayed investments
     */
    private double totalPerformance;

    /**
     * The constructor. Gets past all components of the gui that may change
     * due to actions in the logic.
     *
     * @param investmentTableView     all investments in a Tableview
     * @param platformListView        all platforms in a Listview
     * @param platformFilterChoiceBox choiceBox of all platforms for the filter
     * @param platformChoiceBox       choiceBox of all platforms to create a new investment
     * @param totalPerformanceLabel   label to change the total performance
     *                                of the displayed investments
     * @param status                  label to change the current status
     */
    public JavaFXGUI(TableView<Investment> investmentTableView, ListView<Platform> platformListView,
                     ChoiceBox<Platform> platformFilterChoiceBox,
                     ChoiceBox<Platform> platformChoiceBox, Label totalPerformanceLabel,
                     Label status) {
        this.INVESTMENTS_TABLEVIEW = investmentTableView;
        this.platformListView = platformListView;
        this.PLATFORM_FILTER_CHOICE_BOX = platformFilterChoiceBox;
        this.PLATFORM_CHOICE_BOX = platformChoiceBox;
        this.TOTAL_PERFORMANCE_LABEL = totalPerformanceLabel;
        this.STATUS = status;
    }

    /**
     * Updates the sum of the total performance and displays it in green,
     * red or black, depending on his value
     */
    private void updateTotalPerformance() {
        setAndColorsText(totalPerformance, TOTAL_PERFORMANCE_LABEL);
    }

    /**
     * Updates the status and cleans it after
     */
    //TODO status enum?
    private void updateStatus(String text) {
        this.STATUS.setText(text);
        //TODO how, maybe TimerTask?
        this.STATUS.setText("");
    }

    /**
     * Adds the over handed investment to the tableView and
     * calculates the new total performance
     *
     * @param investment the investment to add
     */
    private void addInvestment(Investment investment) {
        INVESTMENTS_TABLEVIEW.getItems().add(investment);
        totalPerformance += investment.getPerformance();
    }

    @Override
    public void addInvestmentOnDisplay(Investment investment) {
        addInvestment(investment);
        updateTotalPerformance();
    }

    @Override
    public void displayInvestmentList(List<Investment> investmentList) {
        INVESTMENTS_TABLEVIEW.getItems().clear();
        totalPerformance = 0;
        for (Investment investment : investmentList) {
            addInvestment(investment);
        }
        updateTotalPerformance();
    }

    @Override
    public void addPlatform(Platform newPlatform) {
        platformListView.getItems().add(newPlatform);
        PLATFORM_FILTER_CHOICE_BOX.getItems().add(newPlatform);
        PLATFORM_CHOICE_BOX.getItems().add(newPlatform);
    }

    @Override
    public void displayPlatforms(Set<Platform> platforms) {
        if (!platforms.isEmpty()) {
            platformListView.getItems().clear();
            PLATFORM_FILTER_CHOICE_BOX.getItems().clear();
            PLATFORM_CHOICE_BOX.getItems().clear();
            for (Platform platform : platforms) {
                //sets the platforms for the choiceBox
                addPlatform(platform);
            }
            PLATFORM_CHOICE_BOX.setValue(PLATFORM_CHOICE_BOX.getItems().get(0));
        }
    }

    @Override
    public void deletePlatform(Platform platform) {
        platformListView.getItems().remove(platform);
        PLATFORM_FILTER_CHOICE_BOX.getItems().remove(platform);
        PLATFORM_CHOICE_BOX.getItems().remove(platform);
    }
}
