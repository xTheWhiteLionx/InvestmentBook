package logic.investmentBook;

import logic.*;
import logic.platform.FeeType;
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
public class InvestmentBook extends InvestmentBookImpl {

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
        super(platforms, investments);
        if (gui == null) {
            throw new NullPointerException();
        }
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
     * Constructor for an investment book.
     *
     * @param gui                Connection to the gui
     */
    public InvestmentBook(GUIConnector gui) {
        super();
        this.gui = gui;
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
     * @param platform
     */
    //TODO JavaDoc
    public void add(Platform platform) {
        platforms.add(platform);
        gui.addPlatform(platform);
    }

    /**
     * @param investment
     */
    //TODO JavaDoc
    public void add(Investment investment) {
        investments.add(investment);
        Collections.sort(investments);
        gui.addInvestmentOnDisplay(investment);
    }

    /**
     * @param platform
     */
    //TODO JavaDoc
    public void remove(Platform platform) {
        platforms.remove(platform);
        gui.deletePlatform(platform);
    }

    /**
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
     *
     * @param name
     * @return
     */
    public Set<Platform> filterPlatformsByName(String name) {
        Stream<Platform> platformStream = this.platforms.stream();
        if (name != null && !name.isEmpty()) {
            platformStream = platformStream.filter(p ->
                    p.getName().toLowerCase().startsWith(name));
        }

        Set<Platform> filteredPlatform = platformStream.collect(Collectors.toSet());

        gui.displayPlatforms(filteredPlatform);
        return filteredPlatform;
    }

    /**
     * Filters the investments by the given year and
     * returns the filtered investments
     *
     * @param typ
     * @return
     */
    public Set<Platform> filterPlatformsByType(FeeType typ) {
        Stream<Platform> platformStream = this.platforms.stream();
        if (typ != null) {
            platformStream = platformStream.filter(p -> p.getTyp() == typ);
        }

        Set<Platform> filteredPlatform = platformStream.collect(Collectors.toSet());

        gui.displayPlatforms(filteredPlatform);
        return filteredPlatform;
    }

    /**
     * Filters the investments by the given year and
     * returns the filtered investments
     *
     * @param name
     * @return
     */
    public List<Investment> filterInvestmentsByName(String name) {
        Stream<Investment> investmentStream = this.investments.stream();
        if (name != null && !name.isEmpty()) {
            investmentStream = investmentStream.filter(invest ->
                            invest.getStockName().toLowerCase().startsWith(name));
        }

        List<Investment> filteredInvestments = investmentStream.toList();

        gui.displayInvestmentList(filteredInvestments);
        return filteredInvestments;
    }

    /**
     * Filters the investments by the given year and
     * returns the filtered investments
     *
     * @param state
     * @param platform
     * @param month
     * @param quarter
     * @param year
     */
    public List<Investment> filter(State state, Platform platform,
                                   Month month, Quarter quarter, int year) {
        Stream<Investment> investmentStream = this.investments.stream();

        if (state != null) {
            investmentStream = investmentStream.filter(x -> x.getState() == state);
        }
        if (platform != null) {
            investmentStream = investmentStream.filter(x -> x.getPlatform().equals(platform));
        }

        if (year > 0) {
            investmentStream = investmentStream.filter(x -> x.getCreationDate().getYear() == year);
            if (month != null) {
                investmentStream = investmentStream.filter(
                        x -> x.getCreationDate().getMonth().equals(month)
                );
            } else if (quarter != null) {
                investmentStream = investmentStream.filter(
                        x -> quarter.contains(x.getCreationDate().getMonth())
                );
            }
        }

        List<Investment> filteredInvestments = investmentStream.toList();

        gui.displayInvestmentList(filteredInvestments);
        return filteredInvestments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvestmentBook that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(gui, that.gui);
    }
}