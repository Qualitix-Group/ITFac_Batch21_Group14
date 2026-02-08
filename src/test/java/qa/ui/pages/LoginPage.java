package qa.ui.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.time.Duration;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@DefaultUrl("/ui/login")
public class LoginPage extends PageObject {

    private final By username = By.name("username");
    private final By password = By.name("password");
    private final By loginBtn  = By.cssSelector("button[type='submit']");
    private final By globalError = By.xpath("//*[contains(text(),'Invalid username or password')]");
    private final By invalidFeedback = By.cssSelector(".invalid-feedback");

    public void loginAs(String user, String pass) {
        $(username).type(user);
        $(password).type(pass);
        $(loginBtn).click();

        // Wait until the login redirect completes to avoid racing the next navigation
        new WebDriverWait(getDriver(), Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/ui/dashboard"));
    }

    public boolean hasInvalidLoginMessage() {
        return $(globalError).isVisible();
    }

    /** Submit without credentials to trigger client-side validation */
    public void submitBlank() {
        $(loginBtn).click();
    }

    public boolean hasValidationStyling() {
        // either field marked is-invalid or feedback visible with text
        boolean usernameInvalid = $(username).getAttribute("class").contains("is-invalid");
        boolean passwordInvalid = $(password).getAttribute("class").contains("is-invalid");
        boolean feedbackVisible = $(invalidFeedback).isVisible();
        return (usernameInvalid || passwordInvalid) || feedbackVisible;
    }
}
