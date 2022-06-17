package logic;

import logic.platform.Platform;

import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static logic.GeneralMethods.*;

/**
 * This class contains the program's logic.
 *
 * @author Fabian Hardt
 */
//TODO JavaDoc
public class InvestmentBook {

    /**
     * Platforms of the InvestmentBook
     */
    private final Set<Platform> platforms;

    /**
     * Investments of the InvestmentBook
     */
    private final List<Investment> investments;

    /**
     * Connection to the gui.
     */
    private final GUIConnector gui;

    /**
     * Constructor for an investment book.
     * Also calls the constructor with the same name.
     *
     * @param investments the given Investments
     * @param gui         Connection to the gui
     */
    //TODO JavaDoc
    public InvestmentBook(Set<Platform> platforms, List<Investment> investments, GUIConnector gui) {
        this.platforms = platforms;
        this.investments = investments;
        this.gui = gui;

        Collections.sort(this.investments);
        displayInvestmentList();
        this.gui.displayPlatforms(platforms);
    }

    /**
     * Constructor for an investment book.
     *
     * @param investmentBookData the given Investments
     * @param gui                Connection to the gui
     */
    //TODO JavaDoc
    public InvestmentBook(InvestmentBookData investmentBookData, GUIConnector gui) {
        this(investmentBookData.getPlatforms(), investmentBookData.getInvestments(), gui);
    }

    //TODO JavaDoc
    public Set<Platform> getPlatforms() {
        return new HashSet<>(platforms);
    }

    //TODO JavaDoc
    public List<Investment> getInvestments() {
        return new ArrayList<>(investments);
    }

    //TODO JavaDoc
    public void displayInvestmentList() {
        gui.displayInvestmentList(investments);
    }

    //TODO JavaDoc
    public void add(Platform newPlatform) {
        platforms.add(newPlatform);
        gui.addPlatform(newPlatform);
    }

    //TODO JavaDoc
    public void add(Investment newInvestment) {
        investments.add(newInvestment);
        Collections.sort(investments);
        gui.addInvestmentOnDisplay(newInvestment);
    }

    //TODO JavaDoc
    public void remove(Platform platform) {
        platforms.remove(platform);
        gui.deletePlatform(platform);
    }

    //TODO JavaDoc
    public void remove(Investment investment) {
        investments.remove(investment);
        gui.deleteInvestment(investment);
    }

    /**
     * Filter the investments by the given year and
     * returns the filtered investments
     *
     * @param status
     * @param platform
     * @param stockName
     * @param month
     * @param quarter
     * @param year
     */
    public List<Investment> filter(Status status, Platform platform, String stockName,
                       Month month, Quarter quarter, int year) {
        Stream<Investment> investmentStream = investments.stream();

        if (status != null) {
            investmentStream = investmentStream.filter(x -> x.getStatus() == status);
        }
        if (platform != null) {
            investmentStream = investmentStream.filter(x -> x.getPlatform().equals(platform));
        }
        if (stockName != null && !stockName.isEmpty()) {
            investmentStream = investmentStream.filter(x -> x.getStockName().equals(stockName));
        }

        if (year > 0) {
            investmentStream = investmentStream.filter(x -> x.getCreationDate().getYear() == year);
            if (month != null) {
                investmentStream = investmentStream.filter(
                        x -> x.getCreationDate().getMonth().equals(month)
                );
            } else if (quarter != null) {
                investmentStream = investmentStream.filter(
                        x -> quarter.getMonths().contains(x.getCreationDate().getMonth())
                );
            }
        }

        List<Investment> filteredInvestments = investmentStream.toList();

        gui.displayInvestmentList(filteredInvestments);
        return filteredInvestments;
    }

    @Override
    public String toString() {
        return "InvestmentBook{" +
                "platforms=" + platforms +
                ", investments=" + investments +
                ", gui=" + gui +
                '}';
    }
}