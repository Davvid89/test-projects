package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CategoryPage extends BasePage {
    public DemoFooterPage demoNotice;
    private WebDriverWait wait;
    public CategoryPage(WebDriver driver) {
        super(driver);
        demoNotice = new DemoFooterPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(7));
    }

    private By viewCartButtonLocator = By.cssSelector(".added_to_cart");
    private String addToCartButtonCssSelector = ".post-<product_id>>.add_to_cart_button";



    public CategoryPage goTo(String url) {
        driver.navigate().to(url);
        return new CategoryPage(driver);
    }
    public CategoryPage addToCart(String productId) {
        By addToCartButton = By.cssSelector(addToCartButtonCssSelector.replace("<product_id>", productId));
        driver.findElement(addToCartButton).click();
        wait.until(ExpectedConditions.attributeContains(addToCartButton, "class", "added"));
        return this;
    }
    public CartPage viewCart(){
        wait.until(ExpectedConditions.elementToBeClickable(viewCartButtonLocator)).click();
        return new CartPage(driver);
    }
}
