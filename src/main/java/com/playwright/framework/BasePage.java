package com.playwright.framework;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import com.playwright.utils.YamlConfigLoader;
import com.playwright.utils.LoggerUtil;
import com.playwright.elements.WebInput;
import com.playwright.elements.WebButton;
import com.playwright.elements.WebSelect;
import com.playwright.elements.WebRadio;
import com.playwright.elements.WebCheckbox;
import com.playwright.elements.WebEle;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Base Page class for all page objects
 * Provides common functionality for all pages
 */
public class BasePage {
    protected Page page;
    protected Map<String, Object> locators;
    protected LoggerUtil logger;

    public BasePage(Page page, String pageName) {
        this.page = page;
        this.logger = new LoggerUtil(this.getClass());
        // load locators and test data using optional config mapping
        java.util.Map<String, Object> pageConfig = YamlConfigLoader.loadYamlFile("config/page-config.yml");
        java.util.Map<String, Object> mapping = null;
        if (pageConfig != null && pageConfig.containsKey(this.getClass().getSimpleName())) {
            Object m = pageConfig.get(this.getClass().getSimpleName());
            if (m instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> castMapping = (java.util.Map<String, Object>) m;
                mapping = castMapping;
            }
        }

        if (mapping != null && mapping.get("locator") != null) {
            String locatorPath = mapping.get("locator").toString();
            this.locators = YamlConfigLoader.loadYamlFile(locatorPath);
        } else {
            this.locators = YamlConfigLoader.loadLocators(pageName);
        }

        java.util.Map<String, Object> pageTestData;
        if (mapping != null && mapping.get("testdata") != null) {
            String testdataPath = mapping.get("testdata").toString();
            pageTestData = YamlConfigLoader.loadYamlFile(testdataPath);
        } else {
            pageTestData = YamlConfigLoader.loadTestData(pageName);
        }

        // instantiate element wrappers for declared fields matching element types
        Class<?> cls = this.getClass();
        while (cls != null && cls != BasePage.class) {
            for (Field field : cls.getDeclaredFields()) {
                try {
                    Class<?> type = field.getType();
                    String key = field.getName();
                    Object locatorValue = this.locators.get(key);
                    if (locatorValue == null) {
                        if (requiresLocator(type)) {
                            throw new RuntimeException("Locator missing for field '" + key + "' in page '" + this.getClass().getSimpleName() + "'");
                        }
                        continue;
                    }
                    Locator locator = page.locator(locatorValue.toString());
                    Object instance = null;
                    if (WebInput.class.equals(type)) {
                        instance = new WebInput(locator);
                    } else if (WebButton.class.equals(type)) {
                        instance = new WebButton(locator);
                    } else if (WebSelect.class.equals(type)) {
                        instance = new WebSelect(locator);
                    } else if (WebRadio.class.equals(type)) {
                        instance = new WebRadio(locator);
                    } else if (WebCheckbox.class.equals(type)) {
                        instance = new WebCheckbox(locator);
                    }

                    if (instance != null) {
                        field.setAccessible(true);
                        field.set(this, instance);
                        // if there's a test-data entry for this element and it's an input, pre-fill it
                        if (instance instanceof WebInput && pageTestData != null && pageTestData.containsKey(key)) {
                            Object val = pageTestData.get(key);
                            if (val != null) {
                                ((WebInput) instance).fill(val.toString());
                            }
                        }
                    }
                } catch (Exception e) {
                    // do not fail page init on reflection errors
                    logger.warn("Failed to initialize field: " + field.getName() + " - " + e.getMessage());
                }
            }
            cls = cls.getSuperclass();
        }

        // Populate simple page fields (e.g., String expectedPageTitle) from test data if present
        if (pageTestData != null) {
            Class<?> cls2 = this.getClass();
            while (cls2 != null && cls2 != BasePage.class) {
                for (Field field : cls2.getDeclaredFields()) {
                    try {
                        // only set non-wrapper String fields
                        if (field.getType().equals(String.class)) {
                            String key = field.getName();
                            if (pageTestData.containsKey(key)) {
                                Object val = pageTestData.get(key);
                                if (val != null) {
                                    field.setAccessible(true);
                                    field.set(this, val.toString());
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.warn("Failed to set page field from testdata: " + field.getName() + " - " + e.getMessage());
                    }
                }
                cls2 = cls2.getSuperclass();
            }
        }
    }

    private boolean requiresLocator(Class<?> fieldType) {
        return WebInput.class.equals(fieldType)
                || WebButton.class.equals(fieldType)
                || WebSelect.class.equals(fieldType)
                || WebRadio.class.equals(fieldType)
                || WebCheckbox.class.equals(fieldType)
                || WebEle.class.equals(fieldType);
    }

    /**
     * Get locator from YAML using key
     */
    protected Locator getLocator(String locatorKey) {
        Object value = locators.get(locatorKey);
        if (value == null) {
            throw new RuntimeException("Locator key '" + locatorKey + "' not found in YAML");
        }
        String locatorValue = value.toString();
        return page.locator(locatorValue);
    }

    /**
     * Navigate to URL
     */
    public void navigateTo(String url) {
        page.navigate(url);
    }

    /**
     * Click element
     */
    public void click(String locatorKey) {
        getLocator(locatorKey).click();
    }

    /**
     * Type text into element
     */
    public void type(String locatorKey, String text) {
        getLocator(locatorKey).fill(text);
    }

    /**
     * Get text from element
     */
    public String getText(String locatorKey) {
        String text = getLocator(locatorKey).textContent();
        return text;
    }

    /**
     * Wait for element to be visible
     */
    public void waitForElement(String locatorKey) {
        getLocator(locatorKey).waitFor();
    }

    /**
     * Check if element is visible
     */
    public boolean isElementVisible(String locatorKey) {
        try {
            return getLocator(locatorKey).isVisible();
        } catch (Exception e) {
            logger.warn("Element not visible: " + locatorKey);
            return false;
        }
    }

    /**
     * Get current page URL
     */
    public String getCurrentUrl() {
        return page.url();
    }

    /**
     * Get page title
     */
    public String getPageTitle() {
        return page.title();
    }

    /**
     * Reload page
     */
    public void reloadPage() {
        page.reload();
    }

    /**
     * Wait for element count
     */
    public int getElementCount(String locatorKey) {
        int count = getLocator(locatorKey).count();
        return count;
    }
}
