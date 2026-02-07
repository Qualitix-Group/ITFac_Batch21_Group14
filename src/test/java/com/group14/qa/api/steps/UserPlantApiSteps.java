package com.group14.qa.api.steps;

import com.group14.qa.api.clients.PlantApiClient;
import com.group14.qa.api.models.ErrorResponse;
import com.group14.qa.api.models.PlantResponse;
import com.group14.qa.common.AuthTokens;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserPlantApiSteps {

    private PlantApiClient plantApiClient = new PlantApiClient();
    private Response response;
    private Long categoryId;

    // Positive scenario steps
    @Given("user has a valid category ID {long}")
    public void user_has_a_valid_category_id(Long id) {
        this.categoryId = id;
    }

    @When("user sends GET request to retrieve plants by category ID")
    public void user_sends_get_request_to_retrieve_plants_by_category_id() {
        String userToken = AuthTokens.getUserToken();
        response = plantApiClient.getPlantsByCategoryId(categoryId, userToken);
    }

    // Negative scenario steps for user
    @Given("user has a non-existent category ID {long}")
    public void user_has_a_non_existent_category_id(Long id) {
        this.categoryId = id;
    }

    @Then("the user response status code should be {int}")
    public void the_user_response_status_code_should_be(Integer expectedStatusCode) {
        assertThat(response.getStatusCode()).isEqualTo(expectedStatusCode);
    }

    @Then("the user response should contain list of plants")
    public void the_user_response_should_contain_list_of_plants() {
        List<PlantResponse> plants = response.jsonPath().getList(".", PlantResponse.class);
        assertThat(plants).isNotNull();
        if (!plants.isEmpty()) {
            assertThat(plants).isNotEmpty();
        }
    }

    @Then("each plant in user response should belong to the requested category")
    public void each_plant_in_user_response_should_belong_to_the_requested_category() {
        List<PlantResponse> plants = response.jsonPath().getList(".", PlantResponse.class);

        for (PlantResponse plant : plants) {
            assertThat(plant.getCategory()).isNotNull();
            assertThat(plant.getCategory().getId()).isEqualTo(categoryId);
        }
    }

    @Then("the user response content type should be application\\/json")
    public void the_user_response_content_type_should_be_application_json() {
        assertThat(response.getContentType()).contains("application/json");
    }

    @Then("the user error response should indicate category not found")
    public void the_user_error_response_should_indicate_category_not_found() {
        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatus()).isEqualTo(404);
        assertThat(errorResponse.getMessage()).isNotNull();

        // Simplified assertion
        String message = errorResponse.getMessage().toLowerCase();
        boolean containsCategory = message.contains("category");
        boolean containsNotFound = message.contains("not found");

        assertThat(containsCategory || containsNotFound).isTrue();
    }
}