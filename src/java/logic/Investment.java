package logic;

import logic.platform.Platform;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;
import static logic.BigDecimalUtils.*;
import static logic.State.CLOSED;
import static logic.State.OPEN;

/**
 * This class contains the Investment logic.
 *
 * @author Fabian Hardt
 */
public class Investment implements Comparable<Investment> {

    /**
     * Creation Date of the investment as
     * ISO 8601 (YYYY-MM-dd)
     */
    private LocalDate creationDate;

    /**
     * Status of the investment
     * (must be either open or closed)
     */
    private State state = OPEN;

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
    private BigDecimal exchangeRate;

    /**
     * Capital of the investment
     */
    private BigDecimal capital;

    /**
     * Selling price of the investment
     */
    private BigDecimal sellingPrice = BigDecimal.ZERO;

    /**
     * Performance of the investment
     */
    private BigDecimal performance = BigDecimal.ZERO;

    /**
     * Performance of the investment in percent,
     * depending on the capital
     */
    private BigDecimal percentPerformance = BigDecimal.ZERO;

    /**
     * Costs of the investment
     */
    private BigDecimal cost;

    /**
     * Creation Date of the investment as
     * ISO 8601 (YYYY-MM-dd)
     */
    private LocalDate sellingDate;

    /**
     * Holding period of the investment since the buying date, in days.
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
     * @throws NullPointerException     if any argument is null
     * @throws IllegalArgumentException
     */
    //TODO JavaDoc
    public Investment(LocalDate creationDate, Platform platform, String stockName,
                      double exchangeRate, double capital) {
        if (creationDate == null) {
            throw new NullPointerException();
        }
        if (platform == null) {
            throw new NullPointerException();
        }
        if (stockName == null) {
            throw new NullPointerException();
        }
        if (stockName.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (exchangeRate <= 0) {
            throw new IllegalArgumentException();
        }
        if (capital <= 0) {
            throw new IllegalArgumentException();
        }

        this.creationDate = creationDate;
        this.platform = platform;
        this.stockName = stockName;
        this.exchangeRate = BigDecimal.valueOf(exchangeRate);
        this.capital = BigDecimal.valueOf(capital);
        this.cost = BigDecimal.valueOf(platform.getFee(BigDecimal.valueOf(capital))).setScale(2,RoundingMode.HALF_UP);
        this.holdingPeriod = DAYS.between(creationDate, LocalDate.now());
    }

    /**
     * Constructor for a new (open) Investment, with calling of the constructor.
     *
     * @param creationDate
     * @param platform     platform of the investment
     * @param stockName    stock name of the investment
     * @param exchangeRate exchange rate of the investment at which it was bought
     * @param capital      capital of the investment
     * @throws NullPointerException     if any argument is null
     * @throws IllegalArgumentException
     */
    //TODO JavaDoc
    public Investment(LocalDate creationDate, Platform platform, String stockName,
                      BigDecimal exchangeRate, BigDecimal capital) {
        if (creationDate == null) {
            throw new NullPointerException();
        }
        if (platform == null) {
            throw new NullPointerException();
        }
        if (stockName == null) {
            throw new NullPointerException();
        }
        if (stockName.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (!isPositive(exchangeRate)) {
            throw new IllegalArgumentException();
        }
        if (!isPositive(capital)) {
            throw new IllegalArgumentException();
        }

        this.creationDate = creationDate;
        this.platform = platform;
        this.stockName = stockName;
        this.exchangeRate = exchangeRate;
        this.capital = capital;
        this.cost = BigDecimal.valueOf(platform.getFee(capital)).setScale(2,RoundingMode.HALF_UP);
        this.holdingPeriod = DAYS.between(creationDate, LocalDate.now());
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
        this.closeInvestment(sellingDate, BigDecimal.valueOf(sellingPrice));
    }

    /**
     * Checks if the end date is after the start date or are equals
     *
     * @param startDate
     * @param endDate
     * @return true if the end date is after the start date or are equals otherwise false
     */
    //TODO JavaDoc
    private static boolean areAcceptableDates(LocalDate startDate, LocalDate endDate) {
        return startDate.compareTo(endDate) <= 0;
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

    /**
     * Set the creating Date and update the holding period of the investment
     *
     * @param newCreationDate creation Date
     * @throws NullPointerException     if specified the newCreationDate is null
     * @throws IllegalArgumentException
     */
    // TODO: 17.06.2022 JavaDoc
    public void setCreationDate(LocalDate newCreationDate) {
        if (newCreationDate == null) {
            throw new NullPointerException();
        }

        if (sellingDate != null) {
            if (areAcceptableDates(newCreationDate, sellingDate)) {
                this.holdingPeriod = DAYS.between(newCreationDate, sellingDate);
            } else {
                throw new IllegalArgumentException("given creation date is after the selling date,"
                        + " logical error");
            }
        } else {
            this.holdingPeriod = DAYS.between(newCreationDate, LocalDate.now());
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
    public State getState() {
        return state;
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

    /**
     * Set the creating Date and update the holding period of the investment
     *
     * @param stockName creation Date
     * @throws NullPointerException     if specified the newCreationDate is null
     * @throws IllegalArgumentException
     */
    // TODO: 17.06.2022 JavaDoc
    public void setStockName(String stockName) {
        if (stockName == null) {
            throw new NullPointerException();
        }
        if (stockName.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.stockName = stockName;
    }

    /**
     * Returns the exchange rate of the investment
     *
     * @return the exchange rate
     */
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    /**
     * Set the creating Date and update the holding period of the investment
     *
     * @param newExchangeRate creation Date
     * @throws NullPointerException     if specified the newCreationDate is null
     * @throws IllegalArgumentException
     */
    // TODO: 17.06.2022 JavaDoc
    public void setExchangeRate(BigDecimal newExchangeRate) {
        if (!isPositive(newExchangeRate)) {
            throw new IllegalArgumentException("");
        }
        exchangeRate = newExchangeRate;
    }

    /**
     * Returns the capital of the investment
     *
     * @return the capital
     */
    public BigDecimal getCapital() {
        return capital;
    }

    /**
     * Set the creating Date and update the holding period of the investment
     *
     * @param newCapital creation Date
     * @throws NullPointerException     if specified the newCreationDate is null
     * @throws IllegalArgumentException
     */
    // TODO: 17.06.2022 JavaDoc
    public void setCapital(BigDecimal newCapital) {
        if (!isPositive(newCapital)) {
            throw new IllegalArgumentException("");
        }
        capital = newCapital;
        if (state == CLOSED) {
            cost = BigDecimal.valueOf(platform.getFee(capital)).setScale(2,RoundingMode.HALF_UP);
            performance = calcPerformance(sellingPrice);
            percentPerformance = calcPercent(capital, performance);
        }
    }

    /**
     * Returns the selling price of the investment
     *
     * @return the selling price
     */
    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    //TODO JavaDoc
    public BigDecimal calcPerformance(BigDecimal sellingPrice) {
        if (!isPositive(sellingPrice)) {
            throw new IllegalArgumentException("");
        }
        return sellingPrice.subtract(capital.add(cost)).setScale(2, RoundingMode.HALF_UP);
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
    public void closeInvestment(LocalDate newSellingDate, BigDecimal newSellingPrice) {
        if (newSellingDate == null) {
            throw new NullPointerException();
        }
        if (!areAcceptableDates(creationDate, newSellingDate)) {
            throw new IllegalArgumentException("creation date is after the given sellingDate, logical error");
        }
        if (!isPositive(newSellingPrice)) {
            throw new IllegalArgumentException();
        }

        if (sellingDate == null || !sellingDate.isEqual(newSellingDate)) {
            if (sellingDate == null) {
                state = CLOSED;
            } else if (!sellingDate.isEqual(newSellingDate)) {
                cost = BigDecimal.valueOf(platform.getFee(capital)).setScale(2,RoundingMode.HALF_UP);
            }
            //updates the cost of the investment
            cost = add(cost, platform.getFee(newSellingPrice)).setScale(2,RoundingMode.HALF_UP);
            //calculated performance
            performance = calcPerformance(newSellingPrice);
            // rule of three
            percentPerformance = calcPercent(capital, performance);
            sellingPrice = newSellingPrice.setScale(2, RoundingMode.HALF_UP);
            sellingDate = newSellingDate;
            //updates the holding period of the investment
            holdingPeriod = DAYS.between(creationDate, newSellingDate);
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
    public void setSellingPrice(BigDecimal newSellingPrice) {
        if (isPositive(newSellingPrice)) {
            if (!sellingPrice.equals(newSellingPrice)) {
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
    public BigDecimal getPerformance() {
        return performance;
    }

    /**
     * Returns the performance of the investment in percent
     *
     * @return the performance of the investment in percent
     */
    public BigDecimal getPercentPerformance() {
        return percentPerformance;
    }

    /**
     * Returns the cost of the investment
     *
     * @return the cost
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * Compares this investment with the specified investment.  Returns a
     * negative integer, zero, or a positive integer as this investment is less
     * than, equal to, or greater than the specified object.
     *
     * @param other the investment to be compared.
     * @return a negative integer, zero, or a positive integer as this investment
     * is less than, equal to, or greater than the specified investment.
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
                ", state=" + state + "\n" +
                ", platform=" + platform + "\n" +
                ", stockName='" + stockName + '\'' + "\n" +
                ", exchangeRate=" + exchangeRate + "\n" +
                ", capital=" + capital + "\n" +
                ", sellingPrice=" + sellingPrice + "\n" +
                ", performance=" + performance + "\n" +
                ", percentPerformance=" + percentPerformance + "\n" +
                ", cost=" + cost + "\n" +
                ", sellingDate=" + sellingDate + "\n" +
                ", holdingPeriod=" + holdingPeriod + '}' + "\n";
    }

    /**
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Investment that)) {
            return false;
        }
        return Objects.equals(this.exchangeRate, that.exchangeRate)
                && Objects.equals(this.capital, that.capital)
                && Objects.equals(this.sellingPrice, that.sellingPrice)
                && Objects.equals(this.performance, that.performance)
                && Objects.equals(this.percentPerformance, that.percentPerformance)
                && Objects.equals(this.cost, that.cost)
                && this.state == that.state
                && this.platform.equals(that.platform)
                && this.stockName.equals(that.stockName);
    }
}
