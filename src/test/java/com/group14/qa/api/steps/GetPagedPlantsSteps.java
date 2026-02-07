package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

public class GetPagedPlantsSteps {

    private Response response;

    @Step("User retrieves plants with pagination page={0}, size={1}")
    public void getPlantsWithPagination(int page, int size, String token) {
        response = SerenityRest.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get("/api/plants/paged");

        response.then().log().all();
    }

    @Step("Verify paged plants response")
    public void verifyPagedPlantsResponse(int pageSize) {
        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("content", notNullValue())
                .body("totalPages", greaterThanOrEqualTo(0))
                .body("totalElements", greaterThanOrEqualTo(0))
                .body("pageable", notNullValue())
                .body("content.size()", lessThanOrEqualTo(pageSize));
    }


}
