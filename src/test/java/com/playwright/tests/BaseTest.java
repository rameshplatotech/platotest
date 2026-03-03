package com.playwright.tests;

import com.microsoft.playwright.Page;
import com.playwright.framework.BrowserManager;
import com.playwright.utils.LoggerUtil;
import io.qameta.allure.Step;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Base Test class for all test classes
 * Handles browser initialization and cleanup
 */
public class BaseTest {
    protected Page page;
    protected LoggerUtil logger;
    protected String baseUrl;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        logger = new LoggerUtil(this.getClass());
        page = BrowserManager.initializeBrowser();
        // Load suite-level base_url. Prefer a single-suite file specified by -DsuiteFile=<file.yml>
        try {
            String suiteFileProp = System.getProperty("suiteFile");
            java.util.Map<String, Object> suiteCfg = null;
            if (suiteFileProp != null && !suiteFileProp.isEmpty()) {
                String path = suiteFileProp.endsWith(".yml") ? "suites/" + suiteFileProp : "suites/" + suiteFileProp + ".yml";
                suiteCfg = com.playwright.utils.YamlConfigLoader.loadYamlFile(path);
            } else {
                suiteCfg = com.playwright.utils.YamlConfigLoader.loadYamlFile("suites/testng-suite.yml");
            }

            if (suiteCfg != null) {
                if (suiteCfg.containsKey("base_url")) {
                    this.baseUrl = suiteCfg.get("base_url").toString();
                } else if (suiteCfg.containsKey("suites")) {
                    Object s = suiteCfg.get("suites");
                    if (s instanceof java.util.List) {
                        @SuppressWarnings("unchecked")
                        java.util.List<java.util.Map<String, Object>> suites = (java.util.List<java.util.Map<String, Object>>) s;
                        if (!suites.isEmpty()) {
                            java.util.Map<String, Object> first = suites.get(0);
                            if (first.containsKey("tests")) {
                                Object tests = first.get("tests");
                                if (tests instanceof java.util.List) {
                                    @SuppressWarnings("unchecked")
                                    java.util.List<java.util.Map<String, Object>> tlist = (java.util.List<java.util.Map<String, Object>>) tests;
                                    if (!tlist.isEmpty()) {
                                        java.util.Map<String, Object> firstTest = tlist.get(0);
                                        if (firstTest.containsKey("base_url")) {
                                            this.baseUrl = firstTest.get("base_url").toString();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            // ignore
        }

        // If suite-level baseUrl was found, navigate after browser initialization
        try {
            if (this.baseUrl != null && !this.baseUrl.isEmpty()) {
                page.navigate(this.baseUrl);
            }
        } catch (Exception ignored) {
            // ignore navigation errors here; tests may perform their own navigation checks
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        BrowserManager.closeBrowser();
    }

    @Step("Navigate to URL: {url}")
    public void navigateToUrl(String url) {
        page.navigate(url);
    }
}
