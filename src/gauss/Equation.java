package gauss;

import java.util.HashMap;
import java.util.Iterator;
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
}
