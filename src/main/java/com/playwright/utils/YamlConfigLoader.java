package com.playwright.utils;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * YAML Configuration Loader
 * Loads locators, test data, and other configurations from YAML files
 */
public class YamlConfigLoader {
    private static final LoggerUtil logger = new LoggerUtil(YamlConfigLoader.class);
    private static final String LOCATORS_PATH = "locators/";
    private static final String TESTDATA_PATH = "testdata/";

    /**
     * Load locators from YAML file
     */
    public static Map<String, Object> loadLocators(String pageName) {
        String filePath = LOCATORS_PATH + pageName + ".yml";
        return loadYamlFile(filePath);
    }

    /**
     * Load test data from YAML file
     */
    public static Map<String, Object> loadTestData(String dataFileName) {
        String filePath = TESTDATA_PATH + dataFileName + ".yml";
        return loadYamlFile(filePath);
    }

    /**
     * Load any YAML file
     */
    public static Map<String, Object> loadYamlFile(String filePath) {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = YamlConfigLoader.class.getClassLoader().getResourceAsStream(filePath);
            
            if (inputStream == null) {
                throw new RuntimeException("YAML file not found: " + filePath);
            }
            
            Map<String, Object> data = yaml.load(inputStream);
            return data != null ? data : new HashMap<>();
        } catch (Exception e) {
            logger.error("Error loading YAML file: " + filePath + " - " + e.getMessage());
            throw new RuntimeException("Failed to load YAML file: " + filePath, e);
        }
    }

    /**
     * Get property from loaded YAML
     */
    public static String getProperty(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value == null) {
            logger.warn("Property not found: " + key);
            return "";
        }
        return value.toString();
    }

    /**
     * Get nested property from YAML using dot notation
     */
    @SuppressWarnings("unchecked")
    public static String getNestedProperty(Map<String, Object> data, String keyPath) {
        String[] keys = keyPath.split("\\.");
        Object current = data;

        for (String key : keys) {
            if (current instanceof Map) {
                current = ((Map<String, Object>) current).get(key);
            } else {
                return "";
            }
        }

        return current != null ? current.toString() : "";
    }
}
