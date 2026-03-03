package com.playwright.utils;

import java.io.*;
import java.util.Properties;

/**
 * Configuration Manager
 * Loads and manages application configuration from properties file
 */
public class ConfigManager {
    private static final LoggerUtil logger = new LoggerUtil(ConfigManager.class);
    private static Properties properties;
    private static final String CONFIG_FILE = "config.properties";

    static {
        loadConfig();
    }

    /**
     * Load configuration from properties file
     */
    private static void loadConfig() {
        properties = new Properties();
        try (InputStream input = ConfigManager.class.getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
                logger.info("Configuration loaded successfully");
            } else {
                logger.warn("Configuration file not found: " + CONFIG_FILE);
            }
        } catch (IOException e) {
            logger.error("Error loading configuration: " + e.getMessage(), e);
        }
    }

    /**
     * Get property value
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property not found: " + key);
            return "";
        }
        return value;
    }

    /**
     * Get property value with default
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get property as integer
     */
    public static int getIntProperty(String key, int defaultValue) {
        try {
            String value = properties.getProperty(key);
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer property: " + key);
            return defaultValue;
        }
    }

    /**
     * Get property as boolean
     */
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }
}
