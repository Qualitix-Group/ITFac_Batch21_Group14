package com.group14.qa.api.steps;

import com.group14.qa.api.endpoints.NullCategoryIdEndpoints;
import com.group14.qa.testdata.TestUsers;
import io.cucumber.java.en.*;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

public class GetCategoryByNullIdSteps {

    private String bearerToken;

    // Step to provide valid admin token
    @Given("a valid admin bearer token is available for null category ID")
    public void a_valid_admin_bearer_token_is_available() {
        bearerToken = TestUsers.Admin.TOKEN;

        if (bearerToken == null || bearerToken.isEmpty()) {
            throw new RuntimeException("Admin Bearer Token is missing");
        }
    }

    // Step to send GET request with null/empty category ID
    @When("I send a GET request with null category id")
    public void i_send_a_get_request_with_null_category_id() {
        SerenityRest
                .given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("id", "") // Empty ID
                .log().all()
                .when()
                .get(NullCategoryIdEndpoints.GET_CATEGORY_BY_ID)
                .then()
                .log().all();
    }

    // Step to validate status code (expected: 400 or custom Swagger validation)
    @Then("the response status code should be {int} for null category ID")
    public void the_response_status_code_should_be_for_null_category_id(int statusCode) {
        SerenityRest.then().statusCode(statusCode);
    }

    // Step to validate response body for null ID
    @Then("the response should contain validation error message for missing ID")
    public void the_response_should_contain_validation_error_message() {
        SerenityRest.then()
                .body("message", containsString("Required field is not provided"))
                .body("error", notNullValue());
    }
}
