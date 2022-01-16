package logic;

import logic.platform.AbsolutePlatform;
import logic.platform.MixedPlatform;
import logic.platform.PercentPlatform;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static logic.GeneralMethods.*;
import static logic.enums.Status.CLOSED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for the logic of the program and the general methods. While these test cases
 * cover a lot, they do not cover all possible scenarios. It is still possible
 * for the logic to malfunction, albeit in special cases.
 *
 * @author Fabian Hardt
 */
public class RecInvestmentBookTest {

    //TODO JavaDoc
    private final PercentPlatform COINBASE = new PercentPlatform("Coinbase", 1.49);
    private final AbsolutePlatform ONVISTA_BANK = new AbsolutePlatform("Onvista Bank", 7);
    //0,25% of Order Value (min. 8,90 EUR, max. 58,90 EUR)
    private final MixedPlatform MAX_BLUE = new MixedPlatform("Max Blue", 2.5, 8.90);

    /**
     *
     */
    //TODO JavaDoc
    private final String fileName = "result.json";

    /**
     * @return
     */
    //TODO JavaDoc
    private List<Investment> createShortTestInvestments() {
        Investment investment1 = new Investment(
                LocalDate.of(2021, 12, 26),
                ONVISTA_BANK,
                "Amazon",
                1755,
                1755);
        Investment investment2 = new Investment(
                LocalDate.of(2021, 12, 26),
                COINBASE,
                "Bitcoin",
                27550.20,
                2000);

        return List.of(
                investment1,
                investment2
        );
    }

    //TODO JavaDoc
    private InvestmentBook createTestInvestmentBook() {
        return new InvestmentBook(Set.of(
                COINBASE,
                ONVISTA_BANK,
                MAX_BLUE
        ),
                List.of(
                        //List of the real Investments
                        new Investment(
                                LocalDate.of(2018, 10, 5),
                                ONVISTA_BANK, "Take Two",
                                112.49,
                                449.95,
                                457.62,
                                LocalDate.of(2018, 10, 31)
                        ),

                        new Investment(
                                LocalDate.of(2018, 11, 7),
                                ONVISTA_BANK,
                                "Take Two",
                                108.65,
                                434.60,
                                416.18,
                                LocalDate.of(2018, 11, 8)
                        ),

                        new Investment(
                                LocalDate.of(2018, 11, 12),
                                ONVISTA_BANK,
                                "Take Two",
                                100.4,
                                401.58,
                                399.36,
                                LocalDate.of(2019, 6, 24)
                        ),

                        new Investment(
                                LocalDate.of(2019, 1, 3),
                                ONVISTA_BANK,
                                "Take Two",
                                88.73,
                                88.73,
                                91.19,
                                LocalDate.of(2019, 1, 8)
                        ),

                        new Investment(
                                LocalDate.of(2019, 7, 2),
                                ONVISTA_BANK,
                                "Amazon",
                                1755,
                                1755,
                                1633,
                                LocalDate.of(2019, 8, 2)
                        ),

                        new Investment(
                                LocalDate.of(2020, 1, 9),
                                ONVISTA_BANK,
                                "Deutsche Bank",
                                7.10,
                                213.06,
                                228.27,
                                LocalDate.of(2020, 1, 13)
                        ),

                        new Investment(
                                LocalDate.of(2019, 7, 22),
                                ONVISTA_BANK,
                                "Tesla",
                                229.50,
                                229.50,
                                680.70,
                                LocalDate.of(2020, 2, 13)
                        ),

                        new Investment(
                                LocalDate.of(2018, 9, 4),
                                MAX_BLUE,
                                "Ubisoft",
                                96.88,
                                290.65
                        ),

                        new Investment(
                                LocalDate.of(2019, 8, 6),
                                COINBASE,
                                "Bitcoin",
                                11003.76,
                                2005.64,
                                2100.30,
                                LocalDate.of(2020, 10, 21)
                        ),

                        new Investment(
                                LocalDate.of(2019, 8, 6),
                                ONVISTA_BANK,
                                "Amazon",
                                1592.40,
                                1592.40,
                                1616.20,
                                LocalDate.of(2019, 8, 9)
                        ),

                        new Investment(
                                LocalDate.of(2019, 9, 2),
                                ONVISTA_BANK,
                                "Alibaba",
                                162,
                                486,
                                492.60,
                                LocalDate.of(2020, 3, 12)
                        ),

                        new Investment(
                                LocalDate.of(2021, 7, 15),
                                COINBASE,
                                "Bitcoin",
                                27185.88,
                                1998.04,
                                4146.76,
                                LocalDate.of(2021, 11, 8)
                        ),

                        new Investment(
                                LocalDate.of(2021, 11, 9),
                                COINBASE,
                                "Bitcoin",
                                58644.49,
                                3050,
                                2560.92,
                                LocalDate.of(2021, 11, 18)
                        ),

                        new Investment(
                                LocalDate.of(2021, 11, 24),
                                COINBASE,
                                "Shiba Inu",
                                0.00003418,
                                2522.76,
                                3165.94,
                                LocalDate.of(2021, 11, 30)
                        ),

                        new Investment(
                                LocalDate.of(2021, 12, 4),
                                COINBASE,
                                "Shiba Inu",
                                0.00003198,
                                2939
                        )
                )
        );
    }

