package logic;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

        assertEquals(BigDecimal.valueOf(168.87),
                invest.getPlatform().calcSellingExchangeRate(invest, BigDecimal.valueOf(6.60)));
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

        assertEquals(BigDecimal.valueOf(164.2).setScale(2, RoundingMode.HALF_UP),
                invest.getPlatform().calcSellingExchangeRate(invest,
                invest.getPerformance()));
    }
}
