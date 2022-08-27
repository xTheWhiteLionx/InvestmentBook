package logic;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Hello There! This class contains the general variables and Methods.
 *
 * @author xthe_white_lionx
 */
public class DoubleUtils {

    /**
     * Rounds the specified double
     *
     * @param value
     * @return
     */
    //TODO JavaDoc
    public static double round(double value) {
        BigDecimal bd = BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
