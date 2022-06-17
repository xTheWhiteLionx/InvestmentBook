package logic;

import logic.platform.PercentPlatform;
import org.junit.Test;

import java.time.LocalDate;

import static logic.GeneralMethods.calcPercent;
import static logic.HelperClass.COINBASE;
import static org.junit.Assert.assertEquals;

public class PercentPlatformTest {

    //TODO correct
    @Test
    public void testSellingPriceCalculator() {

        Investment invest = new Investment(
                LocalDate.of(2021, 11, 9),
                COINBASE,
                "Bitcoin",
                58644.49,
                3050,
                2560.92,
                LocalDate.of(2021, 11, 18)
        );

        double expected = invest.getExchangeRate() * (1 + (calcPercent(invest.getCapital(), invest.getSellingPrice()) / 100d));
//        System.out.println(calcPercent(invest.getCapital(), invest.getSellingPrice()));
//        System.out.println(expected);
        assertEquals(expected, invest.getPlatform().calcSellingExchangeRate(invest,
                invest.getPerformance()), 0.00);
    }

    @Test
    public void testSellingPriceCalculator2() {

        Investment invest = new Investment(
                LocalDate.of(2021, 11, 9),
                COINBASE,
                "Bitcoin",
                5000,
                500,
                1000,
                LocalDate.of(2021, 11, 18)
        );

        assertEquals(
                invest.getExchangeRate() * ((calcPercent(invest.getCapital(), invest.getSellingPrice()) / 100d)),
                invest.getPlatform().calcSellingExchangeRate(invest, invest.getPerformance()),
                0.0
        );
    }
}
