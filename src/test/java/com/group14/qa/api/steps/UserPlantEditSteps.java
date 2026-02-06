package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

public class UserPlantEditSteps {

    private Response response;

    @Step("User tries to edit plant with ID {0}")
    public void userEditsPlant(int plantId, String userToken) {

        String requestBody = """
                {
                  "name": "UpdatedPlantName",
                  "price": 200.0,
                  "quantity": 20
                }
                """;

        response = SerenityRest.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + userToken)
                .body(requestBody)
                .when()
                .put("/api/plants/" + plantId);

        response.then().log().all();
    }

    @Step("Verify user cannot edit plant - 403 Forbidden")
    public void verifyUserCannotEditPlant() {
        response.then()
                .statusCode(403)
                .body("status", equalTo(403))
                .body("error", equalTo("Forbidden"))
                .body("path", containsString("/api/plants"));
    }
}
