package gui;

import helper.DoubleTools;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

import java.util.Currency;
import java.util.Locale;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.RED;
import static helper.GeneralMethods.round;

/**
 *
 */
public class Style {
    /**
     * Regex for only integers or double values
     * (only two decimal places are guaranteed)
     */
    public static final String TXT_FIELD_REGEX = "^[0-9]+([,.][0-9][0-9])?$";
    /**
     * Symbol of the local currency
     */
    public static final String SYMBOL_OF_CURRENCY = Currency.getInstance(Locale.getDefault())
            .getSymbol();

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

        valueLbl.setText(DoubleTools.toString(round(value)));
        Paint color = BLACK;
        if (value > 0) {
            color = GREEN;
        } else if (value < 0) {
            color = RED;
        }
        valueLbl.setTextFill(color);
    }
}
