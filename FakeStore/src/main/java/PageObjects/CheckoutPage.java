package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutPage extends BasePage {
    WebDriverWait wait;

    private By firstNameFieldLocator = By.cssSelector("#billing_first_name");
    private By lastNameFieldLocator = By.cssSelector("#billing_last_name");
    private By countryCodeArrowLocator = By.cssSelector(".select2-selection__arrow");
    private String countryCodeCssSelector = "li[id*='<countryCode>']";
    private By addressFieldLocator = By.cssSelector("#billing_address_1");
    private By postalCodeFieldLocator = By.cssSelector("#billing_postcode");
    private By cityFieldLocator = By.cssSelector("#billing_city");
    private By phoneFieldLocator = By.cssSelector("#billing_phone");
    private By emailFieldLocator = By.cssSelector("#billing_email");

    protected CheckoutPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public CheckoutPage typeName(String name) {
        wait.until(ExpectedConditions.elementToBeClickable(firstNameFieldLocator)).sendKeys(name);
        return this;
    }

    public CheckoutPage typeLastName(String lastName) {
        wait.until(ExpectedConditions.elementToBeClickable(lastNameFieldLocator)).sendKeys(lastName);
        return this;
    }


    public CheckoutPage chooseCountry(String countryCode) {
        wait.until(ExpectedConditions.elementToBeClickable(countryCodeArrowLocator)).click();
        By countryCodeLocator = By.cssSelector(countryCodeCssSelector.replace("<countryCode>", countryCode));
        wait.until(ExpectedConditions.elementToBeClickable(countryCodeLocator)).click();
        return this;
    }

    public CheckoutPage typeAddress(String address) {
        wait.until(ExpectedConditions.elementToBeClickable(addressFieldLocator)).sendKeys(address);
        return this;
    }

    public CheckoutPage typePostalCode(String postalCode) {
        wait.until(ExpectedConditions.elementToBeClickable(postalCodeFieldLocator)).sendKeys(postalCode);
        return this;
    }


    public CheckoutPage typeCity(String city) {
        wait.until(ExpectedConditions.elementToBeClickable(cityFieldLocator)).sendKeys(city);
        return this;
    }

    public CheckoutPage typePhone(String phone) {
        wait.until(ExpectedConditions.elementToBeClickable(phoneFieldLocator)).sendKeys(phone);
        return this;
    }

    public CheckoutPage typeEmail(String emailAddress) {
        wait.until(ExpectedConditions.elementToBeClickable(emailFieldLocator)).sendKeys(emailAddress);
        return this;
    }
}
