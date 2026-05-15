import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class SauceDemoTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
    }

    @Test
    public void testLoginWithValidCredentials() {
        login("standard_user", "secret_sauce");

        WebElement inventoryContainer = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("inventory_container"))
        );
        Assert.assertTrue(inventoryContainer.isDisplayed(), "Inventory page did not load");
        System.out.println("Login with valid credentials passed!");
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        login("invalid_user", "wrong_password");

        WebElement errorMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("h3[data-test='error']")
                )
        );
        Assert.assertTrue(
                errorMessage.getText().contains("do not match"),
                "Expected error message not shown"
        );
        System.out.println("Login with invalid credentials failed as expected!");
    }

    @Test
    public void testAddItemsToCart() {
        login("standard_user", "secret_sauce");

        List<WebElement> addButtons = driver.findElements(
                By.cssSelector("button.btn_inventory")
        );
        addButtons.get(0).click();
        addButtons.get(1).click();

        WebElement cartBadge = driver.findElement(
                By.cssSelector(".shopping_cart_badge")
        );
        Assert.assertEquals(cartBadge.getText(), "2");
        System.out.println("Two items successfully added to cart!");
    }

    @Test(enabled = false)  // TODO: Investigate flaky checkout button behavior on saucedemo
    public void testCompleteCheckoutFlow() {
        login("standard_user", "secret_sauce");

        driver.findElement(By.cssSelector("button.btn_inventory")).click();
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout"))).click();

// Wait for the first-name field on the checkout info page to be ready
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name")))
                .sendKeys("Chuck");
        driver.findElement(By.id("last-name")).sendKeys("Duru");
        driver.findElement(By.id("postal-code")).sendKeys("28202");
        driver.findElement(By.id("continue")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("finish"))).click();

        WebElement completeHeader = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".complete-header")
                )
        );
        Assert.assertEquals(completeHeader.getText(), "Thank you for your order!");
        System.out.println("Full checkout flow completed successfully!");
    }

    // Helper method - all three tests above need to log in, so we extract it
    private void login(String username, String password) {
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}