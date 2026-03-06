package com.playwright.pages;

import org.testng.asserts.SoftAssert;

import com.microsoft.playwright.Page;
import com.playwright.elements.WebButton;
import com.playwright.elements.WebEle;
import com.playwright.elements.WebInput;
import com.playwright.framework.BasePage;

import io.qameta.allure.Step;

public class SauceLabCartPage extends BasePage {

    private WebEle addToCart;
    private WebInput receipeintName;
    private WebInput receipeintEmail;
    private WebInput yourName;
    private WebInput yourEmail;
    private WebInput message;
    private WebButton addToFinalCart;
    private WebButton shoppingCart;
    private WebEle productName;


    public SauceLabCartPage(Page page) {
        super(page, "SauceLabCartPage");
    }

    @Override
    protected boolean shouldAutoFillInputsOnInit() {
        return false;
    }

    @Step("Add gift card to cart")
    public void addGiftCardToCart() {
        logger.info("Scrolling to 'Add to Cart' button for the gift card");
        addToCart.scrollIntoView();
        logger.info("Clicking 'Add to Cart' button for the gift card");
        addToCart.click();
    }

    @Step("Fill gift card details")
    public void fillGiftCardDetails() {
        fillInputIfAvailable(receipeintName, "receipeintName");
        fillInputIfAvailable(receipeintEmail, "receipeintEmail");
        fillInputIfAvailable(yourName, "yourName");
        fillInputIfAvailable(yourEmail, "yourEmail");
        fillInputIfAvailable(message, "message");
        page.waitForTimeout(5000); // Wait for 5 seconds to ensure the details are filled before adding to cart
        addToFinalCart.click();
    }

    
    @Step("Navigate to shopping cart")
   public void navigateToShoppingCart() {
    page.waitForTimeout(5000); // Wait for 5 seconds to ensure the product is added to the cart
        shoppingCart.click();
    }

    @Step("Verify product in cart")
    public SoftAssert verifyProductInCart() {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(productName.getText(), productName.getValue(), "Product name in cart does not match expected.");
        return softAssert;
    }

    private void fillInputIfAvailable(WebInput input, String key) {
        String value = getTestDataValue(key);
        if (value != null && !value.isBlank()) {
            input.fill(value);
        }
    }
}
