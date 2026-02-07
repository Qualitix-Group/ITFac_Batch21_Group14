package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static com.group14.qa.api.utils.PlantApiConstants.*;
import static org.hamcrest.Matchers.*;

public class UserPlantEditSteps {

    private Response response;

    @Step("User attempts to edit plant with id {0}")
    public void userEditsPlant(int plantId, String token) {

        String body = """
                {
                  "name": "UpdatedPlantName",
                  "price": 200.0,
                  "quantity": 20
                }
                """;

        response = SerenityRest.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .pathParam("id", plantId)
                .body(body)
                .when()
                .put(UPDATE_PLANT_BY_ID);
    }

    @Step("Verify user cannot edit plant")
    public void verifyUserCannotEditPlant() {
        response.then()
                .statusCode(403);
    }
}
