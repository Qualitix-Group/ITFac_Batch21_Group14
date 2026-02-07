package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

public class GetPlantWithoutAuthSteps {

    private Response response;

    @Step("User retrieves plant with id {0} without authentication")
    public void getPlantWithoutToken(int plantId) {

        response = SerenityRest.given()
                .contentType(ContentType.JSON)
                // ❌ No Authorization header
                .when()
                .get("/api/plants/" + plantId);

        response.then().log().all();
    }

    @Step("Verify unauthorized response")
    public void verifyUnauthorizedResponse() {

        response.then()
                .statusCode(401)
                .body("error", notNullValue())
                .body("message", notNullValue());
    }
}
