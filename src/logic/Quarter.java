package logic;

import java.time.Month;
import java.util.Set;

import static java.time.Month.*;

/**
 * Enum of quarter with their months
 *
 * @author xthe_white_lionx
 */
public enum Quarter {
    Q1(JANUARY, FEBRUARY, MARCH),
    Q2(APRIL, MAY, JUNE),
    Q3(JULY, AUGUST, SEPTEMBER),
    Q4(OCTOBER, NOVEMBER, DECEMBER);

    /**
     * Set of months of the Quarter
     */
    private final Set<Month> months;

    /**
     * Constructor of a quarter
     *
     * @param months vararg of months
     */
    Quarter(Month... months) {
        assert months != null;

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
     * Returns the quarter of the given month
     *
     * @param month the given month
     * @return quarter of the month
     */
    public static Quarter getQuarterByMonth(Month month) {
        assert month != null;

        for (Quarter quarter : Quarter.values()) {
            for (Month monthOfQuarter : quarter.months) {
                if (monthOfQuarter == month) {
                    return quarter;
                }
            }
        }
        return null;
    }
}
