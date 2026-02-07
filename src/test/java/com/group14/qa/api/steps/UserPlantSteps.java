package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

public class UserPlantSteps {

    private Response response;

    @Step
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
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(body)
                .post("/api/plants/category/" + categoryId);
    }

    @Step
    public void verifyUserCannotAddPlant() {
        response.then().statusCode(403);
    }
}
