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
}
