package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.BasicAuthPage;
import utils.ConfigReader;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

public class BasicAuthTest {
    private WebDriver driver;
    private BasicAuthPage basicAuthPage;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        basicAuthPage = new BasicAuthPage(driver);
    }

    /**
     * DataProvider for generation test data
     */
    @DataProvider(name = "authScenarios")
    public Object[][] provideAuthData() {
        String validUser = ConfigReader.getProperty("valid.username");
        String validPass = ConfigReader.getProperty("valid.password");

        return new Object[][] {
                { validUser, validPass, true  }, // TC1: Valid credentials
                { "hacker",  validPass, false }, // TC2: Wrong email
                { validUser, "123456",  false }, // TC3: Wrong password
                { "",        "",        false }  // TC4: Empty credentials
        };
    }

    @Test(dataProvider = "authScenarios", description = "Comprehensive test for Basic Auth with positive and negative cases")
    public void testBasicAuthMatrix(String username, String password, boolean expectedSuccess) {
        // Step 1: Open page with credentials
        basicAuthPage.openWithCredentials(username, password);

        // Step 2: Check results
        boolean isSuccess = basicAuthPage.isSuccessMessageDisplayed();

        if (expectedSuccess) {
            assertTrue(isSuccess, "FAILED: Access should be granted for valid credentials!");
        } else {
            assertFalse(isSuccess, "FAILED: Access should be DENIED for invalid/empty credentials!");
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
