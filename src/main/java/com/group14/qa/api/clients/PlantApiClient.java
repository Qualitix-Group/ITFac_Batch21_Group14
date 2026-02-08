package com.group14.qa.api.clients;

import com.group14.qa.api.models.PlantRequest;
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

    public Response getPlantsByCategoryIdWithoutAuth(Long categoryId) {
        return given()
                .baseUri(BASE_URL)
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

    public Response getAllPlantsWithoutAuth() {
        return given()
                .baseUri(BASE_URL)
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

    public Response getPlantByIdWithoutAuth(Long plantId) {
        return given()
                .baseUri(BASE_URL)
                .when()
                .get("/api/plants/{id}", plantId)
                .then()
                .extract()
                .response();
    }

    public Response createPlantUnderCategory(Long categoryId, PlantRequest plantRequest, String token) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", token)
                .contentType("application/json")
                .body(plantRequest)
                .when()
                .post("/api/plants/category/{categoryId}", categoryId)
                .then()
                .extract()
                .response();
    }

    public Response updatePlant(Long plantId, PlantRequest plantRequest, String token) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", token)
                .contentType("application/json")
                .body(plantRequest)
                .when()
                .put("/api/plants/{id}", plantId)
                .then()
                .extract()
                .response();
    }

    public Response getPlantsWithPagination(int page, int size, String token) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", token)
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get("/api/plants/paged")
                .then()
                .extract()
                .response();
    }

    public Response getPlantSummary(String token) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", token)
                .when()
                .get("/api/plants/summary")
                .then()
                .extract()
                .response();
    }
}