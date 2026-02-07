package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static com.group14.qa.api.utils.PlantApiConstants.*;

public class UserPlantSteps {

    private Response response;

    @Step("User attempts to create plant under category {0}")
    public void userCreatesPlant(int categoryId, String token) {

        String body = """
                {
                  "name": "TestPlant",
                  "price": 100.0,
                  "quantity": 10,
                  "category": { "id": %d }
                }
                """.formatted(categoryId);

        response = SerenityRest.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .pathParam("categoryId", categoryId)
                .body(body)
                .when()
                .post(CREATE_PLANT_WITH_CATEGORY);
    }

    @Step("Verify user cannot add plant")
    public void verifyUserCannotAddPlant() {
        response.then()
                .statusCode(403);
    }
}
