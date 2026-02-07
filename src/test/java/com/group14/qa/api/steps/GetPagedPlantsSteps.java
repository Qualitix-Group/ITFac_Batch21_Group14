package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

public class GetPagedPlantsSteps {

    private Response response;

    @Step
    public void getPlantsWithPagination(int page, int size, String token) {
        response = SerenityRest.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .queryParam("page", page)
                .queryParam("size", size)
                .get("/api/plants/paged");
    }

    @Step
    public void verifyPagedPlantsResponse(int pageSize) {
        response.then()
                .statusCode(200)
                .body("content.size()", lessThanOrEqualTo(pageSize))
                .body("totalElements", greaterThanOrEqualTo(0));
    }
}
