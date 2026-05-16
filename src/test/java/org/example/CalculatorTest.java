package org.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CalculatorTest — 65 JUnit 5 tests covering every operation in Calculator.java,
 * including all scientific functions mirrored from the web frontend (calculator.js).
 */
@DisplayName("Calculator — Full Test Suite")
class CalculatorTest {

    private Calculator calc;
    private static final double DELTA = 1e-9;

    @BeforeEach
    void setUp() { calc = new Calculator(); }

    // ══════════════════════════════════════════════════════════════════════
    // BASIC ARITHMETIC
    // ══════════════════════════════════════════════════════════════════════
    @Nested @DisplayName("add()")
    class AddTests {
        @ParameterizedTest(name = "{0} + {1} = {2}")
        @CsvSource({"2,3,5", "0,0,0", "-5,5,0", "-3,-4,-7", "0.5,0.5,1.0",
                    "1000,2000,3000", "-1,1,0", "100,-50,50", "0.1,0.2,0.3", "99,1,100"})
        void add(double a, double b, double expected) {
            assertEquals(expected, calc.add(a, b), DELTA);
        }
    }

    @Nested @DisplayName("subtract()")
    class SubtractTests {
        @ParameterizedTest(name = "{0} - {1} = {2}")
        @CsvSource({"10,3,7", "0,5,-5", "-3,-4,1", "100,100,0"})
        void subtract(double a, double b, double expected) {
            assertEquals(expected, calc.subtract(a, b), DELTA);
        }
    }

    @Nested @DisplayName("multiply()")
    class MultiplyTests {
        @ParameterizedTest(name = "{0} × {1} = {2}")
        @CsvSource({"3,4,12", "0,100,0", "-3,4,-12", "-3,-4,12",
                    "0.5,4,2", "1000,1000,1000000", "7,7,49", "0,0,0", "2.5,4,10"})
        void multiply(double a, double b, double expected) {
            assertEquals(expected, calc.multiply(a, b), DELTA);
        }
    }

    @Nested @DisplayName("divide()")
    class DivideTests {
        @Test void divideNormal()     { assertEquals(4.0, calc.divide(20, 5), DELTA); }
        @Test void divideNegative()   { assertEquals(-5.0, calc.divide(-25, 5), DELTA); }
        @Test void divideFractional() { assertEquals(0.5, calc.divide(1, 2), DELTA); }
        @Test void divideByZero()     {
            var ex = assertThrows(ArithmeticException.class, () -> calc.divide(10, 0));
            assertTrue(ex.getMessage().contains("zero"));
        }
    }

    @Nested @DisplayName("modulo()")
    class ModuloTests {
        @Test void moduloNormal()    { assertEquals(2.0, calc.modulo(17, 5), DELTA); }
        @Test void moduloZero()      { assertEquals(0.0, calc.modulo(10, 5), DELTA); }
        @Test void moduloByZero()    {
            assertThrows(ArithmeticException.class, () -> calc.modulo(10, 0));
        }
    }

    @Nested @DisplayName("power()")
    class PowerTests {
        @Test void powerPositive()   { assertEquals(8.0,  calc.power(2, 3),  DELTA); }
        @Test void powerZeroExp()    { assertEquals(1.0,  calc.power(5, 0),  DELTA); }
        @Test void powerFractional() { assertEquals(2.0,  calc.power(4, 0.5),DELTA); }
        @Test void powerNegBase()    { assertEquals(-8.0, calc.power(-2, 3), DELTA); }
    }

    // ══════════════════════════════════════════════════════════════════════
    // SCIENTIFIC — ROOTS & POWERS
    // ══════════════════════════════════════════════════════════════════════
    @Nested @DisplayName("sqrt()")
    class SqrtTests {
        @Test void sqrtPerfect()    { assertEquals(5.0,  calc.sqrt(25),  DELTA); }
        @Test void sqrtZero()       { assertEquals(0.0,  calc.sqrt(0),   DELTA); }
        @Test void sqrtFractional() { assertEquals(1.5,  calc.sqrt(2.25),DELTA); }
        @Test void sqrtNegative()   {
            assertThrows(IllegalArgumentException.class, () -> calc.sqrt(-1));
        }
    }

    @Nested @DisplayName("square() and cube()")
    class PowerShortcutTests {
        @Test void squarePositive() { assertEquals(49.0, calc.square(7),  DELTA); }
        @Test void squareZero()     { assertEquals(0.0,  calc.square(0),  DELTA); }
        @Test void cubePositive()   { assertEquals(27.0, calc.cube(3),    DELTA); }
        @Test void cubeNegative()   { assertEquals(-8.0, calc.cube(-2),   DELTA); }
    }

