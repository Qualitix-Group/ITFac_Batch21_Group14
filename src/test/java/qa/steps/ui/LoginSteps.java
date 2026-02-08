package qa.steps.ui;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Managed;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import qa.ui.pages.LoginPage;
import qa.utils.TestData;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class LoginSteps {

    @Managed
    WebDriver driver;

    LoginPage loginPage;

    @Given("I am logged in as admin")
    public void iAmLoggedInAsAdmin() {
        loginPage.open();
        loginPage.loginAs(
            TestData.get("admin.username"),
            TestData.get("admin.password")
        );
    }

    @Given("I am logged in as user")
    public void iAmLoggedInAsUser() {
        loginPage.open();
        loginPage.loginAs(
            TestData.get("user.username"),
            TestData.get("user.password")
        );
    }

    @When("I open the login page")
    public void iOpenTheLoginPage() {
        loginPage.open();
    }

    @When("I submit the login form blank")
    public void iSubmitTheLoginFormBlank() {
        loginPage.submitBlank();
    }

    @Then("I should see validation styling on the form")
    public void iShouldSeeValidationStylingOnTheForm() {
        assertTrue(loginPage.hasValidationStyling(),
            "Invalid feedback should be visible and inputs marked invalid");
    }

    @When("my session expires")
    public void mySessionExpires() {
        driver.manage().deleteAllCookies();
    }

    @Then("I should be redirected to the login page")
    public void iShouldBeRedirectedToTheLoginPage() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.urlContains("/ui/login"));
        assertTrue(driver.getCurrentUrl().contains("/ui/login"),
            "User should be redirected to login page");
    }
}
