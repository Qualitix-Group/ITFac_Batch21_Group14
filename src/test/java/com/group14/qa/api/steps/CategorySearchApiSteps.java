package com.group14.qa.api.steps;

import com.group14.qa.api.clients.CategoryApiClient;
import com.group14.qa.api.models.CategoryResponse;
import com.group14.qa.common.AuthTokens;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CategorySearchApiSteps {

    private final CategoryApiClient client = new CategoryApiClient();

    private Response lastResponse;
    private List<CategoryResponse> results = new ArrayList<>();

    @Step("User searches categories by name {0} on page {1} size {2}")
    public void userSearchesByName(String name, int page, int size) {

        String userAuth = AuthTokens.getUserToken();
        if (userAuth == null || userAuth.isBlank()) {
            throw new RuntimeException("User token is missing. Ensure @user hook login works.");
        }

        lastResponse = client.searchCategoriesPage(
                userAuth,
                page,
                size,
                name,
                null,
                "id",
                "asc"
        );

        System.out.println("GET /api/categories/page Status: " + lastResponse.statusCode());
        System.out.println("Response: " + lastResponse.asString());

        results = extractCategoryResults(lastResponse);
    }

    @Step("Verify search response is 200 and all returned category names contain {0}")
    public void verifySearchResultsMatchName(String expectedNamePart) {
        assertNotNull("Response should not be null", lastResponse);
        assertEquals("Expected 200 OK", 200, lastResponse.statusCode());

        // It's okay if result is empty, still 200.
        assertNotNull("Results list should not be null", results);

        for (CategoryResponse c : results) {
            assertNotNull("Category name should not be null", c.getName());
            assertTrue(
                    "Category name should contain '" + expectedNamePart + "' but was '" + c.getName() + "'",
                    c.getName().toLowerCase().contains(expectedNamePart.toLowerCase())
            );
        }
    }

    private List<CategoryResponse> extractCategoryResults(Response response) {

        List<CategoryResponse> out = new ArrayList<>();
        String raw = response.asString().trim();

        if (raw.startsWith("[")) {
            CategoryResponse[] arr = response.as(CategoryResponse[].class);
            for (CategoryResponse c : arr) out.add(c);
            return out;
        }

        JsonPath jp = response.jsonPath();

        try {
            Object content = jp.get("content");
            if (content != null) {
                CategoryResponse[] arr = jp.getObject("content", CategoryResponse[].class);
                if (arr != null) {
                    for (CategoryResponse c : arr) out.add(c);
                }
                return out;
            }
        } catch (Exception ignored) {

        }

        try {
            CategoryResponse single = response.as(CategoryResponse.class);
            if (single != null && single.getName() != null) {
                out.add(single);
            }
        } catch (Exception ignored) {
        }

        return out;
    }

    @Step("User filters categories by parentId {0} on page {1} size {2}")
    public void userFiltersByParentId(Long parentId, int page, int size) {

        String userAuth = AuthTokens.getUserToken();
        if (userAuth == null || userAuth.isBlank()) {
            throw new RuntimeException("User token is missing. Ensure @user hook login works.");
        }

        lastResponse = client.searchCategoriesPage(
                userAuth,
                page,
                size,
                null,
                parentId,
                "id",
                "asc"
        );

        System.out.println("GET /api/categories/page Status: " + lastResponse.statusCode());
        System.out.println("Response: " + lastResponse.asString());

        results = extractCategoryResults(lastResponse);
    }

    @Step("Verify filter response is 200 and results are not empty")
    public void verifyFilterHasData() {
        assertNotNull("Response should not be null", lastResponse);
        assertEquals("Expected 200 OK", 200, lastResponse.statusCode());
        assertNotNull("Results list should not be null", results);

        assertFalse("Expected some categories for given parentId, but got empty list", results.isEmpty());
    }

    @Step("User requests categories page {0} size {1} without filters")
    public void userRequestsOnlyPage(int page, int size) {

        String userAuth = AuthTokens.getUserToken();
        if (userAuth == null || userAuth.isBlank()) {
            throw new RuntimeException("User token is missing. Ensure @user hook login works.");
        }

        lastResponse = client.searchCategoriesPage(
                userAuth,
                page,
                size,
                null,
                null,
                "id",
                "asc"
        );

        System.out.println("GET /api/categories/page Status: " + lastResponse.statusCode());
        System.out.println("Response: " + lastResponse.asString());

        results = extractCategoryResults(lastResponse);
    }

    @Step("Verify page response is 200 and contains no category data")
    public void verifyEmptyPage() {
        assertNotNull("Response should not be null", lastResponse);
        assertEquals("Expected 200 OK", 200, lastResponse.statusCode());
        assertNotNull("Results list should not be null", results);
        assertTrue("Expected empty results for page with no data, but got: " + results.size(), results.isEmpty());
    }


}
