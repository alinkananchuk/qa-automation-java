package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;

public class BasicAuthPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By successMessageLocator = By.cssSelector("div.example p");

    public BasicAuthPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    //Dynamic opens URL, put credentials in line
    public void openWithCredentials(String username, String password) {
        String baseUrl = ConfigReader.getProperty("base.url");
        String path = ConfigReader.getProperty("basic.auth.path");

        String url;
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            url = baseUrl.replace("https://", "https://" + username + ":" + password + "@") + path;
        } else {
            // Если креды пустые — открываем чистый URL (проверка негативного сценария)
            url = baseUrl + path;
        }
        driver.get(url);
    }

    /**
     * Check successful message
     */
    public boolean isSuccessMessageDisplayed() {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessageLocator));
            return element.getText().contains("Congratulations! You must have the proper credentials.");
        } catch (Exception e) {
            return false;
        }
    }


}
