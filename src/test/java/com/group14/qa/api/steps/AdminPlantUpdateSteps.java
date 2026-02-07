package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static com.group14.qa.api.utils.PlantApiConstants.*;
import static org.hamcrest.Matchers.equalTo;

public class AdminPlantUpdateSteps {

    private Response response;

    @Step("Create plant for update")
    public int createPlantForUpdate(String token, int categoryId) {

        String name = "Plant_" + System.currentTimeMillis();
        if (name.length() > 25) {
            name = name.substring(0, 25);
        }

        String body = String.format(
                "{\"name\":\"%s\",\"price\":100.0,\"quantity\":10,\"category\":{\"id\":%d}}",
                name, categoryId
        );

        response = SerenityRest.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .pathParam("categoryId", categoryId)
                .body(body)
                .when()
                .post(CREATE_PLANT_WITH_CATEGORY);

        response.then().statusCode(201);

        return response.path("id");
    }

    @Step("Update plant with id {0}")
    public void updatePlant(int plantId, String token, String name, float price, int quantity) {

        String body = String.format(
                "{\"name\":\"%s\",\"price\":%.2f,\"quantity\":%d}",
                name, price, quantity
        );

        response = SerenityRest.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .pathParam("id", plantId)
                .body(body)
                .when()
                .put(UPDATE_PLANT_BY_ID);
    }

    @Step("Verify plant updated successfully")
    public void verifyPlantUpdatedSuccessfully(String name, float price, int quantity) {
        response.then()
                .statusCode(200)
                .body("name", equalTo(name))
                .body("price", equalTo(price))
                .body("quantity", equalTo(quantity));
    }

    public Response getResponse() {
        return response;
    }
}
