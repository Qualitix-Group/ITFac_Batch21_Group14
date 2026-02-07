package com.group14.qa.api.clients;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PlantApiClient {

    private static final String BASE_URL = "http://localhost:8080";

    public Response getPlantsByCategoryId(Long categoryId, String token) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", token)
                .when()
                .get("/api/plants/category/{categoryId}", categoryId)
                .then()
                .extract()
                .response();
    }
}