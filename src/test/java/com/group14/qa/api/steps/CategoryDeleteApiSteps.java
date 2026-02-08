package com.group14.qa.api.steps;

import com.group14.qa.api.clients.CategoryApiClient;
import com.group14.qa.api.models.ErrorResponse;
import com.group14.qa.common.AuthTokens;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;

import static org.junit.Assert.*;

public class CategoryDeleteApiSteps {

    private final CategoryApiClient client = new CategoryApiClient();

    private Response lastResponse;
    private ErrorResponse errorResponse;

    @Step("User deletes category id {0}")
    public void userDeletesCategory(Long id) {

        String userAuth = AuthTokens.getUserToken();
        if (userAuth == null || userAuth.isBlank()) {
            throw new RuntimeException("User token is missing. Ensure @user hook login works.");
        }

        lastResponse = client.deleteCategory(id, userAuth);

        System.out.println("DELETE /api/categories/" + id + " Status: " + lastResponse.statusCode());
        System.out.println("Response: " + lastResponse.asString());

        if (lastResponse.statusCode() >= 400) {
            errorResponse = lastResponse.as(ErrorResponse.class);
        } else {
            errorResponse = null;
        }
    }

    @Step("Verify forbidden access (403) for delete")
    public void verifyForbiddenDelete() {
        assertNotNull("Response should not be null", lastResponse);
        assertEquals("Expected 403 Forbidden", 403, lastResponse.statusCode());
    }
}
