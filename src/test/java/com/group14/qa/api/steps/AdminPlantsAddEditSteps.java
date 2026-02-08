package com.group14.qa.api.steps;

import com.group14.qa.api.clients.PlantApiClient;
import com.group14.qa.api.models.ErrorResponse;
import com.group14.qa.api.models.PlantRequest;
import com.group14.qa.api.models.PlantResponse;
import com.group14.qa.common.AuthTokens;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AdminPlantsAddEditSteps {

    private PlantApiClient plantApiClient = new PlantApiClient();
    private Response response;
    private PlantRequest plantRequest;
    private Long plantId;
    private Long categoryId;
    private String uniquePlantName;
    private Long existingPlantId;

    private String generateUniquePlantName(String baseName) {
        return baseName + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Given("admin has a valid sub-category ID")
    public void admin_has_a_valid_sub_category_id() {
        String adminToken = AuthTokens.getAdminToken();

        Response allPlantsResponse = plantApiClient.getAllPlants(adminToken);
        assertThat(allPlantsResponse.getStatusCode()).isEqualTo(200);

        List<PlantResponse> plants = allPlantsResponse.jsonPath().getList(".", PlantResponse.class);
        assertThat(plants).isNotEmpty().withFailMessage("No plants found in the system");

        PlantResponse existingPlant = plants.get(0);
        this.categoryId = existingPlant.getEffectiveCategoryId();

        assertThat(this.categoryId).isNotNull().withFailMessage("Could not determine category ID");
        System.out.println("Using category ID: " + this.categoryId + " for plant creation");
    }

    @Given("admin has an invalid category ID that does not exist")
    public void admin_has_an_invalid_category_id_that_does_not_exist() {
        this.categoryId = 99999L;
        System.out.println("Using non-existent category ID: " + this.categoryId);
    }

    @Given("admin has a deleted category ID")
    public void admin_has_a_deleted_category_id() {
        this.categoryId = 88888L; // Another non-existent ID to simulate deleted category
        System.out.println("Using deleted/non-existent category ID: " + this.categoryId);
    }

    @Then("the error response should indicate invalid or deleted category")
    public void the_error_response_should_indicate_invalid_or_deleted_category() {
        assertThat(response.getStatusCode()).isEqualTo(404);

        if (response.getBody().asString().contains("\"status\"")) {
            ErrorResponse errorResponse = response.as(ErrorResponse.class);
            assertThat(errorResponse).isNotNull();
            assertThat(errorResponse.getStatus()).isEqualTo(404);
            assertThat(errorResponse.getMessage()).isNotNull();

            String message = errorResponse.getMessage().toLowerCase();
            boolean isCategoryError = message.contains("category") ||
                    message.contains("not found") ||
                    message.contains("invalid");

            assertThat(isCategoryError).isTrue().withFailMessage(
                    "Expected error message about invalid/deleted category, but got: " + message
            );

            System.out.println("Invalid/deleted category error detected: " + message);
        }
    }

    @Given("an existing plant is available for updating")
    public void an_existing_plant_is_available_for_updating() {
        String adminToken = AuthTokens.getAdminToken();

        Response allPlantsResponse = plantApiClient.getAllPlants(adminToken);
        assertThat(allPlantsResponse.getStatusCode()).isEqualTo(200);

        List<PlantResponse> plants = allPlantsResponse.jsonPath().getList(".", PlantResponse.class);
        assertThat(plants).isNotEmpty().withFailMessage("No plants found in the system");

        PlantResponse existingPlant = plants.get(0);
        this.existingPlantId = existingPlant.getId();
        this.categoryId = existingPlant.getEffectiveCategoryId();

        assertThat(this.existingPlantId).isNotNull().withFailMessage("Could not find plant ID");
        System.out.println("Found existing plant with ID: " + this.existingPlantId + " for updating");
    }

    @Given("admin prepares updated plant data")
    public void admin_prepares_updated_plant_data() {
        this.uniquePlantName = generateUniquePlantName("UpdatedPlant");

        this.plantRequest = new PlantRequest(
                uniquePlantName,
                299.99,
                50
        );

        System.out.println("Preparing to update plant ID " + existingPlantId +
                " with new name: " + uniquePlantName);
    }

    @When("admin sends PUT request to update the plant")
    public void admin_sends_put_request_to_update_the_plant() {
        String adminToken = AuthTokens.getAdminToken();
        System.out.println("Sending PUT request to /api/plants/" + existingPlantId);

        response = plantApiClient.updatePlant(existingPlantId, plantRequest, adminToken);

        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody().asString());
    }

    @Then("the plant should be updated successfully with status {int}")
    public void the_plant_should_be_updated_successfully_with_status(Integer expectedStatusCode) {
        assertThat(response.getStatusCode()).isEqualTo(expectedStatusCode);

        if (expectedStatusCode == 200) {
            PlantResponse updatedPlant = response.as(PlantResponse.class);
            assertThat(updatedPlant).isNotNull();
            assertThat(updatedPlant.getId()).isEqualTo(existingPlantId);
            assertThat(updatedPlant.getName()).isEqualTo(plantRequest.getName());
            assertThat(updatedPlant.getPrice()).isEqualTo(plantRequest.getPrice());
            assertThat(updatedPlant.getQuantity()).isEqualTo(plantRequest.getQuantity());

            System.out.println("Plant updated successfully: ID " + existingPlantId);
        }
    }

    @Then("the response should contain updated plant details")
    public void the_response_should_contain_updated_plant_details() {
        PlantResponse updatedPlant = response.as(PlantResponse.class);

        assertThat(updatedPlant).isNotNull();
        assertThat(updatedPlant.getId()).isEqualTo(existingPlantId);
        assertThat(updatedPlant.getName()).isEqualTo(plantRequest.getName());
        assertThat(updatedPlant.getPrice()).isEqualTo(plantRequest.getPrice());
        assertThat(updatedPlant.getQuantity()).isEqualTo(plantRequest.getQuantity());
    }

    @Then("the updated plant should reflect changes in the system")
    public void the_updated_plant_should_reflect_changes_in_the_system() {
        String adminToken = AuthTokens.getAdminToken();
        Response getResponse = plantApiClient.getPlantById(existingPlantId, adminToken);

        System.out.println("GET updated plant response status: " + getResponse.getStatusCode());
        System.out.println("GET updated plant response body: " + getResponse.getBody().asString());

        assertThat(getResponse.getStatusCode()).isEqualTo(200);

        PlantResponse retrievedPlant = getResponse.as(PlantResponse.class);
        assertThat(retrievedPlant).isNotNull();
        assertThat(retrievedPlant.getId()).isEqualTo(existingPlantId);
        assertThat(retrievedPlant.getName()).isEqualTo(plantRequest.getName());
        assertThat(retrievedPlant.getPrice()).isEqualTo(plantRequest.getPrice());
        assertThat(retrievedPlant.getQuantity()).isEqualTo(plantRequest.getQuantity());
    }


    @Given("admin prepares valid plant data for creation")
    public void admin_prepares_valid_plant_data_for_creation() {
        this.uniquePlantName = generateUniquePlantName("TestPlant");

        this.plantRequest = new PlantRequest(
                uniquePlantName,
                150.0,
                25
        );

        System.out.println("Preparing to create plant with name: " + uniquePlantName + " under category ID: " + categoryId);
    }

    @When("admin sends POST request to create plant")
    public void admin_sends_post_request_to_create_plant() {
        String adminToken = AuthTokens.getAdminToken();
        System.out.println("Sending POST request to /api/plants/category/" + categoryId);

        response = plantApiClient.createPlantUnderCategory(categoryId, plantRequest, adminToken);

        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody().asString());
    }

    @Then("the plant should be created successfully with status {int}")
    public void the_plant_should_be_created_successfully_with_status(Integer expectedStatusCode) {
        assertThat(response.getStatusCode()).isEqualTo(expectedStatusCode);

        if (expectedStatusCode == 201) {
            PlantResponse createdPlant = response.as(PlantResponse.class);
            assertThat(createdPlant).isNotNull();
            assertThat(createdPlant.getId()).isNotNull();
            assertThat(createdPlant.getName()).isEqualTo(plantRequest.getName());
            assertThat(createdPlant.getPrice()).isEqualTo(plantRequest.getPrice());
            assertThat(createdPlant.getQuantity()).isEqualTo(plantRequest.getQuantity());

            this.plantId = createdPlant.getId();
            System.out.println("Plant created successfully with ID: " + this.plantId);
        }
    }

    @Then("the response should contain created plant details")
    public void the_response_should_contain_created_plant_details() {
        PlantResponse createdPlant = response.as(PlantResponse.class);

        assertThat(createdPlant).isNotNull();
        assertThat(createdPlant.getId()).isNotNull().isPositive();
        assertThat(createdPlant.getName()).isEqualTo(plantRequest.getName());
        assertThat(createdPlant.getPrice()).isEqualTo(plantRequest.getPrice());
        assertThat(createdPlant.getQuantity()).isEqualTo(plantRequest.getQuantity());

        Long effectiveCategoryId = createdPlant.getEffectiveCategoryId();
        assertThat(effectiveCategoryId).isNotNull();
        assertThat(effectiveCategoryId).isEqualTo(categoryId);
    }

    @Then("the created plant should be retrievable from the system")
    public void the_created_plant_should_be_retrievable_from_the_system() {
        assertThat(plantId).isNotNull().withFailMessage("Plant ID not captured from creation");

        String adminToken = AuthTokens.getAdminToken();
        Response getResponse = plantApiClient.getPlantById(plantId, adminToken);

        System.out.println("GET plant by ID response status: " + getResponse.getStatusCode());
        System.out.println("GET plant by ID response body: " + getResponse.getBody().asString());

        assertThat(getResponse.getStatusCode()).isEqualTo(200);

        PlantResponse retrievedPlant = getResponse.as(PlantResponse.class);
        assertThat(retrievedPlant).isNotNull();
        assertThat(retrievedPlant.getId()).isEqualTo(plantId);
        assertThat(retrievedPlant.getName()).isEqualTo(plantRequest.getName());

        Long retrievedCategoryId = retrievedPlant.getEffectiveCategoryId();
        assertThat(retrievedCategoryId).isNotNull();
        assertThat(retrievedCategoryId).isEqualTo(categoryId);
    }

    @And("clean up the created test plant")
    public void clean_up_the_created_test_plant() {
        if (plantId != null) {
            String adminToken = AuthTokens.getAdminToken();
            Response deleteResponse = plantApiClient.deletePlantById(plantId, adminToken);

            if (deleteResponse.getStatusCode() == 204) {
                System.out.println("Test plant cleaned up successfully: ID " + plantId);
            } else {
                System.out.println("Note: Could not clean up test plant ID " + plantId);
            }
        }
    }

    @Given("a plant with name {string} already exists in the category")
    public void a_plant_with_name_already_exists_in_the_category(String existingPlantName) {
        String adminToken = AuthTokens.getAdminToken();

        PlantRequest existingPlantRequest = new PlantRequest(
                existingPlantName,
                200.0,
                30
        );

        Response createResponse = plantApiClient.createPlantUnderCategory(categoryId, existingPlantRequest, adminToken);
        assertThat(createResponse.getStatusCode()).isEqualTo(201);

        PlantResponse createdPlant = createResponse.as(PlantResponse.class);
        System.out.println("Created existing plant with ID: " + createdPlant.getId() + " and name: " + existingPlantName);

        this.uniquePlantName = existingPlantName;
    }

    @Given("admin prepares duplicate plant data with existing name")
    public void admin_prepares_duplicate_plant_data_with_existing_name() {

        this.plantRequest = new PlantRequest(
                uniquePlantName,
                180.0,
                15
        );

        System.out.println("Preparing to create duplicate plant with name: " + uniquePlantName + " under category ID: " + categoryId);
    }

    @Then("the error response should indicate duplicate plant name")
    public void the_error_response_should_indicate_duplicate_plant_name() {
        assertThat(response.getStatusCode()).isEqualTo(400);

        if (response.getBody().asString().contains("\"status\"")) {
            ErrorResponse errorResponse = response.as(ErrorResponse.class);
            assertThat(errorResponse).isNotNull();
            assertThat(errorResponse.getStatus()).isEqualTo(400);
            assertThat(errorResponse.getMessage()).isNotNull();

            String message = errorResponse.getMessage().toLowerCase();
            boolean isDuplicateError = message.contains("duplicate") ||
                    message.contains("already exists") ||
                    message.contains("exists") ||
                    message.contains("name");

            assertThat(isDuplicateError).isTrue().withFailMessage(
                    "Expected error message about duplicate plant, but got: " + message
            );

            System.out.println("Duplicate plant error detected: " + message);
        }
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(Integer expectedStatusCode) {
        assertThat(response.getStatusCode()).isEqualTo(expectedStatusCode);
    }
}