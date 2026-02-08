package com.group14.qa.api.steps;

import com.group14.qa.api.endpoints.CategoriesWithParentEndpoints;
import com.group14.qa.testdata.TestUsers;
import io.cucumber.java.en.*;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Map;

public class GetCategoriesWithParentSteps {

    private String bearerToken;

    @Given("a valid JWT token is available for parent category mapping")
    public void a_valid_jwt_token_is_available_for_parent_category_mapping() {
        // Using RegularUser token
        bearerToken = TestUsers.RegularUser.TOKEN;

        if (bearerToken == null || bearerToken.isEmpty()) {
            throw new RuntimeException("JWT token is missing");
        }
    }

    @When("I send a GET request to retrieve categories with parent mapping")
    public void i_send_a_get_request_to_retrieve_categories_with_parent_mapping() {
        SerenityRest
                .given()
                .header("Authorization", "Bearer " + bearerToken)
                .log().all()
                .when()
                .get(CategoriesWithParentEndpoints.GET_ALL_CATEGORIES)
                .then()
                .log().all();
    }

    @Then("the response status code for parent category mapping should be {int}")
    public void the_response_status_code_for_parent_category_mapping_should_be(int statusCode) {
        SerenityRest.then().statusCode(statusCode);
    }

    @Then("the parentName field should be correct for each category")
    public void the_parent_name_field_should_be_correct_for_each_category() {
        // Extract response as a list of maps
        List<Map<String, Object>> categories = SerenityRest.lastResponse().jsonPath().getList("");

        for (Map<String, Object> category : categories) {
            String parentName = (String) category.get("parent");
            Boolean isMainCategory = category.get("parent") == null || parentName.equals("-");

            if (isMainCategory) {
                // Main category: parentName should be "-"
                assert parentName.equals("-") : "Main category parentName is incorrect for category: " + category.get("name");
            } else {
                // Sub-category: parentName should not be "-"
                assert parentName != null && !parentName.equals("-") : "Sub-category parentName is incorrect for category: " + category.get("name");
            }
        }
    }
}
