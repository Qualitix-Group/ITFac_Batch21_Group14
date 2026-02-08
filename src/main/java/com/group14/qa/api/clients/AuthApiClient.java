package com.group14.qa.api.clients;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class AuthApiClient {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String LOGIN_ENDPOINT = "api/auth/login";

    public io.restassured.response.Response login(String username, String password) {
        return given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body("""
                    {
                      "username": "%s",
                      "password": "%s"
                    }
                    """.formatted(username, password))
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .extract()
                .response();
    }
}
