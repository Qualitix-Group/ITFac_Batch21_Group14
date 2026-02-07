package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;


import static com.group14.qa.api.utils.PlantApiConstants.*;
import static org.hamcrest.Matchers.*;

public class AdminPlantDeletedCategorySteps {

    private Response response;

    @Step("Create plant with deleted category ID")
    public void createPlantWithDeletedCategoryId(int deletedCategoryId, String token) {

        String plantName = "TestPlant";

        String requestBody = """
                {
                  "name": "%s",
                  "price": 150.0,
                  "quantity": 25,
                  "category": {
                    "id": %d
                  }
                }
                """.formatted(plantName, deletedCategoryId);

        response = SerenityRest.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .post("/api/plants/category/" + deletedCategoryId);
    }

    @Step("Response status should be client error")
    public void verifyClientError() {
        response.then()
                .statusCode(anyOf(is(400), is(401), is(404)));
    }

    @Step("Verify no plant is created")
    public void verifyNoPlantCreated() {
        response.then()
                .body("id", nullValue())
                .body("name", nullValue());
    }

    public Response getResponse() {
        return response;
    }
}
