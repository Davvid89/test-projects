package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DemoFooterPage extends BasePage {
    protected DemoFooterPage(WebDriver driver) {
        super(driver);
    }

    private By demoNoticeLocator = By.cssSelector(".woocommerce-store-notice__dismiss-link");
    public void close(){
        driver.findElement(demoNoticeLocator).click();
    }
}
