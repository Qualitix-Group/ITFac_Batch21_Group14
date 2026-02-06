package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

public class AdminPlantNegativeSteps {

    private Response response;

    @Step("Create plant with invalid category ID")
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
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .post("/api/plants/category/" + categoryId);
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
