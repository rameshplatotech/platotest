# Playwright Java Automation Framework

A comprehensive test automation framework built with **Playwright**, **Java**, **Maven**, **TestNG**, and **Allure Reporting**.

## 📋 Features

✅ **Page Object Model (POM)** - Clean, maintainable page objects  
✅ **YAML Configuration** - Locators loaded dynamically from YAML files  
✅ **Test Data in YAML** - Separate test data configuration  
✅ **TestNG Suites** - Parallel execution with TestNG  
✅ **Allure Reporting** - Beautiful HTML test reports  
✅ **GitHub Actions CI/CD** - Automated test execution  
✅ **Utility Classes** - Logging, assertions, waits, and more  
✅ **Multi-browser Support** - Chrome, Firefox, Safari via Playwright  

## 📁 Project Structure

```
playwright-java-automation/
├── src/
│   ├── main/
│   │   └── java/com/playwright/
│   │       ├── framework/          # Base classes and browser management
│   │       │   ├── BasePage.java  
│   │       │   └── BrowserManager.java
│   │       ├── pages/              # Page Object Model classes
│   │       │   ├── LoginPage.java
│   │       │   └── HomePage.java
│   │       └── utils/              # Utility classes
│   │           ├── YamlConfigLoader.java
│   │           ├── LoggerUtil.java
│   │           ├── WaitUtil.java
│   │           └── AssertionUtil.java
│   └── test/
│       ├── java/com/playwright/tests/    # Test classes
│       │   ├── BaseTest.java
│       │   ├── LoginTest.java
│       │   └── DashboardTest.java
│       └── resources/
│           ├── locators/            # Locator YAML files
│           │   ├── login.yml
│           │   └── home.yml
│           ├── testdata/           # Test data YAML files
│           │   └── login_testdata.yml
│           ├── suites/             # TestNG suite files
│           │   ├── testng-suite.xml
│           │   └── testng-suite.yml
│           └── log4j2.xml          # Logging configuration
├── .github/workflows/
│   └── automation-tests.yml        # GitHub Actions CI/CD
├── pom.xml                          # Maven configuration
└── README.md                        # This file
```

## 🛠 Prerequisites

- **Java**: 11 or higher
- **Maven**: 3.6 or higher
- **Git**: For version control

## 📦 Dependencies

- **Playwright**: Browser automation
- **TestNG**: Test framework
- **SnakeYAML**: YAML parsing
- **Allure TestNG**: Test reporting
- **SLF4J + Log4j2**: Logging
- **Jackson**: JSON handling
- **Lombok**: Code generation (optional)

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone <repository-url>
cd playwright-java-automation
```

### 2. Install Dependencies
```bash
mvn clean install
```

### 3. Install Playwright Browsers
```bash
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

### 4. Run Tests
```bash
# Run all tests
mvn clean test

# Run with specific browser (chromium, firefox, webkit)
mvn clean test -Dbrowser=chromium

# Run in headless mode
mvn clean test -Dheadless=true

# Run in non-headless mode (see browser)
mvn clean test -Dheadless=false
```

### 5. Generate Allure Report
```bash
# Generate report
mvn allure:report

# Serve report (opens in browser)
mvn allure:serve
```

## 📝 YAML Configuration Files

### Locators (src/test/resources/locators/)
Locator files are named after page classes and contain CSS/XPath selectors:

```yaml
# login.yml
username_input: 'input[name="username"]'
password_input: 'input[name="password"]'
login_button: 'button[type="submit"]'
error_message: '.error-message'
```

### Test Data (src/test/resources/testdata/)
Test data files contain parameters for test cases:

```yaml
# login_testdata.yml
valid_users:
  - username: "user@example.com"
    password: "Password123"
base_url: "https://example.com"
```

### Test Suites (src/test/resources/suites/)
TestNG suite files define parallel execution:

```xml
<!-- testng-suite.xml -->
<suite name="Suite" parallel="methods" thread-count="3">
  <test name="Smoke Tests" parallel="methods" thread-count="2">
    <classes>
      <class name="com.playwright.tests.LoginTest"/>
    </classes>
  </test>
</suite>
```

## 🔧 Creating New Tests

### 1. Create a Page Object
```java
package com.playwright.pages;

public class ProductPage extends BasePage {
    public ProductPage(Page page) {
        super(page, "product");  // Matches product.yml
    }

    public void selectProduct(String productName) {
        click("product_" + productName);
    }
}
```

### 2. Create Locator YAML
```yaml
# src/test/resources/locators/product.yml
product_laptop: 'a[data-product="laptop"]'
product_phone: 'a[data-product="phone"]'
add_to_cart: 'button.add-to-cart'
```

