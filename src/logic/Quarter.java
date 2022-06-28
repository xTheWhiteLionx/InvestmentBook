package logic;

import java.time.Month;
import java.util.Set;

import static java.time.Month.APRIL;
import static java.time.Month.AUGUST;
import static java.time.Month.DECEMBER;
import static java.time.Month.FEBRUARY;
import static java.time.Month.JANUARY;
import static java.time.Month.JULY;
import static java.time.Month.JUNE;
import static java.time.Month.MARCH;
import static java.time.Month.MAY;
import static java.time.Month.NOVEMBER;
import static java.time.Month.OCTOBER;
import static java.time.Month.SEPTEMBER;

/**
 * Enum of quarter.
 * A quarter is a quarter of a year, three months.
 * Denoted by Q1 to Q4
 *
 * @author xthe_white_lionx
 */
public enum Quarter {
    Q1(JANUARY, FEBRUARY, MARCH),
    Q2(APRIL, MAY, JUNE),
    Q3(JULY, AUGUST, SEPTEMBER),
    Q4(OCTOBER, NOVEMBER, DECEMBER);

    /**
     * Months of the quarter
     */
    private final Set<Month> months;

    /**
     * Constructor of a quarter
     *
     * @param months vararg of months
     * @throws NullPointerException if on of the specified month is null
     */
    Quarter(Month... months) {
        if (months == null) {
            throw new NullPointerException("months == null");
        }

        this.months = Set.of(months);
    }

    /**
     * Returns a Set of months of this Quarter
     *
     * @return Set of months of this Quarter
     */
    public Set<Month> getMonths() {
        return this.months;
    }

    /**
     * 
     * @param month
     * @return
     */
    public boolean contains(Month month){
        return this.months.contains(month);
    }

    /**
     * Returns the quarter of the given month
     *
     * @param month the given month
     * @return quarter of the month
     */
    public static Quarter getQuarterOfMonth(Month month) {
        assert month != null;
        Quarter[] quarters = Quarter.values();

        for (Quarter quarter : quarters) {
            if (quarter.months.contains(month)) {
                return quarter;
            }
        }
        return null;
    }
}
