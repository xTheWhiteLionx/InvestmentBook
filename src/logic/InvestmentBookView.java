package logic;

import logic.enums.FeeTypes;
import logic.enums.Quarter;
import logic.enums.Status;
import logic.platform.AbsolutePlatform;
import logic.platform.MixedPlatform;
import logic.platform.PercentPlatform;
import logic.platform.Platform;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static logic.GeneralMethods.*;

/**
 * This class contains the program's logic.
 *
 * @author Fabian Hardt
 */
//TODO JavaDoc
public class InvestmentBookView {

    /**
     * All Platforms as set
     */
    private final Set<Platform> PLATFORMS;

    /**
     * ArrayList of Investments
     */
    private final List<Investment> INVESTMENT_LIST;

    /**
     *
     */
    private final InvestmentBook INVESTMENT_BOOK;

    /**
     * Connection to the gui.
     */
    private final GUIConnector GUI;

    //TODO JavaDoc
    public void updateInvestmentList() {
        GUI.displayInvestmentList(INVESTMENT_LIST);
    }

    /**
     * Constructor for a program.
     *
     * @param investmentBook the given Investments
     * @param gui            Connection to the gui
     */
    //TODO JavaDoc
    public InvestmentBookView(InvestmentBook investmentBook, GUIConnector gui) {
        this.INVESTMENT_BOOK = investmentBook;
        this.PLATFORMS = investmentBook.getPlatforms();
        this.INVESTMENT_LIST = investmentBook.getInvestmentList();
        this.GUI = gui;
        updateInvestmentList();
        GUI.displayPlatforms(PLATFORMS);
    }

    /**
     * Constructor for a program.
     * Also calls the constructor with the same name.
     *
     * @param investmentList the given Investments
     * @param gui            Connection to the gui
     */
    //TODO JavaDoc
    public InvestmentBookView(Set<Platform> platforms, List<Investment> investmentList, GUIConnector gui) {
        this(new InvestmentBook(platforms, investmentList), gui);
    }

    //TODO JavaDoc
    public InvestmentBook getInvestmentBook() {
        return INVESTMENT_BOOK;
    }

    //TODO JavaDoc
    public Set<Platform> getPlatforms() {
        return Collections.unmodifiableSet(PLATFORMS);
    }

    //TODO JavaDoc
    public List<Investment> getInvestmentList() {
        return Collections.unmodifiableList(INVESTMENT_LIST);
    }

