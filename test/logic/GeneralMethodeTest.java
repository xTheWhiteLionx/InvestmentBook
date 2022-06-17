package logic;

import org.junit.Test;

import static logic.GeneralMethods.calcPercentRounded;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author xthe_white_lionx
 */
public class GeneralMethodeTest {

    @Test
    public void testCalcPercentForWholeNumbers() {

        double actual = calcPercentRounded(100, 10);

        double expected = 10;

        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testCalcPercentForDecimalNumbers() {
        assertEquals(10, calcPercentRounded(2560.92, 256.092), 0.0);
    }
}
