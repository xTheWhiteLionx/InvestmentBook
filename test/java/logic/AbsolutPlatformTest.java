package logic;

import org.junit.Test;

import java.time.LocalDate;

import static logic.HelperClass.ONVISTA_BANK;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author xthe_white_lionx
 */
public class AbsolutPlatformTest {

    @Test
    public void testSellingPriceCalculator() {

        Investment invest = new Investment(
                LocalDate.of(2019, 9, 2),
                ONVISTA_BANK,
                "Alibaba",
                162,
                486,
                492.60,
                LocalDate.of(2020, 3, 12)
        );

        assertEquals(168.87, invest.getPlatform().calcSellingExchangeRate(invest, 6.60), 0.00);
    }

    @Test
    public void testSellingPriceCalculator2() {

        Investment invest = new Investment(
                LocalDate.of(2019, 9, 2),
                ONVISTA_BANK,
                "Alibaba",
                162,
                486,
                492.60,
                LocalDate.of(2020, 3, 12)
        );

        assertEquals(164.2, invest.getPlatform().calcSellingExchangeRate(invest,
                invest.getPerformance()), 0.00);
    }
}