    //TODO JavaDoc
    public void addPlatform(FeeTypes platformType, String platformName, double fee, double min) {
        Platform newPlatform;
        switch (platformType) {
            case PERCENT:
                newPlatform = new PercentPlatform(platformName, fee);
                break;
            case ABSOLUTE:
                newPlatform = new AbsolutePlatform(platformName, fee);
                break;
            case MIXED:
                newPlatform = new MixedPlatform(platformName, fee, min);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + platformType);
        }
        PLATFORMS.add(newPlatform);
        GUI.addPlatform(newPlatform);
    }

    //TODO JavaDoc
    public void deletePlatform(Platform platform) {
        PLATFORMS.remove(platform);
        GUI.deletePlatform(platform);
    }

    //TODO JavaDoc
    public void addInvestment(LocalDate creationDate, Platform platform, String stockName,
                              double exchangeRate, double capital, double sellingPrice,
                              LocalDate sellingDate) {
        Investment newInvestment;
        if (sellingPrice > 0 && sellingDate != null) {
            newInvestment = new Investment(creationDate, platform, stockName,
                    exchangeRate, capital, sellingPrice, sellingDate);
        } else {
            newInvestment = new Investment(creationDate, platform, stockName,
                    exchangeRate, capital);
        }
        INVESTMENT_LIST.add(newInvestment);
        GUI.addInvestmentOnDisplay(newInvestment);
    }

    //TODO JavaDoc
    public void deleteInvestment(Investment investment) {
        INVESTMENT_LIST.remove(investment);
        updateInvestmentList();
    }

    private List<Investment> filterByCondition(List<Investment> investmentList,
                                               Predicate<Investment> condition) {
        return investmentList.stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    /**
     * Filter the given Investments by the given status and
     * returns the filtered investments
     *
     * @param status the filter attribute
     * @return filtered investments by the given status
     */
    public List<Investment> filterByStatus(List<Investment> investmentList, Status status) {
        return filterByCondition(investmentList, x -> x.getStatus() == status);
    }

    /**
     * Filter the given Investments by the given platform and
     * returns the filtered investments
     *
     * @param platform the filter attribute
     * @return filtered investments by the given platform
     */
    public List<Investment> filterByPlatform(List<Investment> investmentList, Platform platform) {
        return filterByCondition(investmentList, x -> x.getPlatform().equals(platform));
    }

    /**
     * Filter the given Investments by the given platform and
     * returns the filtered investments
     *
     * @param stockName the filter attribute
     * @return filtered investments by the given platform
     */
    public List<Investment> filterByStockName(List<Investment> investmentList, String stockName) {
        return filterByCondition(investmentList, x -> x.getStockName().equals(stockName));
    }

    /**
     * Filter the given Investments by there creating date, the given month,
     * the given year and returns the filtered investments
     *
     * @param month the filter attribute
     * @return filtered investments by the given month
     */
    public List<Investment> filterByMonthAndYear(List<Investment> investmentList, Month month,
                                                 int year) {
        if (month != null) {
            Predicate<Investment> predicate = x -> x.getCreationDate().getMonth().equals(month);
            return filterByCondition(filterByYear(investmentList, year), predicate);

        }
        throw new IllegalArgumentException("month == null");
    }

    /**
     * Filter the given Investments by there creating date, the given quarter,
     * the given year and returns the filtered investments
     *
     * @param quarter the filter attribute
     * @return filtered investments by the given quarter
     */
    public List<Investment> filterByQuarterAndYear(List<Investment> investmentList, Quarter quarter,
                                                   int year) {
        if (quarter != null) {
            Predicate<Investment> predicate =
                    x -> quarter.getMonths().contains(x.getCreationDate().getMonth());
            return filterByCondition(filterByYear(investmentList, year), predicate);

        }
        throw new IllegalArgumentException("quarter == null");
    }

    /**
     * Filter the given Investments by the given year and
     * returns the filtered investments
     *
     * @param year the filter attribute
     * @return filtered investments by the given year
     */
    public List<Investment> filterByYear(List<Investment> investmentList, int year) {
        //lower border 0 for year value
        if (year >= 0) {
            Predicate<Investment> predicate = x -> x.getCreationDate().getYear() == year;
            return filterByCondition(investmentList, predicate);
        } else {
            throw new IllegalArgumentException("The year is negative");
        }
    }

    //TODO JavaDoc
    public void filter(Status status, Platform platform, String stockName,
                       Month month, Quarter quarter, int year) {
        List<Investment> filteredList = new ArrayList<>(INVESTMENT_LIST);

        if (status != null) {
            filteredList = filterByStatus(filteredList, status);
            System.out.println(1);
            System.out.println(filteredList);
        }
        if (platform != null) {
            filteredList = filterByPlatform(filteredList, platform);
            System.out.println(2);
            System.out.println(filteredList);
        }
        if (!stockName.equals("")) {
            filteredList = filterByStockName(filteredList, stockName);
            System.out.println(3);
            System.out.println(filteredList);
        }
        if (month != null) {
            filteredList = filterByMonthAndYear(filteredList, month, year);
            System.out.println(filteredList);
        } else if (quarter != null) {
            filteredList = filterByQuarterAndYear(filteredList, quarter, year);
            System.out.println(filteredList);
        } else if (year > 0) {
            filteredList = filterByYear(filteredList, year);
            System.out.println(filteredList);
        }
        GUI.displayInvestmentList(filteredList);
    }

    /**
     * Converts the Investments into a string, but only as a short version.
     *
     * @return a string representing some information of the Investments
     */
    protected String toStringShort() {
        StringBuilder builder = new StringBuilder();
        builder.append("|");
        for (int i = 3; i < ATTRIBUTES.length - 1; i++) {
            builder.append(ATTRIBUTES[i])
                    .append(calcSpaces(ATTRIBUTES[i].length()))
                    .append("|");
        }
        builder.append("\n");
        builder.append("|");
        for (int i = 0; i < 6; i++) {
            builder.append("-".repeat(COL_LENGTH))
                    .append("|");
        }
        builder.append("\n");
        for (Investment currInvest : INVESTMENT_LIST) {
            builder.append(currInvest.toStringShort()).append("\n");
        }
        return builder.toString();
    }

    /**
     * Converts the Investments into a string.
     *
     * @return a string representing the Investments
     */
    //TODO optimize
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("|");
        for (String category : ATTRIBUTES) {
            builder.append(category)
                    .append(calcSpaces(category.length()))
                    .append("|");
        }
        builder.append("\n");
        builder.append("|");
        for (int i = 0; i < ATTRIBUTES.length; i++) {
            builder.append("-".repeat(COL_LENGTH))
                    .append("|");
        }
        builder.append("\n");
        for (Investment currInvest : INVESTMENT_LIST) {
            builder.append(currInvest);
        }
        return builder.toString();
    }
}