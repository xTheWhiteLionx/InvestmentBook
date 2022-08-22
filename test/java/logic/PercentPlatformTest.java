package logic;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

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

        BigDecimal expected = BigDecimal.valueOf(103800.75).setScale(2,RoundingMode.HALF_UP);
//        System.out.println(calcPercent(invest.getCapital(), invest.getSellingPrice()));
//        System.out.println(expected);
        System.out.println("invest = " + invest.getPerformance());
        assertEquals(expected, invest.getPlatform().calcSellingExchangeRate(invest,
                invest.getPerformance()));
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

        BigDecimal expected = BigDecimal.valueOf(10000.0).setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, invest.getPlatform().calcSellingExchangeRate(invest, invest.getPerformance()));
    }
}
