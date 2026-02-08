package com.group14.qa.api.tests;

import com.group14.qa.api.steps.CategoryUpdateApiSteps;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CategoryUpdateApiTest {

    private final CategoryUpdateApiSteps steps = new CategoryUpdateApiSteps();

    @When("admin updates category id {int} with name {string} and parentId {int}")
    public void admin_updates_category(Integer id, String name, Integer parentId) {
        steps.adminUpdatesCategory(id.longValue(), name, parentId.longValue());
    }
    @When("admin updates category id {int} with name {string} and parentId is null")
    public void admin_updates_category_with_null_parent(Integer id, String name) {
        steps.adminUpdatesCategory(id.longValue(), name, null);
    }


    @Then("update should be successful and returned name should be {string}")
    public void update_should_be_successful(String expectedName) {
        steps.verifyUpdateSuccess(expectedName);
    }

    @Then("error response status should be {int}")
    public void error_response_status_should_be(Integer status) {
        steps.verifyErrorStatus(status);
    }

    @Then("self parent validation error should be returned with message {string}")
    public void self_parent_validation_error_should_be_returned_with_message(String msg) {
        steps.verifySelfParentValidationError(msg);
    }

    @Then("not found error should be returned with message {string}")
    public void not_found_error_should_be_returned_with_message(String msg) {
        steps.verifyNotFoundError(msg);
    }

    @When("user updates category id {int} with name {string} and parentId {int}")
    public void user_updates_category(Integer id, String name, Integer parentId) {
        steps.updateCategoryAsRole("user", id.longValue(), name, parentId.longValue());
    }

    @Then("access should be forbidden")
    public void access_should_be_forbidden() {
        steps.verifyForbidden();
    }





}
