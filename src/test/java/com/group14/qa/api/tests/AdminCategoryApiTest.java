package com.group14.qa.api.tests;

import com.group14.qa.api.steps.AdminCategoryApiSteps;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AdminCategoryApiTest {

    private final AdminCategoryApiSteps steps = new AdminCategoryApiSteps();

    @When("admin sends POST request to create a category with valid data")
    public void admin_sends_post_request_to_create_a_category_with_valid_data() {
        steps.createCategoryWithValidData();
    }

    @Then("the API should return 201 and newly created category details")
    public void the_api_should_return_201_and_newly_created_category_details() {
        steps.verify201AndResponseBody();
    }

    @And("the created category should be retrievable by id")
    public void the_created_category_should_be_retrievable_by_id() {
        steps.verifyCategoryStoredAndRetrievable();
    }
}
