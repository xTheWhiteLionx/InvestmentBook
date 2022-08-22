package gui;

import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Locale;

import static helper.GeneralMethods.round;
import static javafx.scene.paint.Color.*;
import static logic.BigDecimalUtils.formatMoney;

/**
 *
 */
public class Style {
    /**
     * Symbol of the local currency
     */
    public static final String SYMBOL_OF_CURRENCY = Currency.getInstance(Locale.getDefault())
            .getSymbol();

    /** The date pattern that is used for conversion. Change as you wish. */
    private static final String DATE_PATTERN = "dd.MM.yyyy";

    /** The date formatter. */
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_PATTERN);

    /**
     * Sets the text of the given Labels to the
     * current currency symbol
     *
     * @param labels currency labels
     */
    public static Label createCurrencyLbl() {
        return new Label(SYMBOL_OF_CURRENCY);
    }

    /**
     * Sets the text of the given Labels to the
     * current currency symbol
     *
     * @param labels currency labels
     */
    public static void setCurrenciesForLbls(Label... labels) {
        for (Label lbl : labels) {
            lbl.setText(SYMBOL_OF_CURRENCY);
        }
    }

    /**
     * Sets the given value as text of the given label and
     * colors the text depending on his value.
     * + = green
     * 0 = black
     * - = red
     *
     * @param value    the given value
     * @param valueLbl label to show the value
     */
    public static void setAndColorsText(double value, Label valueLbl) {
        assert valueLbl != null;

        valueLbl.setText(DoubleUtil.formatMoney(round(value)));
        Paint color = BLACK;
        if (value > 0) {
            color = GREEN;
        } else if (value < 0) {
            color = RED;
        }
        valueLbl.setTextFill(color);
    }

    /**
     * Sets the given value as text of the given label and
     * colors the text depending on his value.
     * + = green
     * 0 = black
     * - = red
     *
     * @param value    the given value
     * @param valueLbl label to show the value
     */
    public static void setAndColorsText(BigDecimal value, Label valueLbl) {
        assert valueLbl != null;

        valueLbl.setText(formatMoney(value));
        Paint color = BLACK;
        int compare = value.compareTo(BigDecimal.ZERO);
        if (compare > 0) {
            color = GREEN;
        } else if (compare < 0) {
            color = RED;
        }
        valueLbl.setTextFill(color);
    }

    /**
     * Returns the given date as a well formatted String. The above defined
     * {@link DoubleUtil#DATE_PATTERN} is used.
     *
     * @param date the date to be returned as a string
     * @return formatted string
     */
    public static String format(LocalDate date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMATTER.format(date);
    }
}
