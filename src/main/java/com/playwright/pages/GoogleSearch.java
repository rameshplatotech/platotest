package com.playwright.pages;

import com.microsoft.playwright.Page;
import com.playwright.framework.BasePage;

/**
 * Google Search page (minimal) — demonstrates dynamic wiring
 */
public class GoogleSearch extends BasePage {

    private com.playwright.elements.WebInput searchbox;
    private com.playwright.elements.WebButton searchbtn;
    private String searchResults;

    public GoogleSearch(Page page) {
        super(page, "GoogleSearch");
    }

    public GoogleSearch enterSearch(String text) {
        if (searchbox != null) {
            searchbox.fill(text).pressEnter();
        }
        return this;
    }

    public void clickSearchButton() {
        if (searchbtn != null) {
            searchbtn.nth(1).click();
        }
    }

    public String getSearchResults() {
        return searchResults;
    }
}
