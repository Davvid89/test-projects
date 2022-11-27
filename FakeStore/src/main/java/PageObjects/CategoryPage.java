package PageObjects;

import io.netty.channel.ChannelPromiseAggregator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CategoryPage {
    public Object viewCart;
    private WebDriver driver;
    private WebDriverWait wait;
    public CategoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public CategoryPage goTo(String url) {
        driver.navigate().to(url);
        return new CategoryPage(driver);
    }
    public CategoryPage addtoCart(String productId) {
        By categoryPageAddToCartButton = By.cssSelector(".post-" + productId + ">.add_to_cart_button");
        driver.findElement(categoryPageAddToCartButton).click();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.attributeContains(categoryPageAddToCartButton, "class", "added"));
        return new CategoryPage(driver);
    }
    public CartPage viewCart(){
        By viewCartButton = By.cssSelector(".added_to_cart");
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(viewCartButton)).click();
        return new CartPage(driver);
    }
}
