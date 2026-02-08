package com.group14.qa.api.tests;

import com.group14.qa.api.steps.CategoryCreateAuthApiSteps;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CategoryCreateAuthApiTest {

    private final CategoryCreateAuthApiSteps steps = new CategoryCreateAuthApiSteps();

    @When("user creates a category with name {string} and parentId {int}")
    public void user_creates_category_with_parent(String name, Integer parentId) {
        steps.userTriesToCreateCategory(name, parentId.longValue());
    }

    @When("user creates a main category with name {string}")
    public void user_creates_main_category(String name) {
        steps.userTriesToCreateCategory(name, null);
    }

    @Then("create category access should be forbidden")
    public void create_category_access_should_be_forbidden() {
        steps.verifyForbiddenCreate();
    }
}
