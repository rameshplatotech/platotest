package com.playwright.elements;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.PlaywrightException;

/**
 * Wrapper for radio buttons
 */
public class WebRadio {
    private final Locator locator;

    public WebRadio(Locator locator) {
        this.locator = locator;
    }

    public void select() {
        getEffectiveLocator().check();
    }

    public boolean isSelected() {
        return getEffectiveLocator().isChecked();
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
        return getEffectiveLocator().isVisible();
    }

    public boolean isEnabled() {
        return getEffectiveLocator().isEnabled();
    }

    public void hover() {
        getEffectiveLocator().hover();
    }

    public void focus() {
        getEffectiveLocator().focus();
    }

    public void waitFor() {
        getEffectiveLocator().waitFor();
    }

    private Locator getEffectiveLocator() {
        if (isLightningRadioGroup()) {
            try {
                Locator inner = locator.locator("input");
                if (inner.count() > 0) {
                    return inner.first();
                }
            } catch (PlaywrightException ignored) {
                // fallback to root locator
            }
        }
        return locator;
    }

    private boolean isLightningRadioGroup() {
        try {
            Object raw = locator.evaluate("el => el.tagName ? el.tagName.toLowerCase() : null");
            String tagName = raw == null ? null : raw.toString();
            return "lightning-radio-group".equals(tagName);
        } catch (PlaywrightException e) {
            return false;
        }
    }
}
