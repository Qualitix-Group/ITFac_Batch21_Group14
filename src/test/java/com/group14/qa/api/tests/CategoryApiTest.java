package com.group14.qa.api.tests;

import com.group14.qa.api.steps.CategoryApiSteps;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CategoryApiTest {

    CategoryApiSteps steps = new CategoryApiSteps();

    // User tests
    @When("user requests category with id {int}")
    public void user_requests_category_with_id(Integer id) {
        steps.getCategoryById(id.longValue(), "user");
    }

    // Admin tests
    @When("admin requests category with id {int}")
    public void admin_requests_category_with_id(Integer id) {
        steps.getCategoryById(id.longValue(), "admin");
    }

    // Anonymous tests (if needed)
    @When("anonymous user requests category with id {int}")
    public void anonymous_requests_category_with_id(Integer id) {
        steps.getCategoryById(id.longValue(), "anonymous");
    }

    @Then("category details should be returned")
    public void category_details_should_be_returned() {
        steps.verifyCategorySuccess();
    }

    @Then("error response with status {int} should be returned")
    public void error_response_with_status_should_be_returned(Integer statusCode) {
        steps.verifyErrorResponse(statusCode);
    }

    @Then("access should be unauthorized")
    public void access_should_be_unauthorized() {
        steps.verifyUnauthorizedAccess();
    }
}