    // ══════════════════════════════════════════════════════════════════════
    // SCIENTIFIC — TRIGONOMETRY
    // ══════════════════════════════════════════════════════════════════════
    @Nested @DisplayName("sin() / cos() / tan()")
    class TrigTests {
        @Test void sinDeg90()    { assertEquals(1.0,  calc.sin(90,  true),  DELTA); }
        @Test void sinDeg0()     { assertEquals(0.0,  calc.sin(0,   true),  DELTA); }
        @Test void sinRad()      { assertEquals(1.0,  calc.sin(Math.PI / 2, false), DELTA); }
        @Test void cosDeg0()     { assertEquals(1.0,  calc.cos(0,   true),  DELTA); }
        @Test void cosDeg90()    { assertEquals(0.0,  calc.cos(90,  true),  DELTA); }
        @Test void tanDeg45()    { assertEquals(1.0,  calc.tan(45,  true),  DELTA); }
        @Test void tanDeg0()     { assertEquals(0.0,  calc.tan(0,   true),  DELTA); }
    }

    // ══════════════════════════════════════════════════════════════════════
    // SCIENTIFIC — LOGARITHMS
    // ══════════════════════════════════════════════════════════════════════
    @Nested @DisplayName("log() and ln()")
    class LogTests {
        @Test void log10()         { assertEquals(2.0, calc.log(100), DELTA); }
        @Test void log1()          { assertEquals(0.0, calc.log(1),   DELTA); }
        @Test void logNegative()   { assertThrows(IllegalArgumentException.class, () -> calc.log(-1)); }
        @Test void logZero()       { assertThrows(IllegalArgumentException.class, () -> calc.log(0)); }
        @Test void lnE()           { assertEquals(1.0, calc.ln(Math.E), DELTA); }
        @Test void lnOne()         { assertEquals(0.0, calc.ln(1),      DELTA); }
        @Test void lnNegative()    { assertThrows(IllegalArgumentException.class, () -> calc.ln(-1)); }
    }

    // ══════════════════════════════════════════════════════════════════════
    // SCIENTIFIC — FACTORIAL, INVERSE, ABS
    // ══════════════════════════════════════════════════════════════════════
    @Nested @DisplayName("factorial()")
    class FactorialTests {
        @Test void factorial0()    { assertEquals(1L,   calc.factorial(0)); }
        @Test void factorial1()    { assertEquals(1L,   calc.factorial(1)); }
        @Test void factorial5()    { assertEquals(120L, calc.factorial(5)); }
        @Test void factorial10()   { assertEquals(3628800L, calc.factorial(10)); }
        @Test void factorialNeg()  { assertThrows(IllegalArgumentException.class, () -> calc.factorial(-1)); }
    }

    @Nested @DisplayName("inverse()")
    class InverseTests {
        @Test void inverseTwo()    { assertEquals(0.5,  calc.inverse(2),  DELTA); }
        @Test void inverseFour()   { assertEquals(0.25, calc.inverse(4),  DELTA); }
        @Test void inverseZero()   { assertThrows(ArithmeticException.class, () -> calc.inverse(0)); }
    }

    @Nested @DisplayName("abs()")
    class AbsTests {
        @Test void absPositive()   { assertEquals(5.0, calc.abs(5),   DELTA); }
        @Test void absNegative()   { assertEquals(5.0, calc.abs(-5),  DELTA); }
        @Test void absZero()       { assertEquals(0.0, calc.abs(0),   DELTA); }
    }

    // ══════════════════════════════════════════════════════════════════════
    // UTILITY
    // ══════════════════════════════════════════════════════════════════════
    @Nested @DisplayName("max() / min() / negate() / percent()")
    class UtilityTests {
        @Test void maxNormal()     { assertEquals(9.0,  calc.max(3, 9),    DELTA); }
        @Test void minNormal()     { assertEquals(3.0,  calc.min(3, 9),    DELTA); }
        @Test void negatePos()     { assertEquals(-7.0, calc.negate(7),    DELTA); }
        @Test void negateNeg()     { assertEquals(7.0,  calc.negate(-7),   DELTA); }
        @Test void percent50()     { assertEquals(0.5,  calc.percent(50),  DELTA); }
        @Test void percent100()    { assertEquals(1.0,  calc.percent(100), DELTA); }
        @Test void percentOf()     { assertEquals(30.0, calc.percentOf(15, 200), DELTA); }
    }
}