package gauss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

// An ordered collection of equations.
// Newly created systems are empty.
public class EquationSystem implements Iterable<Equation> {
    private ArrayList<Equation> equations = new ArrayList<>();

    // Number of equations in the system.
    public int size() { return equations.size(); }

    // Retrieves the i-th equation.
    public Equation get(int i) { return equations.get(i); }

    // Appends an equation to the end of the system.
    public void add(Equation eq) { equations.add(eq); }

    // Inserts an equation at given index.
    public void insert(int i, Equation eq) { equations.add(i, eq); }

    // Removes the i-th equation.
    public void remove(int i) { equations.remove(i); }

    public Stream<Equation> stream() {
        return equations.stream();
    }

    @Override
    public Iterator<Equation> iterator() {
        return equations.iterator();
    }

    // Solves the equation system and returns map of variables in the solution.
    public Solution<Map<String, Rational>> solve() {
        var variableSet = new HashSet<String>();
        for (var eq : equations) {
            for (var x : eq) {
                variableSet.add(x.getName());
            }
        }
        var variables = new ArrayList<String>(variableSet);
        var a = new Matrix(size(), variables.size(), (i, j) -> {
            return get(i).get(variables.get(j));
        });
        var b = new Matrix(size(), 1, (i, j) -> {
            return get(i).getConst().neg();
        });
        var x = a.solve(b);
        return x.map(mat -> {
            var map = new HashMap<String, Rational>();
            for (int i = 0; i < variables.size(); ++i) {
                map.put(variables.get(i), mat.get(i, 0));
            }
            return map;
        });
    }
}
