package com.group14.qa.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import java.util.Map;
import java.util.HashMap;

import static org.hamcrest.Matchers.*;

public class AdminPlantUpdateSteps {

    private Response response;
    private int lastUpdatedPlantId;
    private Map<String, Object> lastUpdateData = new HashMap<>();

    @Step("Admin updates plant with ID {0}")
    public void updatePlant(int plantId, String token, String name, float price, int quantity, int categoryId) {
        lastUpdatedPlantId = plantId;
        lastUpdateData.put("name", name);
        lastUpdateData.put("price", price);
        lastUpdateData.put("quantity", quantity);
        lastUpdateData.put("categoryId", categoryId);

        String requestBody = """
                {
                  "name": "%s",
                  "price": %f,
                  "quantity": %d,
                  "category": {
                    "id": %d
                  }
                }
                """.formatted(name, price, quantity, categoryId);

        response = SerenityRest.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .put("/api/plants/" + plantId);

        response.then().log().all();
    }

    @Step("Admin updates plant with ID {0} with minimal data")
    public void updatePlantWithMinimalData(int plantId, String token, String name) {
        lastUpdatedPlantId = plantId;
        lastUpdateData.put("name", name);

        // Only update name, keep other fields as they are
        // Note: This may depend on API requirements
        String requestBody = """
                {
                  "name": "%s"
                }
                """.formatted(name);

        response = SerenityRest.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .put("/api/plants/" + plantId);

        response.then().log().all();
    }

    @Step("Verify plant update successful")
    public void verifyPlantUpdateSuccessful() {
        response.then()
                .statusCode(200)
                .body("id", equalTo(lastUpdatedPlantId))
                .body("name", equalTo(lastUpdateData.get("name")))
                .body("price", equalTo(lastUpdateData.get("price")))
                .body("quantity", equalTo(lastUpdateData.get("quantity")))
                .body("category.id", equalTo(lastUpdateData.get("categoryId")));
    }

    @Step("Verify plant update response structure")
    public void verifyPlantUpdateResponseStructure() {
        response.then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", notNullValue())
                .body("price", notNullValue())
                .body("quantity", notNullValue())
                .body("category", notNullValue())
                .body("category.id", notNullValue())
                .body("category.name", notNullValue());
    }

    @Step("Verify plant update response matches sample format")
    public void verifySampleResponseFormat() {
        // Sample response format from test case:
        // {
        //   "id": 0,
        //   "name": "Anthurium",
        //   "price": 150,
        //   "quantity": 25,
        //   "category": {
        //     "id": 0,
        //     "name": "Anthurium",
        //     "parent": "string",
        //     "subCategories": ["string"]
        //   }
        // }

        response.then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", notNullValue())
                .body("price", notNullValue())
                .body("quantity", notNullValue())
                .body("category", notNullValue())
                .body("category.id", notNullValue())
                .body("category.name", notNullValue());
    }

    @Step("Verify plant details are updated correctly")
    public void verifyPlantDetailsUpdated() {
        response.then()
                .statusCode(200)
                .body("name", equalTo(lastUpdateData.get("name")))
                .body("price", equalTo(Float.parseFloat(lastUpdateData.get("price").toString())))
                .body("quantity", equalTo(Integer.parseInt(lastUpdateData.get("quantity").toString())));
    }

    @Step("Get the response")
    public Response getResponse() {
        return response;
    }

    @Step("Get the updated plant ID")
    public int getUpdatedPlantId() {
        return lastUpdatedPlantId;
    }

    @Step("Print update response details")
    public void printResponseDetails() {
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
        System.out.println("Updated Plant ID: " + lastUpdatedPlantId);
        System.out.println("Updated Name: " + lastUpdateData.get("name"));
        System.out.println("Updated Price: " + lastUpdateData.get("price"));
        System.out.println("Updated Quantity: " + lastUpdateData.get("quantity"));
    }

    @Step("Create a plant for testing update")
    public int createPlantForUpdate(String token, int categoryId) {
        String plantName = "PlantForUpdate_" + System.currentTimeMillis();
        if (plantName.length() > 25) {
            plantName = plantName.substring(0, 25);
        }

        String requestBody = """
                {
                  "name": "%s",
                  "price": 100.0,
                  "quantity": 10,
                  "category": {
                    "id": %d
                  }
                }
                """.formatted(plantName, categoryId);

        Response createResponse = SerenityRest.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .post("/api/plants/category/" + categoryId);

        createResponse.then().log().all();

        if (createResponse.getStatusCode() == 201) {
            int plantId = createResponse.path("id");
            System.out.println("Created plant for update with ID: " + plantId);
            return plantId;
        } else {
            throw new RuntimeException("Failed to create plant for update: " + createResponse.getBody().asString());
        }
    }

    @Step("Admin updates plant with ID {0} without changing category")
    public void updatePlantWithoutCategory(int plantId, String token, String name, float price, int quantity) {
        lastUpdatedPlantId = plantId;
        lastUpdateData.put("name", name);
        lastUpdateData.put("price", price);
        lastUpdateData.put("quantity", quantity);

        // Request body without category field
        String requestBody = String.format(
                "{\"name\": \"%s\", \"price\": %.2f, \"quantity\": %d}",
                name, price, quantity
        );

        response = SerenityRest.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .put("/api/plants/" + plantId);

        response.then().log().all();
    }

    @Step("Admin updates plant name only for ID {0}")
    public void updatePlantNameOnly(int plantId, String token, String name) {
        lastUpdatedPlantId = plantId;
        lastUpdateData.put("name", name);

        // Request body with only name field
        String requestBody = String.format("{\"name\": \"%s\"}", name);

        response = SerenityRest.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .put("/api/plants/" + plantId);

        response.then().log().all();
    }

    @Step("Verify partial update successful (without category)")
    public void verifyPartialUpdateSuccessful() {
        response.then()
                .statusCode(200)
                .body("id", equalTo(lastUpdatedPlantId))
                .body("name", equalTo(lastUpdateData.get("name")))
                .body("price", equalTo(lastUpdateData.get("price")))
                .body("quantity", equalTo(lastUpdateData.get("quantity")));
        // Note: Not checking category since we didn't update it
    }
}