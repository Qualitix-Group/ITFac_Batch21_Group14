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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class AdminPlantApiSteps {

    private PlantApiClient plantApiClient = new PlantApiClient();
    private Response response;
    private Long categoryId;
    private Long plantId;

    @Given("admin has a valid category ID")
    public void admin_has_a_valid_category_id() {
        String adminToken = AuthTokens.getAdminToken();
        Response allPlantsResponse = plantApiClient.getAllPlants(adminToken);

        assertThat(allPlantsResponse.getStatusCode()).isEqualTo(200);

        List<PlantResponse> plants = allPlantsResponse.jsonPath().getList(".", PlantResponse.class);
        assertThat(plants).isNotEmpty().withFailMessage("No plants found in the system");

        PlantResponse firstPlant = plants.get(0);
        this.categoryId = firstPlant.getEffectiveCategoryId();
        assertThat(this.categoryId).isNotNull().withFailMessage("Could not determine category ID");

        System.out.println("Using category ID: " + this.categoryId);
    }

    @Given("admin has a valid category ID with no plants")
    public void admin_has_a_valid_category_id_with_no_plants() {
        this.categoryId = 2L;
        System.out.println("WARNING: Using hardcoded category ID 2. Ensure this category has no plants.");
    }

    @Given("admin has a non-existent category ID")
    public void admin_has_a_non_existent_category_id() {
        this.categoryId = 99999L;
        System.out.println("Using non-existent category ID: " + this.categoryId);
    }

    @When("admin sends GET request to retrieve plants by category ID")
    public void admin_sends_get_request_to_retrieve_plants_by_category_id() {
        String adminToken = AuthTokens.getAdminToken();
        response = plantApiClient.getPlantsByCategoryId(categoryId, adminToken);
    }

    @Given("admin has a valid plant ID")
    public void admin_has_a_valid_plant_id() {
        String adminToken = AuthTokens.getAdminToken();
        Response allPlantsResponse = plantApiClient.getAllPlants(adminToken);

        assertThat(allPlantsResponse.getStatusCode()).isEqualTo(200);

        List<PlantResponse> plants = allPlantsResponse.jsonPath().getList(".", PlantResponse.class);
        assertThat(plants).isNotEmpty().withFailMessage("No plants found in the system");

        this.plantId = plants.get(0).getId();
        assertThat(this.plantId).isNotNull();

        System.out.println("Using plant ID: " + this.plantId);
    }

    @Given("admin has a non-existent plant ID")
    public void admin_has_a_non_existent_plant_id() {
        this.plantId = 99999L;
        System.out.println("Using non-existent plant ID: " + this.plantId);
    }

    @When("admin sends DELETE request to delete plant by ID")
    public void admin_sends_delete_request_to_delete_plant_by_id() {
        String adminToken = AuthTokens.getAdminToken();
        response = plantApiClient.deletePlantById(plantId, adminToken);
    }

    @Given("a test plant exists for admin")
    public void a_test_plant_exists_for_admin() {
        String adminToken = AuthTokens.getAdminToken();
        Response allPlantsResponse = plantApiClient.getAllPlants(adminToken);

        assertThat(allPlantsResponse.getStatusCode()).isEqualTo(200);

        List<PlantResponse> plants = allPlantsResponse.jsonPath().getList(".", PlantResponse.class);
        assertThat(plants).isNotEmpty().withFailMessage("No plants found in the system");

        this.plantId = plants.get(0).getId();

        System.out.println("Test plant ID for deletion: " + this.plantId);
    }

    @Then("the response should have no content")
    public void the_response_should_have_no_content() {
        assertThat(response.getBody().asString()).isEmpty();
    }

    @Then("the response should have status {int}")
    public void the_response_should_have_status(Integer expectedStatusCode) {
        assertThat(response.getStatusCode()).isEqualTo(expectedStatusCode);
    }

    @Then("the plant should be successfully deleted")
    public void the_plant_should_be_successfully_deleted() {
        String adminToken = AuthTokens.getAdminToken();
        Response getResponse = plantApiClient.getPlantById(plantId, adminToken);

        assertThat(getResponse.getStatusCode()).isEqualTo(404);
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
            Long plantCategoryId = plant.getEffectiveCategoryId();
            assertThat(plantCategoryId).isNotNull();
            assertThat(plantCategoryId).isEqualTo(categoryId);
        }
    }

    @Then("the response content type should be application\\/json")
    public void the_response_content_type_should_be_application_json() {
        if (response.getStatusCode() != 204) {
            assertThat(response.getContentType()).contains("application/json");
        }
    }

    @Then("the error response should indicate category not found")
    public void the_error_response_should_indicate_category_not_found() {
        assertThat(response.getStatusCode()).isEqualTo(404);

        if (response.getBody().asString().contains("\"status\"")) {
            ErrorResponse errorResponse = response.as(ErrorResponse.class);
            assertThat(errorResponse).isNotNull();
            assertThat(errorResponse.getStatus()).isEqualTo(404);
            assertThat(errorResponse.getMessage()).isNotNull();
        }
    }

    @Then("the error response should indicate plant not found")
    public void the_error_response_should_indicate_plant_not_found() {
        assertThat(response.getStatusCode()).isEqualTo(404);

        if (response.getBody().asString().contains("\"status\"")) {
            ErrorResponse errorResponse = response.as(ErrorResponse.class);
            assertThat(errorResponse).isNotNull();
            assertThat(errorResponse.getStatus()).isEqualTo(404);
            assertThat(errorResponse.getMessage()).isNotNull();
        }
    }

    @Then("the response should be an empty array")
    public void the_response_should_be_an_empty_array() {
        List<?> responseList = response.jsonPath().getList(".");
        assertThat(responseList).isNotNull();
        assertThat(responseList).isEmpty();
    }

    @Then("no error should occur")
    public void no_error_should_occur() {
        assertThat(response.getStatusCode()).isBetween(200, 299);

        if (response.getContentType().contains("application/json")) {
            String body = response.getBody().asString();
            assertThat(body).doesNotContain("\"error\":");
            assertThat(body).doesNotContain("\"status\":");
        }
    }
}