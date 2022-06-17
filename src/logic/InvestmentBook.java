package logic;

import logic.platform.Platform;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        if (platforms == null) {
            throw new NullPointerException();
        }
        if (investments == null) {
            throw new NullPointerException();
        }
        if (gui == null) {
            throw new NullPointerException();
        }

        this.platforms = platforms;
        Collections.sort(investments);
        this.investments = investments;
        this.gui = gui;

        displayPlatforms();
        displayInvestments();
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

    /**
     *
     * @return
     */
    //TODO JavaDoc
    public Set<Platform> getPlatforms() {
        return new HashSet<>(platforms);
    }

    /**
     *
     * @return
     */
    //TODO JavaDoc
    public List<Investment> getInvestments() {
        return new ArrayList<>(investments);
    }

    /**
     *
     */
    //TODO JavaDoc
    public void displayPlatforms() {
        gui.displayPlatforms(platforms);
    }

    /**
     *
     */
    //TODO JavaDoc
    public void displayInvestments() {
        gui.displayInvestmentList(investments);
    }

    /**
     *
     * @param platform
     */
    //TODO JavaDoc
    public void add(Platform platform) {
        platforms.add(platform);
        gui.addPlatform(platform);
    }

    /**
     *
     * @param investment
     */
    //TODO JavaDoc
    public void add(Investment investment) {
        investments.add(investment);
        Collections.sort(investments);
        gui.addInvestmentOnDisplay(investment);
    }

    /**
     *
     * @param platform
     */
    //TODO JavaDoc
    public void remove(Platform platform) {
        platforms.remove(platform);
        gui.deletePlatform(platform);
    }

    /**
     *
     * @param investment
     */
    //TODO JavaDoc
    public void remove(Investment investment) {
        investments.remove(investment);
        gui.deleteInvestment(investment);
    }

    /**
     * Filters the investments by the given year and
     * returns the filtered investments
     */
    //TODO JavaDoc
    public Set<Platform> filter(String name) {
        if (name != null) {
            Set<Platform> filteredPlatforms = platforms.stream()
                    .filter(p -> p.getName().equals(name))
                    .collect(Collectors.toSet());

            gui.displayPlatforms(filteredPlatforms);
            return filteredPlatforms;
        }
        return getPlatforms();
    }

    /**
     * Filters the investments by the given year and
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

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "InvestmentBook{" +
                "platforms=" + platforms +
                ", investments=" + investments +
                ", gui=" + gui +
                '}';
    }
}