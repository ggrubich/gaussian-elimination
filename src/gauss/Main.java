package gauss;

import java.lang.Void;
import java.util.Map;
import java.util.stream.Stream;

public class Main {
    private static void run(String[] args) {
        var system = new EquationSystem();
        for (var arg : args) {
            system.add(Equation.parse(arg));
        }
        var solution = system.solve();
        solution.accept(new Solution.Visitor<>() {
            public Void visit(Solution.None none) {
                System.out.println("No solutions exist");
                return null;
            }
            public Void visit(Solution.Infinite inf) {
                System.out.println("Infinitely many solutions");
                return null;
            }
            public Void visit(Solution.Unique<Map<String, Rational>> uniq) {
                System.out.println("Unique solution:");
                uniq.get()
                    .entrySet()
                    .stream()
                    .sorted((x, y) -> x.getKey().compareTo(y.getKey()))
                    .forEach(x -> System.out.println(x.getKey() + " = " + x.getValue()));
                return null;
            }
        });
    }

    public static void main(String[] args) {
        try {
            run(args);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
