package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

public class UserPlantSteps {

    private Response response;

    @Step("User tries to create a plant under category {0}")
    public void userCreatesPlant(int categoryId, String userToken) {
        String plantName = "UserPlant_" + System.currentTimeMillis();

        // Ensure plant name is within valid length
        if (plantName.length() > 25) {
            plantName = plantName.substring(0, 25);
        }

        String requestBody = """
                {
                  "name": "%s",
                  "price": 100.0,
                  "quantity": 10,
                  "category": {
                    "id": %d
                  }
                }
                """.formatted(plantName, categoryId);

        response = SerenityRest.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + userToken)
                .body(requestBody)
                .when()
                .post("/api/plants/category/" + categoryId);

        response.then().log().all();
    }

    @Step("Verify user cannot add plant - 403 Forbidden")
    public void verifyUserCannotAddPlant() {
        response.then()
                .statusCode(403)
                .body("status", equalTo(403))
                .body("error", equalTo("Forbidden"))
                .body("path", containsString("/api/plants/category"));
    }

    @Step("Get the response")
    public Response getResponse() {
        return response;
    }
}
