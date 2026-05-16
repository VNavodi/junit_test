package org.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CalculatorTest — JUnit 5 test suite for Calculator.java
 *
 * Covers:
 *  ✓ add        — normal, negatives, zero, doubles
 *  ✓ subtract   — normal, negatives, zero
 *  ✓ multiply   — normal, negatives, zero, fractions
 *  ✓ divide     — normal, decimals, divide-by-zero exception
 *  ✓ modulo     — normal, modulo-by-zero exception
 *  ✓ power      — positive, zero exponent, fractional
 *  ✓ sqrt       — perfect square, non-perfect, negative exception
 *  ✓ factorial  — 0!, small, large, negative exception
 *  ✓ abs        — positive, negative, zero
 *  ✓ max / min  — normal & equal values
 *  ✓ Parameterized add & multiply tests
 *  ✓ @Nested grouping for readability
 */
@DisplayName("🧮 Calculator Test Suite")
class CalculatorTest {

    private Calculator calc;

    @BeforeEach
    void setUp() {
        calc = new Calculator();
    }

    // ─────────────────────────────────────────────────────────────────────
    //  ADD
    // ─────────────────────────────────────────────────────────────────────
    @Nested
    @DisplayName("➕ add()")
    class AddTests {

        @Test
        @DisplayName("adds two positive integers")
        void addPositiveNumbers() {
            assertEquals(10.0, calc.add(4, 6), "4 + 6 should equal 10");
        }

        @Test
        @DisplayName("adds positive and negative number")
        void addPositiveAndNegative() {
            assertEquals(3.0, calc.add(10, -7));
        }

        @Test
        @DisplayName("adds two negative numbers")
        void addTwoNegatives() {
            assertEquals(-9.0, calc.add(-4, -5));
        }

        @Test
        @DisplayName("adding zero returns same number")
        void addZero() {
            assertEquals(7.5, calc.add(7.5, 0));
        }

        @Test
        @DisplayName("adds two doubles with delta precision")
        void addDoubles() {
            assertEquals(0.3, calc.add(0.1, 0.2), 1e-9);
        }

