package logic;

import logic.investmentBook.InvestmentBookData;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;

import static logic.HelperClass.*;
import static logic.investmentBook.InvestmentBookData.fromJson;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for the logic of the program and the general methods. These test cases
 * cover special cases like Exceptions.
 *
 * @author Fabian Hardt
 */
//TODO JavaDoc
public class InvestmentBookDataTest {

    private final File file = new File("test/resources/result.json");

    /**
     * @return
     */
    //TODO JavaDoc
    private InvestmentBookData createTestInvestmentBookJson() {
        return new InvestmentBookData(createPlatforms(),
                Arrays.asList(
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
                                290.65,
                                140.10,
                                LocalDate.of(2022,4,9)
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
     * Tests the methode readInvestmentsJson(File)
     */
    //TODO JavaDoc
    @Test(expected = FileNotFoundException.class)
    public void testReadInvestsJsonFileDoesNotExist() throws IOException {
        File testFile = new File("test.json");
        fromJson(testFile);
    }

    /**
     * Tests the methode readInvestmentsJson(String)
     */
    //TODO JavaDoc
    @Test(expected = IllegalArgumentException.class)
    public void testIsNotAJsonFile() throws IOException {
        File testFile = new File("test/resources/test.txt");
        fromJson(testFile);
    }

//    /**
//     * Tests the methode writeInvestsToJson and creates a Json file
//     */
//    //TODO correct
//    //TODO JavaDoc and rename
//    @Test
//    public void testWriteInvestsToJson() throws IOException {
//        File file = new File(DIRECTORY + "Investments.json");
//
//        InvestmentBookData investmentBookData = createTestInvestmentBookJson();
//        investmentBookData.toJson(file);
//        //TODO how to compare Sets?
//        assertTrue(filesAreEqual("src/main/resources/books/expected.json",
//                DIRECTORY + file.getName()));
//    }

    /**
     * Tests the methode writeInvestmentToJson and readInvestmentsJson
     */
    @Test
    //TODO JavaDoc and rename
    public void testWriteInvestsToJsonAndReadInvestsJson() throws IOException {

        InvestmentBookData expected = createTestInvestmentBookJson();
        expected.toJson(file);

        assertEquals(expected, fromJson(file));
    }
}
