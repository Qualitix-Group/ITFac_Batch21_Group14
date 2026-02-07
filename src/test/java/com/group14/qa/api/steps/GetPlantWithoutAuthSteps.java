package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static com.group14.qa.api.utils.PlantApiConstants.*;
import static org.hamcrest.Matchers.*;

public class GetPlantWithoutAuthSteps {

    private Response response;

    @Step("Get plant by id {0} without authentication")
    public void getPlantWithoutToken(int plantId) {

        response = SerenityRest.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .pathParam("id", plantId)
                .when()
                .get(GET_PLANT_BY_ID);
    }

    @Step("Verify unauthorized response")
    public void verifyUnauthorizedResponse() {
        response.then()
                .statusCode(401)
                .body("error", notNullValue());
    }
}
