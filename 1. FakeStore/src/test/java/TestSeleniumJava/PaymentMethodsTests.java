package TestSeleniumJava;

import TestSeleniumJava.Helpers.TestStatus;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaymentMethodsTests {
    WebDriver driver;
    WebDriverWait wait;

    By checkoutButton = By.cssSelector(".checkout-button");
    By productPageAddToCartButton = By.cssSelector("button[name='add-to-cart']");
    By productPageViewCartButton = By.cssSelector(".woocommerce-message>.button");
    By countryCodeArrow = By.cssSelector(".select2-selection__arrow");
    By firstNameField = By.cssSelector("#billing_first_name");
    By lastNameField = By.cssSelector("#billing_last_name");
    By addressField = By.cssSelector("#billing_address_1");
    By postalCodeField = By.cssSelector("#billing_postcode");
    By cityField = By.cssSelector("#billing_city");
    By phoneField = By.cssSelector("#billing_phone");
    By emailField = By.cssSelector("#billing_email");
    By loadingIcon = By.cssSelector(".blockOverlay");
    By cardNumberFrame = By.cssSelector("[name='__privateStripeFrame8']");
    By cardNumberField = By.cssSelector("[name='cardnumber']");
    By expirationDateFrame = By.cssSelector("[name='__privateStripeFrame9']");
    By expirationDateField = By.cssSelector("[name='exp-date']");
    By cvcFrame = By.cssSelector("[name='__privateStripeFrame10']");
    By cvcField = By.cssSelector("[name='cvc']");

    By secureAuthorizeFrame = By.xpath(".//iframe[contains(@src, 'authorize-with-url-inner')]");
    By secondStepSecure = By.cssSelector(".AuthorizeWithUrlApp-content");
    By authorizeButton = By.cssSelector("button#test-source-authorize-3ds");
    By failButton = By.cssSelector("button#test-source-fail-3ds");

    By orderButton = By.cssSelector("#place_order");

    @RegisterExtension
    TestStatus status = new TestStatus();

    @BeforeEach
    public void testSetUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.navigate().to("https://fakestore.testelka.pl");
        driver.findElement(By.cssSelector(".woocommerce-store-notice__dismiss-link")).click();
    }

    @Test
    public void ordinarySuccessfulPaymentTest() {
        addProductAndViewCart("https://fakestore.testelka.pl/product/egipt-el-gouna/");
        driver.findElement(checkoutButton).click();

        fillOutCheckoutForm();
        fillOutCardData("378282246310005", "0225", "123");
        driver.findElement(orderButton).click();

        waitForOrderToComplete();

        int numberOfOrderReceivedMessages = driver.findElements(By.cssSelector(".woocommerce-thankyou-order-received")).size();
        int expectedNumberOfMessages = 1;
        assertTrue(expectedNumberOfMessages == numberOfOrderReceivedMessages,
                "Number of 'order received' messages is not 1. Was the payment successful?");
    }

    @Test
    public void secureSuccessfulPaymentTest() {
        addProductAndViewCart("https://fakestore.testelka.pl/product/egipt-el-gouna/");
        driver.findElement(checkoutButton).click();
        fillOutCheckoutForm();
        fillOutCardData("4000000000003220", "0225", "123");
        driver.findElement(orderButton).click();
        switchToFrame(secureAuthorizeFrame);
        switchToFrame(secondStepSecure);
        waitForOrderToComplete();

        int numberOfOrderReceivedMessages = driver.findElements(By.cssSelector(".woocommerce-thankyou-order-received")).size();
        int expectedNumberOfMessages = 1;
        assertTrue(expectedNumberOfMessages == numberOfOrderReceivedMessages,
                "Number of 'order received' messages is not 1. Was the payment successful?");
    }

    @Test
    public void secureUnsuccessfulPaymentTest() {
        addProductAndViewCart("https://fakestore.testelka.pl/product/egipt-el-gouna/");
        driver.findElement(checkoutButton).click();

        fillOutCheckoutForm();
        fillOutCardData("4000000000003220", "0225", "123");
        driver.findElement(orderButton).click();

        switchToFrame(secureAuthorizeFrame);
        switchToFrame(secondStepSecure);

        wait.until(ExpectedConditions.elementToBeClickable(failButton)).submit();
        driver.switchTo().defaultContent();

        String errorMessage = waitForErrorMessage();
        String unsuccessfulPaymentErrorMessage = "Nie można przetworzyć tej płatności, spróbuj ponownie lub użyj alternatywnej metody.";
        assertEquals(unsuccessfulPaymentErrorMessage, errorMessage,
                "Error message about unsuccesful payment has not been found.");
    }

    @Test
    public void cardDeclinedTest() {
        addProductAndViewCart("https://fakestore.testelka.pl/product/egipt-el-gouna/");
        driver.findElement(checkoutButton).click();
        fillOutCheckoutForm();

        fillOutCardData("4000008400001629", "0225", "123");
        driver.findElement(orderButton).click();

        switchToFrame(secureAuthorizeFrame);
        switchToFrame(secondStepSecure);
        wait.until(ExpectedConditions.elementToBeClickable(authorizeButton)).submit();
        driver.switchTo().defaultContent();

        String errorMessage = waitForErrorMessage();
        String cardDeclinedErrorMessage = "Karta została odrzucona.";
        assertEquals(cardDeclinedErrorMessage, errorMessage,
                "Error message about declined card has not been found. Was the card declined?");
    }

    @Test
    public void incorrectCardNumberValidationTest() {
        addProductAndViewCart("https://fakestore.testelka.pl/product/egipt-el-gouna/");
        driver.findElement(checkoutButton).click();
        fillOutCardData("4000000000003221", "0225", "123");
        driver.findElement(orderButton).click();
        String wrongCardNumberErrorMessage = waitForErrorMessage();
        String expectedErrorMessage = "Numer karty nie jest prawidłowym numerem karty kredytowej.";
        assertEquals(expectedErrorMessage, wrongCardNumberErrorMessage,
                "Error message about wrong card number has not been found.");
    }

    @Test
    public void incompleteCardNumberValidationTest() {
        addProductAndViewCart("https://fakestore.testelka.pl/product/egipt-el-gouna/");
        driver.findElement(checkoutButton).click();
        fillOutCardData("", "0225", "123");
        driver.findElement(orderButton).click();
        String incompleteCardNumberErrorMessage = waitForErrorMessage();
        String expectedErrorMessage = "Numer karty jest niekompletny.";
        assertEquals(expectedErrorMessage, incompleteCardNumberErrorMessage,
                "Error message about incomplete card number has not been found.");
    }

    @Test
    public void incompleteExpirationDateValidationTest() {
        addProductAndViewCart("https://fakestore.testelka.pl/product/egipt-el-gouna/");
        driver.findElement(checkoutButton).click();
        fillOutCardData("4000000000003220", "", "123");
        driver.findElement(orderButton).click();
        String incompleteExpirationDateErrorMessage = waitForErrorMessage();
        String expectedErrorMessage = "Data ważności karty jest niekompletna.";
        assertEquals(expectedErrorMessage, incompleteExpirationDateErrorMessage,
                "Error message about incomplete expiration date has not been found.");
    }

    @Test
    public void cardExpiredValidationTest() {
        addProductAndViewCart("https://fakestore.testelka.pl/product/egipt-el-gouna/");
        driver.findElement(checkoutButton).click();
        fillOutCardData("4000000000003220", "10/18", "123");
        driver.findElement(orderButton).click();
        String cardExpiredErrorMessage = waitForErrorMessage();
        String expectedErrorMessage = "Rok ważności karty upłynął w przeszłości";
        assertEquals(expectedErrorMessage, cardExpiredErrorMessage,
                "Error message about expired card has not been found.");
    }

    @Test
    public void incompleteCvcNumberValidationTest() {
        addProductAndViewCart("https://fakestore.testelka.pl/product/egipt-el-gouna/");
        driver.findElement(checkoutButton).click();
        fillOutCardData("4000000000003220", "10/30", "");
        driver.findElement(orderButton).click();
        String incompleteCvcNumberErrorMessage = waitForErrorMessage();
        String expectedErrorMessage = "Kod bezpieczeństwa karty jest niekompletny.";
        assertEquals(expectedErrorMessage, incompleteCvcNumberErrorMessage,
                "Error message about incomplete cvc number has not been found.");
    }

    @AfterEach
    public void closeDriver(TestInfo info) throws IOException {
        if (status.isFailed) {
            System.out.println("Test screenshot is available at: " + takeScreenshot(info));
        }
        driver.quit();
    }

    private String takeScreenshot(TestInfo info) throws IOException {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        LocalDateTime timeNow = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
        String path = "E:\\Test_Projects\\ScreenShoots\\" + info.getDisplayName() + " " + formatter.format(timeNow) + ".png";
        FileHandler.copy(screenshot, new File(path));
        return path;
    }

    private void switchToFrame(By frameLocator) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
        wait.until(d -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
    }

    private void addProductAndViewCart(String productPageUrl) {
        addProductToCart(productPageUrl);
        viewCart();
    }

    private void viewCart() {
        wait.until(ExpectedConditions.elementToBeClickable(productPageViewCartButton)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".shop_table")));
    }

    private void addProductToCart() {
        WebElement addToCartButton = driver.findElement(productPageAddToCartButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true)", addToCartButton);
        addToCartButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(productPageViewCartButton));
    }

    private void addProductToCart(String productPageUrl) {
        driver.navigate().to(productPageUrl);
        addProductToCart();
    }

    private void fillOutCheckoutForm() {
        wait.until(ExpectedConditions.elementToBeClickable(firstNameField)).sendKeys("First");
        wait.until(ExpectedConditions.elementToBeClickable(lastNameField)).sendKeys("Last");
        wait.until(ExpectedConditions.elementToBeClickable(countryCodeArrow)).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li[id *= '-PL']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(addressField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(addressField)).sendKeys("Wielicka 2 / 15");
        wait.until(ExpectedConditions.elementToBeClickable(postalCodeField)).sendKeys("80 - 001");
        wait.until(ExpectedConditions.elementToBeClickable(cityField)).sendKeys("Sopot");
        wait.until(ExpectedConditions.elementToBeClickable(phoneField)).sendKeys("696969");
        wait.until(ExpectedConditions.elementToBeClickable(emailField)).sendKeys("test1 @testelka.pl");
    }

    private void waitForOrderToComplete() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.urlContains("/zamowienie/zamowienie-otrzymane/"));
    }

    private String waitForErrorMessage() {
        By errorList = By.cssSelector("ul.woocommerce - error");
        wait.until(d -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
        return wait.until(ExpectedConditions.presenceOfElementLocated(errorList)).getText();
    }

    private void fillOutCardData(String cardNumber, String expirationDate, String cvc) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingIcon));
        driver.findElement(By.cssSelector("#terms")).click();
        switchToFrame(cardNumberFrame);
        WebElement cardNumberElement = wait.until(ExpectedConditions.elementToBeClickable(cardNumberField));

        slowType(cardNumberElement, cardNumber);
        driver.switchTo().defaultContent();
        switchToFrame(expirationDateFrame);
        WebElement expirationDateElement = wait.until(ExpectedConditions.elementToBeClickable(expirationDateField));
        slowType(expirationDateElement, expirationDate);
        driver.switchTo().defaultContent();
        switchToFrame(cvcFrame);
        WebElement cvcElement = wait.until(ExpectedConditions.elementToBeClickable(cvcField));
        slowType(cvcElement, cvc);
        driver.switchTo().defaultContent();
    }

    private void slowType(WebElement element, String text) {
        for (int i = 0; i < text.length(); i++) {
            element.sendKeys(Character.toString(text.charAt(i)));
        }
    }
}