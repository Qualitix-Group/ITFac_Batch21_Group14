package com.group14.qa.api.steps;

import com.group14.qa.api.endpoints.DeleteCategoryEndpoints;
import com.group14.qa.testdata.TestUsers;
import io.cucumber.java.en.*;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

public class DeleteAlreadyDeletedCategorySteps {

    private String bearerToken;

    @Given("a valid admin bearer token is available for deleting category")
    public void a_valid_admin_bearer_token_is_available_for_deleting_category() {
        bearerToken = TestUsers.Admin.TOKEN;

        if (bearerToken == null || bearerToken.isEmpty()) {
            throw new RuntimeException("Admin Bearer Token is missing");
        }
    }

    @When("I send a DELETE request for an already deleted category id {int}")
    public void i_send_a_delete_request_for_an_already_deleted_category_id(int id) {
        SerenityRest
                .given()
                .header("Authorization", "Bearer " + bearerToken)
                .pathParam("id", id)
                .log().all()
                .when()
                .delete(DeleteCategoryEndpoints.DELETE_CATEGORY_BY_ID)
                .then()
                .log().all();
    }

    @Then("the response status code for deleted category should be {int}")
    public void the_response_status_code_for_deleted_category_should_be(int statusCode) {
        SerenityRest.then().statusCode(statusCode);
    }

    @Then("the response should contain category not found error message")
    public void the_response_should_contain_category_not_found_error_message() {
        SerenityRest.then()
                .body("status", equalTo(404))
                .body("error", equalTo("NOT_FOUND"))
                .body("message", containsString("Category not found"));
    }
}
