package org.example;

/**
 * Main — entry point / demonstration of Calculator.java
 *
 * Run this class directly in IntelliJ (▶) to see all
 * Calculator operations printed to the console.
 */
public class Main {

    public static void main(String[] args) {
        Calculator calc = new Calculator();

        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║   Java Calculator — Demo Run     ║");
        System.out.println("╚══════════════════════════════════╝\n");

        // ── Basic arithmetic ──────────────────────────────────
        System.out.println("── Basic Arithmetic ──");
        System.out.printf("  10 + 5        = %.1f%n", calc.add(10, 5));
        System.out.printf("  10 - 3        = %.1f%n", calc.subtract(10, 3));
        System.out.printf("  4  × 7        = %.1f%n", calc.multiply(4, 7));
        System.out.printf("  20 ÷ 4        = %.1f%n", calc.divide(20, 4));
        System.out.printf("  17 %% 5        = %.1f%n", calc.modulo(17, 5));

        // ── Advanced operations ───────────────────────────────
        System.out.println("\n── Advanced Operations ──");
        System.out.printf("  2  ^ 10       = %.0f%n",  calc.power(2, 10));
        System.out.printf("  √144          = %.1f%n",  calc.sqrt(144));
        System.out.printf("  6!            = %d%n",    calc.factorial(6));
        System.out.printf("  |−42|         = %.1f%n",  calc.abs(-42));
        System.out.printf("  max(7, 13)    = %.1f%n",  calc.max(7, 13));
        System.out.printf("  min(7, 13)    = %.1f%n",  calc.min(7, 13));

        // ── Error handling demo ───────────────────────────────
        System.out.println("\n── Error Handling ──");
        try {
            calc.divide(10, 0);
        } catch (ArithmeticException e) {
            System.out.println("  divide(10, 0) → " + e.getMessage());
        }
        try {
            calc.sqrt(-9);
        } catch (IllegalArgumentException e) {
            System.out.println("  sqrt(-9)      → " + e.getMessage());
        }
        try {
            calc.factorial(-3);
        } catch (IllegalArgumentException e) {
            System.out.println("  factorial(-3) → " + e.getMessage());
        }

        System.out.println("\n✅ All operations completed successfully.");
    }
}