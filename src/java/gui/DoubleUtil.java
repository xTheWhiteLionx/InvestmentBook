package gui;

import javafx.scene.control.TextField;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;

/**
 * Helper functions for handling dates.
 *
 * @author
 */
public class DoubleUtil {

    /**
     * Regex for only integers or double values
     * (only two decimal places are guaranteed)
     */
    public static final String DOUBLE_PATTERN = "^[0-9]+([,.][0-9][0-9])?$";

    public static final DecimalFormat df = new DecimalFormat("0.00");

    public static final DecimalFormat dfMoney = new DecimalFormat("0.00 " + Style.SYMBOL_OF_CURRENCY);

    /**
     * Examines if the given text field matches the regex for
     * a valid double
     *
     * @param txtFld to be examined text field
     * @return true if the text, of the text field, is a valid double
     * otherwise false
     */
    public static boolean isValidDouble(TextField txtFld) {
        assert txtFld != null;

        return txtFld.getText().matches(DOUBLE_PATTERN);
    }

    /**
     * Checks the String whether it is a valid date.
     *
     * @param text
     * @return true if the String is a valid date
     */
    public static boolean isValidDouble(String text) {
        return text.matches(DOUBLE_PATTERN);
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
    public static double parse(String text) {
        assert text != null;
        assert isValidDouble(text);

        return Double.parseDouble(text.replace(",", "."));
    }

    /**
     * Returns a string out of the given double value and
     * replace commas with dots
     *
     * @param value the given double value
     * @return string out of double value
     */
    public static String format(double value) {
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
    public static String formatMoney(double value) {
        return format(value) + " " + Style.SYMBOL_OF_CURRENCY;
    }
}
