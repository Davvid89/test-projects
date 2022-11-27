import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.awt.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StartClass {

    Dimension ssSize = Toolkit.getDefaultToolkit().getScreenSize();
    double w = ssSize.getWidth();
    double h = ssSize.getHeight();
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeAll
    static void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void contextAndPage(){
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize((int)w, (int)h));
        page = context.newPage();
    }

    @AfterEach
    void closeContext(){
        context.close();
    }

    public String passToAccount(){
        String password = "Moje!bardzo@mocne#haslo";
        return password;
    }
}