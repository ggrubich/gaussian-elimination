package gauss;

// Solution of a matrix equation. There are three types of solutions:
//  - none - equation has no solutions
//  - infinite - equation has infinitely many solutions
//  - unique - equation has only one solution
public abstract class Solution<T> {
    private Solution() {}

    public static class None<T> extends Solution<T> {
        @Override
        public <R> R accept(Visitor<T, R> v) {
            return v.visit(this);
        }
    }

    public static class Infinite<T> extends Solution<T> {
        @Override
        public <R> R accept(Visitor<T, R> v) {
            return v.visit(this);
        }
    }

    public static class Unique<T> extends Solution<T> {
        private final T value;

        public Unique(T val) { value = val; }

        public T get() { return value; }

        @Override
        public <R> R accept(Visitor<T, R> v) {
            return v.visit(this);
        }
    }

    public interface Visitor<T, R> {
        R visit(None<T> none);
        R visit(Infinite<T> inf);
        R visit(Unique<T> uniq);
    }

    public abstract <R> R accept(Visitor<T, R> v);

    public static <T> None<T> none() { return new None<>(); }
    public static <T> Infinite<T> infinite() { return new Infinite<>(); }
    public static <T> Unique<T> unique(T val) { return new Unique<>(val); }

    public boolean isNone() { return this instanceof None; }
    public boolean isInfinite() { return this instanceof Infinite; }
    public boolean isUnique() { return this instanceof Unique; }

    public Unique<T> asUnique() { return (Unique<T>)this; }
}
