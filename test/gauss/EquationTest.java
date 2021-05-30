package gauss;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class EquationTest {
    @Test
    public void testGetSet() {
        var eq = new Equation()
            .set("x", new Rational(2))
            .set("y", new Rational(3))
            .setConst(new Rational(-5));
        eq.set("x", new Rational(0));
        eq.set("z", new Rational(1));
        assertEquals(eq.size(), 2, "number of variables");
        assertEquals(new Rational(0), eq.get("x"), "variable x");
        assertEquals(new Rational(3), eq.get("y"), "variable y");
        assertEquals(new Rational(1), eq.get("z"), "variable z");
        assertEquals(new Rational(0), eq.get("a"), "variable a");
        assertEquals(new Rational(-5), eq.getConst(), "const coefficient");
    }

    @Test
    public void testIterate() {
        var eq = new Equation()
            .set("x", new Rational(2))
            .set("y", new Rational(3))
            .set("z", new Rational(1))
            .set("a", new Rational(0));
        var expected = Map.of(
            "x", new Rational(2),
            "y", new Rational(3),
            "z", new Rational(1));
        var actual = new HashMap<String, Rational>();
        for (var x : eq) {
            actual.put(x.getName(), x.getValue());
        }
        assertEquals(expected, actual, "equation contents");
    }

    @Test
    public void testToString() {
        var eq = new Equation()
            .set("x", new Rational(1))
            .set("y", new Rational(-2))
            .set("z", new Rational(3))
            .setConst(new Rational(-4));
        var expected = "x - 2y + 3z - 4 = 0";
        assertEquals(expected, eq.toString());
    }

    private void assertEquationEquals(Equation expected, Equation actual) {
        assertEquals(expected.size(), actual.size(), "number of variables");
        for (var x : actual) {
            var name = x.getName();
            assertEquals(expected.get(name), actual.get(name), "coefficient of " + name);
        }
        assertEquals(expected.getConst(), actual.getConst(), "const coefficient");
    }

    @Test
    public void testParse1() {
        var expected = new Equation()
            .set("x1", new Rational(-3, 4))
            .set("x2", new Rational(-11, 4))
            .set("x3", new Rational(12))
            .setConst(new Rational(-2));
        var actual = Equation.parse(" -3/4x1 - 2.75 x2 + 12*x3 = 2 ");
        assertEquationEquals(expected, actual);
    }

    @Test
    public void testParse2() {
        var expected = new Equation()
            .set("x", new Rational(3, 8))
            .set("y", new Rational(-7))
            .setConst(new Rational(-6));
        var actual = Equation.parse("x -2 - 2.5y  + 1/2 y = +0.625x + 4 + 5 * y");
        assertEquationEquals(expected, actual);
    }

    @Test
    public void testParse3() {
        var expected = new Equation()
            .set("a", new Rational(1));
        var actual = Equation.parse("0*x + 2y = 0.5y + 3/2 y - a");
        assertEquationEquals(expected, actual);
    }

    @Test
    public void testParseToString() {
        var expected = new Equation()
            .set("x_123", new Rational(1))
            .set("y", new Rational(-2, 3))
            .set("z", new Rational(3, 8))
            .setConst(new Rational(-4));
        var actual = Equation.parse(expected.toString());
        assertEquationEquals(expected, actual);
    }

    private void assertParseThrows(String input) {
        assertThrows(Equation.ParseException.class, () -> {
            Equation.parse(input);
        }, input);
    }

    @Test
    public void testParseFail() {
        assertParseThrows("0 + + 2y = 3");
        assertParseThrows("*x = 0");
        assertParseThrows("0 = 2* + x");
        assertParseThrows("3x = ");
        assertParseThrows("12. = 0");
        assertParseThrows("+ -1 = 0");
        assertParseThrows("1/x = 2");
        assertParseThrows("y + 2 = 0 &");
    }
}
