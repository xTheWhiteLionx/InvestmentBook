package logic;

import logic.investmentBook.InvestmentBook;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import static logic.HelperClass.*;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for the logic of the program and the general methods. While these test cases
 * cover a lot, they do not cover all possible scenarios. It is still possible
 * for the logic to malfunction, albeit in special cases.
 *
 * @author xthe_white_lionx
 */
public class InvestmentBookTest {

    @Test
    public void testFilterByStatus() {
        InvestmentBook investmentBook = createInvestmentBook();

        assertEquals(Collections.emptyList(),
                investmentBook.filter(State.OPEN,
                        MAX_BLUE,
                        null,
                        null,
                        0));
    }

    @Test
    public void testFilterByPlatform() {
        InvestmentBook investmentBook = createInvestmentBook();

        assertEquals(Collections.emptyList(),
                investmentBook.filter(null,
                        MAX_BLUE,
                        null,
                        null,
                        0));
    }

    @Test
    public void testFilterByMonth() {
        Investment investment2 = new Investment(LocalDate.of(2020, 8, 22),
                ONVISTA_BANK,
                "Deutsche Bank",
                7.10,
                213.06);

        InvestmentBook investmentBook = createInvestmentBook();

        assertEquals(List.of(investment2), investmentBook.filter(null,
                null,
                Month.AUGUST,
                null,
                2020));
    }

    @Test
    public void testFilterByQuarter() {

        Investment investment1 = new Investment(LocalDate.of(2021, 12, 10),
                ONVISTA_BANK,
                "Amazon",
                1755,
                1755);

        InvestmentBook investmentBook = createInvestmentBook();

        assertEquals(List.of(investment1),
                investmentBook.filter(null,
                        null,
                        null,
                        Quarter.Q4,
                        2021));
    }

    @Test
    public void testFilterByYear() {
        Investment investment2 = new Investment(LocalDate.of(2020, 8, 22),
                ONVISTA_BANK,
                "Deutsche Bank",
                7.10,
                213.06);

        InvestmentBook investmentBook = createInvestmentBook();

        assertEquals(List.of(investment2),
                investmentBook.filter(null,
                        null,
                        null,
                        null,
                        2020));
    }
}
