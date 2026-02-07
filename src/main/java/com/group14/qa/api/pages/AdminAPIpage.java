package com.group14.qa.api.pages;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.pages.PageObject;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AdminAPIpage extends PageObject {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    private Response lastResponse;
    private String lastToken;

    public AdminAPIpage() {
        RestAssured.baseURI = BASE_URL;
    }

    @Step("Get Admin Bearer Token")
    public String getAdminToken() {
        if (lastToken != null && lastToken.startsWith("Bearer ")) {
            return lastToken;
        }

        try {
            Response response = RestAssured.given()
                    .contentType("application/json")
                    .body("{\"username\": \"" + ADMIN_USERNAME + "\", \"password\": \"" + ADMIN_PASSWORD + "\"}")
                    .when()
                    .post("/api/auth/login")
                    .then()
                    .extract()
                    .response();

            if (response.getStatusCode() == 200) {
                String token = response.jsonPath().getString("token");
                lastToken = "Bearer " + token;
                return lastToken;
            }
        } catch (Exception e) {
            System.out.println("Error getting admin token: " + e.getMessage());
        }

        // Fallback for testing - return a mock token
        return "Bearer mock-admin-token-for-testing";
    }

    @Step("Retrieve plants by category ID {0}")
    public void retrievePlantsByCategoryId(int categoryId, String token) {
        RequestSpecification request = RestAssured.given()
                .header("Authorization", token)
                .contentType("application/json");

        lastResponse = request.when()
                .get("/api/plants/category/" + categoryId)
                .then()
                .extract()
                .response();
    }

    @Step("Get last response status code")
    public int getLastResponseStatusCode() {
        return lastResponse != null ? lastResponse.getStatusCode() : 0;
    }

    @Step("Verify response contains plants list")
    public void verifyResponseContainsPlantsList() {
        assertThat(lastResponse).isNotNull();

        List<Map<String, Object>> plants = lastResponse.jsonPath().getList("$");

        assertThat(plants)
                .as("Response should contain a list of plants")
                .isNotNull();

        System.out.println("Found " + plants.size() + " plants in response");
    }

    @Step("Verify plants belong to category {0}")
    public void verifyPlantsBelongToCategory(int expectedCategoryId) {
        List<Map<String, Object>> plants = lastResponse.jsonPath().getList("$");

        assertThat(plants)
                .as("Plants list should not be null")
                .isNotNull();

        for (Map<String, Object> plant : plants) {
            Integer categoryId = plant.get("categoryId") != null ?
                    Integer.parseInt(plant.get("categoryId").toString()) : null;

            assertThat(categoryId)
                    .as("Plant should belong to category ID " + expectedCategoryId)
                    .isNotNull()
                    .isEqualTo(expectedCategoryId);
        }

        System.out.println("All " + plants.size() + " plants belong to category ID " + expectedCategoryId);
    }

    @Step("Verify category ID in response matches {0}")
    public void verifyCategoryIdInResponseMatches(int expectedCategoryId) {
        List<Map<String, Object>> plants = lastResponse.jsonPath().getList("$");

        if (plants != null && !plants.isEmpty()) {
            Map<String, Object> firstPlant = plants.get(0);
            if (firstPlant.get("categoryId") != null) {
                Integer actualCategoryId = Integer.parseInt(firstPlant.get("categoryId").toString());

                assertThat(actualCategoryId)
                        .as("Category ID in response should match requested category ID")
                        .isEqualTo(expectedCategoryId);
            }
        }
    }

    @Step("Verify Content-Type is JSON")
    public void verifyContentTypeIsJson() {
        String contentType = lastResponse.getHeader("Content-Type");

        assertThat(contentType)
                .as("Content-Type should be application/json, but was: " + contentType)
                .isNotNull()
                .contains("application/json");
    }
}