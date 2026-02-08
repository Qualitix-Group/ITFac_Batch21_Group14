package com.group14.qa.api.steps;

import com.group14.qa.api.endpoints.AllCategoriesEndpoints;
import com.group14.qa.testdata.TestUsers;
import io.cucumber.java.en.*;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

public class GetAllCategoriesSteps {

    private String bearerToken;

    @Given("a valid JWT token is available for all categories")
    public void a_valid_jwt_token_is_available_for_all_categories() {
        // Get token from TestUsers
        bearerToken = TestUsers.Admin.TOKEN;  // or RegularUser if testing user access

        if (bearerToken == null || bearerToken.isEmpty()) {
            throw new RuntimeException("JWT token is missing");
        }
    }

    @When("I send a GET request to retrieve all categories")
    public void i_send_a_get_request_to_retrieve_all_categories() {
        SerenityRest
                .given()
                .header("Authorization", "Bearer " + bearerToken)
                .log().all()
                .when()
                .get(AllCategoriesEndpoints.GET_ALL_CATEGORIES)
                .then()
                .log().all();
    }

    @Then("the response status code for all categories should be {int}")
    public void the_response_status_code_for_all_categories_should_be(int statusCode) {
        SerenityRest.then().statusCode(statusCode);
    }

    @Then("the response should contain a list of categories")
    public void the_response_should_contain_a_list_of_categories() {
        SerenityRest.then()
                .body("", not(empty()))
                .body("$", instanceOf(java.util.List.class))
                .body("id", everyItem(notNullValue()))
                .body("name", everyItem(notNullValue()))
                .body("parent", everyItem(anything())); // parent can be null or string
    }
}