    /**
     * If this test fails, all other tests can be ignored - they might just fail
     * because they require one of the two following tested methods.
     */
    @Test
    public void testToStringShort() {
        InvestmentBookView programm = new InvestmentBookView(new HashSet<>(), createShortTestInvestments()
                , new FakeGUI());
        assertEquals("""
                |stockName           |exchangeRate        |capital             |sellingPrice        |absolutePerformance |percentPerformance  |
                |--------------------|--------------------|--------------------|--------------------|--------------------|--------------------|
                |Amazon              |1755,00 EUR         |1755,00 EUR         |0,00 EUR            |0,00 EUR            |0,00 %              |
                |Bitcoin             |27550,20 EUR        |2000,00 EUR         |0,00 EUR            |0,00 EUR            |0,00 %              |
                """, programm.toStringShort());
    }

    /**
     * If this test fails, all other tests can be ignored - they might just fail
     * because they require one of the two tested methods.
     */
    @Test
    public void testToString() {

        InvestmentBookView programm = new InvestmentBookView(new HashSet<>(), createShortTestInvestments()
                , new FakeGUI());
        assertEquals("""
                |creationDate        |status              |platform            |stockName           |exchangeRate        |capital             |sellingPrice        |absolutePerformance |percentPerformance  |cost                |
                |--------------------|--------------------|--------------------|--------------------|--------------------|--------------------|--------------------|--------------------|--------------------|--------------------|
                |2021-12-26          |OPEN                |Onvista Bank        |Amazon              |1755,00 EUR         |1755,00 EUR         |0,00 EUR            |0,00 EUR            |0,00 %              |7,00 EUR            |
                |2021-12-26          |OPEN                |Coinbase            |Bitcoin             |27550,20 EUR        |2000,00 EUR         |0,00 EUR            |0,00 EUR            |0,00 %              |29,80 EUR           |
                """, programm.toString());
    }

    /**
     * Test the methode setSellingPrice and their hole updates
     */
    @Test
    //TODO JavaDoc
    public void testCloseInvestment() {
        LocalDate date = LocalDate.now();
        Investment result = new Investment(date, ONVISTA_BANK, "Microsoft", 250, 250);
        result.closeInvestment(date, 500);

        assertEquals(CLOSED, result.getStatus());
        assertEquals(236, result.getPerformance(), 0.0);
        assertEquals(94.4, result.getPercentPerformance(), 0.0);
        assertEquals(14, result.getCost(), 0.0);
        assertEquals(date, result.getSellingDate());
    }

    //TODO JavaDoc
    private boolean filesAreEqual(String fileName1, String fileName2) throws IOException {

        Path f1 = Paths.get(fileName1);
        Path f2 = Paths.get(fileName2);

        long size = Files.size(f1);
        if (size != Files.size(f2)) {
            return false;
        }

        return Arrays.equals(Files.readAllBytes(f1), Files.readAllBytes(f2));
    }

    /**
     * Tests the methode writeInvestsToJson and creates a Json file
     */
    //TODO correct
    //TODO JavaDoc and rename
    @Test
    public void testWriteInvestsToJson() throws IOException {
        String fileName = "Investments.json";

        InvestmentBook investmentBook = createTestInvestmentBook();
        writeInvestmentBookToJson(createTestInvestmentBook(), fileName);
        //TODO how to compare Sets?
        assertTrue(filesAreEqual("books/expected.json", fileName));
    }

    /**
     * Tests the methode writeInvestmentToJson and readInvestmentsJson
     */
    @Test
    //TODO JavaDoc and rename
    public void testWriteInvestsToJsonAndReadInvestsJson() {

        InvestmentBook expected = createTestInvestmentBook();
        writeInvestmentBookToJson(expected, fileName);

        assertEquals(expected, readInvestmentBookFromJson("result.json"));
    }

