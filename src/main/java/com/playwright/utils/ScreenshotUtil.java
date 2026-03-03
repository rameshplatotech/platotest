package com.playwright.utils;

import com.microsoft.playwright.Page;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Screenshot and Report Utility
 * Handles screenshot capture for test reports
 */
public class ScreenshotUtil {
    private static final LoggerUtil logger = new LoggerUtil(ScreenshotUtil.class);
    private static final String SCREENSHOT_DIR = "target/screenshots";

    /**
     * Take screenshot and save to file
     */
    public static String takeScreenshot(Page page, String testName) {
        try {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String filename = testName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + "/" + filename;
            
            page.screenshot(new Page.ScreenshotOptions().setPath(java.nio.file.Paths.get(filePath)));
            return filePath;
        } catch (Exception e) {
            logger.error("Failed to capture screenshot: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Take screenshot with custom directory
     */
    public static String takeScreenshot(Page page, String testName, String directory) {
        try {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String filename = testName + "_" + timestamp + ".png";
            String filePath = directory + "/" + filename;
            
            page.screenshot(new Page.ScreenshotOptions().setPath(java.nio.file.Paths.get(filePath)));
            return filePath;
        } catch (Exception e) {
            logger.error("Failed to capture screenshot: " + e.getMessage(), e);
            return null;
        }
    }
}
