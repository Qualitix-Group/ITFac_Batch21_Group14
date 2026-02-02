package com.group14.qa.ui.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;

@DefaultUrl("http://localhost:8080/ui/login")
public class LoginPage extends PageObject {

    @FindBy(name = "username")
    private WebElementFacade usernameField;

    @FindBy(name = "password")
    private WebElementFacade passwordField;

    @FindBy(css = "button[type='submit']")
    private WebElementFacade loginButton;

    @FindBy(css = ".alert.alert-danger")
    private WebElementFacade errorMessage;

    public void openLoginPage() {
        open();
    }

    // Generic login method - accepts any credentials
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    // Individual action methods
    public void enterUsername(String username) {
        usernameField.type(username);
    }

    public void enterPassword(String password) {
        passwordField.type(password);
    }

    public void clickLogin() {
        loginButton.click();
    }

    // Validation methods
    public boolean isErrorMessageDisplayed() {
        try {
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        if (isErrorMessageDisplayed()) {
            return errorMessage.getText();
        }
        return "";
    }

    public boolean isDashboardDisplayed() {
        try {
            String url = getDriver().getCurrentUrl();
            return url != null &&
                    (url.contains("/dashboard") ||
                            (url.contains("/ui/") && !url.contains("/login")));
        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrentUrl() {
        try {
            return getDriver().getCurrentUrl();
        } catch (Exception e) {
            return "";
        }
    }

    // Simple wait
    public void waitFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}