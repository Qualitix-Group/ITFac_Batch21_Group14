package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

public class AdminPlantDeletedCategorySteps {

    private Response response;
    private String lastUsedPlantName;

    @Step("Admin creates a plant with deleted category ID {0}")
    public void createPlantWithDeletedCategoryId(int deletedCategoryId, String token) {
        String plantName = "TestPlant_Del_" + System.currentTimeMillis();
        lastUsedPlantName = plantName;

        // Ensure plant name is within valid length (3-25 characters)
        if (plantName.length() > 25) {
            plantName = plantName.substring(0, 25);
        }

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

        response.then().log().all();
    }

    @Step("Admin creates a plant with deleted category ID {0} and name {1}")
    public void createPlantWithDeletedCategoryIdAndName(int deletedCategoryId, String plantName, String token) {
        lastUsedPlantName = plantName;

        // Ensure plant name is within valid length (3-25 characters)
        if (plantName.length() > 25) {
            plantName = plantName.substring(0, 25);
        }

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

        response.then().log().all();
    }

    @Step("Verify error response for deleted category ID")
    public void verifyDeletedCategoryError() {
        int statusCode = response.getStatusCode();

        System.out.println("Actual Status Code: " + statusCode);

        if (statusCode == 400) {
            response.then()
                    .statusCode(400)
                    .body("status", notNullValue())
                    .body("error", notNullValue())
                    .body("message", notNullValue())
                    .body("timestamp", notNullValue());
        } else if (statusCode == 401) {
            response.then()
                    .statusCode(401)
                    .body("status", equalTo(401))
                    .body("error", equalTo("UNAUTHORIZED"))
                    .body("message", notNullValue())
                    .body("timestamp", notNullValue());
        } else if (statusCode == 404) {
            response.then()
                    .statusCode(404)
                    .body("status", equalTo(404))
                    .body("error", notNullValue())
                    .body("message", notNullValue())
                    .body("timestamp", notNullValue());
        } else {
            // Fail for unexpected status codes
            response.then().statusCode(400);
        }
    }

    @Step("Verify error response matches documented sample format")
    public void verifyDocumentedErrorFormat() {
        // Based on test case documentation:
        // {
        //   "status": 0,
        //   "error": "string",
        //   "message": "string",
        //   "timestamp": "2026-01-18T18:52:35.001Z"
        // }
        response.then()
                .statusCode(400)
                .body("status", equalTo(0))
                .body("error", notNullValue())
                .body("message", notNullValue())
                .body("timestamp", notNullValue());
    }

    @Step("Verify error response for 400 Bad Request")
    public void verify400BadRequestError() {
        response.then()
                .statusCode(400)
                .body("status", equalTo(400))
                .body("error", notNullValue())
                .body("message", notNullValue())
                .body("timestamp", notNullValue());
    }

    @Step("Verify no new plant was created")
    public void verifyNoPlantCreated() {
        response.then()
                .body("id", nullValue())
                .body("name", nullValue());
    }

    @Step("Get the response")
    public Response getResponse() {
        return response;
    }

    @Step("Print response details")
    public void printResponseDetails() {
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
        System.out.println("Error: " + response.path("error"));
        System.out.println("Message: " + response.path("message"));
        System.out.println("Timestamp: " + response.path("timestamp"));
    }

    @Step("Verify plant creation is rejected")
    public void verifyPlantCreationRejected() {
        int statusCode = response.getStatusCode();

        // Accept any error status code (400, 401, 404, etc.)
        if (statusCode >= 400 && statusCode < 500) {
            System.out.println("✓ Plant creation correctly rejected with status: " + statusCode);
            response.then()
                    .statusCode(statusCode)
                    .body("status", notNullValue())
                    .body("error", notNullValue())
                    .body("message", notNullValue());
        } else {
            throw new AssertionError("Expected error status (4xx) but got: " + statusCode);
        }
    }
}