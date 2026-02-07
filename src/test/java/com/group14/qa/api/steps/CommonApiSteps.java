package com.group14.qa.api.steps;

import io.cucumber.java.en.Given;

public class CommonApiSteps {

    @Given("admin is authenticated")
    public void admin_is_authenticated() {
        // Authentication is handled by @Before("@admin") hook in AdminAuthHook
        // No action needed here, just a placeholder step
    }

    @Given("user is authenticated")
    public void user_is_authenticated() {
        // Authentication is handled by @Before("@user") hook in UserAuthHook
        // No action needed here, just a placeholder step
    }
}