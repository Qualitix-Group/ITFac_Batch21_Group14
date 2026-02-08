// src/test/java/com/group14/qa/api/steps/GetCategoryByIdStringValueSteps.java
package com.group14.qa.api.steps;

import com.group14.qa.api.endpoints.StringCategoryIdEndpoints;
import com.group14.qa.testdata.TestUsers;
import io.cucumber.java.en.*;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

public class GetCategoryByIdStringValueSteps {

    private String bearerToken;

    // Step to provide a valid admin token
    @Given("a valid admin bearer token is available for string category ID")
    public void a_valid_admin_bearer_token_is_available_for_string_category_id() {
        bearerToken = TestUsers.Admin.TOKEN;

        if (bearerToken == null || bearerToken.isEmpty()) {
            throw new RuntimeException("Admin Bearer Token is missing");
        }

        // Optional: print token for debugging
        System.out.println("Bearer Token: " + bearerToken);
    }

    // Step to send GET request with string category ID
    @When("I send a GET request with string category id {string}")
    public void i_send_a_get_request_with_string_category_id(String id) {
        SerenityRest
                .given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("id", id)
                .log().all()
                .when()
                .get(StringCategoryIdEndpoints.GET_CATEGORY_BY_ID)
                .then()
                .log().all();
    }

    // Step to validate status code (expecting 400)
    @Then("the response status code should be {int} for string category ID")
    public void the_response_status_code_should_be_for_string_category_id(int statusCode) {
        SerenityRest.then().statusCode(statusCode);
    }

    // Step to validate error message
    @Then("the response should display an error message for string ID")
    public void the_response_should_display_an_error_message_for_string_id() {
        SerenityRest.then()
                .body("status", equalTo(400))
                .body("error", equalTo("BAD_REQUEST"))
                .body("message", containsString("Failed to convert value of type"));
    }
}
