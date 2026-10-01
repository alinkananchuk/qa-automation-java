package tests.api;

import api.BasicAuthClient;
import config.Credentials;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/** Source of truth for the Basic Auth behaviour: verifies exact HTTP status codes. */
public class BasicAuthApiTest {

    private final BasicAuthClient client = new BasicAuthClient();

    @Test(description = "Valid credentials -> 200")
    public void validCredentialsReturn200() {
        assertEquals(client.getStatusCode(Credentials.valid()), 200);
    }

    @Test(description = "No Authorization header -> 401")
    public void noCredentialsReturn401() {
        assertEquals(client.getStatusCodeWithoutAuth(), 401);
    }

    @Test(description = "Wrong password -> 401")
    public void wrongPasswordReturns401() {
        assertEquals(client.getStatusCode(Credentials.withWrongPassword()), 401);
    }

    @Test(description = "Wrong username -> 401")
    public void wrongUsernameReturns401() {
        assertEquals(client.getStatusCode(Credentials.withWrongUsername()), 401);
    }
}
