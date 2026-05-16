package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CalculatorApplication — Spring Boot entry point.
 * Starts embedded Tomcat on port 8080 and serves:
 *   - REST API  → /api/calculate
 *   - Web UI    → / (from src/main/resources/static/)
 */
@SpringBootApplication
public class CalculatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(CalculatorApplication.class, args);
        System.out.println("\n✅ Calculator API running → http://localhost:8080");
        System.out.println("🌐 Web UI              → http://localhost:8080/index.html\n");
    }
}
