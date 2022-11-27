package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductPage {
    private WebDriver driver;
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(7));
    public ProductPage(WebDriver driver) {
        this.driver = driver;
    }

    private By addToCartButtonLocator = By.cssSelector("button[name='add-to-cart']");
    private By viewCartButtonLocator = By.cssSelector(".woocommerce-message>.button");
    private  By productQuantityFieldLocator = By.cssSelector("input.qty");

    public ProductPage goTo(String productURL) {
        driver.navigate().to(productURL);
        return new ProductPage(driver);
    }

    public ProductPage addToCart() {
        WebElement addButton = driver.findElement(addToCartButtonLocator);
        addButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(viewCartButtonLocator));
        return new ProductPage(driver);
    }

    public CartPage viewCart() {
        wait.until(ExpectedConditions.elementToBeClickable(viewCartButtonLocator)).click();
        return new CartPage(driver);
    }

    public ProductPage addToCart(int quantity) {
        WebElement quantityField = driver.findElement(productQuantityFieldLocator);
        quantityField.clear();
        quantityField.sendKeys(String.valueOf(quantity));
        return addToCart();

    }
}
