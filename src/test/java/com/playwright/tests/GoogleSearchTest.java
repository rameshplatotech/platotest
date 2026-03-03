package com.playwright.tests;

import com.playwright.pages.GoogleSearch;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

public class GoogleSearchTest extends BaseTest {

    @Test(description = "Perform a Google search and verify results")
    @Severity(SeverityLevel.CRITICAL)
    public void testGoogleSearch() {
        logger.info("Test: testGoogleSearch started");

        // BaseTest.setup() navigates to suite base_url (Google)
        GoogleSearch gs = new GoogleSearch(page);
        gs.enterSearch("playwright java");
        gs.clickSearchButton();

        // small wait for results to load
        page.waitForTimeout(1500);

        // Basic assertion: page title should contain search term
        String title = page.title();
        assertThat(title).containsIgnoringCase("playwright");

        // verify testdata-driven field populated
        assertThat(gs.getSearchResults()).isEqualTo("Search completed");
    }
}
