# 🧮 Calculator — Java + Web + CI/CD

[![CI/CD Pipeline](https://github.com/VNavodi/junit_test/actions/workflows/ci.yml/badge.svg)](https://github.com/VNavodi/junit_test/actions/workflows/ci.yml)
[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://openjdk.org/projects/jdk/17/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5.12.2-green?logo=junit5)](https://junit.org/junit5/)
[![Maven](https://img.shields.io/badge/Maven-3.x-red?logo=apachemaven)](https://maven.apache.org/)
[![GitHub Pages](https://img.shields.io/badge/Web-GitHub%20Pages-blue?logo=github)](https://vnavodi.github.io/junit_test/)

A full-stack calculator project combining a **Java backend engine**, **51 JUnit 5 tests**, and a **premium web UI** — all automatically built, tested, and deployed through **GitHub Actions CI/CD**.

---

## 🌐 Live Web Calculator

> **[▶ Open Calculator →](https://vnavodi.github.io/junit_test/)**

Automatically deployed to GitHub Pages on every push to `master`.

---

## 📁 Project Structure

```
junit_test/
│
├── src/
│   ├── main/java/org/example/
│   │   ├── Calculator.java        ← Java engine (11 operations)
│   │   └── Main.java
│   └── test/java/org/example/
│       └── CalculatorTest.java   ← 51 JUnit 5 tests
│
├── calculator/                   ← Web Calculator (deployed to GitHub Pages)
│   ├── index.html                  Structure & layout
│   ├── style.css                   Glassmorphism dark UI
│   └── calculator.js               Full calculator logic
│
├── .github/
│   └── workflows/
│       └── ci.yml                ← 4-job CI/CD pipeline
│
└── pom.xml                       ← Maven (JUnit 5 + Surefire 3.x)
```

---

## ⚙️ CI/CD Pipeline

The pipeline runs on every **push** and **pull request** to `master`.

```
git push
    │
    ├──► Job 1: ☕ Java — Build & JUnit 5 Tests
    │         Compiles, runs all 51 tests, uploads Surefire reports
    │         (Runs in parallel with Job 3)
    │
    ├──► Job 3: 🌐 Web — Validate Calculator Files
    │         Checks HTML/CSS/JS exist, verifies JS syntax,
    │         confirms CSS & JS are linked in HTML
    │         (Runs in parallel with Job 1)
    │
    ├──► Job 2: 📦 Java — Package JAR          [needs: Job 1]
    │         Builds JAR, uploads as GitHub artifact (30 days)
    │
    └──► Job 4: 🚀 Deploy to GitHub Pages      [needs: Job 1 + Job 3]
              Deploys calculator/ folder to GitHub Pages
              (Only on push to master, not on PRs)
```

### Pipeline Flow Diagram

```
push/PR
  │
  ├─────────────────────┐
  ▼                     ▼
[test-java]       [validate-web]
  │   │                 │
  │   └────────────┐    │
  ▼                ▼    ▼
[package-java]  [deploy-web]
(JAR artifact)  (GitHub Pages)
```

---

## ☕ Java Calculator — Operations

| Method | Description | Edge Cases |
|---|---|---|
| `add(a, b)` | a + b | — |
| `subtract(a, b)` | a − b | — |
| `multiply(a, b)` | a × b | — |
| `divide(a, b)` | a ÷ b | Throws `ArithmeticException` if b=0 |
| `modulo(a, b)` | a % b | Throws `ArithmeticException` if b=0 |
| `power(base, exp)` | baseᵉˣᵖ | — |
| `sqrt(n)` | √n | Throws `IllegalArgumentException` if n<0 |
| `factorial(n)` | n! | Throws `IllegalArgumentException` if n<0 |
| `abs(n)` | \|n\| | — |
| `max(a, b)` | larger of a, b | — |
| `min(a, b)` | smaller of a, b | — |

---

## 🧪 JUnit 5 Tests — 51 Tests, 8 Groups

| Test Group | Tests | Features Used |
|---|---|---|
| `AddTests` | 10 | `@ParameterizedTest`, delta precision |
| `SubtractTests` | 4 | Normal & edge cases |
| `MultiplyTests` | 9 | `@ParameterizedTest`, negatives |
| `DivideTests` | 5 | `assertThrows`, message check |
| `ModuloTests` | 3 | Exception for zero divisor |
| `PowerTests` | 4 | Zero exponent, fractional |
| `SqrtTests` | 4 | Perfect square, exception |
| `FactorialTests` | 5 | 0!, large n, exception |
| `UtilityTests` | 7 | abs, max, min |

---

## 🌐 Web Calculator — Features

### Standard Mode
- `+` `−` `×` `÷` `%` with proper operand clearing
- Chained operations, decimal input
- Memory: `MC` `MR` `M+` `M−`
- Calculation history (click to restore)

### Scientific Mode
| Button | Function |
|---|---|
| `sin` `cos` `tan` | Trig (DEG/RAD toggle) |
| `log` `ln` | Logarithms |
| `√x` `x²` `x³` `xʸ` | Powers & roots |
| `1/x` `n!` `\|x\|` | Reciprocal, factorial, absolute |
| `π` `e` | Constants |

### ⌨️ Keyboard Shortcuts
| Key | Action |
|---|---|
| `0–9` `.` | Input |
| `+` `-` `*` `/` `^` | Operators |
| `Enter` or `=` | Calculate |
| `Backspace` / `Delete` | Delete digit |
| `Escape` / `C` | Clear all |
| `%` | Percent |
| `s` | Square root |
| `F9` | Toggle sign |

---

## 🚀 Running Locally

### Java Tests
```powershell
# Using IntelliJ's bundled Maven (Windows)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
& "C:\Program Files\JetBrains\IntelliJ IDEA 2026.1\plugins\maven\lib\maven3\bin\mvn.cmd" -B test
```

**Or:** Open `CalculatorTest.java` in IntelliJ → click ▶ Run

### Web Calculator
```powershell
# Simple Python server
python -m http.server 8765 --directory calculator/
# Then open http://localhost:8765
```

---

## 🔧 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Build | Apache Maven 3.x |
| Testing | JUnit Jupiter 5.12.2 |
| Test Runner | Maven Surefire 3.2.5 |
| Web | HTML5 + Vanilla CSS + JavaScript (ES6) |
| Fonts | Google Fonts (Inter, JetBrains Mono) |
| CI/CD | GitHub Actions |
| Hosting | GitHub Pages |
