package com.group14.qa.api.steps;

import com.group14.qa.api.clients.CategoryApiClient;
import com.group14.qa.api.models.CategoryResponse;
import com.group14.qa.api.models.CreateCategoryRequest;
import com.group14.qa.api.models.ErrorResponse;
import com.group14.qa.common.AuthTokens;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;

import static org.junit.Assert.*;

public class CategoryCreateApiSteps {

    private final CategoryApiClient client = new CategoryApiClient();

    private Response lastResponse;
    private CategoryResponse categoryResponse;
    private ErrorResponse errorResponse;

    @Step("Admin creates a category with name {0} and parentId {1}")
    public void adminCreatesCategory(String name, Long parentId) {

        String adminAuth = AuthTokens.getAdminToken();
        if (adminAuth == null || adminAuth.isBlank()) {
            throw new RuntimeException("Admin token missing. Ensure @admin hook runs and login succeeds.");
        }

        CreateCategoryRequest body = new CreateCategoryRequest(name, parentId);

        lastResponse = client.createCategory(adminAuth, body);

        System.out.println("POST /api/categories Status: " + lastResponse.statusCode());
        System.out.println("Response: " + lastResponse.asString());

        if (lastResponse.statusCode() >= 200 && lastResponse.statusCode() < 300) {
            categoryResponse = lastResponse.as(CategoryResponse.class);
            errorResponse = null;
        } else {
            errorResponse = lastResponse.as(ErrorResponse.class);
            categoryResponse = null;
        }
    }

    @Step("Verify category created successfully (201) with name {0}")
    public void verifyCategoryCreated(String expectedName) {
        assertNotNull("Response should not be null", lastResponse);
        assertEquals("Expected 201 Created", 201, lastResponse.statusCode());

        assertNotNull("Category response should be parsed", categoryResponse);
        assertNotNull("Created category id should not be null", categoryResponse.getId());
        assertEquals("Category name mismatch", expectedName, categoryResponse.getName());
    }

    @Step("Verify create category error status {0}")
    public void verifyCreateCategoryError(int expectedStatus) {
        assertNotNull("Response should not be null", lastResponse);
        assertEquals("Wrong status code", expectedStatus, lastResponse.statusCode());
        assertNotNull("Error response should be parsed", errorResponse);
    }

}
