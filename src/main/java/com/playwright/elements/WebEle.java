package com.playwright.elements;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.MouseButton;
import com.microsoft.playwright.options.WaitForSelectorState;

/**
 * Lightweight wrapper around any Playwright Locator for common helper operations.
 */
public class WebEle {
    private final Locator locator;

    public WebEle(Locator locator) {
        this.locator = locator;
    }

    public WebEle click() {
        locator.click();
        return this;
    }

    public WebEle doubleClick() {
        locator.dblclick();
        return this;
    }

    public WebEle rightClick() {
        locator.click(new Locator.ClickOptions().setButton(MouseButton.RIGHT));
        return this;
    }

    public String getText() {
        return locator.textContent();
    }

    public String getValue() {
        return locator.inputValue();
    }

    public String getAttribute(String name) {
        return locator.getAttribute(name);
    }

    public WebEle hover() {
        locator.hover();
        return this;
    }

    public WebEle focus() {
        locator.focus();
        return this;
    }

    public WebEle scrollIntoView() {
        locator.scrollIntoViewIfNeeded();
        return this;
    }

    public WebEle waitFor() {
        locator.waitFor();
        return this;
    }

    public WebEle waitFor(WaitForSelectorState state) {
        locator.waitFor(new Locator.WaitForOptions().setState(state));
        return this;
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

    public boolean isDisabled() {
        return locator.isDisabled();
    }

    public int count() {
        return locator.count();
    }

    public WebEle nth(int index) {
        return new WebEle(locator.nth(index));
    }

    public Locator getLocator() {
        return locator;
    }
}
