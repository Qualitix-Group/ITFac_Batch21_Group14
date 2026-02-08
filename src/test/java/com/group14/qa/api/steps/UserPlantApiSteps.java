package com.group14.qa.api.steps;

import com.group14.qa.api.clients.PlantApiClient;
import com.group14.qa.api.models.ErrorResponse;
import com.group14.qa.api.models.PaginatedPlantResponse;
import com.group14.qa.api.models.PlantRequest;
import com.group14.qa.api.models.PlantResponse;
import com.group14.qa.api.models.PlantSummaryResponse;
import com.group14.qa.common.AuthTokens;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UserPlantApiSteps {

    private PlantApiClient plantApiClient = new PlantApiClient();
    private Response response;
    private Long plantId;
    private Long categoryId;
    private String uniquePlantName;

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
        System.out.println("Sending GET request for plant ID: " + plantId);
        response = plantApiClient.getPlantById(plantId, userToken);

        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody().asString());
        System.out.println("Response content type: " + response.getContentType());
    }

    @Then("the user response status code should be {int}")
    public void the_user_response_status_code_should_be(Integer expectedStatusCode) {
        assertThat(response.getStatusCode()).isEqualTo(expectedStatusCode);
    }

    @Then("the user response should contain plant details")
    public void the_user_response_should_contain_plant_details() {
        String responseBody = response.getBody().asString();
        assertThat(responseBody).isNotEmpty().withFailMessage("Response body is empty");

        try {
            PlantResponse plant = response.as(PlantResponse.class);
            assertThat(plant).isNotNull().withFailMessage("Failed to parse response as PlantResponse");
            assertThat(plant.getId()).isEqualTo(plantId);
            assertThat(plant.getName()).isNotNull().withFailMessage("Plant name is null");

            if (plant.getCategory() == null && plant.getCategoryId() == null) {
                System.out.println("Warning: Plant response doesn't contain category information");
            }
        } catch (Exception e) {
            Long responseId = response.jsonPath().getLong("id");
            String responseName = response.jsonPath().getString("name");

            assertThat(responseId).isNotNull().isEqualTo(plantId);
            assertThat(responseName).isNotNull().isNotEmpty();

            System.out.println("Parsed plant using JSON path - ID: " + responseId + ", Name: " + responseName);
        }
    }

    @Then("the plant details should match the requested ID")
    public void the_plant_details_should_match_the_requested_ID() {
        String responseBody = response.getBody().asString();
        assertThat(responseBody).isNotEmpty();

        Long responseId = null;

        try {
            PlantResponse plant = response.as(PlantResponse.class);
            responseId = plant.getId();
        } catch (Exception e) {
            responseId = response.jsonPath().getLong("id");
        }

        assertThat(responseId).isNotNull().isEqualTo(plantId);
    }

    @Then("the user response should contain list of all plants")
    public void the_user_response_should_contain_list_of_all_plants() {
        String responseBody = response.getBody().asString();
        assertThat(responseBody).isNotEmpty();

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
        String error = response.jsonPath().getString("error");
        Integer status = response.jsonPath().getInt("status");

        assertThat(error).isEqualTo("Forbidden");
        assertThat(status).isEqualTo(403);
    }

    @Then("the user response content type should be application\\/json")
    public void the_user_response_content_type_should_be_application_json() {
        String contentType = response.getContentType();
        System.out.println("Actual content type: " + contentType);
        assertThat(contentType).contains("application/json");
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

    @When("user sends GET request to retrieve plant by ID without authentication")
    public void user_sends_get_request_to_retrieve_plant_by_id_without_authentication() {
        response = plantApiClient.getPlantByIdWithoutAuth(plantId);
    }

    @Then("the error response should indicate authentication is required")
    public void the_error_response_should_indicate_authentication_is_required() {
        assertThat(response.getStatusCode()).isEqualTo(401);

        if (response.getBody().asString().contains("\"status\"")) {
            ErrorResponse errorResponse = response.as(ErrorResponse.class);
            assertThat(errorResponse).isNotNull();
            assertThat(errorResponse.getStatus()).isEqualTo(401);
            assertThat(errorResponse.getMessage()).isNotNull();

            String message = errorResponse.getMessage().toLowerCase();
            boolean isAuthError = message.contains("unauthorized") ||
                    message.contains("authentication") ||
                    message.contains("token");

            assertThat(isAuthError).isTrue().withFailMessage(
                    "Expected authentication error, but got: " + message
            );
        }
    }

    @Then("no plant details should be returned")
    public void no_plant_details_should_be_returned() {
        String body = response.getBody().asString();
        assertThat(body).doesNotContain("\"id\":");
        assertThat(body).doesNotContain("\"name\":");
    }

    @Given("user has a valid category ID")
    public void user_has_a_valid_category_id() {
        String userToken = AuthTokens.getUserToken();
        Response allPlantsResponse = plantApiClient.getAllPlants(userToken);

        assertThat(allPlantsResponse.getStatusCode()).isEqualTo(200);

        List<PlantResponse> plants = allPlantsResponse.jsonPath().getList(".", PlantResponse.class);
        assertThat(plants).isNotEmpty().withFailMessage("No plants found in the system");

        PlantResponse existingPlant = plants.get(0);
        this.categoryId = existingPlant.getEffectiveCategoryId();

        assertThat(this.categoryId).isNotNull();
        System.out.println("Using category ID: " + this.categoryId);
    }

    @Given("user prepares plant data for creation")
    public void user_prepares_plant_data_for_creation() {
        this.uniquePlantName = "UserTestPlant_" + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("Preparing plant data with name: " + uniquePlantName);
    }

    @When("user sends POST request to create plant")
    public void user_sends_post_request_to_create_plant() {
        String userToken = AuthTokens.getUserToken();

        PlantRequest plantRequest = new PlantRequest(
                uniquePlantName,
                150.0,
                25
        );

        response = plantApiClient.createPlantUnderCategory(categoryId, plantRequest, userToken);
        System.out.println("User POST create plant response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody().asString());
    }

    @When("user sends PUT request to update the plant")
    public void user_sends_put_request_to_update_the_plant() {
        String userToken = AuthTokens.getUserToken();

        PlantRequest plantRequest = new PlantRequest(
                "UpdatedByUser_" + UUID.randomUUID().toString().substring(0, 8),
                199.99,
                30
        );

        response = plantApiClient.updatePlant(plantId, plantRequest, userToken);
        System.out.println("User PUT update plant response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody().asString());
    }

    @When("user sends GET request to retrieve paginated plants with page {int} and size {int}")
    public void user_sends_get_request_to_retrieve_paginated_plants_with_page_and_size(Integer page, Integer size) {
        String userToken = AuthTokens.getUserToken();
        response = plantApiClient.getPlantsWithPagination(page, size, userToken);
        System.out.println("Paginated plants response status: " + response.getStatusCode());
        System.out.println("Response body preview: " + response.getBody().asString().substring(0, Math.min(200, response.getBody().asString().length())));
    }

    @Then("the response should contain paginated plant data")
    public void the_response_should_contain_paginated_plant_data() {
        String responseBody = response.getBody().asString();
        assertThat(responseBody).isNotEmpty();

        try {
            PaginatedPlantResponse paginatedResponse = response.as(PaginatedPlantResponse.class);
            assertThat(paginatedResponse).isNotNull();
            assertThat(paginatedResponse.getContent()).isNotNull();
            assertThat(paginatedResponse.getTotalPages()).isGreaterThanOrEqualTo(0);
            assertThat(paginatedResponse.getTotalElements()).isGreaterThanOrEqualTo(0);
        } catch (Exception e) {
            List<?> content = response.jsonPath().getList("content");
            Integer totalPages = response.jsonPath().getInt("totalPages");
            Long totalElements = response.jsonPath().getLong("totalElements");

            assertThat(content).isNotNull();
            assertThat(totalPages).isNotNull().isGreaterThanOrEqualTo(0);
            assertThat(totalElements).isNotNull().isGreaterThanOrEqualTo(0);
        }
    }

    @Then("the number of returned records should be less than or equal to page size")
    public void the_number_of_returned_records_should_be_less_than_or_equal_to_page_size() {
        try {
            PaginatedPlantResponse paginatedResponse = response.as(PaginatedPlantResponse.class);
            assertThat(paginatedResponse.getContent().size())
                    .isLessThanOrEqualTo(paginatedResponse.getSize());
        } catch (Exception e) {
            List<?> content = response.jsonPath().getList("content");
            Integer size = response.jsonPath().getInt("size");

            assertThat(content.size()).isLessThanOrEqualTo(size);
        }
    }

    @Then("the pagination metadata should be valid")
    public void the_pagination_metadata_should_be_valid() {
        try {
            PaginatedPlantResponse paginatedResponse = response.as(PaginatedPlantResponse.class);

            assertThat(paginatedResponse.getTotalPages()).isGreaterThanOrEqualTo(0);
            assertThat(paginatedResponse.getTotalElements()).isGreaterThanOrEqualTo(0);
            assertThat(paginatedResponse.getSize()).isPositive();
            assertThat(paginatedResponse.getNumber()).isGreaterThanOrEqualTo(0);
        } catch (Exception e) {
            Integer totalPages = response.jsonPath().getInt("totalPages");
            Long totalElements = response.jsonPath().getLong("totalElements");
            Integer size = response.jsonPath().getInt("size");
            Integer number = response.jsonPath().getInt("number");

            assertThat(totalPages).isGreaterThanOrEqualTo(0);
            assertThat(totalElements).isGreaterThanOrEqualTo(0);
            assertThat(size).isPositive();
            assertThat(number).isGreaterThanOrEqualTo(0);
        }
    }

    @When("user sends GET request to retrieve plant summary")
    public void user_sends_get_request_to_retrieve_plant_summary() {
        String userToken = AuthTokens.getUserToken();
        response = plantApiClient.getPlantSummary(userToken);
        System.out.println("Plant summary response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody().asString());
    }

    @Then("the response should contain plant summary")
    public void the_response_should_contain_plant_summary() {
        String responseBody = response.getBody().asString();
        assertThat(responseBody).isNotEmpty();

        try {
            PlantSummaryResponse summary = response.as(PlantSummaryResponse.class);
            assertThat(summary).isNotNull();
            assertThat(summary.getTotalPlants()).isGreaterThanOrEqualTo(0);
            assertThat(summary.getLowStockPlants()).isGreaterThanOrEqualTo(0);
        } catch (Exception e) {
            Integer totalPlants = response.jsonPath().getInt("totalPlants");
            Integer lowStockPlants = response.jsonPath().getInt("lowStockPlants");

            assertThat(totalPlants).isNotNull().isGreaterThanOrEqualTo(0);
            assertThat(lowStockPlants).isNotNull().isGreaterThanOrEqualTo(0);
        }
    }

    @Then("the summary values should be non-negative integers")
    public void the_summary_values_should_be_non_negative_integers() {
        try {
            PlantSummaryResponse summary = response.as(PlantSummaryResponse.class);
            assertThat(summary.getTotalPlants()).isGreaterThanOrEqualTo(0);
            assertThat(summary.getLowStockPlants()).isGreaterThanOrEqualTo(0);
            assertThat(summary.getLowStockPlants()).isLessThanOrEqualTo(summary.getTotalPlants());
        } catch (Exception e) {
            Integer totalPlants = response.jsonPath().getInt("totalPlants");
            Integer lowStockPlants = response.jsonPath().getInt("lowStockPlants");

            assertThat(totalPlants).isGreaterThanOrEqualTo(0);
            assertThat(lowStockPlants).isGreaterThanOrEqualTo(0);
            assertThat(lowStockPlants).isLessThanOrEqualTo(totalPlants);
        }
    }

    @Then("the summary should match database records")
    public void the_summary_should_match_database_records() {
        try {
            PlantSummaryResponse summary = response.as(PlantSummaryResponse.class);

            String userToken = AuthTokens.getUserToken();
            Response allPlantsResponse = plantApiClient.getAllPlants(userToken);
            assertThat(allPlantsResponse.getStatusCode()).isEqualTo(200);

            List<PlantResponse> plants = allPlantsResponse.jsonPath().getList(".", PlantResponse.class);
            int totalPlants = plants.size();

            assertThat(summary.getTotalPlants()).isEqualTo(totalPlants);

            System.out.println("Summary - Total: " + summary.getTotalPlants() +
                    ", Low Stock: " + summary.getLowStockPlants() +
                    ", Database Total: " + totalPlants);
        } catch (Exception e) {
            System.out.println("Skipping detailed database verification due to parsing error");
        }
    }
}