        @ParameterizedTest(name = "{0} + {1} = {2}")
        @DisplayName("parameterized add")
        @CsvSource({
            "1,   1,   2",
            "0,   0,   0",
            "-5,  3,  -2",
            "100, 200, 300",
            "1.5, 2.5, 4.0"
        })
        void parameterizedAdd(double a, double b, double expected) {
            assertEquals(expected, calc.add(a, b), 1e-9);
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    //  SUBTRACT
    // ─────────────────────────────────────────────────────────────────────
    @Nested
    @DisplayName("➖ subtract()")
    class SubtractTests {

        @Test
        @DisplayName("subtracts smaller from larger")
        void subtractNormal() {
            assertEquals(5.0, calc.subtract(10, 5));
        }

        @Test
        @DisplayName("subtract resulting in negative")
        void subtractNegativeResult() {
            assertEquals(-3.0, calc.subtract(2, 5));
        }

        @Test
        @DisplayName("subtract zero leaves value unchanged")
        void subtractZero() {
            assertEquals(8.0, calc.subtract(8, 0));
        }

        @Test
        @DisplayName("subtracts two negatives")
        void subtractNegatives() {
            assertEquals(2.0, calc.subtract(-3, -5));
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    //  MULTIPLY
    // ─────────────────────────────────────────────────────────────────────
    @Nested
    @DisplayName("✖️ multiply()")
    class MultiplyTests {

        @Test
        @DisplayName("multiplies two positive integers")
        void multiplyPositives() {
            assertEquals(24.0, calc.multiply(4, 6));
        }

        @Test
        @DisplayName("multiply by zero returns zero")
        void multiplyByZero() {
            assertEquals(0.0, calc.multiply(99, 0));
        }

        @Test
        @DisplayName("multiply two negatives returns positive")
        void multiplyTwoNegatives() {
            assertEquals(12.0, calc.multiply(-3, -4));
        }

        @Test
        @DisplayName("multiply negative and positive returns negative")
        void multiplyNegativePositive() {
            assertEquals(-15.0, calc.multiply(3, -5));
        }

        @ParameterizedTest(name = "{0} × {1} = {2}")
        @DisplayName("parameterized multiply")
        @CsvSource({
            "2,   3,   6",
            "0,   5,   0",
            "-2,  4,  -8",
            "1.5, 2,   3.0",
            "7,   7,   49"
        })
        void parameterizedMultiply(double a, double b, double expected) {
            assertEquals(expected, calc.multiply(a, b), 1e-9);
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    //  DIVIDE
    // ─────────────────────────────────────────────────────────────────────
    @Nested
    @DisplayName("➗ divide()")
    class DivideTests {

        @Test
        @DisplayName("divides two integers evenly")
        void divideNormal() {
            assertEquals(5.0, calc.divide(10, 2));
        }

        @Test
        @DisplayName("divide returns decimal result")
        void divideDecimalResult() {
            assertEquals(2.5, calc.divide(5, 2));
        }

        @Test
        @DisplayName("divide negative by positive")
        void divideNegative() {
            assertEquals(-3.0, calc.divide(-9, 3));
        }

        @Test
        @DisplayName("divide by zero throws ArithmeticException")
        void divideByZeroThrows() {
            ArithmeticException ex = assertThrows(
                ArithmeticException.class,
                () -> calc.divide(10, 0)
            );
            assertTrue(ex.getMessage().contains("zero"),
                "Exception message should mention 'zero'");
        }

        @Test
        @DisplayName("zero divided by any number is zero")
        void zeroDividedByNumber() {
            assertEquals(0.0, calc.divide(0, 5));
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    //  MODULO
    // ─────────────────────────────────────────────────────────────────────
    @Nested
    @DisplayName("% modulo()")
    class ModuloTests {

        @Test
        @DisplayName("10 % 3 = 1")
        void moduloNormal() {
            assertEquals(1.0, calc.modulo(10, 3));
        }

        @Test
        @DisplayName("even number % 2 = 0 (even check)")
        void moduloEven() {
            assertEquals(0.0, calc.modulo(8, 2));
        }

        @Test
        @DisplayName("modulo by zero throws ArithmeticException")
        void moduloByZeroThrows() {
            assertThrows(ArithmeticException.class,
                () -> calc.modulo(5, 0));
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    //  POWER
    // ─────────────────────────────────────────────────────────────────────
    @Nested
    @DisplayName("^ power()")
    class PowerTests {

        @Test
        @DisplayName("2 ^ 10 = 1024")
        void powerNormal() {
            assertEquals(1024.0, calc.power(2, 10));
        }

        @Test
        @DisplayName("any number ^ 0 = 1")
        void powerZeroExponent() {
            assertEquals(1.0, calc.power(999, 0));
        }

        @Test
        @DisplayName("2 ^ 0.5 = √2")
        void powerFractionalExponent() {
            assertEquals(Math.sqrt(2), calc.power(2, 0.5), 1e-9);
        }

        @Test
        @DisplayName("negative base ^ even exponent = positive")
        void powerNegativeBase() {
            assertEquals(9.0, calc.power(-3, 2));
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    //  SQRT
    // ─────────────────────────────────────────────────────────────────────
    @Nested
    @DisplayName("√ sqrt()")
    class SqrtTests {

        @Test
        @DisplayName("sqrt(25) = 5")
        void sqrtPerfectSquare() {
            assertEquals(5.0, calc.sqrt(25));
        }

        @Test
        @DisplayName("sqrt(2) ≈ 1.4142...")
        void sqrtNonPerfect() {
            assertEquals(Math.sqrt(2), calc.sqrt(2), 1e-9);
        }

        @Test
        @DisplayName("sqrt(0) = 0")
        void sqrtZero() {
            assertEquals(0.0, calc.sqrt(0));
        }

        @Test
        @DisplayName("sqrt of negative throws IllegalArgumentException")
        void sqrtNegativeThrows() {
            IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> calc.sqrt(-4)
            );
            assertTrue(ex.getMessage().contains("negative"),
                "Message should mention 'negative'");
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    //  FACTORIAL
    // ─────────────────────────────────────────────────────────────────────
    @Nested
    @DisplayName("n! factorial()")
    class FactorialTests {

        @Test
        @DisplayName("0! = 1")
        void factorialZero() {
            assertEquals(1L, calc.factorial(0));
        }

        @Test
        @DisplayName("1! = 1")
        void factorialOne() {
            assertEquals(1L, calc.factorial(1));
        }

        @Test
        @DisplayName("5! = 120")
        void factorialFive() {
            assertEquals(120L, calc.factorial(5));
        }

        @Test
        @DisplayName("10! = 3628800")
        void factorialTen() {
            assertEquals(3628800L, calc.factorial(10));
        }

        @Test
        @DisplayName("factorial of negative throws IllegalArgumentException")
        void factorialNegativeThrows() {
            assertThrows(IllegalArgumentException.class,
                () -> calc.factorial(-1));
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    //  ABS / MAX / MIN
    // ─────────────────────────────────────────────────────────────────────
    @Nested
    @DisplayName("🔧 utility: abs, max, min")
    class UtilityTests {

        @Test @DisplayName("abs of negative = positive")
        void absNegative() { assertEquals(7.0, calc.abs(-7)); }

        @Test @DisplayName("abs of positive stays positive")
        void absPositive() { assertEquals(5.0, calc.abs(5)); }

        @Test @DisplayName("abs of zero = zero")
        void absZero() { assertEquals(0.0, calc.abs(0)); }

        @Test @DisplayName("max returns larger value")
        void maxNormal() { assertEquals(9.0, calc.max(3, 9)); }

        @Test @DisplayName("max of equal values")
        void maxEqual() { assertEquals(5.0, calc.max(5, 5)); }

        @Test @DisplayName("min returns smaller value")
        void minNormal() { assertEquals(2.0, calc.min(2, 8)); }

        @Test @DisplayName("min of equal values")
        void minEqual() { assertEquals(4.0, calc.min(4, 4)); }
    }
}