package com.playwright.tests;

import org.testng.annotations.Test;
import com.playwright.pages.SauceLabCartPage;

public class SauceLabCartTest extends BaseTest {

    @Test(description = "Test adding items to cart on Sauce Labs demo site")
    public void testAddToCart() {
        logger.info("Test: testAddToCart started");
        SauceLabCartPage sauceLabCartPage = new SauceLabCartPage(page);
        logger.info("Navigating to Sauce Labs demo site");
        sauceLabCartPage.addGiftCardToCart();
        sauceLabCartPage.fillGiftCardDetails();
        //sauceLabCartPage.navigateToShoppingCart();
        //sauceLabCartPage.verifyProductInCart().assertAll();
    }
    
}
