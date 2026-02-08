package com.group14.qa.api.clients;
import com.group14.qa.api.models.UpdateCategoryRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import java.util.HashMap;
import java.util.Map;
import com.group14.qa.api.models.CreateCategoryRequest;



public class CategoryApiClient {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String CATEGORY_BY_ID = "/api/categories/{id}";
    private static final String CREATE_CATEGORY = "/api/categories";



    public Response getCategoryById(Long id, String token) {
    return given()
            .baseUri(BASE_URL)
            .header("Authorization", token)
            .when()
            .get("/api/categories/{id}", id)
            .then()
            .extract()
            .response();
}


    public Response createCategory(Object categoryBody, String token) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", token)   // ✅ token already has Bearer
                .contentType("application/json")
                .body(categoryBody)
                .log().headers()
                .log().body()
                .when()
                .post("/api/categories")
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response updateCategory(Long id, String authHeader, String name, Long parentId) {

        Map<String, Object> body = new HashMap<>();
        body.put("name", name);

        // If parentId is null, we simply DON'T send it (makes it a main/root category)
        // If your backend requires explicit null, tell me and I’ll change it to body.put("parentId", null)
        if (parentId != null) {
            body.put("parentId", parentId);
        }

        var req = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(body);

        if (authHeader != null && !authHeader.isBlank()) {
            req.header("Authorization", authHeader);
        }

        return req.when()
                .put(CATEGORY_BY_ID, id)
                .then()
                .extract()
                .response();
    }

    public Response createCategory(String authHeader, CreateCategoryRequest body) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("name", body.getName());
        payload.put("parentId", body.getParentId()); // can be null

        var req = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload);

        if (authHeader != null && !authHeader.isBlank()) {
            req.header("Authorization", authHeader);
        }

        return req.when()
                .post(CREATE_CATEGORY)
                .then()
                .extract()
                .response();
    }
    public Response deleteCategory(Long id, String authHeader) {

        var req = given()
                .baseUri(BASE_URL)
                .accept(ContentType.JSON);

        if (authHeader != null && !authHeader.isBlank()) {
            req.header("Authorization", authHeader);
        }

        return req.when()
                .delete("/api/categories/{id}", id)
                .then()
                .extract()
                .response();
    }

    public Response searchCategoriesPage(String authHeader,
                                         Integer page,
                                         Integer size,
                                         String name,
                                         Long parentId,
                                         String sortField,
                                         String sortDir) {

        var req = given()
                .baseUri(BASE_URL)
                .accept(ContentType.JSON)
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sortField", sortField)
                .queryParam("sortDir", sortDir);

        if (name != null && !name.isBlank()) {
            req.queryParam("name", name);
        }
        if (parentId != null) {
            req.queryParam("parentId", parentId);
        }

        if (authHeader != null && !authHeader.isBlank()) {
            req.header("Authorization", authHeader);
        }

        return req.when()
                .get("/api/categories/page")
                .then()
                .extract()
                .response();
    }




}
