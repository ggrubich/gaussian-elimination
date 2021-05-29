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
}
