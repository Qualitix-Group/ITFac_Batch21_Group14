package qa.steps.api;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import qa.steps.api.ApiContext;
import qa.utils.TestData;
import qa.utils.TestDataFactory;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CategoryAPISteps {

    private String categoryName;
    private int createdCategoryId;
    private int existingCategoryId;
    private int parentCategoryId;
    private int mainCategoryId;
    private int subCategoryId;
    private String mainCategoryName;

    private void ensureExistingCategoryId() {
        // Dynamically create category if needed (no hardcoded IDs)
        if (existingCategoryId == 0) {
            existingCategoryId = TestDataFactory.getOrCreateCategory();
        }
        if (ApiContext.getBaseUri() == null) {
            ApiContext.setBaseUri(TestData.get("base.url"));
        }
    }

    @When("I send a POST request to {string} with a valid name")
    public void iSendAPOSTRequestToWithAValidName(String endpoint) {
        categoryName = "Cat" + (System.nanoTime() % 100000);
        if (categoryName.length() > 10) {
            categoryName = categoryName.substring(0, 10);
        }

        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body("{\"name\":\"" + categoryName + "\"}")
        .when()
            .post(endpoint));

        // Track created category for cleanup (if creation was successful)
        trackCreatedCategoryFromResponse();
    }

    private void trackCreatedCategoryFromResponse() {
        try {
            int status = ApiContext.getResponse().getStatusCode();
            if (status == 200 || status == 201) {
                Integer id = ApiContext.getResponse().jsonPath().getInt("id");
                if (id == null) {
                    id = ApiContext.getResponse().jsonPath().getInt("categoryId");
                }
                if (id != null && id > 0) {
                    ApiContext.trackCreatedEntity("categories", id);
                }
            }
        } catch (Exception ignored) {
            // Response might not have an ID (e.g., validation error)
        }
    }

    @When("I send a POST request to {string} with name {string}")
    public void iSendAPOSTRequestToWithName(String endpoint, String name) {
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body("{\"name\":\"" + name + "\"}")
        .when()
            .post(endpoint));
    }

    @When("I send a POST request to create a category with missing name")
    public void iSendAPOSTRequestToCreateACategoryWithMissingName() {
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body("{}")
        .when()
            .post("/api/categories"));
    }

    @When("I send a POST request to create a category with invalid parent")
    public void iSendAPOSTRequestToCreateACategoryWithInvalidParent() {
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body("{\"name\":\"BadParent\",\"parent\":{\"id\":999999}}")
        .when()
            .post("/api/categories"));
    }

    @When("I send a POST request to create a duplicate category")
    public void iSendAPOSTRequestToCreateADuplicateCategory() {
        if (categoryName == null || categoryName.isBlank()) {
            categoryName = "CatDup";
        }
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body("{\"name\":\"" + categoryName + "\"}")
        .when()
            .post("/api/categories"));
    }

    @Given("a main category and a sub category exist")
    public void aMainCategoryAndASubCategoryExist() {
        mainCategoryId = TestDataFactory.createCategory(null);
        subCategoryId = TestDataFactory.createCategory(mainCategoryId);
        mainCategoryName = fetchCategoryNameById(mainCategoryId);
    }

    @Given("I have created a category for testing")
    public void iHaveCreatedACategoryForTesting() {
        ensureExistingCategoryId();
        categoryName = "Tmp" + (System.nanoTime() % 100000);
        if (categoryName.length() > 10) {
            categoryName = categoryName.substring(0, 10);
        }

        var createResponse = given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body("{\"name\":\"" + categoryName + "\"}")
        .when()
            .post("/api/categories")
        .then()
            .statusCode(anyOf(is(200), is(201)))
            .extract()
            .jsonPath();

        Integer id = createResponse.getInt("id");
        if (id == null) {
            id = createResponse.getInt("categoryId");
        }
        createdCategoryId = id != null ? id : existingCategoryId;

        // Track for automatic cleanup after test
        if (createdCategoryId > 0 && createdCategoryId != existingCategoryId) {
            ApiContext.trackCreatedEntity("categories", createdCategoryId);
        }
    }

    @When("I send a PUT request to update the category")
    public void iSendAPUTRequestToUpdateTheCategory() {
        String updatedName = "Upd" + (System.nanoTime() % 1000);
        if (updatedName.length() < 3) updatedName = "Upd" + updatedName;
        if (updatedName.length() > 10) updatedName = updatedName.substring(0, 10);

        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body("{\"name\":\"" + updatedName + "\"}")
        .when()
            .put("/api/categories/" + createdCategoryId));
    }

    @When("I send a PUT request to update the category with parentId")
    public void iSendAPUTRequestToUpdateTheCategoryWithParentId() {
        ensureExistingCategoryId();
        if (parentCategoryId == 0) {
            parentCategoryId = TestDataFactory.getOrCreateCategory();
        }
        if (parentCategoryId == existingCategoryId) {
            parentCategoryId = TestDataFactory.createCategory(null);
        }
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body("{\"name\":\"updcat\",\"parent\":{\"id\":" + parentCategoryId + "}}")
        .when()
            .put("/api/categories/" + existingCategoryId));
    }

    @When("I send a PUT request to update the category with null parentId")
    public void iSendAPUTRequestToUpdateTheCategoryWithNullParentId() {
        ensureExistingCategoryId();
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body("{\"name\":\"updcat\",\"parent\":null}")
        .when()
            .put("/api/categories/" + existingCategoryId));
    }

    @When("I send a PUT request to set the category as its own parent")
    public void iSendAPUTRequestToSetTheCategoryAsItsOwnParent() {
        ensureExistingCategoryId();
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body("{\"name\":\"self\",\"parent\":{\"id\":" + existingCategoryId + "}}")
        .when()
            .put("/api/categories/" + existingCategoryId));
    }

    @When("I send a PUT request to update the category with invalid parentId")
    public void iSendAPUTRequestToUpdateTheCategoryWithInvalidParentId() {
        ensureExistingCategoryId();
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body("{\"name\":\"updcat\",\"parent\":{\"id\":999999}}")
        .when()
            .put("/api/categories/" + existingCategoryId));
    }

    @When("I send a PUT request to update the existing category")
    public void iSendAPUTRequestToUpdateTheExistingCategory() {
        ensureExistingCategoryId();
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body("{\"name\":\"userupd\"}")
        .when()
            .put("/api/categories/" + existingCategoryId));
    }

    @When("I send a DELETE request to delete the category")
    public void iSendADELETERequestToDeleteTheCategory() {
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
        .when()
            .delete("/api/categories/" + createdCategoryId));
    }

    @When("I send a GET request to the existing category")
    public void iSendAGETRequestToTheExistingCategory() {
        ensureExistingCategoryId();
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
        .when()
            .get("/api/categories/" + existingCategoryId));
    }

    @When("I send a DELETE request to the existing category")
    public void iSendADELETERequestToTheExistingCategory() {
        ensureExistingCategoryId();
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
        .when()
            .delete("/api/categories/" + existingCategoryId));
    }

    @When("I send a GET request to categories page with page {int}")
    public void iSendAGETRequestToCategoriesPageWithPage(int page) {
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .queryParam("page", page)
        .when()
            .get("/api/categories/page"));
    }

    @When("I send a GET request to categories page with name {string}")
    public void iSendAGETRequestToCategoriesPageWithName(String name) {
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .queryParam("page", 0)
            .queryParam("name", name)
        .when()
            .get("/api/categories/page"));
    }

    @When("I send a GET request to categories page with parentId {int}")
    public void iSendAGETRequestToCategoriesPageWithParentId(int parentId) {
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .queryParam("page", 0)
            .queryParam("parentId", parentId)
        .when()
            .get("/api/categories/page"));
    }

    @When("I send a GET request to categories with name {string}")
    public void iSendAGETRequestToCategoriesWithName(String name) {
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .queryParam("name", name)
        .when()
            .get("/api/categories"));
    }

    @When("I send a GET request to categories with parentId {int}")
    public void iSendAGETRequestToCategoriesWithParentId(int parentId) {
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .queryParam("parentId", parentId)
        .when()
            .get("/api/categories"));
    }

    @Then("the categories page response should be empty")
    public void theCategoriesPageResponseShouldBeEmpty() {
        List<?> list = ApiContext.getResponse().jsonPath().getList("content");
        assertTrue(list == null || list.isEmpty(), "Expected empty page content");
    }

    @Then("the category list response should be empty")
    public void theCategoryListResponseShouldBeEmpty() {
        List<?> list = ApiContext.getResponse().jsonPath().getList("");
        assertTrue(list == null || list.isEmpty(), "Expected empty category list");
    }

    @Then("each category should contain required fields")
    public void eachCategoryShouldContainRequiredFields() {
        List<?> list = ApiContext.getResponse().jsonPath().getList("");
        assertTrue(list != null, "Category list should not be null");
        if (list.isEmpty()) return;
        ApiContext.getResponse().then()
            .body("[0].id", notNullValue())
            .body("[0].name", notNullValue())
            .body("[0].parentName", notNullValue())
            .body("[0].subCategories", notNullValue());
    }

    @Then("parent category mapping should be valid")
    public void parentCategoryMappingShouldBeValid() {
        List<java.util.Map<String, Object>> list = ApiContext.getResponse().jsonPath().getList("");
        assertTrue(list != null && !list.isEmpty(), "Category list should not be empty");
        boolean foundMain = false;
        boolean foundSub = false;
        for (var cat : list) {
            Object idObj = cat.get("id");
            Object parentObj = cat.get("parentName");
            String parentName = parentObj == null ? "" : parentObj.toString();
            if (idObj != null && mainCategoryId == Integer.parseInt(idObj.toString())) {
                foundMain = parentName == null || parentName.equals("-") || parentName.isBlank();
            }
            if (idObj != null && subCategoryId == Integer.parseInt(idObj.toString())) {
                foundSub = mainCategoryName != null && !mainCategoryName.isBlank()
                    && parentName != null
                    && parentName.contains(mainCategoryName);
            }
        }
        assertTrue(foundMain, "Main category should have '-' or empty parentName");
        assertTrue(foundSub, "Sub-category should have parentName matching main category");
    }

    private String fetchCategoryNameById(int id) {
        String token = ApiContext.getToken();
        if (token == null || token.isBlank()) {
            token = qa.api.steps.AuthClient.getToken("admin.username", "admin.password");
        }
        var resp = given()
            .baseUri(TestData.get("base.url"))
            .header("Authorization", "Bearer " + token)
            .get("/api/categories/" + id);
        if (resp.getStatusCode() == 200) {
            return resp.jsonPath().getString("name");
        }
        return "";
    }

    @And("the response should contain the category id")
    public void theResponseShouldContainTheCategoryId() {
        ensureExistingCategoryId();
        ApiContext.getResponse().then().body("id", anyOf(equalTo(existingCategoryId), notNullValue()));
    }
}
