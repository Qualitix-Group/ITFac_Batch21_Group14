package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

public class AdminPlantSteps {

    private Response response;

    @Step("Admin creates a plant under sub-category {0}")
    public void createPlant(int subCategoryId, String token, String plantName) {

        String requestBody = """
                {
                  "name": "%s",
                  "price": 150.0,
                  "quantity": 25,
                  "category": {
                    "id": %d
                  }
                }
                """.formatted(plantName, subCategoryId);

        response = SerenityRest.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .post("/api/plants/category/" + subCategoryId);
    }

    @Step("Verify plant is created successfully")
    public void verifyPlantCreated(String plantName, int subCategoryId) {
        response.then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo(plantName))
                .body("price", equalTo(150.0f))
                .body("quantity", equalTo(25))
                .body("category.id", equalTo(subCategoryId));
    }

    @Step("Verify duplicate plant creation is rejected")
    public void verifyDuplicatePlantError() {
        response.then()
                .statusCode(400)
                .body("error", equalTo("DUPLICATE_RESOURCE"))
                .body("message", containsString("already exists"))
                .body("timestamp", notNullValue());
    }
}
