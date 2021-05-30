package gauss;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;

// Linear equation represented as
//   a1*x1 + a2*x2 + ... + a_n*x_n + b = 0
// Newly created equations are initialized with zeros.
public class Equation implements Iterable<Equation.Entry> {
    private HashMap<String, Rational> variables = new HashMap<>();
    private Rational constant = Rational.ZERO;

    // Retrieves given variable's coefficient.
    public Rational get(String name) {
        return variables.getOrDefault(name, Rational.ZERO);
    }

    // Sets given variable's coefficient.
    // Returns this to allow method chaining.
    public Equation set(String name, Rational value) {
        if (value.equals(Rational.ZERO)) {
            variables.remove(name);
        }
        else {
            variables.put(name, value);
        }
        return this;
    }

    // Retrieves the constant coefficient.
    public Rational getConst() {
        return constant;
    }

    // Sets the constant coefficient.
    // Returns this to allow method chaining.
    public Equation setConst(Rational value) {
        constant = value;
        return this;
    }

    // Count of non-zero variables.
    public int size() {
        return variables.size();
    }

    // Pair containing variable's name and its coefficient.
    public static class Entry {
        private final String name;
        private final Rational value;

        public Entry(String name, Rational value) {
            this.name = name;
            this.value = value;
        }

        public String getName() { return name; }
        public Rational getValue() { return value; }
    }

    // Stream of variables in the equation.
    Stream<Entry> stream() {
        return variables.entrySet().stream()
            .map(x -> new Entry(x.getKey(), x.getValue()));
    }

    // Iterator of variables in the equation.
    @Override
    public Iterator<Entry> iterator() {
        return stream().iterator();
    }

    @Override
    public String toString() {
        var buf = new StringBuilder();
        for (var entry : this) {
            var name = entry.getName();
            var val = entry.getValue();
            if (val.compareTo(Rational.ZERO) < 0) {
                buf.append("- ");
            }
            else if (buf.length() > 0) {
                buf.append("+ ");
            }
            if (!val.abs().equals(new Rational(1))) {
                buf.append(val.abs().toString());
            }
            buf.append(name);
            buf.append(" ");
        }
        if (constant.compareTo(Rational.ZERO) < 0) {
            buf.append("- ");
        }
        else if (buf.length() > 0) {
            buf.append("+ ");
        }
        buf.append(constant.abs().toString());
        buf.append(" = 0");
        return buf.toString();
    }

    public static class ParseException extends RuntimeException {
        public ParseException(String msg) {
            super(msg);
        }
    }

    private static class Parser {
        private final String input;
        private int cursor;

        public Parser(String input_) {
            input = input_;
            cursor = 0;
        }

        private boolean eof() {
            return cursor >= input.length();
        }

        private char peek() {
            if (eof()) {
                return 0;
            }
            return input.charAt(cursor);
        }

        private void next() {
            if (!eof()) {
                ++cursor;
            }
        }

        private void skipSpaces() {
            while (Character.isWhitespace(peek())) {
                next();
            }
        }

        private void fail(Function<String, String> msgFun) {
            throw new ParseException(msgFun.apply(eof() ? "EOF" : "`" + peek() + "`"));
        }

        private BigInteger parseNatural() {
            var out = BigInteger.ZERO;
            if (!Character.isDigit(peek())) {
                fail(c -> "Unexpected " + c + " at the start of a natural number, expecting a digit");
            }
            do {
                out = out.multiply(BigInteger.TEN)
                    .add(BigInteger.valueOf(Character.digit(peek(), 10)));
                next();
            } while (Character.isDigit(peek()));
            return out;
        }

        private Rational parseRational() {
            var a = parseNatural();
            if (peek() == '.') {
                next();
                int start = cursor;
                var b = parseNatural();
                int len = cursor - start;
                return new Rational(a).add(new Rational(b, BigInteger.TEN.pow(len)));
            }
            skipSpaces();
            if (peek() == '/') {
                next();
                skipSpaces();
                var b = parseNatural();
                return new Rational(a, b);
            }
            return new Rational(a);
        }

        private String parseName() {
            var buf = new StringBuilder();
            if (!Character.isAlphabetic(peek())) {
                fail(c -> "Unexpected " + c + " at the start of a name, expecting a letter");
            }
            do {
                buf.append(peek());
                next();
            } while (Character.isAlphabetic(peek()) || Character.isDigit(peek()) || peek() == '_');
            return buf.toString();
        }

        private Equation parseExpr() {
            var result = new Equation();
            int sign = 1;
            skipSpaces();
            if (peek() == '+') {
                next();
            }
            else if (peek() == '-') {
                next();
                sign = -1;
            }
            while (true) {
                skipSpaces();
                // lone variable
                if (Character.isAlphabetic(peek())) {
                    var name = parseName();
                    result.set(name, result.get(name).add(new Rational(sign)));
                }
                else {
                    var number = new Rational(sign).mul(parseRational());
                    skipSpaces();
                    // coefficient followed by a variable
                    if (peek() == '*' || Character.isAlphabetic(peek())) {
                        if (peek() == '*') {
                            next();
                            skipSpaces();
                        }
                        var name = parseName();
                        result.set(name, result.get(name).add(number));
                    }
                    // constant coefficient
                    else {
                        result.setConst(result.getConst().add(number));
                    }
                }
                skipSpaces();
                if (peek() == '+') {
                    next();
                    sign = 1;
                }
                else if (peek() == '-') {
                    next();
                    sign = -1;
                }
                else {
                    break;
                }
            }
            return result;
        }

        public Equation parseEquation() {
            var lhs = parseExpr();
            skipSpaces();
            if (peek() != '=') {
                fail(c -> "Unexpected " + c + " in equation, expecting `=`");
            }
            next();
            var rhs = parseExpr();
            if (!eof()) {
                fail(c -> "Unexpected " + c + " in equation, expecting EOF");
            }
            for (var x : rhs) {
                lhs.set(x.getName(), lhs.get(x.getName()).sub(x.getValue()));
            }
            lhs.setConst(lhs.getConst().sub(rhs.getConst()));
            return lhs;
        }
    }

    // Parses an equation from its string representation.
    public static Equation parse(String input) {
        return new Parser(input).parseEquation();
    }
}
