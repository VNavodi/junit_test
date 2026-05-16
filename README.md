# Java Calculator — Full-Stack CI/CD Project

[![CI/CD Pipeline](https://github.com/VNavodi/junit_test/actions/workflows/ci.yml/badge.svg?branch=master)](https://github.com/VNavodi/junit_test/actions/workflows/ci.yml)
[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![JUnit 5](https://img.shields.io/badge/JUnit-5.12.2-red?logo=junit5&logoColor=white)](https://junit.org/junit5/)
[![Tests](https://img.shields.io/badge/Tests-74%20passing-success)](https://github.com/VNavodi/junit_test/actions)
[![GitHub Pages](https://img.shields.io/badge/Live%20Demo-GitHub%20Pages-blue?logo=github)](https://vnavodi.github.io/junit_test/)

> A **full-stack calculator** built with Java + Spring Boot backend, HTML/CSS/JS frontend,
> 74 JUnit 5 tests, and a complete GitHub Actions CI/CD pipeline.

---

## 📐 Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                        Full-Stack Architecture                       │
├────────────────────────┬────────────────────────────────────────────┤
│      FRONTEND          │              BACKEND                        │
│                        │                                             │
│  index.html            │  CalculatorApplication.java                 │
│  ├─ Standard mode      │  └─ Spring Boot entry point (port 8080)    │
│  ├─ Scientific mode    │                                             │
│  ├─ History panel      │  CalculatorController.java                  │
│  └─ Memory buttons     │  └─ POST /api/calculate                    │
│                        │  └─ GET  /api/health                       │
│  style.css             │                                             │
│  └─ Glassmorphism UI   │  Calculator.java  ← the math engine        │
│                        │  ├─ add, subtract, multiply, divide        │
│  calculator-api.js     │  ├─ modulo, power, sqrt, square, cube      │
│  └─ fetch('/api/...')──┼─►├─ sin, cos, tan (DEG/RAD)               │
│     calls Java for     │  ├─ log, ln, factorial, inverse            │
│     every calculation  │  └─ abs, max, min, negate, percent        │
│                        │                                             │
│  Runs in: Browser      │  Runs in: JVM (Java 17 + Tomcat)          │
└────────────────────────┴────────────────────────────────────────────┘
                         │
             ┌───────────▼───────────┐
             │   GitHub Actions       │
             │   CI/CD Pipeline       │
             │                        │
             │  ☕ Java Build+Tests   │
             │  📦 Package JAR        │
             │  🌐 Validate Web       │
             │  🚀 Deploy Pages       │
             └────────────────────────┘
```

---

## 📁 Project Structure

```
junit_test/
│
├── src/
│   ├── main/
│   │   ├── java/org/example/
│   │   │   ├── Calculator.java              ← Math engine (all operations)
│   │   │   ├── CalculatorApplication.java   ← Spring Boot entry point
│   │   │   └── CalculatorController.java    ← REST API endpoints
│   │   └── resources/
│   │       ├── application.properties       ← Server config (port 8080)
│   │       └── static/                      ← Served by Spring Boot
│   │           ├── index.html               ← Web UI (Java-powered)
│   │           ├── style.css                ← Glassmorphism dark theme
│   │           └── calculator-api.js        ← Calls Java REST API
│   │
│   └── test/java/org/example/
│       └── CalculatorTest.java              ← 74 JUnit 5 tests
│
├── calculator/                              ← Static version (GitHub Pages)
│   ├── index.html
│   ├── style.css
│   └── calculator.js
│
├── .github/workflows/
│   └── ci.yml                              ← 4-job CI/CD pipeline
│
└── pom.xml                                 ← Maven + Spring Boot 3.2.5
```

---

## ⚙️ CI/CD Pipeline

Every `git push` to `master` triggers the full pipeline automatically:

```
git push origin master
        │
        ├──────────────────────────┐
        ▼                          ▼
[☕ Java — Build & Tests]    [🌐 Web — Validate Files]
  • Compile all Java            • Check HTML/CSS/JS exist
  • Run 74 JUnit 5 tests        • Node.js syntax check
  • Upload Surefire reports      • Verify CSS/JS linked in HTML
        │                          │
        └────────────┬─────────────┘
                     │
          ┌──────────┴──────────┐
          ▼                     ▼
 [📦 Package JAR]     [🚀 Deploy to GitHub Pages]
   Build fat JAR          Push calculator/ to Pages
   Upload artifact         Live at: vnavodi.github.io/junit_test
   (30-day retention)      (only on master push)
```

---

## ☕ Calculator.java — Operations

| Category | Method | Description | Error Handling |
|---|---|---|---|
| **Basic** | `add(a, b)` | a + b | — |
| | `subtract(a, b)` | a − b | — |
| | `multiply(a, b)` | a × b | — |
| | `divide(a, b)` | a ÷ b | `ArithmeticException` if b=0 |
| | `modulo(a, b)` | a % b | `ArithmeticException` if b=0 |
| | `power(a, b)` | aᵇ | — |
| **Scientific** | `sqrt(n)` | √n | `IllegalArgumentException` if n<0 |
| | `square(n)` | n² | — |
| | `cube(n)` | n³ | — |
| | `sin(n, deg)` | sin(n) | DEG/RAD mode |
| | `cos(n, deg)` | cos(n) | DEG/RAD mode |
| | `tan(n, deg)` | tan(n) | DEG/RAD mode |
| | `log(n)` | log₁₀(n) | `IllegalArgumentException` if n≤0 |
| | `ln(n)` | logₑ(n) | `IllegalArgumentException` if n≤0 |
| | `factorial(n)` | n! | `IllegalArgumentException` if n<0 |
| | `inverse(n)` | 1/n | `ArithmeticException` if n=0 |
| **Utility** | `abs(n)` | \|n\| | — |
| | `max(a, b)` | larger value | — |
| | `min(a, b)` | smaller value | — |
| | `negate(n)` | −n | — |
| | `percent(n)` | n ÷ 100 | — |

---

## 🧪 JUnit 5 Tests — 74 Tests, 14 Groups

```
CalculatorTest
 ├─ AddTests          (10)  @ParameterizedTest with @CsvSource
 ├─ SubtractTests      (4)  edge cases
 ├─ MultiplyTests      (9)  @ParameterizedTest
 ├─ DivideTests        (4)  assertThrows for ArithmeticException
 ├─ ModuloTests        (3)  zero divisor guard
 ├─ PowerTests         (4)  fractional exponents
 ├─ SqrtTests          (4)  negative input exception
 ├─ PowerShortcutTests (4)  square() and cube()
 ├─ TrigTests          (7)  sin/cos/tan in DEG and RAD
 ├─ LogTests           (7)  log10, ln, invalid input guards
 ├─ FactorialTests     (5)  0!, large n, negative exception
 ├─ InverseTests       (3)  1/x, divide-by-zero guard
 ├─ AbsTests           (3)  positive, negative, zero
 └─ UtilityTests       (7)  max, min, negate, percent, percentOf
```

**Result: `Tests run: 74, Failures: 0, Errors: 0`** ✅

---

## 🌐 Web Calculator — Features

### Standard Mode
| Button | Action |
|---|---|
| `0–9` `.` | Input digits |
| `+` `−` `×` `÷` | Binary operations (calls Java) |
| `=` | Calculate via Java API |
| `AC` | Clear all |
| `+/−` | Toggle sign (calls Java) |
| `%` | Percent (calls Java) |
| `MC` `MR` `M+` `M−` | Memory (local JS state) |

### Scientific Mode
| Button | Function |
|---|---|
| `sin` `cos` `tan` | Trig — respects DEG/RAD toggle |
| `log` `ln` | Logarithms |
| `√x` `x²` `x³` | Powers and roots |
| `xʸ` | Custom power |
| `1/x` `n!` `\|x\|` | Inverse, factorial, absolute |
| `π` `e` | Constants (local) |
| `DEG` | Toggle degrees ↔ radians |

### ⌨️ Keyboard Shortcuts
| Key | Action |
|---|---|
| `0–9` | Digits |
| `+ - * /` | Operators |
| `Enter` or `=` | Calculate |
| `Backspace` / `Delete` | Delete digit |
| `Escape` / `C` | Clear all |
| `%` | Percent |
| `F9` | Toggle sign |

---

## 🚀 Running Locally

### Prerequisites
- Java 17 JDK
- Maven (or use IntelliJ's bundled Maven)

### Start the full-stack app

```powershell
# Set JAVA_HOME
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"

# Run Spring Boot
& "C:\Program Files\JetBrains\IntelliJ IDEA 2026.1\plugins\maven\lib\maven3\bin\mvn.cmd" spring-boot:run
```

Then open → **http://localhost:8080**

> If port 8080 is already in use:
> ```powershell
> $proc = Get-NetTCPConnection -LocalPort 8080 | Select-Object -First 1 -ExpandProperty OwningProcess
> Stop-Process -Id $proc -Force
> ```

### Run tests only

```powershell
& mvn.cmd test
# Output: Tests run: 74, Failures: 0
```

---

## 🔧 Tech Stack

| Layer | Technology | Version |
|---|---|---|
| Language | Java | 17 |
| Framework | Spring Boot | 3.2.5 |
| Build | Apache Maven | 3.x |
| Testing | JUnit Jupiter | 5.12.2 |
| Test Runner | Maven Surefire | 3.2.5 |
| Frontend | HTML5 + CSS3 + JavaScript ES6 | — |
| Fonts | Google Fonts (Inter, JetBrains Mono) | — |
| CI/CD | GitHub Actions | — |
| Hosting | GitHub Pages | — |

---

## 🔗 Links

| Resource | URL |
|---|---|
| 🌐 Live Calculator (GitHub Pages) | https://vnavodi.github.io/junit_test/ |
| ⚙️ GitHub Actions Runs | https://github.com/VNavodi/junit_test/actions |
| 💾 Repository | https://github.com/VNavodi/junit_test |
| 🔍 API Health | http://localhost:8080/api/health *(when running locally)* |
