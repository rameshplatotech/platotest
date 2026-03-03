package com.playwright.elements;

import com.microsoft.playwright.Locator;

/**
 * Wrapper for button elements with selective Playwright helpers
 */
public class WebButton {
    private final Locator locator;

    public WebButton(Locator locator) {
        this.locator = locator;
    }

    public void click() {
        locator.click();
    }

    public void doubleClick() {
        locator.dblclick();
    }

    public boolean isEnabled() {
        return locator.isEnabled();
    }

    public String getText() {
        return locator.textContent();
    }

    public String getInnerText() {
        return locator.innerText();
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

    public String getAttribute(String name) {
        return locator.getAttribute(name);
    }

    /**
     * Expose Playwright's nth selector helper so callers can operate on a specific match.
     */
    public WebButton nth(int index) {
        return new WebButton(locator.nth(index));
    }

    /**
     * Access the underlying Locator for advanced Playwright helpers that are not wrapped yet.
     */
    public Locator getLocator() {
        return locator;
    }
}
