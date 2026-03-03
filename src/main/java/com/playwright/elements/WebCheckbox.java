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
}
