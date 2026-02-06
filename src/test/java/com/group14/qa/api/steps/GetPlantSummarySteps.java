package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

public class GetPlantSummarySteps {

    private Response response;

    @Step("User requests plant summary")
    public void getPlantSummary(String token) {

        response = SerenityRest.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/plants/summary");

        response.then().log().all();
    }

    @Step("Verify plant summary response is valid")
    public void verifyPlantSummaryResponse() {

        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("totalPlants", notNullValue())
                .body("lowStockPlants", notNullValue())
                .body("totalPlants", greaterThanOrEqualTo(0))
                .body("lowStockPlants", greaterThanOrEqualTo(0));
    }
}
