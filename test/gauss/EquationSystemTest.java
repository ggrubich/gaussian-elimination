package gauss;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class EquationSystemTest {
    @Test
    public void testSolveUnique() {
        var system = new EquationSystem();
        system.add(new Equation()
            .set("x1", new Rational(-2))
            .set("x2", new Rational(3))
            .set("x3", new Rational(1))
            .setConst(new Rational(1)));
        system.add(new Equation()
            .set("x1", new Rational(-4))
            .set("x2", new Rational(5))
            .set("x3", new Rational(4))
            .setConst(new Rational(7)));
        system.add(new Equation()
            .set("x1", new Rational(4))
            .set("x2", new Rational(-9))
            .set("x3", new Rational(2))
            .setConst(new Rational(9)));
        var expected = Map.of(
            "x1", new Rational(1),
            "x2", new Rational(1),
            "x3", new Rational(-2));
        var sol = system.solve();
        assertTrue(sol.isUnique(), "unique solution exists");
        assertEquals(expected, sol.asUnique().get(), "solution matches");
    }

    @Test
    public void testSolveNone() {
        var system = new EquationSystem();
        system.add(new Equation()
            .set("x", new Rational(2))
            .set("y", new Rational(3))
            .setConst(new Rational(4)));
        system.add(new Equation()
            .set("x", new Rational(2))
            .set("y", new Rational(3))
            .setConst(new Rational(2)));
        var sol = system.solve();
        assertTrue(sol.isNone(), "no solution exists");
    }

    @Test
    public void testSolveInfinite() {
        var system = new EquationSystem();
        system.add(new Equation()
            .set("x", new Rational(1))
            .set("y", new Rational(3))
            .set("z", new Rational(4))
            .setConst(new Rational(1)));
        system.add(new Equation()
            .set("x", new Rational(-2))
            .set("y", new Rational(2))
            .set("z", new Rational(1))
            .setConst(new Rational(-3)));
        var sol = system.solve();
        assertTrue(sol.isInfinite(), "infinitely many solutions exist");
    }
}
