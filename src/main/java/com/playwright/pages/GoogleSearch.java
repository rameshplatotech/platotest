package com.playwright.pages;

import com.microsoft.playwright.Page;
import com.playwright.elements.WebButton;
import com.playwright.elements.WebInput;
import com.playwright.framework.BasePage;

/**
 * Google Search page (minimal) — demonstrates dynamic wiring
 */
public class GoogleSearch extends BasePage {

    private WebInput searchbox;
    private WebButton searchbtn;
    private String searchResults;

    public GoogleSearch(Page page) {
        super(page, "GoogleSearch");
    }

    public GoogleSearch enterSearch(String text) {
        if (searchbox != null) {
            searchbox.fill(text).pressEnter();
            page.waitForLoadState();
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
