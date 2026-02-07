package com.group14.qa.api.steps;

import com.group14.qa.api.endpoints.CategoryEndpoints;
import com.group14.qa.testdata.TestUsers;
import io.cucumber.java.en.*;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

public class GetCategoryByIdSteps {

    private String bearerToken;

    // Step to provide a valid admin token
    @Given("a valid admin bearer token is available")
    public void a_valid_admin_bearer_token_is_available() {
        // Get token from TestUsers
        bearerToken = TestUsers.Admin.TOKEN;

        // Print token to console for debugging


        if (bearerToken == null || bearerToken.isEmpty()) {
            throw new RuntimeException("Admin Bearer Token is missing");
        }
    }

    // Step to send GET request to API
    @When("I send a GET request to get category by id {int}")
    public void i_send_a_get_request_to_get_category_by_id(int id) {

        SerenityRest
                .given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("id", id)
                .log().all()          // logs the request for debugging
                .when()
                .get(CategoryEndpoints.GET_CATEGORY_BY_ID)
                .then()
                .log().all();         // logs the response for debugging
    }

    // Step to validate status code
    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int statusCode) {
        SerenityRest.then().statusCode(statusCode);
    }

    // Step to validate response body
    @Then("the response should contain valid category details")
    public void the_response_should_contain_valid_category_details() {
        SerenityRest.then()
                .body("id", notNullValue())
                .body("id", instanceOf(Integer.class))
                .body("name", notNullValue())
                .body("name", instanceOf(String.class))
                .body("parent", anything())
                .body("subCategories", isA(java.util.List.class));
    }
}
