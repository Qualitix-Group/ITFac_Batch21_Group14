package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static com.group14.qa.api.utils.PlantApiConstants.*;
import static org.hamcrest.Matchers.*;

public class AdminPlantNegativeSteps {

    private Response response;

    @Step("Admin attempts to create plant with invalid category id {0}")
    public void createPlantWithInvalidCategoryId(int categoryId, String token) {

        String requestBody = """
                {
                  "name": "TestPlant",
                  "price": 150.0,
                  "quantity": 25,
                  "category": {
                    "id": %d
                  }
                }
                """.formatted(categoryId);

        response = SerenityRest.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .pathParam("categoryId", categoryId)
                .body(requestBody)
                .when()
                .post(CREATE_PLANT_WITH_CATEGORY);
    }

    @Step("Verify API returns client error")
    public void verifyClientError() {
        response.then()
                .statusCode(anyOf(is(400), is(401), is(404)));
    }

    @Step("Verify no plant was created")
    public void verifyNoPlantCreated() {
        response.then()
                .body("id", nullValue())
                .body("name", nullValue());
    }

    public Response getResponse() {
        return response;
    }
}
