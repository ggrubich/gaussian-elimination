package gauss;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiFunction;

// Matrix of rational numbers.
public class Matrix {
    ArrayList<Rational> cells;
    int height_;
    int width_;

    public int height() { return height_; }

    public int width() { return width_; }

    // Constructs a matrix from a seed function. Seed takes a (row, column) pair
    // and returns a value to be placed in that location.
    public Matrix(int height_, int width_, BiFunction<Integer, Integer, Rational> seed) {
        cells = new ArrayList<>();
        this.height_ = height_;
        this.width_ = width_;
        for (int i = 0; i < height_; ++i) {
            for (int j = 0; j < width_; ++j) {
                cells.add(seed.apply(i, j));
            }
        }
    }

    // Constructs a matrix from the given list of consecutive rows.
    public Matrix(int height_, int width_, Rational... data) {
        this(height_, width_, (i, j) -> data[i * width_ + j]);
    }

    // Accesses the cell at (row, col).
    public Rational get(int row, int col) {
        return cells.get(row * width() + col);
    }

    // Modifies a cell at (row, col).
    public void set(int row, int col, Rational val) {
        cells.set(row * width() + col, val);
    }

    private void swapRows(int a, int b) {
        if (a != b) {
            for (int i = 0; i < width(); ++i) {
                var tmp = get(a, i);
                set(a, i, get(b, i));
                set(b, i, tmp);
            }
        }
    }

    private void subtractRows(int a, int b, Rational ratio) {
        for (int i = 0; i < width(); ++i) {
            set(a, i, get(a, i).sub(get(b, i).mul(ratio)));
        }
    }

    private void multiplyRow(int a, Rational ratio) {
        for (int i = 0; i < width(); ++i) {
            set(a, i, get(a, i).mul(ratio));
        }
    }

    // Solves equation `this * x = y` and returns x.
    // If a finite solution doesn't exist, returns nothing.
    // Input matrices are not modified.
    // TODO: Detect infinite solutions.
    public Optional<Matrix> solve(Matrix y) {
        if (height() != y.height()) {
            throw new IllegalArgumentException("Matrix heights don't match");
        }
        if (height() < width()) {
            return Optional.empty();
        }
        int n = width();
        var aug = new Matrix(height(), n + y.width(), (i, j) -> {
            return j < n ? get(i, j) : y.get(i, j - n);
        });
        for (int k = 0; k < n; ++k) {
            int max = k;
            for (int i = k+1; i < aug.height(); ++i) {
                if (aug.get(i, k).abs().compareTo(aug.get(max, k).abs()) > 0) {
                    max = i;
                }
            }
            if (aug.get(max, k).equals(Rational.ZERO)) {
                return Optional.empty();
            }
            aug.swapRows(max, k);
            for (int i = 0; i < aug.height(); ++i) {
                if (i == k) {
                    continue;
                }
                var ratio = aug.get(i, k).div(aug.get(k, k));
                aug.subtractRows(i, k, ratio);
            }
        }
        for (int i = n; i < aug.height(); ++i) {
            for (int j = n; j < aug.width(); ++j) {
                if (!aug.get(i, j).equals(Rational.ZERO)) {
                    return Optional.empty();
                }
            }
        }
        for (int k = 0; k < n; ++k) {
            var ratio = aug.get(k, k).inv();
            aug.multiplyRow(k, ratio);
        }
        var result = new Matrix(n, y.width(), (i, j) -> {
            return aug.get(i, n + j);
        });
        return Optional.of(result);
    }

    @Override
    public String toString() {
        var buf = new StringBuilder();
        for (int i = 0; i < height(); ++i) {
            for (int j = 0; j < width(); ++j) {
                buf.append(get(i, j).toString());
                if (j + 1 < width()) {
                    buf.append("\t");
                }
            }
            buf.append("\n");
        }
        return buf.toString();
    }
}
