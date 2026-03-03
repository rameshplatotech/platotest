package com.playwright.elements;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.SelectOption;
import java.util.List;

/**
 * Wrapper for select elements
 */
public class WebSelect {
    private final Locator locator;

    public WebSelect(Locator locator) {
        this.locator = locator;
    }

    public void selectByValue(String value) {
        locator.selectOption(value);
    }

    public void selectByLabel(String label) {
        locator.selectOption(new SelectOption().setLabel(label));
    }

    public void selectByIndex(int index) {
        locator.selectOption(new SelectOption().setIndex(index));
    }

    public List<String> selectOptions(String... values) {
        List<String> selected = locator.selectOption(values);
        return selected;
    }

    public boolean isVisible() {
        return locator.isVisible();
    }

    /**
     * Target a specific select instance when locator matches multiple.
     */
    public WebSelect nth(int index) {
        return new WebSelect(locator.nth(index));
    }

    /**
     * Access the underlying Locator for helper methods not exposed here.
     */
    public Locator getLocator() {
        return locator;
    }

    public String getValue() {
        return locator.inputValue();
    }

    public List<String> getSelectedValues() {
        return locator.selectOption();
    }

    public void focus() {
        locator.focus();
    }

    public void hover() {
        locator.hover();
    }

    public boolean isEnabled() {
        return locator.isEnabled();
    }

    public void waitFor() {
        locator.waitFor();
    }

    public String getAttribute(String name) {
        return locator.getAttribute(name);
    }

    public void scrollIntoView() {
        locator.scrollIntoViewIfNeeded();
    }
}
