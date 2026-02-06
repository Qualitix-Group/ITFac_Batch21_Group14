package com.group14.qa.ui.steps;

import com.group14.qa.ui.pages.LoginPage;
import io.cucumber.java.en.Given;
import net.serenitybdd.core.Serenity;
import static org.assertj.core.api.Assertions.assertThat;

public class CommonSteps {

    private LoginPage loginPage;

    // Admin login (already working)
    @Given("I am logged in as an admin user")
    public void i_am_logged_in_as_an_admin_user() {
        loginPage.open();
        loginPage.enterUsername("admin");
        loginPage.enterPassword("admin123"); // Use your actual admin password
        loginPage.clickLogin();

        // Wait and verify login
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertThat(loginPage.getDriver().getCurrentUrl()).doesNotContain("/login");
        Serenity.takeScreenshot();
    }

    // ADD THIS METHOD for regular user login
    @Given("I am logged in as a regular user")
    public void i_am_logged_in_as_a_regular_user() {
        loginPage.open();
        loginPage.enterUsername("testuser"); // Use your regular user username
        loginPage.enterPassword("test123"); // Use your regular user password
        loginPage.clickLogin();

        // Wait and verify login
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertThat(loginPage.getDriver().getCurrentUrl()).doesNotContain("/login");
        Serenity.takeScreenshot();
    }

    @Given("I open the application")
    public void i_open_the_application() {
        loginPage.open();
    }
}