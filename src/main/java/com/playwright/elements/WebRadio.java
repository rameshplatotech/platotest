package com.playwright.elements;

import com.microsoft.playwright.Locator;

/**
 * Wrapper for radio buttons
 */
public class WebRadio {
    private final Locator locator;

    public WebRadio(Locator locator) {
        this.locator = locator;
    }

    public void select() {
        locator.check();
    }

    public boolean isSelected() {
        return locator.isChecked();
    }

    /**
     * Target a specific radio button by index.
     */
    public WebRadio nth(int index) {
        return new WebRadio(locator.nth(index));
    }

    /**
     * Return the underlying Locator for advanced helpers.
     */
    public Locator getLocator() {
        return locator;
    }

    public boolean isVisible() {
        return locator.isVisible();
    }

    public boolean isEnabled() {
        return locator.isEnabled();
    }

    public void hover() {
        locator.hover();
    }

    public void focus() {
        locator.focus();
    }

    public void waitFor() {
        locator.waitFor();
    }
}
