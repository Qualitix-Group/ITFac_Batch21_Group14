package com.group14.qa.api.steps;

import com.group14.qa.api.clients.CategoryApiClient;
import com.group14.qa.api.models.CreateCategoryRequest;
import com.group14.qa.api.models.ErrorResponse;
import com.group14.qa.common.AuthTokens;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;

import static org.junit.Assert.*;

public class CategoryCreateAuthApiSteps {

    private final CategoryApiClient client = new CategoryApiClient();

    private Response lastResponse;
    private ErrorResponse errorResponse;

    @Step("User tries to create category with name {0} and parentId {1}")
    public void userTriesToCreateCategory(String name, Long parentId) {

        String userAuth = AuthTokens.getUserToken();
        if (userAuth == null || userAuth.isBlank()) {
            throw new RuntimeException("User token is missing. Ensure @user hook login works.");
        }

        CreateCategoryRequest body = new CreateCategoryRequest(name, parentId);

        lastResponse = client.createCategory(userAuth, body);

        System.out.println("POST /api/categories Status: " + lastResponse.statusCode());
        System.out.println("Response: " + lastResponse.asString());

        if (lastResponse.statusCode() >= 400) {
            errorResponse = lastResponse.as(ErrorResponse.class); // safe due to ignoreUnknown
        } else {
            errorResponse = null;
        }
    }

    @Step("Verify forbidden access (403) for create category")
    public void verifyForbiddenCreate() {
        assertNotNull("Response should not be null", lastResponse);
        assertEquals("Expected 403 Forbidden", 403, lastResponse.statusCode());
    }
}