    /**
     * Tests the methode readGson() and reads a Json file
     */
    //TODO JavaDoc
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
        System.out.println(calcPercent(invest.getCapital(), invest.getSellingPrice()));
        System.out.println(expected);
        assertEquals(expected, invest.getPlatform().calcSellingExchangeRate(invest, invest.getPerformance()), 0.00);
    }

    /**
     * Tests the methode readGson() and reads a Json file
     */
    //TODO JavaDoc
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

        double expected = 408.45;
        assertEquals(expected, invest.getPlatform().calcSellingExchangeRate(invest, invest.getPerformance()), 0.000001);
    }

    /**
     * Tests the methode calcPercent for whole numbers
     */
    @Test
    //TODO necessary?
    public void testCalcPercentForWholeNumbers() {

        double actual = calcPercentRounded(100, 10);

        double expected = 10;

        assertEquals(expected, actual, 0.0);
    }

    /**
     * Tests the methode calcPercent for decimal numbers
     */
    @Test
    //TODO JavaDoc
    public void testCalcPercentForDecimalNumbers() {
        assertEquals(10, calcPercentRounded(2560.92, 256.092), 0.0);
    }

    /**
     */
    //TODO JavaDoc
    @Test
    public void testWriteAndReadDate() {
        Investment expected = new Investment(
                LocalDate.of(2019, 7, 2),
                ONVISTA_BANK,
                "Amazon",
                1755,
                1755,
                1633,
                LocalDate.of(2019, 8, 2)
        );

        writeInvestmentBookToJson(new InvestmentBook(
                Set.of(
                        new AbsolutePlatform("Onvista Bank", 7)
                ),
                List.of(expected)
        ), "books/expectedDate.json");
        Investment result = readInvestmentBookFromJson("expectedDate.json")
                .getInvestmentList().get(0);

        assertEquals(expected.getCreationDate(), result.getCreationDate());
        assertEquals(expected.getSellingDate(), result.getSellingDate());

    }

    //TODO correct
//    /**
//     *
//     */
//    //TODO JavaDoc
//    @Test
//    public void testFilterByMonth() {
//
//        Investment invest1 = new Investment(ONVISTA_BANK,
//                "Amazon",
//                1755,
//                1755, 1633);
//        invest1.setCreationDate(LocalDate.of(2021, 12, 10));
//
//        Investment invest2 = new Investment(ONVISTA_BANK,
//                "Deutsche Bank",
//                7.10,
//                213.06, 228.27);
//        invest2.setCreationDate(LocalDate.of(2021, 8, 22));
//        InvestmentBook investmentBook = new InvestmentBook(List.of(invest1, invest2), new FakeGUI());
//
//        assertEquals(List.of(invest2), investmentBook.filterByMonthAndYear(List.of(invest1, invest2), Month.AUGUST, 2021));
//    }
//
//    /**
//     *
//     */
//    //TODO JavaDoc
//    @Test
//    public void testFilterByQuarter() {
//
//        Investment invest1 = new Investment(ONVISTA_BANK,
//                "Amazon",
//                1755,
//                1755, 1633);
//        invest1.setCreationDate(LocalDate.of(2021, 12, 10));
//
//        Investment invest2 = new Investment(ONVISTA_BANK,
//                "Deutsche Bank",
//                7.10,
//                213.06, 228.27);
//        invest2.setCreationDate(LocalDate.of(2021, 8, 22));
//
//        assertEquals(List.of(invest1), createTestProgram(List.of(invest1, invest2)).filterByQuarterAndYear(List.of(invest1, invest2), Quarter.Q4, 2021));
//    }
//
//    /**
//     *
//     */
//    //TODO JavaDoc
//    @Test
//    public void testFilterByYear() {
//
//        Investment invest1 = new Investment(ONVISTA_BANK,
//                "Amazon",
//                1755,
//                1755, 1633);
//        invest1.setCreationDate(LocalDate.of(2021, 12, 10));
//
//        Investment invest2 = new Investment(ONVISTA_BANK,
//                "Deutsche Bank",
//                7.10,
//                213.06, 228.27);
//        invest2.setCreationDate(LocalDate.of(2012, 8, 22));
//
//        assertEquals(List.of(invest2), createTestProgram(List.of(invest1, invest2)).filterByYear(List.of(invest1, invest2), 2012));
//    }
//
//    /**
//     *
//     */
//    //TODO JavaDoc
//    @Test
//    public void testFilterByPlatform() {
//
//        Investment invest1 = new Investment(COINBASE,
//                "invest1",
//                1755,
//                1755, 1633);
//        invest1.setCreationDate(LocalDate.of(2021, 12, 10));
//
//        Investment invest2 = new Investment(COINBASE,
//                "invest2",
//                7.10,
//                213.06, 228.27);
//        invest2.setCreationDate(LocalDate.of(2012, 8, 22));
//
//        Investment invest3 = new Investment(COINBASE,
//                "invest3",
//                1755,
//                1755, 1633);
//        invest1.setCreationDate(LocalDate.of(2021, 12, 10));
//
//        Investment invest4 = new Investment(MAX_BLUE,
//                "invest4",
//                7.10,
//                213.06, 228.27);
//        invest2.setCreationDate(LocalDate.of(2012, 8, 22));
//
//        assertEquals(List.of(invest1, invest2, invest3), createTestProgram(List.of(invest1, invest2, invest3, invest4)).filterByPlatform(List.of(invest1, invest2, invest3, invest4), COINBASE));
//    }
}
