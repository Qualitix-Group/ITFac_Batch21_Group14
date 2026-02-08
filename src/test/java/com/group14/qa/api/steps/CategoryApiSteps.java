//package com.group14.qa.api.steps;
//
//import com.group14.qa.api.clients.CategoryApiClient;
//import com.group14.qa.api.models.CategoryResponse;
//import com.group14.qa.common.AuthTokens;
//import net.serenitybdd.annotations.Step;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class CategoryApiSteps {
//
//    CategoryApiClient client = new CategoryApiClient();
//    private io.restassured.response.Response response;
//
//    @Step("User requests category by id {0}")
//    public void getCategoryById(Long id, String role) {
//
//        String token = role.equals("admin")
//                ? AuthTokens.getAdminToken()
//                : AuthTokens.getUserToken();
//
//        response = client.getCategoryById(id, token);
//    }
//
//    @Step("Verify category is returned successfully")
//    public void verifyCategorySuccess() {
//
//        CategoryResponse category = response.as(CategoryResponse.class);
//
//        assertThat(response.statusCode()).isEqualTo(200);
//        assertThat(category.getId()).isNotNull();
//        assertThat(category.getName()).isNotBlank();
//    }
//
//    @Step("Verify error response with status {0}")
//    public void verifyErrorResponse(int statusCode) {
//        assertThat(response.statusCode()).isEqualTo(statusCode);
//    }
//}


package com.group14.qa.api.steps;

import com.group14.qa.api.clients.CategoryApiClient;
import com.group14.qa.api.models.CategoryResponse;
import com.group14.qa.api.models.ErrorResponse;
import com.group14.qa.common.AuthTokens;
import net.serenitybdd.annotations.Step;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryApiSteps {

    CategoryApiClient client = new CategoryApiClient();
    private io.restassured.response.Response response;

    @Step("{string} requests category by id {0}")
    public void getCategoryById(Long id, String role) {
        String token = getTokenByRole(role);
        System.out.println("Using token for " + role + ": " +
                (token != null ? token.substring(0, Math.min(20, token.length())) + "..." : "null"));

        response = client.getCategoryById(id, token);

        // Log response for debugging
        System.out.println("Response Status: " + response.statusCode());
        System.out.println("Response Body: " + response.asString());
    }

    @Step("Verify category is returned successfully")
    public void verifyCategorySuccess() {
        assertThat(response.statusCode()).isEqualTo(200);

        CategoryResponse category = response.as(CategoryResponse.class);
        assertThat(category.getId()).isNotNull();
        assertThat(category.getName()).isNotBlank();

        // Optional: log the category details
        System.out.println("Retrieved Category - ID: " + category.getId() +
                ", Name: " + category.getName());
    }

    @Step("Verify error response with status {0}")
    public void verifyErrorResponse(int expectedStatusCode) {
        assertThat(response.statusCode()).isEqualTo(expectedStatusCode);

        // If you want to verify error response structure
        if (expectedStatusCode >= 400) {
            ErrorResponse error = response.as(ErrorResponse.class);
            System.out.println("Error Response - Status: " + error.getStatus() +
                    ", Message: " + error.getMessage());
        }
    }

    @Step("Verify unauthorized access")
    public void verifyUnauthorizedAccess() {
        assertThat(response.statusCode()).isEqualTo(401);
        System.out.println("Access unauthorized as expected");
    }

    private String getTokenByRole(String role) {
        switch (role.toLowerCase()) {
            case "admin":
                return AuthTokens.getAdminToken();
            case "user":
                return AuthTokens.getUserToken();
            case "anonymous":
                return null; // No token for anonymous access
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}