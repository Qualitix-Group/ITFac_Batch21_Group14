package com.group14.qa.api.steps;

import com.group14.qa.api.endpoints.CategorysummaryEndpoints;
import com.group14.qa.testdata.TestUsers;
import io.cucumber.java.en.*;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

public class GetCategorySummarySteps {

    private String bearerToken;

    @Given("a valid admin bearer token is available for category summary")
    public void a_valid_admin_bearer_token_is_available_for_category_summary() {
        bearerToken = TestUsers.Admin.TOKEN;

        if (bearerToken == null || bearerToken.isEmpty()) {
            throw new RuntimeException("Admin Bearer Token is missing");
        }
    }

    @When("I send a GET request to category summary API")
    public void i_send_a_get_request_to_category_summary_api() {
        SerenityRest
                .given()
                .header("Authorization", "Bearer " + bearerToken)
                .log().all()
                .when()
                .get(CategorysummaryEndpoints.GET_CATEGORY_SUMMARY)
                .then()
                .log().all();
    }

    @Then("the response status code for category summary should be {int}")
    public void the_response_status_code_for_category_summary_should_be(int statusCode) {
        SerenityRest.then().statusCode(statusCode);
    }

    @Then("the response should contain category summary details")
    public void the_response_should_contain_category_summary_details() {
        SerenityRest.then()
                .body("mainCategories", notNullValue())
                .body("mainCategories", instanceOf(Integer.class))
                .body("subCategories", notNullValue())
                .body("subCategories", instanceOf(Integer.class));
    }
}
