package gauss;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RationalTest {
    @Test
    public void testAbs() {
        assertEquals(new Rational(3, 5), new Rational(-3, 5).abs());
    }

    @Test
    public void testAdd() {
        assertEquals(
            new Rational(5, 6),
            new Rational(3, 4).add(new Rational(1, 12)),
            "3/4 + 1/12 == 5/6");
    }

    @Test
    public void testSub() {
        assertEquals(
            new Rational(-1, 3),
            new Rational(1, 5).sub(new Rational(8, 15)),
            "1/5 - 8/15 == -1/3");
    }

    @Test
    public void testNeg() {
        assertEquals(new Rational(1, 3), new Rational(-1, 3).neg());
    }

    @Test
    public void testMul() {
        assertEquals(
            new Rational(3, 10),
            new Rational(2, 5).mul(new Rational(3, 4)),
            "2/5 * 3/4 == 3/10");
    }

    @Test
    public void testDiv() {
        assertEquals(
            new Rational(-5, 6),
            new Rational(2, 3).div(new Rational(4, -5)),
            "2/3 div 4/-5 == -5/6");
    }

    @Test
    public void testInv() {
        assertEquals(new Rational(-4, 3), new Rational(-3, 4).inv());
    }

    @Test
    public void testEquals() {
        assertFalse(new Rational(1, 5).equals(new Rational(1, 4)));
    }

    @Test
    public void testCompareLT() {
        assertTrue(new Rational(-5, 6).compareTo(new Rational(-5, 7)) < 0,
            "-5/6 < -5/7");
    }

    @Test
    public void testCompareEQ() {
        assertTrue(new Rational(2, 3).compareTo(new Rational(2, 3)) == 0,
            "2/3 == 2/3");
    }

    @Test
    public void testCompareGT() {
        assertTrue(new Rational(1, 3).compareTo(new Rational(-1, 5)) > 0,
            "1/3 > -1/5");
    }

    @Test
    public void testToStringZero() {
        assertEquals("0", new Rational(0).toString());
    }

    @Test
    public void testToStringInteger() {
        assertEquals("-42", new Rational(-42).toString());
    }

    @Test
    public void testToStringPositiveDecimal() {
        assertEquals("10.023", new Rational(10023, 1000).toString());
    }

    @Test
    public void testToStringNegativeDecimal() {
        assertEquals("-0.0013", new Rational(-13, 10000).toString());
    }

    @Test
    public void testToStringFraction() {
        assertEquals("-12/13", new Rational(-12, 13).toString());
    }
}
