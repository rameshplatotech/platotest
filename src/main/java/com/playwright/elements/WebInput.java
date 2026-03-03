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

    public WebInput fill(String text) {
        locator.fill(text);
        return this;
    }

    public WebInput clear() {
        locator.fill("");
        return this;
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

    /**
     * Allow targeting a specific input when multiple matches exist.
     */
    public WebInput nth(int index) {
        return new WebInput(locator.nth(index));
    }

    /**
     * Expose the Locator when callers need Playwright features not wrapped here.
     */
    public Locator getLocator() {
        return locator;
    }

    public void press(String key) {
        locator.press(key);
    }

    public void focus() {
        locator.focus();
    }

    public void hover() {
        locator.hover();
    }

    public boolean isEnabled() {
        return locator.isEnabled();
    }

    public void waitFor() {
        locator.waitFor();
    }
}
