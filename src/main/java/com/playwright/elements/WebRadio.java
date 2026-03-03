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
}
