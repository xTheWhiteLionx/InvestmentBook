package logic;

import logic.enums.Status;
import logic.platform.Platform;

import java.time.LocalDate;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;
import static logic.GeneralMethods.*;
import static logic.enums.Status.*;

/**
 * This class contains the Investment logic.
 *
 * @author Fabian Hardt
 */
public class Investment {

    /**
     * Creation Date of the investment as
     * ISO 8601 (YYYY-MM-dd)
     */
    private LocalDate creationDate;

    /**
     * Status of the investment
     * (must be either open or closed)
     */
    private Status status;

    /**
     * Platform of the investment
     */
    private final Platform platform;

    /**
     * Stock name of the investment
     */
    private String stockName;

    /**
     * Exchange rate of the investment at which it was bought
     */
    private double exchangeRate;

    /**
     * Capital of the investment
     */
    private double capital;

    /**
     * Selling price of the investment,
     * if it is 0 then it was not sold yet
     */
    private double sellingPrice;

    /**
     * Performance of the investment
     */
    private double performance;

    /**
     * Performance of the investment in percent,
     * depending on the capital
     */
    private double percentPerformance;

    /**
     * Costs of the investment
     */
    private double cost;

    /**
     * Creation Date of the investment as
     * ISO 8601 (YYYY-MM-dd)
     */
    private LocalDate sellingDate;

    /**
     * Holding period of the investment in days
     */
    private long holdingPeriod;

    /**
     * Constructor for a new (open) Investment, with calling of the constructor.
     *
     * @param creationDate
     * @param platform     platform of the investment
     * @param stockName    stock name of the investment
     * @param exchangeRate exchange rate of the investment at which it was bought
     * @param capital      capital of the investment
     */
    //TODO JavaDoc
    public Investment(LocalDate creationDate, Platform platform, String stockName,
                      double exchangeRate, double capital) {
        assert creationDate != null;
        assert platform != null;
        assert !stockName.isEmpty();
        assert exchangeRate > 0;
        assert capital > 0;

        this.creationDate = creationDate;
        this.status = OPEN;
        this.platform = platform;
        this.stockName = stockName;
        this.exchangeRate = exchangeRate;
        this.capital = capital;
        this.sellingPrice = 0;
        this.cost = platform.getFee(capital);
        this.performance = 0;
        this.percentPerformance = 0;
        this.sellingDate = null;
        this.holdingPeriod = 0;
    }

    /**
     * Constructor for a closed Investment, creates an open investment, with
     * calling of the constructor of an open Investment, which gets closed at
     * the next step by calling closeInvestment().
     *
     * @param creationDate
     * @param platform     platform of the investment
     * @param stockName    stock name of the investment
     * @param exchangeRate exchange rate of the investment at which it was bought
     * @param capital      capital of the investment
     * @param sellingPrice selling price of the investment
     * @param sellingDate
     */
    //TODO JavaDoc
    //TODO closed Investment
    public Investment(LocalDate creationDate, Platform platform, String stockName,
                      double exchangeRate, double capital, double sellingPrice,
                      LocalDate sellingDate) {
        this(creationDate, platform, stockName, exchangeRate, capital);
        this.closeInvestment(sellingDate, sellingPrice);
    }

    /**
     * Returns the creation Date of the investment
     * as LocalDate
     *
     * @return creation Date
     */
    public LocalDate getCreationDate() {
        return creationDate;
    }

    //TODO JavaDoc
    private boolean areAcceptableDates(LocalDate startDate, LocalDate endDate) {
        return (startDate.isBefore(endDate) || startDate.isEqual(endDate));
    }

    /**
     * Set the creating Date and update the holding period of the investment
     *
     * @param newCreationDate creation Date
     */
    public void setCreationDate(LocalDate newCreationDate) {
        if (sellingDate != null) {
            //checks if the selling date is after the creation date or are equals
            if (areAcceptableDates(newCreationDate, sellingDate)) {
                this.holdingPeriod = DAYS.between(newCreationDate, sellingDate);
            } else {
                throw new IllegalArgumentException("given creation date is after the selling date, logical error");
            }
        }
        this.creationDate = newCreationDate;
    }

    /**
     * Returns the selling Date of the investment
     * as LocalDate
     *
     * @return selling Date
     */
    public LocalDate getSellingDate() {
        return sellingDate;
    }

    /**
     * Set the selling Date and update the holding period of the investment
     *
     * @param newSellingDate
     */
    public void setSellingDate(LocalDate newSellingDate) {
        if (newSellingDate != null) {
            if (areAcceptableDates(creationDate, newSellingDate)) {
                if (!sellingDate.isEqual(newSellingDate)) {
                    holdingPeriod = DAYS.between(creationDate, newSellingDate);
                    sellingDate = newSellingDate;
                }
            } else {
                throw new IllegalArgumentException("creation date is after the given selling date, logical error");
            }
        } else {
            throw new IllegalArgumentException("given selling date is null");
        }
    }

    /**
     * Returns the holding period of the investment
     *
     * @return holding period
     */
    public long getHoldingPeriod() {
        return holdingPeriod;
    }

    /**
     * Returns the status of the investment
     *
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Returns the platform of the investment
     *
     * @return the platform
     */
    public Platform getPlatform() {
        return platform;
    }

    /**
     * Returns the stock name of the investment
     *
     * @return the stock name
     */
    public String getStockName() {
        return stockName;
    }

    //TODO JavaDoc
    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    /**
     * Returns the exchange rate of the investment
     *
     * @return the exchange rate
     */
    public double getExchangeRate() {
        return exchangeRate;
    }

