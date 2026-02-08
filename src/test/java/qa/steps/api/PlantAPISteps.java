package qa.steps.api;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import qa.utils.TestData;
import qa.utils.TestDataFactory;
import qa.steps.api.ApiContext;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlantAPISteps {

    private int createdPlantId;
    private int validCategoryId;
    private String lastCreatedPlantName;

    private static final int DEFAULT_PRICE = 150;
    private static final int DEFAULT_QUANTITY = 25;

    private void initIds() {
        // Dynamically create sub-category if needed (plants require sub-categories)
        if (validCategoryId == 0) {
            validCategoryId = TestDataFactory.getOrCreateSubCategory();
        }
        if (ApiContext.getBaseUri() == null) {
            ApiContext.setBaseUri(TestData.get("base.url"));
        }
    }

    @When("I send a POST request to create a plant with valid data")
    public void iSendAPOSTRequestToCreateAPlantWithValidData() {
        initIds();
        String name = "A" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 7);
        lastCreatedPlantName = name;

        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body(String.format("""
                {
                  "name":"%s",
                  "price":%.2f,
                  "quantity":%d,
                  "category":{"id":%d}
                }
                """, name, (double) DEFAULT_PRICE, DEFAULT_QUANTITY, validCategoryId))
        .when()
            .post("/api/plants/category/" + validCategoryId));

        // Track created entity for cleanup (if creation was successful)
        trackCreatedPlantFromResponse();
    }

    private void trackCreatedPlantFromResponse() {
        try {
            int status = ApiContext.getResponse().getStatusCode();
            if (status == 200 || status == 201) {
                Integer id = ApiContext.getResponse().jsonPath().getInt("id");
                if (id == null) {
                    id = ApiContext.getResponse().jsonPath().getInt("plantId");
                }
                if (id != null && id > 0) {
                    ApiContext.trackCreatedEntity("plants", id);
                    createdPlantId = id;
                }
            }
        } catch (Exception ignored) {
            // Response might not have an ID (e.g., validation error)
        }
    }

    @When("I send a POST request to create a plant with price {int}")
    public void iSendAPOSTRequestToCreateAPlantWithPrice(int price) {
        initIds();
        String name = "B" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 7);
        lastCreatedPlantName = name;

        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body(String.format("""
                {
                  "name":"%s",
                  "price":%.2f,
                  "quantity":%d,
                  "category":{"id":%d}
                }
                """, name, (double) price, DEFAULT_QUANTITY, validCategoryId))
        .when()
            .post("/api/plants/category/" + validCategoryId));
    }

    @Given("I have created a plant for testing")
    public void iHaveCreatedAPlantForTesting() {
        initIds();
        createdPlantId = createPlantForTest(DEFAULT_QUANTITY);
    }

    @Given("I have created a plant with quantity {int}")
    public void iHaveCreatedAPlantWithQuantity(int quantity) {
        initIds();
        createdPlantId = createPlantForTest(quantity);
    }

    @When("I send a POST request to create a duplicate plant in the same category")
    public void iSendAPOSTRequestToCreateADuplicatePlantInTheSameCategory() {
        initIds();
        if (lastCreatedPlantName == null || lastCreatedPlantName.isBlank()) {
            lastCreatedPlantName = "Dup" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 7);
        }
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body(String.format("""
                {
                  "name":"%s",
                  "price":%.2f,
                  "quantity":%d,
                  "category":{"id":%d}
                }
                """, lastCreatedPlantName, (double) DEFAULT_PRICE, DEFAULT_QUANTITY, validCategoryId))
        .when()
            .post("/api/plants/category/" + validCategoryId));
    }

    @When("I send a POST request to create a plant with invalid category id")
    public void iSendAPOSTRequestToCreateAPlantWithInvalidCategoryId() {
        initIds();
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body("""
                {
                  "name":"BadCat",
                  "price":10.0,
                  "quantity":1,
                  "category":{"id":999999}
                }
                """)
        .when()
            .post("/api/plants/category/999999"));
    }

    @When("I send a POST request to create a plant under a deleted category")
    public void iSendAPOSTRequestToCreateAPlantUnderADeletedCategory() {
        initIds();
        int categoryId = TestDataFactory.createCategory(null);
        // delete category
        given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .delete("/api/categories/" + categoryId);

        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body("""
                {
                  "name":"DeletedCatPlant",
                  "price":10.0,
                  "quantity":1,
                  "category":{"id":%d}
                }
                """.formatted(categoryId))
        .when()
            .post("/api/plants/category/" + categoryId));
    }

    @When("I send a PUT request to update the plant")
    public void iSendAPUTRequestToUpdateThePlant() {
        String updatedName = "U" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 7);

        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body(String.format("""
                {
                  "id":%d,
                  "name":"%s",
                  "price":150.00,
                  "quantity":8
                }
                """, createdPlantId, updatedName))
        .when()
            .put("/api/plants/" + createdPlantId));
    }

    @When("I send a PUT request to update an existing plant")
    public void iSendAPUTRequestToUpdateAnExistingPlant() {
        initIds();
        // Create a plant as ADMIN (TestDataFactory always uses admin token)
        // so this works even when current user is a regular user
        if (createdPlantId == 0) {
            createdPlantId = TestDataFactory.getOrCreatePlant();
        }
        String updName = "U" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 7);
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body("""
                {
                  "id":%d,
                  "name":"%s",
                  "price":9.0,
                  "quantity":1
                }
                """.formatted(createdPlantId, updName))
        .when()
            .put("/api/plants/" + createdPlantId));
    }

    @When("I send a DELETE request to delete the plant")
    public void iSendADELETERequestToDeleteThePlant() {
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
        .when()
            .delete("/api/plants/" + createdPlantId));
    }

    @When("I send a DELETE request to the existing plant")
    public void iSendADELETERequestToTheExistingPlant() {
        initIds();
        // Create a plant as ADMIN (TestDataFactory always uses admin token)
        // so this works even when current user is a regular user
        if (createdPlantId == 0) {
            createdPlantId = TestDataFactory.getOrCreatePlant();
        }
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
        .when()
            .delete("/api/plants/" + createdPlantId));
    }

    @When("I send a GET request to get the plant")
    public void iSendAGETRequestToGetThePlant() {
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
        .when()
            .get("/api/plants/" + createdPlantId));
    }

    @When("I send a GET request to plants by valid category id")
    public void iSendAGETRequestToPlantsByValidCategoryId() {
        initIds();
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
        .when()
            .get("/api/plants/category/" + validCategoryId));
    }

    @When("I send a GET request to plants by invalid category format")
    public void iSendAGETRequestToPlantsByInvalidCategoryFormat() {
        initIds();
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
        .when()
            .get("/api/plants/category/abc"));
    }

    @When("I send a GET request to plants by non existing category")
    public void iSendAGETRequestToPlantsByNonExistingCategory() {
        initIds();
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
        .when()
            .get("/api/plants/category/999999"));
    }

    @When("I send a GET request to plants by empty category")
    public void iSendAGETRequestToPlantsByEmptyCategory() {
        initIds();
        int emptyCategoryId = TestDataFactory.createCategory(null);
        ApiContext.trackCreatedEntity("categories", emptyCategoryId);
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
        .when()
            .get("/api/plants/category/" + emptyCategoryId));
    }

    @When("I send a GET request to paged plants with page {int} and size {int}")
    public void iSendAGETRequestToPagedPlantsWithPageAndSize(int page, int size) {
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .queryParam("page", page)
            .queryParam("size", size)
        .when()
            .get("/api/plants/paged"));
    }

    @When("I send a GET request to plant summary")
    public void iSendAGETRequestToPlantSummary() {
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
        .when()
            .get("/api/plants/summary"));
    }

    @Then("the plant summary should contain totals")
    public void thePlantSummaryShouldContainTotals() {
        ApiContext.getResponse().then()
            .body("totalPlants", notNullValue())
            .body("lowStockPlants", notNullValue());
    }

    @Then("the plant list should be empty")
    public void thePlantListShouldBeEmpty() {
        List<?> list = ApiContext.getResponse().jsonPath().getList("");
        assertTrue(list == null || list.isEmpty(), "Plant list should be empty");
    }

    @Given("no plants exist in the system")
    public void noPlantsExistInTheSystem() {
        String baseUrl = TestData.get("base.url");
        String token = qa.api.steps.AuthClient.getToken("admin.username", "admin.password");
        for (int attempt = 0; attempt < 5; attempt++) {
            Response resp = given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .get("/api/plants");
            if (resp.getStatusCode() != 200) return;
            List<java.util.Map<String, Object>> plants = resp.jsonPath().getList("");
            if (plants == null || plants.isEmpty()) return;
            for (var plant : plants) {
                Object idObj = plant.get("id");
                if (idObj == null) continue;
                int id = idObj instanceof Integer ? (Integer) idObj : Integer.parseInt(idObj.toString());
                given()
                    .baseUri(baseUrl)
                    .header("Authorization", "Bearer " + token)
                    .delete("/api/plants/" + id);
            }
        }
    }

    @Then("the lowStock flag should be true")
    public void theLowStockFlagShouldBeTrue() {
        JsonPath json = ApiContext.getResponse().then().extract().jsonPath();

        // Check for lowStock field in the response (API-PLANT-05)
        Object lowStockRaw = json.get("lowStock");
        if (lowStockRaw == null) lowStockRaw = json.get("isLowStock");
        if (lowStockRaw == null) lowStockRaw = json.get("low_stock");

        assertNotNull(lowStockRaw, "lowStock flag is missing from API response when quantity < 5");

        Boolean lowStock = parseBoolean(lowStockRaw);
        assertNotNull(lowStock, "lowStock flag exists but is not boolean");
        assertTrue(lowStock, "lowStock should be true when quantity < 5");
    }

    private int createPlantForTest(int quantity) {
        String name = "T" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 7);
        lastCreatedPlantName = name;

        JsonPath jsonPath = given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body(String.format("""
                {
                  "name":"%s",
                  "price":%.2f,
                  "quantity":%d,
                  "category":{"id":%d}
                }
                """, name, (double) DEFAULT_PRICE, quantity, validCategoryId))
        .when()
            .post("/api/plants/category/" + validCategoryId)
        .then()
            .statusCode(anyOf(is(200), is(201)))
            .extract()
            .jsonPath();

        Integer id = jsonPath.getInt("id");
        if (id == null) id = jsonPath.getInt("plantId");
        int plantId = id != null ? id : 0;

        // Track for automatic cleanup after test
        if (plantId > 0) {
            ApiContext.trackCreatedEntity("plants", plantId);
        }

        return plantId;
    }

    private Boolean parseBoolean(Object obj) {
        if (obj instanceof Boolean b) return b;
        if (obj instanceof String s) return Boolean.parseBoolean(s);
        return null;
    }

}
