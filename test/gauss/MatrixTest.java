package gauss;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MatrixTest {
    private void assertMatrixEquals(Matrix expected, Matrix actual) {
        assertEquals(expected.height(), actual.height(), "matrix heights match");
        assertEquals(expected.width(), actual.width(), "matrix widths match");
        for (int i = 0; i < expected.height(); ++i) {
            for (int j = 0; j < expected.width(); ++j) {
                assertEquals(expected.get(i, j), actual.get(i, j),
                    "elements at (" + i + ", " + j + ") match");
            }
        }
    }

    @Test
    public void testSolveUnique() {
        var a = new Matrix(3, 3,
            new Rational(1), new Rational(3), new Rational(2),
            new Rational(2), new Rational(1, 2), new Rational(3),
            new Rational(5), new Rational(6), new Rational(1, 3));
        var x1 = new Matrix(3, 2,
            new Rational(1), new Rational(0),
            new Rational(2), new Rational(1, 2),
            new Rational(3), new Rational(4));
        var y = new Matrix(3, 2,
            new Rational(13), new Rational(19, 2),
            new Rational(12), new Rational(49, 4),
            new Rational(18), new Rational(13, 3));
        var x2 = a.solve(y);
        assertTrue(x2.isUnique(), "unique solution exists");
        assertMatrixEquals(x1, x2.asUnique().get());
    }

    @Test
    public void testSolveNone() {
        var a = new Matrix(3, 3,
            new Rational(1), new Rational(3), new Rational(2),
            new Rational(2), new Rational(1, 2), new Rational(3),
            new Rational(-8), new Rational(-2), new Rational(-12));
        var y = new Matrix(3, 1,
            new Rational(13),
            new Rational(12),
            new Rational(18));
        var x = a.solve(y);
        assertTrue(x.isNone(), "solution doesn't exist");
    }

    @Test
    public void testSolveInfinite1() {
        var a = new Matrix(3, 3,
            new Rational(1), new Rational(3), new Rational(2),
            new Rational(2), new Rational(1, 2), new Rational(3),
            new Rational(-8), new Rational(-2), new Rational(-12));
        var y = new Matrix(3, 1,
            new Rational(13),
            new Rational(12),
            new Rational(-48));
        var x = a.solve(y);
        assertTrue(x.isInfinite(), "solution is infinite");
    }

    @Test
    public void testSolveInfinite2() {
        var a = new Matrix(2, 3,
            new Rational(1), new Rational(3), new Rational(2),
            new Rational(2), new Rational(1, 2), new Rational(3));
        var y = new Matrix(2, 1,
            new Rational(13),
            new Rational(12));
        var x = a.solve(y);
        assertTrue(x.isInfinite(), "solution is infinite");
    }
}
