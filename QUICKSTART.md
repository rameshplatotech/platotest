# Playwright Java Automation Framework - Getting Started Guide

## Quick Start

### 1. Prerequisites
- Java 11+ installed
- Maven 3.6+ installed
- Git installed

### 2. Project Setup

```bash
# Navigate to project directory
cd playwright-java-automation

# Install dependencies
mvn clean install

# Install Playwright browsers
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

### 3. Run Tests

```bash
# Run all tests in parallel
mvn clean test

# Run with specific browser
mvn clean test -Dbrowser=firefox

# Run in non-headless mode
mvn clean test -Dheadless=false

# Generate Allure report
mvn allure:report

# View Allure report
mvn allure:serve
```

## Project Structure Overview

- **Framework Classes** (`src/main/java/com/playwright/framework/`)
  - `BasePage.java` - Base class for all page objects
  - `BrowserManager.java` - Browser initialization and lifecycle

- **Page Objects** (`src/main/java/com/playwright/pages/`)
  - `LoginPage.java` - Login page implementation
  - `HomePage.java` - Home/Dashboard page implementation

- **Utilities** (`src/main/java/com/playwright/utils/`)
  - `YamlConfigLoader.java` - Load YAML configurations
  - `LoggerUtil.java` - Logging functionality
  - `WaitUtil.java` - Wait mechanisms
  - `AssertionUtil.java` - Custom assertions

- **Test Classes** (`src/test/java/com/playwright/tests/`)
  - `BaseTest.java` - Base test class with setup/teardown
  - `LoginTest.java` - Login test scenarios
  - `DashboardTest.java` - Dashboard test scenarios

- **Configurations**
  - `src/test/resources/locators/` - YAML files with page locators
  - `src/test/resources/testdata/` - YAML files with test data
  - `src/test/resources/suites/` - TestNG suite configurations

## Adding New Tests

### Step 1: Create Locator YAML File
Create `src/test/resources/locators/productpage.yml`:
```yaml
product_name: '.product-title'
product_price: '.product-price'
add_to_cart: 'button.add-to-cart'
quantity: 'input[name="quantity"]'
```

### Step 2: Create Page Class
Create `src/main/java/com/playwright/pages/ProductPage.java`:
```java
package com.playwright.pages;

import com.microsoft.playwright.Page;
import com.playwright.framework.BasePage;

public class ProductPage extends BasePage {
    public ProductPage(Page page) {
        super(page, "productpage");
    }

    public String getProductName() {
        return getText("product_name");
    }

    public void addToCart() {
        click("add_to_cart");
    }
}
```

### Step 3: Create Test Data YAML
Create `src/test/resources/testdata/product_testdata.yml`:
```yaml
test_product: "Laptop"
expected_price: "$999.99"
quantity: 2
```

### Step 4: Create Test Class
Create `src/test/java/com/playwright/tests/ProductTest.java`:
```java
package com.playwright.tests;

import com.playwright.pages.ProductPage;
import com.playwright.utils.YamlConfigLoader;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import java.util.Map;

@Epic("Shopping")
@Feature("Product Management")
public class ProductTest extends BaseTest {

    @Test(description = "Add product to cart")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddToCart() {
        navigateToUrl("https://example.com/products");
        
        Map<String, Object> testData = YamlConfigLoader.loadTestData("product_testdata");
        
        ProductPage productPage = new ProductPage(page);
        String productName = productPage.getProductName();
        
        logger.info("Product: " + productName);
        productPage.addToCart();
    }
}
```

## Parallel Execution

Edit `src/test/resources/suites/testng-suite.xml` to set thread count:

```xml
<suite name="Suite" parallel="methods" thread-count="5">
    <test name="Product Tests" parallel="methods" thread-count="3">
        <classes>
            <class name="com.playwright.tests.ProductTest"/>
        </classes>
    </test>
</suite>
```

## Logging

Logs are configured in `src/test/resources/log4j2.xml`:
- Console output
- Rolling file output to `logs/automation.log`
- Separate logging levels for different packages

## CI/CD Integration

GitHub Actions workflow is configured in `.github/workflows/automation-tests.yml`:
- Runs on every push and pull request
- Tests with multiple Java versions and browsers
- Generates and publishes Allure reports

## Common Commands

```bash
# Clean and build
mvn clean install

# Run tests with verbose output
mvn clean test -X

# Skip tests during build
mvn clean install -DskipTests

# Run specific test class
mvn clean test -Dtest=LoginTest

# Run specific test method
mvn clean test -Dtest=LoginTest#testValidLogin

# Generate Allure report
mvn allure:report

# View Allure report in browser
mvn allure:serve

# Check for errors
mvn compile
```

## Browser Options

Use system properties to control browser:

```bash
# Set browser type
mvn test -Dbrowser=chromium    # chromium, firefox, webkit
mvn test -Dheadless=false      # See browser window
```

## Troubleshooting

1. **Playwright browsers not found**: Run `mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"`

2. **YAML file not found**: Ensure file is in `src/test/resources/` and referenced correctly

3. **Locator not working**: Verify the CSS/XPath selector in the YAML file

4. **Tests timeout**: Increase wait time or check if element exists

## Next Steps

1. Customize locators in YAML files for your application
2. Create page objects for all pages in your application
3. Create test data YAML files
4. Write test classes extending BaseTest
5. Run tests locally and in CI/CD
6. Review Allure reports for test execution details
