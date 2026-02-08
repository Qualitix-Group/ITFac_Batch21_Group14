package com.group14.qa.api.steps;

import com.group14.qa.api.clients.CategoryApiClient;
import com.group14.qa.common.AuthTokens;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;

public class AdminCategoryApiSteps {

    private final CategoryApiClient client = new CategoryApiClient();

    private Response createResponse;
    private Long createdCategoryId;

    // test data for TC_API_ADMIN_CAT_ADD_001
    private final String expectedName = "Anthurium";
    private final String expectedParent = "Plants";

    public void createCategoryWithValidData() {

        var requestBody = new java.util.LinkedHashMap<String, Object>();
        requestBody.put("name", expectedName);
        requestBody.put("parent", expectedParent);
        requestBody.put("subCategories", java.util.List.of("Indoor"));

        String token = AuthTokens.getAdminToken();

        createResponse = client.createCategory(requestBody, token);

        // 🔥 Always log response to see what backend returns
        System.out.println("Create Category Status: " + createResponse.getStatusCode());
        System.out.println("Create Category Body: " + createResponse.asString());

        // ✅ First make sure it's created
        createResponse.then().statusCode(201);

        // ✅ Safe extraction
        createdCategoryId = createResponse.jsonPath().getLong("id");
        if (createdCategoryId == null) {
            throw new AssertionError("Category created but response does not contain 'id'. Response: " + createResponse.asString());
        }
    }


    public void verify201AndResponseBody() {
        createResponse
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo(expectedName))
                .body("parent", equalTo(expectedParent))
                .body("subCategories", notNullValue())
                .body("subCategories", hasItem("Indoor"));
    }

    public void verifyCategoryStoredAndRetrievable() {
        // "Stored in DB" in API tests is commonly verified by retrieving it back.
        // If you have DB access, that would be an integration test; API-level proof is GET by id.
        Response getResponse = client.getCategoryById(createdCategoryId, AuthTokens.getAdminToken());

        getResponse
                .then()
                .statusCode(anyOf(is(200), is(201))) // depending on your backend, usually 200
                .body("id", equalTo(createdCategoryId.intValue())) // rest-assured may read as int
                .body("name", equalTo(expectedName))
                .body("parent", equalTo(expectedParent))
                .body("subCategories", hasItem("Indoor"));
    }
}
