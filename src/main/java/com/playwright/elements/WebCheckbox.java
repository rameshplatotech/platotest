package com.playwright.elements;

import com.microsoft.playwright.Locator;

/**
 * Wrapper for checkbox elements
 */
public class WebCheckbox {
    private final Locator locator;

    public WebCheckbox(Locator locator) {
        this.locator = locator;
    }

    public void check() {
        locator.check();
    }

    public void uncheck() {
        locator.uncheck();
    }

    public boolean isChecked() {
        return locator.isChecked();
    }

    public void toggle() {
        if (locator.isChecked()) {
            locator.uncheck();
        } else {
            locator.check();
        }
    }

    /**
     * Access Playwright's nth helper for checkbox collections.
     */
    public WebCheckbox nth(int index) {
        return new WebCheckbox(locator.nth(index));
    }

    /**
     * Expose the raw Locator for advanced interactions.
     */
    public Locator getLocator() {
        return locator;
    }

    public boolean isVisible() {
        return locator.isVisible();
    }

    public boolean isHidden() {
        return locator.isHidden();
    }

    public boolean isEnabled() {
        return locator.isEnabled();
    }

    public void focus() {
        locator.focus();
    }

    public void hover() {
        locator.hover();
    }

    public void waitFor() {
        locator.waitFor();
    }
}
