package com.playwright.utils;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import java.util.function.BooleanSupplier;

/**
 * Wait Utility class for handling waits
 */
public class WaitUtil {
    private static final int DEFAULT_WAIT_TIME = 30000; // 30 seconds in milliseconds

    /**
     * Wait for page to load
     */
    public static void waitForPageLoad(Page page) {
        page.waitForLoadState();
    }

    /**
     * Wait for element to be visible
     */
    public static void waitForElementVisible(Locator locator) {
        locator.waitFor();
    }

    /**
     * Wait for element to be visible with custom timeout
     */
    public static void waitForElementVisible(Locator locator, int timeoutMs) {
        locator.waitFor(new Locator.WaitForOptions().setTimeout(timeoutMs));
    }

    /**
     * Wait for condition
     */
    public static void waitForCondition(BooleanSupplier condition, int timeoutMs) {
        long startTime = System.currentTimeMillis();
        while (!condition.getAsBoolean()) {
            if (System.currentTimeMillis() - startTime > timeoutMs) {
                throw new RuntimeException("Condition not met within timeout period");
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Wait interrupted", e);
            }
        }
    }

    /**
     * Wait for condition with default timeout
     */
    public static void waitForCondition(BooleanSupplier condition) {
        waitForCondition(condition, DEFAULT_WAIT_TIME);
    }

    /**
     * Wait for time (in milliseconds)
     */
    public static void waitForTime(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Wait interrupted", e);
        }
    }

    /**
     * Wait for time (in seconds)
     */
    public static void waitForSeconds(int seconds) {
        waitForTime(seconds * 1000L);
    }
}
