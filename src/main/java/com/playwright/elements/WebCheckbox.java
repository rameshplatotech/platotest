package com.playwright.elements;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.PlaywrightException;

/**
 * Wrapper for checkbox elements
 */
public class WebCheckbox {
    private final Locator locator;

    public WebCheckbox(Locator locator) {
        this.locator = locator;
    }

    public void check() {
        getEffectiveLocator().check();
    }

    public void uncheck() {
        getEffectiveLocator().uncheck();
    }

    public boolean isChecked() {
        return getEffectiveLocator().isChecked();
    }

    public void toggle() {
        Locator effective = getEffectiveLocator();
        if (effective.isChecked()) {
            effective.uncheck();
        } else {
            effective.check();
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
        return getEffectiveLocator().isVisible();
    }

    public boolean isHidden() {
        return getEffectiveLocator().isHidden();
    }

    public boolean isEnabled() {
        return getEffectiveLocator().isEnabled();
    }

    public void focus() {
        getEffectiveLocator().focus();
    }

    public void hover() {
        getEffectiveLocator().hover();
    }

    public void waitFor() {
        getEffectiveLocator().waitFor();
    }

    private Locator getEffectiveLocator() {
        if (isLightningCheckbox()) {
            try {
                Locator inner = locator.locator("input");
                if (inner.count() > 0) {
                    return inner.first();
                }
            } catch (PlaywrightException ignored) {
                // fallback to root locator if inner input cannot be resolved
            }
        }
        return locator;
    }

    private boolean isLightningCheckbox() {
        try {
            Object raw = locator.evaluate("el => el.tagName ? el.tagName.toLowerCase() : null");
            String tagName = raw == null ? null : raw.toString();
            return "lightning-checkbox".equals(tagName);
        } catch (PlaywrightException e) {
            return false;
        }
    }
}
