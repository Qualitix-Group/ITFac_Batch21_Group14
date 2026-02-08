package com.group14.qa.api.steps;

import com.group14.qa.api.clients.CategoryApiClient;
import com.group14.qa.api.models.CategoryResponse;
import com.group14.qa.api.models.ErrorResponse;
import com.group14.qa.api.models.UpdateCategoryRequest;
import com.group14.qa.common.AuthTokens;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;

import static org.junit.Assert.*;

public class CategoryUpdateApiSteps {

    private final CategoryApiClient client = new CategoryApiClient();

    private Response lastResponse;
    private CategoryResponse categoryResponse;
    private ErrorResponse errorResponse;

    @Step("Admin updates category id {0} with name {1} and parentId {2}")
    public void adminUpdatesCategory(Long id, String name, Long parentId) {

        String adminAuth = AuthTokens.getAdminToken();
        if (adminAuth == null || adminAuth.isBlank()) {
            throw new RuntimeException("Admin token is missing. Ensure @admin hook login works.");
        }

        UpdateCategoryRequest body = new UpdateCategoryRequest(name, parentId);

        lastResponse = client.updateCategory(id, adminAuth, name, parentId);

        System.out.println("PUT /api/categories/" + id + " Status: " + lastResponse.statusCode());
        System.out.println("Response: " + lastResponse.asString());

        if (lastResponse.statusCode() >= 200 && lastResponse.statusCode() < 300) {
            categoryResponse = lastResponse.as(CategoryResponse.class);
            errorResponse = null;
        } else {
            errorResponse = lastResponse.as(ErrorResponse.class);
            categoryResponse = null;
        }
    }

    @Step("Verify update category success with status 200 and name {0}")
    public void verifyUpdateSuccess(String expectedName) {
        assertNotNull("Response should not be null", lastResponse);
        assertEquals("Expected 200 OK", 200, lastResponse.statusCode());

        assertNotNull("Category response should be parsed", categoryResponse);
        assertNotNull("Updated category id should not be null", categoryResponse.getId());
        assertEquals("Updated name mismatch", expectedName, categoryResponse.getName());

    }

    @Step("Verify error response status {0}")
    public void verifyErrorStatus(int expectedStatus) {
        assertNotNull("Response should not be null", lastResponse);
        assertEquals("Wrong status code", expectedStatus, lastResponse.statusCode());
        assertNotNull("Error response should be parsed", errorResponse);
    }

    @Step("Verify self-parent validation error (400) and message contains {0}")
    public void verifySelfParentValidationError(String expectedMessagePart) {
        assertNotNull("Response should not be null", lastResponse);
        assertEquals("Expected 400 Bad Request", 400, lastResponse.statusCode());

        assertNotNull("Error response should be parsed", errorResponse);
        assertNotNull("Error message should not be null", errorResponse.getMessage());
        assertTrue(
                "Expected error message to contain: " + expectedMessagePart + " but was: " + errorResponse.getMessage(),
                errorResponse.getMessage().toLowerCase().contains(expectedMessagePart.toLowerCase())
        );
    }

    @Step("Verify not found error (404) and message contains {0}")
    public void verifyNotFoundError(String expectedMessagePart) {
        assertNotNull("Response should not be null", lastResponse);
        assertEquals("Expected 404 Not Found", 404, lastResponse.statusCode());

        assertNotNull("Error response should be parsed", errorResponse);
        assertNotNull("Error message should not be null", errorResponse.getMessage());

        assertTrue(
                "Expected error message to contain: " + expectedMessagePart + " but was: " + errorResponse.getMessage(),
                errorResponse.getMessage().toLowerCase().contains(expectedMessagePart.toLowerCase())
        );
    }
    @Step("{0} updates category id {1} with name {2} and parentId {3}")
    public void updateCategoryAsRole(String role, Long id, String name, Long parentId) {

        String authHeader = null;

        if ("admin".equalsIgnoreCase(role)) {
            authHeader = AuthTokens.getAdminToken();
        } else if ("user".equalsIgnoreCase(role)) {
            authHeader = AuthTokens.getUserToken();
        } // anonymous => null

        if (!"anonymous".equalsIgnoreCase(role) && (authHeader == null || authHeader.isBlank())) {
            throw new RuntimeException(role + " token is missing. Ensure @" + role + " hook login works.");
        }

        var body = new com.group14.qa.api.models.UpdateCategoryRequest(name, parentId);

        lastResponse = client.updateCategory(id, authHeader, name, parentId);

        System.out.println("PUT /api/categories/" + id + " Status: " + lastResponse.statusCode());
        System.out.println("Response: " + lastResponse.asString());

        if (lastResponse.statusCode() >= 200 && lastResponse.statusCode() < 300) {
            categoryResponse = lastResponse.as(com.group14.qa.api.models.CategoryResponse.class);
            errorResponse = null;
        } else {
            errorResponse = lastResponse.as(com.group14.qa.api.models.ErrorResponse.class);
            categoryResponse = null;
        }
    }
    @Step("Verify forbidden access (403)")
    public void verifyForbidden() {
        assertNotNull("Response should not be null", lastResponse);
        assertEquals("Expected 403 Forbidden", 403, lastResponse.statusCode());
    }





}
