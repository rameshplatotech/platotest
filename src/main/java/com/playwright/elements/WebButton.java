package com.playwright.elements;

import com.microsoft.playwright.Locator;

/**
 * Wrapper for button elements
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
}