    //TODO JavaDoc
    public void setExchangeRate(double newExchangeRate) {
        if (newExchangeRate > 0) {
            if (exchangeRate != newExchangeRate) {
                exchangeRate = newExchangeRate;
            }
        } else {
            //TODO write case
            throw new IllegalArgumentException("");
        }
    }

    /**
     * Returns the capital of the investment
     *
     * @return the capital
     */
    public double getCapital() {
        return capital;
    }

    //TODO JavaDoc
    public void setCapital(double newCapital) {
        if (newCapital > 0) {
            if (capital != newCapital) {
                capital = newCapital;
                if (status == CLOSED) {
                    cost = platform.getFee(capital);
                    performance = calcPerformance(sellingPrice);
                    percentPerformance = calcPercentRounded(capital, performance);
                }
            }
        } else {
            //TODO write case
            throw new IllegalArgumentException("");
        }
    }

    /**
     * Returns the selling price of the investment
     *
     * @return the selling price
     */
    public double getSellingPrice() {
        return sellingPrice;
    }

    //TODO JavaDoc
    public double calcPerformance(double sellingPrice) {
        if (sellingPrice >= 0) {
            return roundDouble(sellingPrice - (capital + cost));
        } else {
            //TODO write case
            throw new IllegalArgumentException("");
        }
    }

    /**
     * Sets the selling price of the investment and updates the selling costs,
     * the performance, the performance in percent, the selling Date,
     * the holding period and the status of the investment
     *
     * @param newSellingDate
     * @param newSellingPrice the given selling price on which the investment were sold
     */
    //TODO JavaDoc
    public void closeInvestment(LocalDate newSellingDate, double newSellingPrice) {
        assert newSellingPrice >= 0;

        if (newSellingDate != null) {
            if (newSellingPrice > 0) {
                if (areAcceptableDates(creationDate, newSellingDate)) {
                    if (sellingDate == null || !sellingDate.isEqual(newSellingDate)) {
                        //updates the holding period of the investment
                        holdingPeriod = DAYS.between(creationDate, newSellingDate);
                        //updates the selling Date of the investment
                        sellingDate = newSellingDate;
                    }
                    if (sellingPrice != newSellingPrice) {
                        if (status == OPEN) {
                            status = CLOSED;
                        } else {
                            //TODO explain tactic
                            cost = platform.getFee(capital);
                        }
                        //updates the cost of the investment
                        cost = roundDouble(cost + platform.getFee(newSellingPrice));
                        //calculated performance
                        performance = calcPerformance(newSellingPrice);
                        System.out.println(performance);
                        // rule of three
                        percentPerformance = calcPercentRounded(capital, performance);
                        sellingPrice = newSellingPrice;
                    }
                } else {
                    throw new IllegalArgumentException("creation date is after the given sellingDate, logical error");

                }
            }
        }
    }

    /**
     * Sets the selling price of the investment and updates the selling costs,
     * the performance, the performance in percent, the selling Date,
     * the holding period and the status of the investment
     *
     * @param newSellingPrice the given selling price on which the investment were sold
     */
    //TODO JavaDoc
    //TODO never used?
    public void setSellingPrice(double newSellingPrice) {
        if (newSellingPrice >= 0) {
            if (sellingPrice != newSellingPrice) {
                closeInvestment(LocalDate.now(), newSellingPrice);
            }
        } else {
            throw new IllegalArgumentException("the given selling price is negative");
        }
    }

    /**
     * Returns the absolute performance of the investment
     *
     * @return the absolute performance
     */
    public double getPerformance() {
        return performance;
    }

    /**
     * Returns the performance of the investment in percent
     *
     * @return the performance of the investment in percent
     */
    public double getPercentPerformance() {
        return percentPerformance;
    }

    /**
     * Returns the cost of the investment
     *
     * @return the cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * Converts the Investments into a string, but only a short version.
     *
     * @return a string representing some information of the Investments
     */
    protected String toStringShort() {
        StringBuilder builder = new StringBuilder();
        builder.append("|")
                .append(stockName)
                .append(calcSpaces(stockName.length()))
                .append("|");

        String str = String.format("%.2f %s", exchangeRate, CURRENCY);
        builder.append(str)
                .append(calcSpaces(str.length())).append("|");

        str = String.format("%.2f %s", capital, CURRENCY);
        builder.append(str)
                .append(calcSpaces(str.length())).append("|");

        str = String.format("%.2f %s", sellingPrice, CURRENCY);
        builder.append(str)
                .append(calcSpaces(str.length())).append("|");

        str = String.format("%.2f %s", performance, CURRENCY);
        builder.append(str)
                .append(calcSpaces(str.length())).append("|");

        str = String.format("%.2f", percentPerformance) + " %";
        builder.append(str)
                .append(calcSpaces(str.length())).append("|");

        return builder.toString();
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        builder.append("|")
                .append(creationDate)
                .append(calcSpaces(creationDate.toString().length()))
                .append("|")
                .append(status.name())
                .append(calcSpaces(status.name().length()))
                .append("|")
                .append(platform.getName())
                .append(calcSpaces(platform.getName().length()))
                .append(toStringShort());

        String str = String.format("%.2f %s", cost, CURRENCY);
        builder.append(str)
                .append(calcSpaces(str.length())).append("|");
        builder.append("\n");

        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Investment that)) return false;
        return that.exchangeRate == exchangeRate
                && that.capital == capital
                && that.sellingPrice == sellingPrice
                && that.performance == performance
                && that.percentPerformance == percentPerformance
                && that.cost == cost
                && status == that.status
                && platform.equals(that.platform)
                && stockName.equals(that.stockName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status,
                platform,
                stockName,
                exchangeRate,
                capital,
                sellingPrice,
                performance,
                percentPerformance,
                cost
        );
    }
}
