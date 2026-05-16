package org.example;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * CalculatorController — REST API that connects the web frontend to Calculator.java.
 *
 * Endpoints:
 *   POST /api/calculate  — handles all operations (binary + unary/scientific)
 *
 * Request body examples:
 *   Binary:   { "type":"binary",  "a":10, "operator":"+",   "b":5 }
 *   Unary:    { "type":"unary",   "function":"sin", "value":30, "angleDeg":true }
 *
 * Response:
 *   Success:  { "result":15.0, "expression":"10.0 + 5.0", "error":false }
 *   Error:    { "result":"Error", "message":"Division by zero", "error":true }
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")   // allow requests from any origin (GitHub Pages, etc.)
public class CalculatorController {

    private final Calculator calculator;

    public CalculatorController(Calculator calculator) {
        this.calculator = calculator;   // injected by Spring (@Service)
    }

    @PostMapping("/calculate")
    public ResponseEntity<Map<String, Object>> calculate(@RequestBody Map<String, Object> req) {
        Map<String, Object> res = new HashMap<>();
        try {
            String type = (String) req.get("type");
            double result;
            String expression;

            // ── Binary operations: +  −  ×  ÷  %  ^ ─────────────────────
            if ("binary".equals(type)) {
                double a = toDouble(req.get("a"));
                double b = toDouble(req.get("b"));
                String op = (String) req.get("operator");

                result = switch (op) {
                    case "+", "＋"   -> calculator.add(a, b);
                    case "−", "-"    -> calculator.subtract(a, b);
                    case "×", "*"    -> calculator.multiply(a, b);
                    case "÷", "/"    -> calculator.divide(a, b);
                    case "%"         -> calculator.modulo(a, b);
                    case "^"         -> calculator.power(a, b);
                    default -> throw new IllegalArgumentException("Unknown operator: " + op);
                };
                expression = a + " " + op + " " + b;

            // ── Unary / scientific operations ─────────────────────────────
            } else if ("unary".equals(type)) {
                double value  = toDouble(req.get("value"));
                String fn     = (String) req.get("function");
                boolean deg   = Boolean.TRUE.equals(req.get("angleDeg"));

                result = switch (fn) {
                    case "sin"     -> calculator.sin(value, deg);
                    case "cos"     -> calculator.cos(value, deg);
                    case "tan"     -> calculator.tan(value, deg);
                    case "log"     -> calculator.log(value);
                    case "ln"      -> calculator.ln(value);
                    case "sqrt"    -> calculator.sqrt(value);
                    case "pow2"    -> calculator.square(value);
                    case "pow3"    -> calculator.cube(value);
                    case "inv"     -> calculator.inverse(value);
                    case "fact"    -> (double) calculator.factorial((int) value);
                    case "abs"     -> calculator.abs(value);
                    case "negate"  -> calculator.negate(value);
                    case "percent" -> calculator.percent(value);
                    default -> throw new IllegalArgumentException("Unknown function: " + fn);
                };
                expression = fn + "(" + value + ")";

            } else {
                throw new IllegalArgumentException("Unknown request type: " + type);
            }

            res.put("result",     Double.isNaN(result) || Double.isInfinite(result) ? "Error" : result);
            res.put("expression", expression);
            res.put("error",      false);

        } catch (Exception e) {
            res.put("error",   true);
            res.put("message", e.getMessage());
            res.put("result",  "Error");
        }
        return ResponseEntity.ok(res);
    }

    // ── Health check ──────────────────────────────────────────────────────
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "Calculator API"));
    }

    // ── Helper: convert Object → double safely ────────────────────────────
    private double toDouble(Object val) {
        if (val instanceof Number n) return n.doubleValue();
        return Double.parseDouble(String.valueOf(val));
    }
}
