package tests.ui;

import config.Credentials;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.BasicAuthPage;
import tests.base.BaseTest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class BasicAuthUiTest extends BaseTest {

    @Test(description = "Successful authorization with valid credentials")
    public void successfulAuth() {
        BasicAuthPage page = new BasicAuthPage(driver)
                .authorizeAs(Credentials.valid())
                .open();

        assertEquals(page.getHeader(), "Basic Auth");
        assertTrue(page.getMessage().contains("Congratulations! You must have the proper credentials."),
                "Success message is missing");
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][]{
                {Credentials.withWrongPassword()},
                {Credentials.withWrongUsername()},
                {Credentials.wrong()}
        };
    }

    @Test(dataProvider = "invalidCredentials",
            description = "Invalid credentials: protected content is not shown")
    public void protectedContentIsHiddenForInvalidCredentials(Credentials credentials) {
        BasicAuthPage page = new BasicAuthPage(driver)
                .authorizeAs(credentials)
                .open();


        assertFalse(page.isAuthorized(),
                "Protected content must not be shown for " + credentials);
    }
}