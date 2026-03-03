package com.playwright.tests;

import com.microsoft.playwright.Page;
import com.playwright.framework.BrowserManager;
import com.playwright.utils.LoggerUtil;
import io.qameta.allure.Step;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Base Test class for all test classes
 * Handles browser initialization and cleanup
 */
public class BaseTest {
    protected Page page;
    protected LoggerUtil logger;
    protected String baseUrl;

    @BeforeSuite(alwaysRun = true)
    public void cleanTestNgReportResources() {
        Path jquery = Paths.get("target/surefire-reports/jquery-3.6.0.min.js");
        LoggerUtil suiteLogger = new LoggerUtil(BaseTest.class);
        try {
            Files.deleteIfExists(jquery);
        } catch (IOException e) {
            suiteLogger.warn("Could not clean TestNG reporter assets: " + e.getMessage());
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        logger = new LoggerUtil(this.getClass());
        page = BrowserManager.initializeBrowser();
        // Load suite-level base_url.
        try {
            String suiteXmlProp = System.getProperty("suiteXmlFile");
            boolean xmlLoaded = false;
            if (suiteXmlProp != null && !suiteXmlProp.isBlank()) {
                String xmlBaseUrl = resolveBaseUrlFromTestNgXml(suiteXmlProp);
                if (xmlBaseUrl != null && !xmlBaseUrl.isBlank()) {
                    this.baseUrl = xmlBaseUrl;
                    xmlLoaded = true;
                }
            }

            if (!xmlLoaded) {
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

    private String resolveBaseUrlFromTestNgXml(String suiteXmlFile) {
        try (InputStream inputStream = openSuiteXmlStream(suiteXmlFile)) {
            if (inputStream == null) {
                logger.warn("TestNG suite XML not found: " + suiteXmlFile);
                return null;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            } catch (Exception ignored) {
                // best effort to skip external DTDs
            }
            factory.setValidating(false);
            factory.setNamespaceAware(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            NodeList parameters = document.getElementsByTagName("parameter");
            for (int i = 0; i < parameters.getLength(); i++) {
                Node node = parameters.item(i);
                if (node instanceof Element) {
                    Element element = (Element) node;
                    if ("base_url".equals(element.getAttribute("name"))) {
                        return element.getAttribute("value");
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Unable to read base_url from TestNG suite XML: " + suiteXmlFile, e);
        }
        return null;
    }

    private InputStream openSuiteXmlStream(String suiteXmlFile) throws IOException {
        Path xmlPath = Paths.get(suiteXmlFile);
        if (!xmlPath.isAbsolute()) {
            xmlPath = xmlPath.toAbsolutePath().normalize();
        }
        if (Files.exists(xmlPath)) {
            return Files.newInputStream(xmlPath);
        }

        InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(suiteXmlFile);
        if (resourceStream != null) {
            return resourceStream;
        }

        String trimmed = suiteXmlFile.startsWith("/") ? suiteXmlFile.substring(1) : suiteXmlFile;
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(trimmed);
    }
}
