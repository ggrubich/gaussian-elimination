package gauss;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class EquationSystemTest {
    @Test
    public void testSolveUnique() {
        var system = new EquationSystem();
        system.add(Equation.parse("-2*x1 + 3*x2 + x3 = -1"));
        system.add(Equation.parse("-4*x1 + 5*x2 + 4*x3 = -7"));
        system.add(Equation.parse("4*x1 - 9*x2 + 2*x3 = -9"));
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
        system.add(Equation.parse("2x + 3y + 4 = 0"));
        system.add(Equation.parse("2x + 3y + 2 = 0"));
        var sol = system.solve();
        assertTrue(sol.isNone(), "no solution exists");
    }

    @Test
    public void testSolveInfinite() {
        var system = new EquationSystem();
        system.add(Equation.parse("x + 3y + 4z = -1"));
        system.add(Equation.parse("-2x + 2y + z = 3"));
        var sol = system.solve();
        assertTrue(sol.isInfinite(), "infinitely many solutions exist");
    }
}
