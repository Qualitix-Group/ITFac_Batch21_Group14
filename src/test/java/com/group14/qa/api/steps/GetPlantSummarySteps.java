package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

public class GetPlantSummarySteps {

    private Response response;

    @Step
    public void getPlantSummary(String token) {
        response = SerenityRest.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .get("/api/plants/summary");
    }

    @Step
    public void verifyPlantSummaryResponse() {
        response.then()
                .statusCode(200)
                .body("totalPlants", greaterThanOrEqualTo(0))
                .body("lowStockPlants", greaterThanOrEqualTo(0));
    }
}
