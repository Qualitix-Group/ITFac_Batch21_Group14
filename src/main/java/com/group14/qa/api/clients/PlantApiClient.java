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

    public Response deletePlantById(Long plantId, String token) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", token)
                .when()
                .delete("/api/plants/{id}", plantId)
                .then()
                .extract()
                .response();
    }

    public Response getAllPlants(String token) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", token)
                .when()
                .get("/api/plants")
                .then()
                .extract()
                .response();
    }

    public Response getPlantById(Long plantId, String token) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", token)
                .when()
                .get("/api/plants/{id}", plantId)
                .then()
                .extract()
                .response();
    }
}