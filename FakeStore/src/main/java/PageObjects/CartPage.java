package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CartPage {
    WebDriver driver;
    WebDriverWait wait;
    public CartPage(WebDriver driver) {
        this.driver = driver;
    }
    private By shopTableLocator = By.cssSelector(".shop_table");
    private By productQuantityFieldLocator = By.cssSelector("div.quantity>input");
    private  String removeProductButtonCssSelector = "a[data-product_id='<product_id>']";

    public int getProductsAmount(String productId) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(7));
        wait.until(ExpectedConditions.presenceOfElementLocated(shopTableLocator));
        By removeProductButton = By.cssSelector(removeProductButtonCssSelector.replace("<productId>", productId));
        return driver.findElements(removeProductButton).size();
    }

    public int getProductQuantity() {
        String quantityString = driver.findElement(productQuantityFieldLocator).getAttribute("value");
        return Integer.parseInt(quantityString);
    }
}
