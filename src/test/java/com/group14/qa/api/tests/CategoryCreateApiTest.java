package com.group14.qa.api.tests;

import com.group14.qa.api.steps.CategoryCreateApiSteps;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CategoryCreateApiTest {

    private final CategoryCreateApiSteps steps = new CategoryCreateApiSteps();

    @When("admin creates a category with name {string} and parentId {int}")
    public void admin_creates_category_with_parent(String name, Integer parentId) {
        steps.adminCreatesCategory(name, parentId.longValue());
    }

    @When("admin creates a main category with name {string}")
    public void admin_creates_main_category(String name) {
        steps.adminCreatesCategory(name, null);
    }

    @Then("category should be created successfully with name {string}")
    public void category_should_be_created_successfully(String expectedName) {
        steps.verifyCategoryCreated(expectedName);
    }

    @Then("create category error status should be {int}")
    public void create_category_error_status_should_be(Integer status) {
        steps.verifyCreateCategoryError(status);
    }
}
