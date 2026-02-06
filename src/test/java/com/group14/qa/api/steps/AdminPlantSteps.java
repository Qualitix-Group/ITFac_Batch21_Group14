package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

public class AdminPlantSteps {

    private Response response;
    private String lastUsedPlantName;

    @Step("Admin creates a plant under sub category {0}")
    public void createPlantUnderSubCategory(int subCategoryId, String token) {
        lastUsedPlantName = "SunFlower7";
        createPlantWithDetails(subCategoryId, token, lastUsedPlantName, 150.0f, 25);
    }

    @Step("Admin creates a plant under sub category {0} with name {1}")
    public void createPlantWithName(int subCategoryId, String token, String plantName) {
        lastUsedPlantName = plantName;
        createPlantWithDetails(subCategoryId, token, plantName, 150.0f, 25);
    }

    @Step("Admin creates a duplicate plant with same name")
    public void createDuplicatePlant(int subCategoryId, String token) {
        if (lastUsedPlantName == null) {
            lastUsedPlantName = "SunFlower4"; // default fallback
        }
        createPlantWithDetails(subCategoryId, token, lastUsedPlantName, 150.0f, 25);
    }

    @Step("Admin creates a plant with details under sub category {0}")
    private void createPlantWithDetails(int subCategoryId, String token, String plantName, float price, int quantity) {
        String requestBody = """
                {
                  "name": "%s",
                  "price": %f,
                  "quantity": %d,
                  "category": {
                    "id": %d
                  }
                }
                """.formatted(plantName, price, quantity, subCategoryId);

        response = SerenityRest.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .post("/api/plants/category/" + subCategoryId);

        response.then().log().all();
    }

    @Step("Verify plant is created successfully")
    public void verifyPlantCreationResponse(int subCategoryId) {
        response.then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo(lastUsedPlantName != null ? lastUsedPlantName : "SunFlower4"))
                .body("price", equalTo(150.0f))
                .body("quantity", equalTo(25))
                .body("category.id", equalTo(subCategoryId));
    }

    @Step("Verify duplicate plant error response")
    public void verifyDuplicatePlantError() {
        response.then()
                .statusCode(400)
                .body("status", equalTo(400))
                .body("error", equalTo("DUPLICATE_RESOURCE"))
                .body("message", containsString("already exists"))
                .body("timestamp", notNullValue());
    }

    @Step("Verify duplicate plant error response with custom message")
    public void verifyDuplicatePlantErrorWithMessage(String expectedMessage) {
        response.then()
                .statusCode(400)
                .body("status", equalTo(400))
                .body("error", equalTo("DUPLICATE_RESOURCE"))
                .body("message", containsString(expectedMessage))
                .body("timestamp", notNullValue());
    }
}