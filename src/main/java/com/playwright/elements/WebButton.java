package com.playwright.elements;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.PlaywrightException;

/**
 * Wrapper for button elements with selective Playwright helpers
 */
public class WebButton {
    private final Locator locator;

    public WebButton(Locator locator) {
        this.locator = locator;
    }

    public void click() {
        getEffectiveLocator().click();
    }

    public void doubleClick() {
        getEffectiveLocator().dblclick();
    }

    public boolean isEnabled() {
        return getEffectiveLocator().isEnabled();
    }

    public String getText() {
        return getEffectiveLocator().textContent();
    }

    public String getInnerText() {
        return getEffectiveLocator().innerText();
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

    private Locator getEffectiveLocator() {
        if (isLightningButton()) {
            try {
                Locator inner = locator.locator("button");
                if (inner.count() > 0) {
                    return inner.first();
                }
            } catch (PlaywrightException ignored) {
                // fallback to the root locator if inner button is not reachable
            }
        }
        return locator;
    }

    private boolean isLightningButton() {
        try {
            Object raw = locator.evaluate("el => el.tagName ? el.tagName.toLowerCase() : null");
            String tagName = raw == null ? null : raw.toString();
            return "lightning-button".equals(tagName);
        } catch (PlaywrightException e) {
            return false;
        }
    }
}
