package logic;

import logic.platform.MixedPlatform;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class MixedPlatformTest {
    /**
     *
     */
    //TODO JavaDoc
    //0,25% of Order Value (min. 8,90 EUR, max. 58,90 EUR)
    private final MixedPlatform MAX_BLUE = new MixedPlatform("Max Blue", 2.5, 8.90);

    /**
     * Tests the methode readGson() and reads a Json file
     */
    //TODO JavaDoc
    @Test
    public void testSellingPriceCalculatorReal() {
        Investment invest = new Investment(
                LocalDate.of(2018, 9, 4),
                MAX_BLUE,
                "Ubisoft",
                290.65,
                290.65, 408.45, LocalDate.now());

        BigDecimal expected = BigDecimal.valueOf(408.45).setScale(2, RoundingMode.HALF_UP);
        assertEquals(expected, invest.getPlatform().calcSellingExchangeRate(invest, invest.getPerformance()));
    }
}
