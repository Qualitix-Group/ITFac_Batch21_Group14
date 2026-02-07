package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

public class UserPlantEditSteps {

    private Response response;

    @Step
    public void userEditsPlant(int plantId, String token) {

        String body = """
                {
                  "name": "UpdatedPlantName",
                  "price": 200.0,
                  "quantity": 20
                }
                """;

        response = SerenityRest.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(body)
                .put("/api/plants/" + plantId);
    }

    @Step
    public void verifyUserCannotEditPlant() {
        response.then().statusCode(403);
    }
}
