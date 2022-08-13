package logic;

import logic.platform.AbsolutePlatform;
import org.junit.Test;

import java.time.LocalDate;

import static logic.State.CLOSED;
import static org.junit.Assert.assertEquals;

public class InvestmentTest {
    /**
     *
     */
    //TODO JavaDoc
    private final AbsolutePlatform ONVISTA_BANK = new AbsolutePlatform("Onvista Bank", 7);


    /**
     * Test the methode setSellingPrice and their hole updates
     */
    @Test
    //TODO JavaDoc
    public void testCloseInvestment() {
        LocalDate date = LocalDate.now();
        Investment result = new Investment(date, ONVISTA_BANK, "Microsoft", 250, 250);
        result.closeInvestment(date, 500);

        assertEquals(CLOSED, result.getState());
        assertEquals(236, result.getPerformance(), 0.0);
        assertEquals(94.4, result.getPercentPerformance(), 0.0);
        assertEquals(14, result.getCost(), 0.0);
        assertEquals(date, result.getSellingDate());
    }
}
