package helper;

import gui.Style;
import javafx.scene.control.TextField;

public class DoubleTools {

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

        return txtFld.getText().matches(Style.TXT_FIELD_REGEX);
    }

    /**
     * Examines if the given text field matches the regex for
     * a valid double
     *
     * @param txtFld to be examined text field
     * @return true if the text, of the text field, is a valid double
     * otherwise false
     */
    public static boolean isValidDouble(String text) {
        return text.matches(Style.TXT_FIELD_REGEX);
    }

    /**
     * Returns a double out of the given text field and
     * replace dots with commas
     *
     * @param txtFld the given text field
     * @return double out of text field
     */
    public static double toDouble(String text) {
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
    public static String toString(double value) {
        return String.valueOf(value).replace(".", ",");
    }
}
