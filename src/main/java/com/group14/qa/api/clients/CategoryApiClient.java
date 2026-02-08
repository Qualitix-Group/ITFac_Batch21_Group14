package com.group14.qa.api.clients;

import com.group14.qa.common.AuthTokens;

import static io.restassured.RestAssured.given;

public class CategoryApiClient {

    private static final String BASE_URL = "http://localhost:8080";

    public io.restassured.response.Response getCategoryById(Long id, String token) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", token)
                .when()
                .get("/api/categories/{id}", id)
                .then()
                .extract()
                .response();
    }
}
