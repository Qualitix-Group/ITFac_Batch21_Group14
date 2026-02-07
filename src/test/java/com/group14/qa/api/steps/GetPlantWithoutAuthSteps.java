package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

public class GetPlantWithoutAuthSteps {

    private Response response;

    @Step
    public void getPlantWithoutToken(int plantId) {
        response = SerenityRest.given()
                .contentType(ContentType.JSON)
                .get("/api/plants/" + plantId);
    }

    @Step
    public void verifyUnauthorizedResponse() {
        response.then()
                .statusCode(401)
                .body("error", notNullValue());
    }
}
