package gauss;

import java.math.BigInteger;
import java.util.Optional;

// Immutable unbounded rational numbers.
// Rationals are stored as irreducable fractions with a positive denominator.
public class Rational implements Comparable<Rational> {
    private final BigInteger num;
    private final BigInteger den;

    public BigInteger numerator() { return num; }

    public BigInteger denominator() { return den; }

    public Rational(BigInteger p, BigInteger q) {
        if (q.signum() == 0) {
            throw new IllegalArgumentException("Zero denominator");
        }
        if (q.signum() < 0) {
            p = p.negate();
            q = q.negate();
        }
        var d = p.gcd(q);
        p = p.divide(d);
        q = q.divide(d);
        num = p;
        den = q;
    }

    public Rational(long p, long q) {
        this(BigInteger.valueOf(p), BigInteger.valueOf(q));
    }

    public Rational(long n) {
        this(n, 1);
    }

    public static final Rational ZERO = new Rational(0);
    public static final Rational ONE = new Rational(1);

    // Absolute value of a number.
    public Rational abs() {
        return new Rational(num.abs(), den);
    }

    // Adds two numbers.
    public Rational add(Rational x) {
        var q = den.multiply(x.den);
        var p = num.multiply(x.den).add(x.num.multiply(den));
        return new Rational(p, q);
    }

    // Negates the number.
    public Rational neg() {
        return new Rational(num.negate(), den);
    }

    // Subtracts two numbers.
    public Rational sub(Rational x) {
        return add(x.neg());
    }

    // Multiplies two numbers.
    public Rational mul(Rational x) {
        var p = num.multiply(x.num);
        var q = den.multiply(x.den);
        return new Rational(p, q);
    }

    // Inverts the number.
    public Rational inv() {
        return new Rational(den, num);
    }

    // Divides two numbers.
    public Rational div(Rational x) {
        return mul(x.inv());
    }

    @Override
    public int compareTo(Rational x) {
        return sub(x).num.signum();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        var x = (Rational)other;
        return num.equals(x.num) && den.equals(x.den);
    }

    private static final BigInteger BIG_TWO = BigInteger.valueOf(2);
    private static final BigInteger BIG_FIVE = BigInteger.valueOf(5);

    private Optional<String> toDecimal() {
        int twos = 0;
        int fives = 0;
        var q = den;
        while (q.mod(BIG_TWO).equals(BigInteger.ZERO)) {
            q = q.divide(BIG_TWO);
            twos += 1;
        }
        while (q.mod(BIG_FIVE).equals(BigInteger.ZERO)) {
            q = q.divide(BIG_FIVE);
            fives += 1;
        }
        if (!q.equals(BigInteger.ONE)) {
            return Optional.empty();
        }

        var p = num;
        if (twos < fives) {
            p = p.multiply(BIG_TWO.pow(fives - twos));
        }
        else if (fives < twos) {
            p = p.multiply(BIG_FIVE.pow(twos - fives));
        }
        p = p.abs();
        int tens = Integer.max(twos, fives);

        var buf = new StringBuilder(p.toString());
        if (buf.length() < tens+1) {
            var padding = new StringBuilder();
            while (buf.length() + padding.length() < tens + 1) {
                padding.append('0');
            }
            buf.insert(0, padding.toString());
        }
        if (tens > 0) {
            buf.insert(buf.length() - tens, '.');
        }
        if (num.signum() < 0) {
            buf.insert(0, '-');
        }
        return Optional.of(buf.toString());
    }

    @Override
    public String toString() {
        var dec = toDecimal();
        if (dec.isPresent()) {
            return dec.get();
        }
        else {
            var buf = new StringBuilder();
            buf.append(num.toString());
            buf.append("/");
            buf.append(den.toString());
            return buf.toString();
       }
    }
}
