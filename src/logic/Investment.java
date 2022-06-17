package logic;

import logic.platform.Platform;

import java.time.LocalDate;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;
import static logic.GeneralMethods.*;
import static logic.Status.CLOSED;
import static logic.Status.OPEN;

/**
 * This class contains the Investment logic.
 *
 * @author Fabian Hardt
 */
public class Investment implements Comparable<Investment>{

    /**
     * Creation Date of the investment as
     * ISO 8601 (YYYY-MM-dd)
     */
    private LocalDate creationDate;

    /**
     * Status of the investment
     * (must be either open or closed)
     */
    private Status status = OPEN;

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
    private double sellingPrice = 0;

    /**
     * Performance of the investment
     */
    private double performance = 0;

    /**
     * Performance of the investment in percent,
     * depending on the capital
     */
    private double percentPerformance = 0;

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
    private long holdingPeriod = 0;

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
        this.platform = platform;
        this.stockName = stockName;
        this.exchangeRate = exchangeRate;
        this.capital = capital;
        this.cost = platform.getFee(capital);
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
            exchangeRate = newExchangeRate;
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
            return round(sellingPrice - (capital + cost));
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
                        cost = round(cost + platform.getFee(newSellingPrice));
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
     *
     * @param other
     * @return
     */
    @Override
    public int compareTo(Investment other) {
        return this.creationDate.compareTo(other.creationDate);
    }

    /**
     * Converts the Investment into a string.
     *
     * @return a string representing of the Investment
     */
    @Override
    public String toString() {
        return "Investment{" +
                "creationDate=" + creationDate + "\n" +
                ", status=" + status + "\n" +
                ", platform=" + platform + "\n" +
                ", stockName='" + stockName + '\'' + "\n" +
                ", exchangeRate=" + exchangeRate + "\n" +
                ", capital=" + capital + "\n" +
                ", sellingPrice=" + sellingPrice + "\n" +
                ", performance=" + performance + "\n" +
                ", percentPerformance=" + percentPerformance + "\n" +
                ", cost=" + cost + "\n" +
                ", sellingDate=" + sellingDate + "\n" +
                ", holdingPeriod=" + holdingPeriod +
                '}';
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Investment that)) return false;
        return this.exchangeRate == that.exchangeRate
                && this.capital == that.capital
                && this.sellingPrice == that.sellingPrice
                && this.performance == that.performance
                && this.percentPerformance == that.percentPerformance
                && this.cost == that.cost
                && this.status == that.status
                && this.platform.equals(that.platform)
                && this.stockName.equals(that.stockName);
    }
}
