package logic;

import gui.Style;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;

import static gui.DoubleUtil.isValidDouble;

public class BigDecimalUtils {

    public static final BigDecimal HUNDRED = new BigDecimal(100);

    public static final DecimalFormat df = new DecimalFormat("0.00");

    public static final NumberFormat currency = NumberFormat.getCurrencyInstance();

    public static boolean isPositive(BigDecimal number){
        return number.compareTo(BigDecimal.ZERO) >= 0;
    }

    public static BigDecimal calcPercent(BigDecimal base, BigDecimal pct){
        return new BigDecimal(100).divide(base,2, RoundingMode.HALF_UP).multiply(pct)
                .setScale(2,RoundingMode.HALF_UP);
    }

    public static BigDecimal add(BigDecimal base, double augend){
        return base.add(BigDecimal.valueOf(augend));
    }

    /**
     * Converts a String in the format of the defined
     * to a {@link LocalDate} object.
     *
     * Returns null if the String could not be converted.
     *
     * @param dateString the date as String
     * @return the date object or null if it could not be converted
     */
    public static BigDecimal parse(String text) {
        assert text != null;
        assert isValidDouble(text);

        return new BigDecimal(text.replace(",", "."));
    }

    /**
     * Returns a string out of the given double value and
     * replace commas with dots
     *
     * @param value the given double value
     * @return string out of double value
     */
    public static String format(BigDecimal value) {
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(value);
    }

    /**
     * Returns a string out of the given double value and
     * replace commas with dots
     *
     * @param value the given double value
     * @return string out of double value
     */
    public static String formatMoney(BigDecimal value) {
        return format(value) + " " + Style.SYMBOL_OF_CURRENCY;
    }
}
