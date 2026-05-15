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

public class LocatorPracticeTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    // 1. Locating by ID
    @Test
    public void testFindByID() {
        driver.get("https://the-internet.herokuapp.com/login");
        WebElement username = driver.findElement(By.id("username"));
        Assert.assertTrue(username.isDisplayed());
        System.out.println("Found username field by ID");
    }

    // 2. Locating by name attribute
    @Test
    public void testFindByName() {
        driver.get("https://the-internet.herokuapp.com/login");
        WebElement password = driver.findElement(By.name("password"));
        Assert.assertTrue(password.isDisplayed());
        System.out.println("Found password field by name");
    }

    // 3. Locating by CSS selector
    @Test
    public void testFindByCssSelector() {
        driver.get("https://the-internet.herokuapp.com/login");
        WebElement loginButton = driver.findElement(By.cssSelector("button.radius"));
        Assert.assertEquals(loginButton.getText().trim(), "Login");
        System.out.println("Found login button by CSS selector");
    }

    // 4. Locating by XPath
    @Test
    public void testFindByXPath() {
        driver.get("https://the-internet.herokuapp.com/login");
        WebElement loginButton = driver.findElement(
                By.xpath("//button[@type='submit']")
        );
        Assert.assertTrue(loginButton.isDisplayed());
        System.out.println("Found login button by XPath");
    }

    // 5. Dynamic content - this is where explicit waits matter
    @Test
    public void testDynamicLoading() {
        driver.get("https://the-internet.herokuapp.com/dynamic_loading/1");

        // Click the Start button
        driver.findElement(By.cssSelector("#start button")).click();

        // Wait for the "Hello World!" text to appear (takes ~5 seconds)
        WebElement helloText = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("finish"))
        );

        Assert.assertEquals(helloText.getText(), "Hello World!");
        System.out.println("Dynamic content loaded successfully");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}