package com.playwright.elements;

import com.microsoft.playwright.Locator;

/**
 * Wrapper for input-like elements (text fields, textareas)
 */
public class WebInput {
    private final Locator locator;

    public WebInput(Locator locator) {
        this.locator = locator;
    }

    public void fill(String text) {
        locator.fill(text);
    }

    public void clear() {
        locator.fill("");
    }

    public String getValue() {
        return locator.inputValue();
    }

    public boolean isVisible() {
        return locator.isVisible();
    }

    public void pressEnter() {
        locator.press("Enter");
    }
}
