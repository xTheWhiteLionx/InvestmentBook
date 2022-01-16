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

import static logic.GeneralMethods.roundDouble;
import static logic.GeneralMethods.setAndColorText;


/**
 * This class is responsible for changing the gui when the logic deems it
 * necessary. Created by the gui and then passed as a parameter into the logic.
 *
 * @author Fabian Hardt
 */
public class JavaFXGUI implements GUIConnector {

    /**
     * All investments in a Tableview
     */
    private final TableView<Investment> INVESTMENTS_TABLEVIEW;

    //TODO JavaDoc
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
     *
     */
    //TODO JavaDoc
    private final Label status;

    /**
     * sum of the total performance
     * of the displayed investments
     */
    private double totalPerformance;

    /**
     * The constructor. Gets past all components of the gui that may change
     * due to actions in the logic.
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
        this.status = status;
    }

    /**
     * Updates the sum of the total performance and displays it in green,
     * red or black, depending on his value
     */
    private void updateTotalPerformance() {
        setAndColorText(roundDouble(totalPerformance),TOTAL_PERFORMANCE_LABEL);
    }

    private void updateStatus(String text){
        //TODO how, maybe TimerTask?
        this.status.setText(text);
        this.status.setText("");
    }

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
        updateStatus("test");
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
