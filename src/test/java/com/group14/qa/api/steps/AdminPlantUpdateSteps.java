package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

public class AdminPlantUpdateSteps {

    private Response response;
    private int plantId;
    private String expectedName;
    private float expectedPrice;
    private int expectedQuantity;
    private int expectedCategoryId;

    @Step("Create a plant for update")
    public int createPlantForUpdate(String token, int categoryId) {

        String requestBody = """
                {
                  "name": "PlantToUpdate",
                  "price": 100.0,
                  "quantity": 10,
                  "category": { "id": %d }
                }
                """.formatted(categoryId);

        Response createResponse = SerenityRest.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .post("/api/plants/category/" + categoryId);

        createResponse.then().statusCode(201);

        return createResponse.path("id");
    }

    @Step("Update plant details")
    public void updatePlant(int plantId, String token, String name, float price, int quantity, int categoryId) {

        this.plantId = plantId;
        this.expectedName = name;
        this.expectedPrice = price;
        this.expectedQuantity = quantity;
        this.expectedCategoryId = categoryId;

        String requestBody = """
                {
                  "name": "%s",
                  "price": %f,
                  "quantity": %d,
                  "category": { "id": %d }
                }
                """.formatted(name, price, quantity, categoryId);

        response = SerenityRest.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .put("/api/plants/" + plantId);
    }

    @Step("Verify plant update successful")
    public void verifyPlantUpdateSuccessful() {
        response.then()
                .statusCode(200)
                .body("id", equalTo(plantId))
                .body("name", equalTo(expectedName))
                .body("price", equalTo(expectedPrice))
                .body("quantity", equalTo(expectedQuantity))
                .body("category.id", equalTo(expectedCategoryId));
    }
}
