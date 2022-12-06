package POMTests;

import PageObjects.CartPage;
import PageObjects.CheckoutPage;
import PageObjects.ProductPage;
import org.junit.jupiter.api.Test;

public class PaymentTestsPOM extends BaseTest {

    private String name= "myName";
    private String lastName= "lastName";
    private String countryCode = "PL";
    private String address = "PL";
    private String postalCode = "PL";
    private String city = "PL";
    private String phone = "PL";
    private String emailAddress = "PL";


    @Test
    public void butWithoutAccountTest() {
        ProductPage productPage = new ProductPage(driver).goTo("https://fakestore.testelka.pl/product/egipt-el-gouna/");
        productPage.demoNotice.close();
        CartPage cartPage = productPage.addToCart().viewCart();
        CheckoutPage checkoutPage = cartPage.goToCheckout();
        checkoutPage.typeName(name)
                .typeLastName(lastName)
                .chooseCountry(countryCode)
                .typeAddress(address)
                .typePostalCode(postalCode)
                .typeCity(city)
                .typePhone(phone)
                .typeEmail(emailAddress);

    }
}
