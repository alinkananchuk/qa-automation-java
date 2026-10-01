package pages;

import config.ConfigReader;
import config.Credentials;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class BasicAuthPage extends BasePage {

    private static final By HEADER = By.cssSelector("div.example h3");
    private static final By MESSAGE = By.cssSelector("div.example p");

    private final ChromeDriver chrome;

    public BasicAuthPage(ChromeDriver driver) {
        super(driver);
        this.chrome = driver;
    }

    /**
     * Basic Auth is a native browser modal that Selenium cannot see or interact with,
     * so the Authorization header is sent via CDP instead of typing into the dialog.
     */
    public BasicAuthPage authorizeAs(Credentials credentials) {
        String raw = credentials.username() + ":" + credentials.password();
        String token = Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
        chrome.executeCdpCommand("Network.enable", Map.of());
        chrome.executeCdpCommand("Network.setExtraHTTPHeaders",
                Map.of("headers", Map.of("Authorization", "Basic " + token)));
        return this;
    }

    public BasicAuthPage open() {
        driver.get(ConfigReader.baseUrl() + ConfigReader.basicAuthPath());
        return this;
    }

    public String getHeader() {
        return getText(HEADER);
    }

    public String getMessage() {
        return getText(MESSAGE);
    }

    /**
     * True only if the protected content (the success message) is present on the page.
     * With invalid credentials the browser shows its native auth modal and a blank page.
     */
    public boolean isAuthorized() {
        return !driver.findElements(MESSAGE).isEmpty();
    }
}