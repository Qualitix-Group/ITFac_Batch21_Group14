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

public class AdminPlantApiSteps {

    private PlantApiClient plantApiClient = new PlantApiClient();
    private Response response;
    private Long categoryId;

    @Given("admin has a valid category ID {long}")
    public void admin_has_a_valid_category_id(Long id) {
        this.categoryId = id;
    }

    @When("admin sends GET request to retrieve plants by category ID")
    public void admin_sends_get_request_to_retrieve_plants_by_category_id() {
        String adminToken = AuthTokens.getAdminToken();
        response = plantApiClient.getPlantsByCategoryId(categoryId, adminToken);
    }

    @Given("admin has a non-existent category ID {long}")
    public void admin_has_a_non_existent_category_id(Long id) {
        this.categoryId = id;
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer expectedStatusCode) {
        assertThat(response.getStatusCode()).isEqualTo(expectedStatusCode);
    }

    @Then("the response should contain list of plants")
    public void the_response_should_contain_list_of_plants() {
        List<PlantResponse> plants = response.jsonPath().getList(".", PlantResponse.class);
        assertThat(plants).isNotNull();
        assertThat(plants).isNotEmpty();
    }

    @Then("each plant should belong to the requested category")
    public void each_plant_should_belong_to_the_requested_category() {
        List<PlantResponse> plants = response.jsonPath().getList(".", PlantResponse.class);

        for (PlantResponse plant : plants) {
            assertThat(plant.getCategory()).isNotNull();
            assertThat(plant.getCategory().getId()).isEqualTo(categoryId);
        }
    }

    @Then("the response content type should be application\\/json")
    public void the_response_content_type_should_be_application_json() {
        assertThat(response.getContentType()).contains("application/json");
    }

    @Then("the error response should indicate category not found")
    public void the_error_response_should_indicate_category_not_found() {
        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatus()).isEqualTo(404);
        assertThat(errorResponse.getMessage()).isNotNull();

        // Simplified assertion - check if message contains either "category" or "not found"
        String message = errorResponse.getMessage().toLowerCase();
        boolean containsCategory = message.contains("category");
        boolean containsNotFound = message.contains("not found");

        assertThat(containsCategory || containsNotFound).isTrue();
    }

    @Then("the error response should have status {int}")
    public void the_error_response_should_have_status(Integer expectedStatus) {
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(errorResponse.getStatus()).isEqualTo(expectedStatus);
    }
}