package org.example;

import org.springframework.stereotype.Service;

/**
 * Calculator — complete math engine (mirrors calculator.js logic).
 *
 * All operations that exist in the web frontend also exist here,
 * so the REST API calls this class instead of doing math in JavaScript.
 */
@Service
public class Calculator {

    // ── Binary operations ─────────────────────────────────────────────────

    public double add(double a, double b)      { return a + b; }
    public double subtract(double a, double b) { return a - b; }
    public double multiply(double a, double b) { return a * b; }

    public double divide(double a, double b) {
        if (b == 0) throw new ArithmeticException("Division by zero is not allowed.");
        return a / b;
    }

    public double modulo(double a, double b) {
        if (b == 0) throw new ArithmeticException("Modulo by zero is not allowed.");
        return a % b;
    }

    public double power(double base, double exponent) { return Math.pow(base, exponent); }

    // ── Scientific — trigonometry ─────────────────────────────────────────

    /** sin — angleDeg=true means input is in degrees */
    public double sin(double n, boolean angleDeg) {
        return Math.sin(angleDeg ? Math.toRadians(n) : n);
    }

    public double cos(double n, boolean angleDeg) {
        return Math.cos(angleDeg ? Math.toRadians(n) : n);
    }

    public double tan(double n, boolean angleDeg) {
        return Math.tan(angleDeg ? Math.toRadians(n) : n);
    }

    // ── Scientific — logarithms ───────────────────────────────────────────

    public double log(double n) {
        if (n <= 0) throw new IllegalArgumentException("log undefined for n ≤ 0: " + n);
        return Math.log10(n);
    }

    public double ln(double n) {
        if (n <= 0) throw new IllegalArgumentException("ln undefined for n ≤ 0: " + n);
        return Math.log(n);
    }

    // ── Scientific — powers & roots ───────────────────────────────────────

    public double sqrt(double n) {
        if (n < 0) throw new IllegalArgumentException("Cannot compute sqrt of negative: " + n);
        return Math.sqrt(n);
    }

    public double square(double n)  { return Math.pow(n, 2); }
    public double cube(double n)    { return Math.pow(n, 3); }

    // ── Scientific — misc ─────────────────────────────────────────────────

    public double inverse(double n) {
        if (n == 0) throw new ArithmeticException("Cannot compute 1/0.");
        return 1.0 / n;
    }

    public long factorial(int n) {
        if (n < 0) throw new IllegalArgumentException("Factorial undefined for negative: " + n);
        long result = 1;
        for (int i = 2; i <= n; i++) result *= i;
        return result;
    }

    public double abs(double n) { return Math.abs(n); }

    // ── Utility ───────────────────────────────────────────────────────────

    public double max(double a, double b) { return Math.max(a, b); }
    public double min(double a, double b) { return Math.min(a, b); }

    /** Toggle positive/negative sign */
    public double negate(double n) { return -n; }

    /** Convert n to percentage: 50 → 0.5 */
    public double percent(double n) { return n / 100.0; }

    /** n% of base: e.g. 200 + 15% → percentOf(15, 200) = 30 */
    public double percentOf(double n, double base) { return base * n / 100.0; }
}
