package logic;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * Hello There! This class contains the general variables and Methods.
 *
 * @author xthe_white_lionx
 */
public class GeneralMethods {

    /**
     * The directory of the package of the json files
     */
    public static final String DIRECTORY = "books/";

    /**
     * Rounds the given double
     *
     * @param value
     * @return
     */
    //TODO JavaDoc
    public static double round(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Calculates the percent of the given price depending on the given base (100%).
     * rule of three.
     *
     * @param base  the given base (100%)
     * @param price the given price
     * @return percent of the given price
     */
    public static double calcPercent(double base, double price) {
        return (100d / base) * price;
    }

    /**
     * Calculates the percent of the given price depending on the given base (100%),
     * with calling the methode calcPercent
     *
     * @param base  the given base (100%)
     * @param price the given price
     * @return percent of the given price
     */
    //TODO JavaDoc
    public static double calcPercentRounded(double base, double price) {
        return round(calcPercent(base, price));
    }

}
