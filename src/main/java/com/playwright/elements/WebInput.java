package com.playwright.elements;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.PlaywrightException;

/**
 * Wrapper for input-like elements (text fields, textareas)
 */
public class WebInput {
    private final Locator locator;

    public WebInput(Locator locator) {
        this.locator = locator;
    }

    public WebInput fill(String text) {
        getEffectiveLocator().fill(text);
        return this;
    }

    /**
     * Fill using the current value from the locator (e.g., lightning-input).
     */
    public WebInput fill() {
        return fill(getValue());
    }

    public WebInput clear() {
        getEffectiveLocator().fill("");
        return this;
    }

    public String getValue() {
        return getEffectiveLocator().inputValue();
    }

    public boolean isVisible() {
        return getEffectiveLocator().isVisible();
    }

    public void pressEnter() {
        getEffectiveLocator().press("Enter");
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
        getEffectiveLocator().press(key);
    }

    public void focus() {
        getEffectiveLocator().focus();
    }

    public void hover() {
        getEffectiveLocator().hover();
    }

    public boolean isEnabled() {
        return getEffectiveLocator().isEnabled();
    }

    public void waitFor() {
        getEffectiveLocator().waitFor();
    }

    private Locator getEffectiveLocator() {
        if (isLightningInput()) {
            try {
                Locator inner = locator.locator("input");
                if (inner.count() > 0) {
                    return inner.first();
                }
            } catch (PlaywrightException ignored) {
                // fallback to root locator when inner input cannot be resolved
            }
        }
        return locator;
    }

    private boolean isLightningInput() {
        try {
            Object raw = locator.evaluate("el => el.tagName ? el.tagName.toLowerCase() : null");
            String tagName = raw == null ? null : raw.toString();
            return "lightning-input".equals(tagName);
        } catch (PlaywrightException e) {
            return false;
        }
    }
}
