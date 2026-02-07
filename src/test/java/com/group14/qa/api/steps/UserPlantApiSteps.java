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
    private Long plantId;

    @Given("user has a valid plant ID")
    public void user_has_a_valid_plant_id() {
        String userToken = AuthTokens.getUserToken();
        Response allPlantsResponse = plantApiClient.getAllPlants(userToken);

        assertThat(allPlantsResponse.getStatusCode()).isEqualTo(200);

        List<PlantResponse> plants = allPlantsResponse.jsonPath().getList(".", PlantResponse.class);
        assertThat(plants).isNotEmpty().withFailMessage("No plants found in the system");

        this.plantId = plants.get(0).getId();
        System.out.println("Using plant ID: " + this.plantId);
    }

    @Given("user has a valid plant ID for delete test")
    public void user_has_a_valid_plant_id_for_delete_test() {
        String userToken = AuthTokens.getUserToken();
        Response allPlantsResponse = plantApiClient.getAllPlants(userToken);

        assertThat(allPlantsResponse.getStatusCode()).isEqualTo(200);

        List<PlantResponse> plants = allPlantsResponse.jsonPath().getList(".", PlantResponse.class);
        assertThat(plants).isNotEmpty().withFailMessage("No plants found in the system");

        this.plantId = plants.get(0).getId();
        System.out.println("Using plant ID for delete test: " + this.plantId);
    }

    @Given("user has a non-existent plant ID {long}")
    public void user_has_a_non_existent_plant_id(Long id) {
        this.plantId = id;
    }

    @When("user sends DELETE request to delete plant by ID")
    public void user_sends_delete_request_to_delete_plant_by_id() {
        String userToken = AuthTokens.getUserToken();
        response = plantApiClient.deletePlantById(plantId, userToken);
    }

    @When("user sends GET request to retrieve all plants")
    public void user_sends_get_request_to_retrieve_all_plants() {
        String userToken = AuthTokens.getUserToken();
        response = plantApiClient.getAllPlants(userToken);
    }

    @When("user sends GET request to retrieve plant by ID")
    public void user_sends_get_request_to_retrieve_plant_by_id() {
        String userToken = AuthTokens.getUserToken();
        response = plantApiClient.getPlantById(plantId, userToken);
    }

    @Then("the user response status code should be {int}")
    public void the_user_response_status_code_should_be(Integer expectedStatusCode) {
        assertThat(response.getStatusCode()).isEqualTo(expectedStatusCode);
    }

    @Then("the user response should contain plant details")
    public void the_user_response_should_contain_plant_details() {
        System.out.println("Response body: " + response.getBody().asString());

        PlantResponse plant = response.as(PlantResponse.class);
        assertThat(plant).isNotNull();
        assertThat(plant.getId()).isEqualTo(plantId);
        assertThat(plant.getName()).isNotNull();

        boolean hasCategoryInfo = plant.getCategory() != null || plant.getCategoryId() != null;
        assertThat(hasCategoryInfo).isTrue().withFailMessage("Plant should have category information");
    }

    @Then("the plant details should match the requested ID")
    public void the_plant_details_should_match_the_requested_ID() {
        PlantResponse plant = response.as(PlantResponse.class);
        assertThat(plant.getId()).isEqualTo(plantId);
    }

    @Then("the user response should contain list of all plants")
    public void the_user_response_should_contain_list_of_all_plants() {
        List<PlantResponse> plants = response.jsonPath().getList(".", PlantResponse.class);
        assertThat(plants).isNotNull();
        assertThat(plants).isNotEmpty();
    }

    @Then("each plant in response should have valid details")
    public void each_plant_in_response_should_have_valid_details() {
        List<PlantResponse> plants = response.jsonPath().getList(".", PlantResponse.class);

        for (PlantResponse plant : plants) {
            assertThat(plant.getId()).isNotNull();
            assertThat(plant.getName()).isNotNull().isNotEmpty();
            assertThat(plant.getPrice()).isNotNull().isPositive();
            assertThat(plant.getQuantity()).isNotNull().isGreaterThanOrEqualTo(0);

            boolean hasCategoryInfo = plant.getCategory() != null || plant.getCategoryId() != null;
            assertThat(hasCategoryInfo).isTrue().withFailMessage("Plant should have category information");
        }
    }

    @Then("the user error response should indicate plant not found")
    public void the_user_error_response_should_indicate_plant_not_found() {
        assertThat(response.getStatusCode()).isEqualTo(404);

        if (response.getBody().asString().contains("\"status\"")) {
            ErrorResponse errorResponse = response.as(ErrorResponse.class);
            assertThat(errorResponse).isNotNull();
            assertThat(errorResponse.getStatus()).isEqualTo(404);
            assertThat(errorResponse.getMessage()).isNotNull();
        }
    }

    @Then("the user error response should indicate forbidden access")
    public void the_user_error_response_should_indicate_forbidden_access() {
        assertThat(response.getStatusCode()).isEqualTo(403);
        assertThat(response.jsonPath().getString("error")).isEqualTo("Forbidden");
        assertThat(response.jsonPath().getInt("status")).isEqualTo(403);
    }

    @Then("the user response content type should be application\\/json")
    public void the_user_response_content_type_should_be_application_json() {
        assertThat(response.getContentType()).contains("application/json");
    }

    @Then("no error should occur for user")
    public void no_error_should_occur_for_user() {
        assertThat(response.getStatusCode()).isBetween(200, 299);

        if (response.getContentType().contains("application/json")) {
            String body = response.getBody().asString();
            assertThat(body).doesNotContain("\"error\":");
            assertThat(body).doesNotContain("\"status\":");
        }
    }
}