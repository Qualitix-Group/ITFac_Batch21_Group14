package qa.steps.api;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import qa.api.steps.AuthClient;
import qa.utils.TestData;
import qa.utils.TestDataFactory;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SecurityAPISteps {

    private int categoryId;
    private int plantId;
    private int saleId;

    private void initBaseUri() {
        if (ApiContext.getBaseUri() == null) {
            ApiContext.setBaseUri(TestData.get("base.url"));
        }
    }

    private void ensureTestDataExists() {
        // Dynamically create test data if needed (no hardcoded IDs)
        // Plants require sub-categories, so use getOrCreateSubCategory()
        if (categoryId == 0) {
            categoryId = TestDataFactory.getOrCreateSubCategory();
        }
        if (plantId == 0) {
            plantId = TestDataFactory.getOrCreatePlant();
        }
    }

    private void ensureSaleExists() {
        ensureTestDataExists();
        if (saleId == 0) {
            saleId = TestDataFactory.createSale(plantId, 1);
        }
    }

    @When("I send a GET request to {string} without authentication")
    public void iSendAGETRequestToWithoutAuthentication(String endpoint) {
        initBaseUri();
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
        .when()
            .get(endpoint));
    }

    @When("I send a GET request to {string} with invalid token")
    public void iSendAGETRequestToWithInvalidToken(String endpoint) {
        initBaseUri();
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", "Bearer abc.def.ghi")
        .when()
            .get(endpoint));
    }

    @When("I send a POST request to create a plant with invalid payload")
    public void iSendAPOSTRequestToCreateAPlantWithInvalidPayload() {
        initBaseUri();
        ensureTestDataExists();
        ApiContext.setToken(AuthClient.getToken("admin.username", "admin.password"));

        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body("{\"name\":\"BadPlant\",\"price\":0,\"quantity\":-1}")
        .when()
            .post("/api/plants/category/" + categoryId));
    }

    @When("I send a DELETE request to the configured sale")
    public void iSendADELETERequestToTheConfiguredSale() {
        initBaseUri();
        ensureSaleExists();
        ApiContext.setToken(AuthClient.getToken("admin.username", "admin.password"));

        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
        .when()
            .delete("/api/sales/" + saleId));
    }

    @And("I send a DELETE request to the configured sale again")
    public void iSendADELETERequestToTheConfiguredSaleAgain() {
        // Use the same saleId from the previous step (already deleted)
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
        .when()
            .delete("/api/sales/" + saleId));
    }

    @When("I send a DELETE request to {string}")
    public void iSendADELETERequestTo(String endpoint) {
        initBaseUri();
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
        .when()
            .delete(endpoint));
    }

    @When("I send a POST request to create a category")
    public void iSendAPOSTRequestToCreateACategory() {
        initBaseUri();
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body("{\"name\":\"forbidden-test\"}")
        .when()
            .post("/api/categories"));
    }

    @And("the response should contain status {int} and a message")
    public void theResponseShouldContainStatusAndAMessage(int expectedStatus) {
        var jsonPath = ApiContext.getResponse().then().extract().jsonPath();
        assertEquals(expectedStatus, jsonPath.getInt("status"));
        String message = jsonPath.getString("message");
        assertNotNull(message);
        assertFalse(message.isBlank());
    }
}
