package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static com.group14.qa.api.utils.PlantApiConstants.*;
import static org.hamcrest.Matchers.*;

public class GetPlantSummarySteps {

    private Response response;

    @Step("Get plant summary")
    public void getPlantSummary(String token) {

        response = SerenityRest.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(GET_PLANT_SUMMARY);
    }

    @Step("Verify plant summary response")
    public void verifyPlantSummaryResponse() {
        response.then()
                .statusCode(200)
                .body("totalPlants", greaterThanOrEqualTo(0))
                .body("lowStockPlants", greaterThanOrEqualTo(0));
    }
}
