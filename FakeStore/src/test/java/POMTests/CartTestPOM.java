package POMTests;

import PageObjects.CategoryPage;
import PageObjects.ProductPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CartTestPOM {

    WebDriver driver;
    WebDriverWait wait;
    String productId = "386";
    String productURL = "https://fakestore.testelka.pl/product/egipt-el-gouna/";
    String categoryURL = "https://fakestore.testelka.pl/product-category/windsurfing/";

    @BeforeEach
    public void testSetUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.navigate().to("https://fakestore.testelka.pl");
        driver.manage().window().maximize();
        driver.findElement(By.cssSelector(".woocommerce-store-notice__dismiss-link")).click();
    }

    @Test
    public void addToCartFromProductPageTest() {
        ProductPage productPage = new ProductPage(driver);
        int productAmount = productPage.goTo(productURL).addToCart().viewCart().getProductsAmount(productId);

        assertTrue(productAmount == 1,
                "Remove button was not found for a product with id=386 (Egipt - El Gouna). " +
                        "Was the product added to cart?");
    }

    @Test
    public void addToCartFromCategoryPageTest() {
        CategoryPage categoryPage = new CategoryPage(driver);
        int productAmount = categoryPage.goTo(categoryURL).addtoCart(productId).viewCart().getProductsAmount(productId);
        assertTrue(productAmount == 1,
                "Remove button was not found for a product with id=386 (Egipt - El Gouna). " +
                        "Was the product added to cart?");
    }

    @Test
    public void addOneProductTenTimesTest() {
        ProductPage productPage = new ProductPage(driver);
        int productQuantity = productPage.goTo(productURL).addToCart(10).viewCart().getProductQuantity();

        assertEquals(10, productQuantity,
                "Quantity of the product is not what expected. Expected: 10, but was " + productQuantity);
    }

    @AfterEach
    public void closeDriver() {
        driver.quit();
    }
}
