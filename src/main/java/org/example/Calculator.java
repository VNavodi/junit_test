package org.example;

/**
 * Calculator — core arithmetic engine.
 *
 * Operations  : add, subtract, multiply, divide, modulo, power, sqrt, factorial
 * Edge cases  : divide/modulo by zero throws ArithmeticException
 *               sqrt of negative throws IllegalArgumentException
 *               factorial of negative throws IllegalArgumentException
 */
public class Calculator {

    // ── Basic arithmetic ──────────────────────────────────────────────────

    public double add(double a, double b) {
        return a + b;
    }

    public double subtract(double a, double b) {
        return a - b;
    }

    public double multiply(double a, double b) {
        return a * b;
    }

    /**
     * Divides a by b.
     * @throws ArithmeticException if b == 0
     */
    public double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        return a / b;
    }

    /**
     * Returns a % b (remainder).
     * @throws ArithmeticException if b == 0
     */
    public double modulo(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Modulo by zero is not allowed.");
        }
        return a % b;
    }

    // ── Advanced operations ───────────────────────────────────────────────

    /** Returns base raised to the power of exponent. */
    public double power(double base, double exponent) {
        return Math.pow(base, exponent);
    }

    /**
     * Returns the square root of n.
     * @throws IllegalArgumentException if n < 0
     */
    public double sqrt(double n) {
        if (n < 0) {
            throw new IllegalArgumentException("Cannot compute sqrt of a negative number: " + n);
        }
        return Math.sqrt(n);
    }

    /**
     * Returns n! (factorial) for non-negative integers.
     * @throws IllegalArgumentException if n < 0
     */
    public long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers: " + n);
        }
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    // ── Utility ───────────────────────────────────────────────────────────

    /** Returns the absolute value of n. */
    public double abs(double n) {
        return Math.abs(n);
    }

    /** Returns the larger of a and b. */
    public double max(double a, double b) {
        return Math.max(a, b);
    }

    /** Returns the smaller of a and b. */
    public double min(double a, double b) {
        return Math.min(a, b);
    }
}
