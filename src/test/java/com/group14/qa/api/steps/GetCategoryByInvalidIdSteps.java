package com.group14.qa.api.steps;

import com.group14.qa.api.endpoints.CategoryEndpoints;
import com.group14.qa.testdata.TestUsers;
import io.cucumber.java.en.*;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

public class GetCategoryByInvalidIdSteps {

    private String bearerToken;

    // Step to provide a valid admin token
    @Given("a valid admin bearer token is available for invalid category ID")
    public void a_valid_admin_bearer_token_is_available_for_invalid_category_id() {
        // Get token from TestUsers
        bearerToken = TestUsers.Admin.TOKEN;

        // Optional: print token for debugging
        System.out.println("Bearer Token: " + bearerToken);

        if (bearerToken == null || bearerToken.isEmpty()) {
            throw new RuntimeException("Admin Bearer Token is missing");
        }
    }

    // Step to send GET request with invalid category ID
    @When("I send a GET request with invalid category id {int}")
    public void i_send_a_get_request_with_invalid_category_id(int id) {
        SerenityRest
                .given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("id", id)
                .log().all() // log request
                .when()
                .get(CategoryEndpoints.GET_CATEGORY_BY_ID)
                .then()
                .log().all(); // log response
    }

    // Step to validate status code for invalid category
    @Then("the response status code should be {int} for invalid category")
    public void the_response_status_code_should_be_for_invalid_category(int statusCode) {
        SerenityRest.then().statusCode(statusCode);
    }

    // Step to validate response body for invalid category
    @Then("the response should contain category not found message")
    public void the_response_should_contain_category_not_found_message() {
        SerenityRest.then()
                .body("status", equalTo(404))
                .body("error", equalTo("NOT_FOUND"))
                .body("message", containsString("Category not found"));
    }
}
