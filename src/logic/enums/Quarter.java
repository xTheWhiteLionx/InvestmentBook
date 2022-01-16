package logic.enums;

import org.jetbrains.annotations.NotNull;

import java.time.Month;
import java.util.Set;

/**
 * Enum of quarter with their months
 *
 * @author Fabian Hardt
 */
public enum Quarter {
    Q1(Month.JANUARY, Month.FEBRUARY, Month.MARCH),
    Q2(Month.APRIL, Month.MAY, Month.JUNE),
    Q3(Month.JULY, Month.AUGUST, Month.SEPTEMBER),
    Q4(Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER);

    //TODO JavaDoc
    private final Set<Month> months;

    /**
     * Constructor of a quarter
     *
     * @param months vararg of months
     */
    Quarter(Month... months) {
        this.months = Set.of(months);
    }

    /**
     * Returns a Set of months of the Quarter
     *
     * @return Set of months of the Quarter
     */
    public Set<Month> getMonths() {
        return months;
    }

    /**
     *
     * @param month
     * @return
     */
    //TODO JavaDoc
    public static @NotNull Quarter getQuarterByMonth(Month month){
        for (Quarter quarter: Quarter.values()) {
            for (Month monthOfQuarter: quarter.months) {
                if (monthOfQuarter == month) {
                    return quarter;
                }
            }
        }
        throw new IllegalArgumentException("value does not apply to month");
    }
}
