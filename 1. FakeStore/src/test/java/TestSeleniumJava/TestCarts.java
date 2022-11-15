package TestSeleniumJava;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestCarts {
    WebDriver driver;
    WebDriverWait wait;
    String productId = "386";
    By productPageAddToCartButton = By.cssSelector("button[name='add-to-cart']");
    By categoryPageAddToCartButton = By.cssSelector(".post-" + productId + ">.add_to_cart_button");
    By removeProductButton = By.cssSelector("a[data-product_id='" + productId + "']");
    By productPageViewCartButton = By.cssSelector(".woocommerce-message>.button");
    By shopTable = By.cssSelector(".shop_table");
    By cartQuantityField = By.cssSelector("input.qty");
    By updateCartButton = By.cssSelector("[name='update_cart']");
    String[] productPages = {"/egipt-el-gouna/", "/wspinaczka-via-ferraty/", "/wspinaczka-island-peak/",
            "/fuerteventura-sotavento/", "/grecja-limnos/", "/windsurfing-w-karpathos/",
            "/wyspy-zielonego-przyladka-sal/", "/wakacje-z-yoga-w-kraju-kwitnacej-wisni/",
            "/wczasy-relaksacyjne-z-yoga-w-toskanii/", "/yoga-i-pilates-w-hiszpanii/"};

    @BeforeEach
    public void testSetUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.navigate().to("https://fakestore.testelka.pl");
        driver.findElement(By.cssSelector(".woocommerce-store-notice__dismiss-link")).click();
        driver.manage().window().maximize();

    }

    @Test
    public void addToCartFromProductPageTest() {
        addProductAndViewCart("https://fakestore.testelka.pl/product/egipt-el-gouna/");
        assertTrue(driver.findElements(removeProductButton).size() == 1,
                "Remove button was not found for a product with id=386 (Egipt - El Gouna). " +
                        "Was the product added to cart?");
    }

    @Test
    public void addToCartFromCategoryPageTest() {
        driver.navigate().to("https://fakestore.testelka.pl/product-category/windsurfing/");
        driver.findElement(categoryPageAddToCartButton).click();
        By viewCartButton = By.cssSelector(".added_to_cart");
        wait.until(ExpectedConditions.elementToBeClickable(viewCartButton));
        driver.findElement(viewCartButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(shopTable));
        assertTrue(driver.findElements(removeProductButton).size() == 1,
                "Remove button was not found for a product with id=386 (Egipt - El Gouna). " +
                        "Was the product added to cart?");
    }

    @Test
    public void addOneProductTenTimesTest() {
        addProductAndViewCart("https://fakestore.testelka.pl/product/egipt-el-gouna/", "10");
        String quantityString = driver.findElement(By.cssSelector("div.quantity>input")).getAttribute("value");
        int quantity = Integer.parseInt(quantityString);
        assertEquals(10, quantity,
                "Quantity of the product is not what expected. Expected: 10, but was " + quantity);
    }

    @Test
    public void addTenProductsToCartTest() {
        for (String productPage : productPages) {
            addProductToCart("https://fakestore.testelka.pl/product" + productPage);
        }
        viewCart();
        int numberOfItems = driver.findElements(By.cssSelector(".cart_item")).size();
        assertEquals(10, numberOfItems,
                "Number of items in the cart is not correct. Expected: 10, but was: " + numberOfItems);
    }

    @Test
    public void changeNumberOfProductsTest() {
        addProductAndViewCart("https://fakestore.testelka.pl/product/egipt-el-gouna/");
        WebElement quantityField = driver.findElement(cartQuantityField);
        quantityField.clear();
        quantityField.sendKeys("8");
        WebElement updateButton = driver.findElement(updateCartButton);
        wait.until(ExpectedConditions.elementToBeClickable(updateButton));
        updateButton.click();
        String quantityString = driver.findElement(By.cssSelector("div.quantity>input")).getAttribute("value");
        int quantity = Integer.parseInt(quantityString);
        assertEquals(8, quantity,
                "Quantity of the product is not what expected. Expected: 2, but was " + quantity);
    }

    @Test
    public void removePositionFromCartTest() {
        addProductAndViewCart("https://fakestore.testelka.pl/product/egipt-el-gouna/");
        driver.findElement(removeProductButton).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".blockOverlay")));
        int numberOfEmptyCartMessages = driver.findElements(By.cssSelector("p.cart-empty")).size();
        assertEquals(1, numberOfEmptyCartMessages,
                "One message about empty cart was expected, but found " + numberOfEmptyCartMessages);
    }

    @AfterEach
    public void closeDriver() {
        driver.quit();
    }

    private void addProductToCart() {
        WebElement addToCartButton = driver.findElement(productPageAddToCartButton);
        addToCartButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(productPageViewCartButton));
    }

    private void addProductToCart(String productPageUrl) {
        driver.navigate().to(productPageUrl);
        addProductToCart();
    }

    private void addProductToCart(String productPageUrl, String quantity) {
        driver.navigate().to(productPageUrl);
        WebElement quantityField = driver.findElement(By.cssSelector("input.qty"));
        quantityField.clear();
        quantityField.sendKeys(quantity);
        addProductToCart();
    }

    private void viewCart() {
        wait.until(ExpectedConditions.elementToBeClickable(productPageViewCartButton)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(shopTable));
    }

    private void addProductAndViewCart(String productPageUrl) {
        addProductToCart(productPageUrl);
        viewCart();
    }

    private void addProductAndViewCart(String productPageUrl, String quantity) {
        addProductToCart(productPageUrl, quantity);
        viewCart();
    }

}

