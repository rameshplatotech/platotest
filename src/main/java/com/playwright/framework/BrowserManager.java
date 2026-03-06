package com.playwright.framework;

import com.microsoft.playwright.*;
import com.playwright.utils.LoggerUtil;
import com.playwright.utils.ConfigManager;

/**
 * Browser Manager class to handle browser initialization and management
 */
public class BrowserManager {
    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;
    private static Page page;
    private static final LoggerUtil logger = new LoggerUtil(BrowserManager.class);

    private static final String BROWSER_TYPE = System.getProperty("browser", ConfigManager.getProperty("browser.type", "chromium"));
    private static final boolean HEADLESS;

    static {
        String headlessProperty = System.getProperty("headless");
        if (headlessProperty == null) {
            HEADLESS = ConfigManager.getBooleanProperty("browser.headless", true);
        } else {
            HEADLESS = Boolean.parseBoolean(headlessProperty);
        }
    }

    /**
     * Initialize browser
     */
    public static Page initializeBrowser() {
        try {
            playwright = Playwright.create();
        } catch (Exception e) {
            logger.error("Error creating Playwright instance: " + e.getMessage());
            throw new RuntimeException("Failed to create Playwright instance", e);
        }
        
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(HEADLESS);
        
        try {
            browser = switch (BROWSER_TYPE.toLowerCase()) {
                case "firefox" -> {
                    yield playwright.firefox().launch(launchOptions);
                }
                case "webkit" -> {
                    yield playwright.webkit().launch(launchOptions);
                }
                default -> {
                    yield playwright.chromium().launch(launchOptions);
                }
            };
        } catch (Exception e) {
            logger.error("Error launching browser: " + e.getMessage());
            logger.warn("Falling back to Chromium browser");
            try {
                browser = playwright.chromium().launch(launchOptions);
            } catch (Exception chromiumError) {
                logger.error("Failed to launch Chromium: " + chromiumError.getMessage());
                throw new RuntimeException("Failed to initialize browser", chromiumError);
            }
        }
        
        browserContext = browser.newContext();
        page = browserContext.newPage();
        
        return page;
    }

    /**
     * Get current page
     */
    public static Page getPage() {
        if (page == null) {
            return initializeBrowser();
        }
        return page;
    }

    /**
     * Close browser
     */
    public static void closeBrowser() {
        try {
            if (page != null) {
                try { page.close(); } catch (Exception ignored) { }
            }
            if (browserContext != null) {
                try { browserContext.close(); } catch (Exception ignored) { }
            }
            if (browser != null) {
                try { browser.close(); } catch (Exception ignored) { }
            }
            if (playwright != null) {
                try { playwright.close(); } catch (Exception ignored) { }
            }
        } catch (Exception ignored) {
            // ignore cleanup errors
        } finally {
            page = null;
            browserContext = null;
            browser = null;
            playwright = null;
        }
    }

    /**
     * Reset browser context (clear cookies, cache, etc)
     */
    public static void resetBrowserContext() {
        if (browserContext != null) {
            browserContext.close();
        }
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }
}
