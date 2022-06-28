package helper;

import gui.Style;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Helper {

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
     * Creates an changeListener which examines if the given text field is a valid double and
     * depending on this validity, toggles the accessibility of the given button
     *
     * @param txtFld to be examined text field
     * @param btn    the given button
     * @return String changeListener
     */
    public static ChangeListener<String> createChangeListener(TextField txtFld, Button btn) {
        assert txtFld != null;
        assert btn != null;

        return (observable, oldValue, newValue) -> btn.setDisable(!isValidDouble(txtFld));
    }

    /**
     * Returns a double out of the given text field and
     * replace dots with commas
     *
     * @param txtFld the given text field
     * @return double out of text field
     */
    public static double doubleOfTextField(TextField txtFld) {
        assert txtFld != null;
        assert isValidDouble(txtFld);

        return Double.parseDouble(txtFld.getText().replace(",", "."));
    }

    /**
     * Returns a string out of the given double value and
     * replace commas with dots
     *
     * @param value the given double value
     * @return string out of double value
     */
    public static String stringOfDouble(double value) {
        return String.valueOf(value).replace(".", ",");
    }
}
