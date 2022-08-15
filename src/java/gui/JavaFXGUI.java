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

import static gui.Style.setAndColorsText;
import static gui.Style.setCurrenciesForLbls;


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
     * All {@link Platform}s in a Listview
     */
    private final ListView<Platform> platformListView;

    /**
     * ChoiceBox of all platforms for the filter
     */
    private final ChoiceBox[] platformFilterChoiceBoxes;

    /**
     * Label to show the total performance
     * of the displayed investments
     */
    private final Label totalPerformanceLabel;

    /**
     * label to show the current status
     */
    private final Label status;

    /**
     * sum of the total performance
     * of the displayed investments
     */
    private double totalPerformance;

    /**
     * The constructor. Gets past all components of the gui that may change
     * due to actions in the logic.
     *  @param investmentTableView          all investments in a Tableview
     * @param platformListView             all platforms in a Listview
     * @param platformFilterChoiceBoxes
 * //     * @param platformFilterChoiceBox choiceBox of all platforms for the filter
 * //     * @param platformChoiceBox       choiceBox of all platforms to create a new investment
     * @param totalPerformanceLabel        label to change the total performance
*                                     of the displayed investments
     * @param currencyLbls                 labels to show the local currency
     * @param status                       label to change the current status
     */
    public JavaFXGUI(TableView<Investment> investmentTableView, ListView<Platform> platformListView,
                     ChoiceBox[] platformFilterChoiceBoxes, Label totalPerformanceLabel,
                     Label[] currencyLbls, Label status) {
        this.investmentTableView = investmentTableView;
        this.platformListView = platformListView;
        this.platformFilterChoiceBoxes = platformFilterChoiceBoxes;
        this.totalPerformanceLabel = totalPerformanceLabel;
        this.status = status;

        //TODO?
        setCurrenciesForLbls(currencyLbls);
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
    private void updateStatus(Status status) {
        this.status.setText(status.name());
        //TODO how, maybe TimerTask?
//        Task<String> task = new Task<>() {
//
//        };
        this.status.setText("");
    }

    /**
     * Adds the over handed investment to the tableView and
     * calculates the new total performance
     *
     * @param investment the investment to add
     */
    private void addInvestment(Investment investment) {
        investmentTableView.getItems().add(investment);
        totalPerformance += investment.getPerformance();
    }

    @Override
    public void addInvestmentOnDisplay(Investment investment) {
        addInvestment(investment);
        updateTotalPerformanceLabel();
    }

    @Override
    public void displayInvestmentList(List<Investment> investmentList) {
        investmentTableView.getItems().clear();
        totalPerformance = 0;
        for (Investment investment : investmentList) {
            addInvestment(investment);
        }
        updateTotalPerformanceLabel();
    }

    @Override
    public void deleteInvestment(Investment investment) {
        investmentTableView.getItems().remove(investment);
        totalPerformance -= investment.getPerformance();
        updateTotalPerformanceLabel();
    }

    @Override
    public void addPlatform(Platform newPlatform) {
        assert newPlatform != null;

        platformListView.getItems().add(newPlatform);
        for (ChoiceBox<Platform> platform_choice_box : platformFilterChoiceBoxes) {
            //sets the platforms for the choiceBox
            platform_choice_box.getItems().add(newPlatform);
        }
    }

    @Override
    public void displayPlatforms(Set<Platform> platforms) {
        platformListView.getItems().clear();
        for (ChoiceBox<Platform> platform_choice_box : platformFilterChoiceBoxes) {
            platform_choice_box.getItems().clear();
        }
        for (Platform platform : platforms) {
            addPlatform(platform);
        }
    }

    @Override
    public void deletePlatform(Platform platform) {
        platformListView.getItems().remove(platform);
        for (ChoiceBox<Platform> platform_choice_box : platformFilterChoiceBoxes) {
            platform_choice_box.getItems().remove(platform);
        }
    }
}
