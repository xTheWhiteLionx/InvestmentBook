package logic;

import logic.platform.AbsolutePlatform;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static logic.State.CLOSED;
import static org.junit.Assert.assertEquals;

public class InvestmentTest {
    /**
     *
     */
    //TODO JavaDoc
    private final AbsolutePlatform ONVISTA_BANK = new AbsolutePlatform(id, "Onvista Bank", 7);


    /**
     * Test the methode setSellingPrice and their hole updates
     */
    @Test
    //TODO JavaDoc
    public void testCloseInvestment() {
        LocalDate date = LocalDate.now();
        Investment result = new Investment(date, ONVISTA_BANK, "Microsoft", 250, 250);
        result.closeInvestment(date, BigDecimal.valueOf(500));

        assertEquals(CLOSED, result.getState());
        assertEquals(BigDecimal.valueOf(236.0).setScale(2), result.getPerformance());
        assertEquals(BigDecimal.valueOf(94.40).setScale(2), result.getPercentPerformance());
        assertEquals(BigDecimal.valueOf(14.0).setScale(2), result.getCost());
        assertEquals(date, result.getSellingDate());
    }
}
