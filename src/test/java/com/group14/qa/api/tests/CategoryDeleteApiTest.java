package com.group14.qa.api.tests;

import com.group14.qa.api.steps.CategoryDeleteApiSteps;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CategoryDeleteApiTest {

    private final CategoryDeleteApiSteps steps = new CategoryDeleteApiSteps();

    @When("user deletes category with id {int}")
    public void user_deletes_category_with_id(Integer id) {
        steps.userDeletesCategory(id.longValue());
    }

    @Then("delete access should be forbidden")
    public void delete_access_should_be_forbidden() {
        steps.verifyForbiddenDelete();
    }
}
