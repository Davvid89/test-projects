package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CartPage extends BasePage {
    private WebDriverWait wait;
    public CartPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(7));
    }
    private By shopTableLocator = By.cssSelector("form>.shop_table");
    private By productQuantityFieldLocator = By.cssSelector("div.quantity>input");
    private String removeProductButtonCssSelector = "a[data-product_id='<product_id>']";
    private By categoryItemLocator = By.cssSelector(".cart_item");
    private By updateCartButtonLocator = By.cssSelector("[name='update_cart']");
    private By loaderLocator = By.cssSelector(".blockOverlay");
    private By checkoutButtonLocator = By.cssSelector(".checkout-button");




    public int getProductQuantity() {
        waitForShopTable();
        String quantityString = driver.findElement(productQuantityFieldLocator).getAttribute("value");
        return Integer.parseInt(quantityString);
    }

    public boolean isProductInCart(String productId) {
        waitForShopTable();
        By removeProductButton = By.cssSelector(removeProductButtonCssSelector.replace("<productId>", productId));
        int productRecords = driver.findElements(removeProductButton).size();
        boolean presenceOfProduct = false;
        if(productRecords == 1){
            presenceOfProduct = true;
        } else if (productRecords>1) {
            throw new IllegalArgumentException("There is more than one record for the product in cart.");
        }
        return presenceOfProduct;
    }

    public int getNumberOfProducts() {
        waitForShopTable();
        return driver.findElements(categoryItemLocator).size();
    }

    private void waitForShopTable(){
        wait = new WebDriverWait(driver, Duration.ofSeconds(7));
        wait.until(ExpectedConditions.presenceOfElementLocated(shopTableLocator));
    }

    public CartPage changeQuantity(int quantity) {
        WebElement quantityField = driver.findElement(productQuantityFieldLocator);
        quantityField.clear();
        quantityField.sendKeys(Integer.toString(quantity));
        return this;
    }

    public CartPage updateCart() {
        WebElement updateButton = driver.findElement(updateCartButtonLocator);
        wait.until(ExpectedConditions.elementToBeClickable(updateButton));
        updateButton.click();
        return this;
    }

    public CartPage removeProduct(String productId) {
        By removeProductButton = By.cssSelector(removeProductButtonCssSelector.replace("<productId>", productId));
        driver.findElement(removeProductButton).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));
        return this;
    }

    public boolean isCartEmpty() {
        int shopTableElements = driver.findElements(shopTableLocator).size();
        if (shopTableElements == 1){
            return false;
        } else if (shopTableElements == 0) {
            return true;
        } else {
            throw new IllegalArgumentException("Wrong number of shop table elements: there can by only one or non");
        }

    }

    public CheckoutPage goToCheckout() {
        driver.findElement(checkoutButtonLocator).click();
        return new CheckoutPage(driver);
    }
}
