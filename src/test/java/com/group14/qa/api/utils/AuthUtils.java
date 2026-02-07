package com.group14.qa.api.utils;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.annotations.Step;

public class AuthUtils {

    private static String authToken;

    @Step("Authenticate as {0} with username: {1} and password: {2}")
    public static void authenticate(String role, String username, String password) {
        // Based on your Swagger documentation, you need to implement the actual login API call
        // This is a template - adjust according to your actual login endpoint
        String loginResponse = SerenityRest.given()
                .contentType("application/json")
                .body("{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}")
                .when()
                .post("http://localhost:8080/api/login")
                .then()
                .extract()
                .path("token"); // Adjust based on your actual response structure

        authToken = "Bearer " + loginResponse;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static void clearToken() {
        authToken = null;
    }
}