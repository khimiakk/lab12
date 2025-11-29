import ucu.task1.ATM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ATMTest {

    private ATM atm;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setUp() {
        atm = new ATM();

        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    private String output() {
        return out.toString().trim();
    }

    @Test
    void test1() {
        atm.giveMoney(870);

        String text = output();
        assertTrue(text.contains("Denomination: 500"));
        assertTrue(text.contains("Denomination: 200"));
        assertTrue(text.contains("Denomination: 100"));
        assertTrue(text.contains("Denomination: 50"));
        assertTrue(text.contains("Denomination: 20"));
    }

    @Test
    void test2() {
        atm.giveMoney(-10);

        assertEquals("Requested amount must be positive.", output());
    }

    @Test
    void test3() {
        atm.giveMoney(125);

        assertEquals(
                "This ATM can only dispense multiples of 10.",
                output()
        );
    }

    @Test
    void test4() {
        atm.giveMoney(9999999);

        String text = output();
        assertTrue(text.contains("This ATM can only dispense multiples of 10."));
    }

    @Test
    void test5() {
        atm.giveMoney(1000000);

        String text = output();
        assertTrue(text.contains("ATM doesn't have enough money."));
    }

}
