import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.junit.jupiter.api.Test;

public class Motointegrator extends StartClass {

    String productURL = "https://motointegrator.com/pl/en/";

    @Test
    void ratingCheck() {
        page.navigate(productURL);

        page.click("div[class*='MostPopularServicesstyled'] img[alt='Tires change']");
        page.click("a[href$='city-lublin']");
        page.locator("section[class*='Resultsstyled__ResultsContainer']>article:nth-child(3) img").click();

        PlaywrightAssertions.assertThat((page.locator("li[data-testid='is-open-now-wrapper']>div")));
        PlaywrightAssertions.assertThat(page.locator("div[data-testid='review-header'] p:first-of-type ")).containsText("5");
    }
}