### 3. Create Test Data YAML
```yaml
# src/test/resources/testdata/product_testdata.yml
products:
  - name: "laptop"
    price: 999.99
  - name: "phone"
    price: 699.99
```

### 4. Create Test Class
```java
@Epic("Shopping")
@Feature("Product Selection")
public class ProductTest extends BaseTest {
    
    @Test
    public void testSelectProduct() {
        navigateToUrl("https://example.com/products");
        ProductPage productPage = new ProductPage(page);
        productPage.selectProduct("laptop");
    }
}
```

## 📊 Allure Reporting

Test annotations for better reports:
```java
@Epic("Authentication")
@Feature("Login")
@Story("User should be able to login")
@Severity(SeverityLevel.CRITICAL)
@Test
public void testLogin() { }
```

## 🔄 Parallel Execution

Configure parallel execution in `testng-suite.xml`:
```xml
<suite name="Suite" parallel="methods" thread-count="5">
```

Or in method level:
```xml
<test name="Tests" parallel="classes" thread-count="3">
```

## 🌐 GitHub Actions CI/CD

The `.github/workflows/automation-tests.yml` file:
- Runs tests on every push and pull request
- Tests with Java 11 and 17
- Tests with Chromium, Firefox, and WebKit
- Generates Allure reports
- Uploads artifacts

## 🧠 CodeQL Security Scans

A dedicated `codeql` job in `.github/workflows/automation-tests.yml` performs static code analysis with GitHub CodeQL. It:
1. Checks out the repository and initializes the CodeQL database for Java
2. Builds the project (`mvn -B -DskipTests package`) so the scanner can inspect compiled classes
3. Runs `github/codeql-action/analyze@v4`, pushing alerts to the GitHub Security tab

You don’t need to supply tokens—the job uses the GitHub Actions `security-events` permission. Browse the **Security → Code scanning alerts** tab after a run to see the findings.

## 🐞 SpotBugs Static Analysis

The `spotbugs` job runs after the tests (`mvn -B -DskipTests spotbugs:check`) and uploads the generated XML report so you can inspect the bug patterns in the workflow artifacts. SpotBugs uses the Maven plugin configured with `Max` effort and `Low` threshold to surface the most meaningful issues while still keeping the build green (`failOnError=false`).

## 📐 Checkstyle Linting

The new `checkstyle` job runs `mvn -B -DskipTests checkstyle:check` after the tests and uploads `target/checkstyle-result.xml`. The plugin uses Google’s `google_checks.xml` ruleset, covers both main and test sources, and writes the report under `target/` so you can download it from the workflow artifact when you want to review violations.

## 📚 Utility Classes

### YamlConfigLoader
```java
// Load locators
Map<String, String> locators = YamlConfigLoader.loadLocators("login");

// Load test data
Map<String, Object> testData = YamlConfigLoader.loadTestData("login_testdata");
```

### LoggerUtil
```java
LoggerUtil logger = new LoggerUtil(this.getClass());
logger.info("Info message");
logger.error("Error message", exception);
```

### WaitUtil
```java
WaitUtil.waitForSeconds(2);
WaitUtil.waitForCondition(() -> isElementVisible, 30000);
```

### AssertionUtil
```java
AssertionUtil.assertEqual(actual, expected, "Message");
AssertionUtil.assertElementVisible(locator, "Element should be visible");
```

## 🐛 Troubleshooting

### Issue: Locator not found in YAML
- Check YAML file format (is it in kebab-case?)
- Verify file location in `src/test/resources/locators/`
- Check class name matches YAML file name

### Issue: Tests timing out
- Increase timeout in `WaitUtil`
- Check if element selector is correct
- Verify page is loaded

### Issue: Allure report not generating
```bash
# Clear previous reports
rm -rf allure-results/
mvn clean test
mvn allure:report
```

## 💡 Best Practices

1. **Use Page Object Model** - Keep pages separate from tests
2. **Store Locators in YAML** - Single source of truth for selectors
3. **Use Test Data Files** - Easy to manage test parameters
4. **Meaningful Test Names** - Clear description of test purpose
5. **Use Annotations** - @Epic, @Feature, @Story for reports
6. **Log Important Steps** - Help with debugging
7. **Parallel Execution** - Speed up test execution
8. **Handle Waits** - Use WaitUtil instead of hard sleeps

## 📄 License

This project is licensed under the MIT License.

## 🤝 Contributing

1. Create a feature branch
2. Make your changes
3. Push to the branch
4. Create a Pull Request

## 📧 Support

For issues or questions, please open an issue in the repository.
