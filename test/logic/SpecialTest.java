package logic;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static logic.GeneralMethods.calcSpaces;
import static logic.GeneralMethods.readInvestmentBookFromJson;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for the logic of the program and the general methods. These test cases
 * cover special cases like Exceptions.
 *
 * @author Fabian Hardt
 */
//TODO JavaDoc
public class SpecialTest {

    /**
     * Tests the exception to the calcSpaces() methode
     * for zero
     */
    //TODO JavaDoc
    @Test(expected = IllegalArgumentException.class)
    public void testCalcSpacesZero() {
        calcSpaces(0);
    }

    /**
     * Tests the exception to the calcSpaces() methode
     * for negative numbers
     */
    //TODO JavaDoc
    @Test(expected = IllegalArgumentException.class)
    public void testCalcSpacesNegativeNumbers() {
        calcSpaces(-10);
    }

    /**
     * Tests the methode readInvestmentsJson(File)
     */
    //TODO JavaDoc
    @Test(expected = FileNotFoundException.class)
    public void testReadInvestsJsonFileDoesNotExist() {
        File testFile = new File("test.json");

        readInvestmentBookFromJson(testFile);
    }

    /**
     * Tests the methode readInvestmentsJson(String)
     */
    //TODO JavaDoc
    @Test(expected = FileNotFoundException.class)
    public void testFileReaderIsNotReady() {

        readInvestmentBookFromJson("test.json");
    }

    /**
     * Tests the methode readInvestmentsJson(String)
     */
    //TODO JavaDoc
    @Test(expected = IllegalArgumentException.class)
    public void testIsNotAJsonFile() {

        readInvestmentBookFromJson("test.txt");
    }

    private int fakultaet(int n) {
        if (n < 3) {
            return n > 0 ? n : 1;
        } else {
            int result = 1;
            for (int i = 1; i <= n; i++) {
                result*=i;
            }
            return result;
        }
    }

    @Test
    public void testFakultaet(){
        assertEquals(1, fakultaet(0));
        assertEquals(1, fakultaet(1));
        assertEquals(2, fakultaet(2));
        assertEquals(6, fakultaet(3));
        assertEquals(24, fakultaet(4));
    }

    private int fibonacci(int n) {
        if (n < 3) {
            return n > 0 ? 1 : 0;
        } else {
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }

    @Test
    public void testFibonacci(){
        assertEquals(0, fibonacci(0));
        assertEquals(1, fibonacci(1));
        assertEquals(1, fibonacci(2));
        assertEquals(2, fibonacci(3));
        assertEquals(3, fibonacci(4));
        assertEquals(5, fibonacci(5));
        assertEquals(8, fibonacci(6));
    }
}
