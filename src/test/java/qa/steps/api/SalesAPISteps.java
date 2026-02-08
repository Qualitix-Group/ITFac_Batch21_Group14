package qa.steps.api;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import qa.utils.TestData;
import qa.utils.TestDataFactory;
import io.restassured.response.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SalesAPISteps {

    private static final int DEFAULT_SALE_QUANTITY = 1;

    private int createdSaleId;
    private int plantId;
    private int initialStock;
    private int soldQuantity = DEFAULT_SALE_QUANTITY;

    private void initIds() {
        // Dynamically create plant if needed (no hardcoded IDs)
        if (plantId == 0) {
            plantId = TestDataFactory.getOrCreatePlant();
        }
        if (ApiContext.getBaseUri() == null) {
            ApiContext.setBaseUri(TestData.get("base.url"));
        }
    }

    @Given("the plant has available stock")
    public void thePlantHasAvailableStock() {
        initIds();
        int stock = getPlantStock();
        assertTrue(stock > 0, "Precondition: plant stock must be positive");
    }

    @When("I record the initial plant stock")
    public void iRecordTheInitialPlantStock() {
        initIds();
        initialStock = getPlantStock();
        assertTrue(initialStock > 0, "Stock must be positive before selling");
    }

    @When("I send a POST request to create a sale with valid quantity")
    public void iSendAPOSTRequestToCreateASaleWithValidQuantity() {
        initIds();
        soldQuantity = DEFAULT_SALE_QUANTITY;

        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .formParam("quantity", soldQuantity)
        .when()
            .post("/api/sales/plant/" + plantId));

        if (ApiContext.getResponse().getStatusCode() == 200 || ApiContext.getResponse().getStatusCode() == 201) {
            createdSaleId = ApiContext.getResponse().then().extract().path("id");
            // Track for automatic cleanup
            if (createdSaleId > 0) {
                ApiContext.trackCreatedEntity("sales", createdSaleId);
            }
        }
    }

    @When("I send a POST request to create a sale with quantity {int}")
    public void iSendAPOSTRequestToCreateASaleWithQuantity(int quantity) {
        initIds();
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .formParam("quantity", quantity)
        .when()
            .post("/api/sales/plant/" + plantId));
    }

    @When("I send a POST request to create a sale with invalid plant id")
    public void iSendAPOSTRequestToCreateASaleWithInvalidPlantId() {
        initIds();
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .formParam("quantity", 1)
        .when()
            .post("/api/sales/plant/999999"));
    }

    @When("I send a POST request to create a sale without quantity")
    public void iSendAPOSTRequestToCreateASaleWithoutQuantity() {
        initIds();
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
        .when()
            .post("/api/sales/plant/" + plantId));
    }

    @When("I send a POST request to create a sale with empty body")
    public void iSendAPOSTRequestToCreateASaleWithEmptyBody() {
        initIds();
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .contentType("application/json")
            .body("{}")
        .when()
            .post("/api/sales/plant/" + plantId));
    }

    @When("I send a GET request to the created sale")
    public void iSendAGETRequestToTheCreatedSale() {
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
        .when()
            .get("/api/sales/" + createdSaleId));
    }

    @Given("no sales exist in the system")
    public void noSalesExistInTheSystem() {
        initIds();
        deleteAllSalesViaApi();
    }

    @Given("I have created a sale for testing")
    public void iHaveCreatedASaleForTesting() {
        initIds();
        createdSaleId = createSaleAsAdmin("1");
        // Track for automatic cleanup
        if (createdSaleId > 0) {
            ApiContext.trackCreatedEntity("sales", createdSaleId);
        }
    }

    @When("I send a DELETE request to delete the sale")
    public void iSendADELETERequestToDeleteTheSale() {
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
        .when()
            .delete("/api/sales/" + createdSaleId));
    }

    @When("I send a DELETE request to delete the created sale")
    public void iSendADELETERequestToDeleteTheCreatedSale() {
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
        .when()
            .delete("/api/sales/" + createdSaleId));
    }

    @Then("the plant stock should be reduced by the sold quantity")
    public void thePlantStockShouldBeReducedBySoldQuantity() {
        int afterStock = getPlantStock();

        // Note: Cleanup is now handled automatically by TestHooks via ApiContext.trackCreatedEntity()

        assertEquals(initialStock - soldQuantity, afterStock, "Stock should reduce by sold quantity");
    }

    @And("the response size should be greater than or equal to {int}")
    public void theResponseSizeShouldBeGreaterThanOrEqualTo(int minSize) {
        ApiContext.getResponse().then().body("size()", greaterThanOrEqualTo(minSize));
    }

    @And("sales should be sorted by soldAt descending")
    public void salesShouldBeSortedBySoldAtDescending() {
        List<String> timestamps = ApiContext.getResponse().then().extract().jsonPath().getList("soldAt", String.class);
        assertTrue(isDescending(timestamps), "Sales should be sorted by soldAt descending");
    }

    @Then("the sale response should contain required fields")
    public void theSaleResponseShouldContainRequiredFields() {
        ApiContext.getResponse().then()
            .body("id", is(greaterThanOrEqualTo(0)))
            .body("plant", notNullValue())
            .body("quantity", notNullValue())
            .body("totalPrice", notNullValue())
            .body("soldAt", notNullValue());
    }

    @Then("the sales list should be empty")
    public void theSalesListShouldBeEmpty() {
        List<?> list = ApiContext.getResponse().jsonPath().getList("");
        assertTrue(list == null || list.isEmpty(), "Sales list should be empty");
    }

    private int createSaleAsAdmin(String qty) {
        var resp = given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
            .formParam("quantity", qty)
        .when()
            .post("/api/sales/plant/" + plantId);

        return resp.then()
            .statusCode(anyOf(is(201), is(200)))
            .extract()
            .path("id");
    }

    private int getPlantStock() {
        var resp = given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
        .when()
            .get("/api/plants/" + plantId);
        assertEquals(200, resp.getStatusCode(), "Precondition failed: plant must exist");
        return resp.then().extract().path("quantity");
    }

    /** Delete all sales via API to force empty-list scenarios. */
    @SuppressWarnings("unchecked")
    private void deleteAllSalesViaApi() {
        String baseUrl = TestData.get("base.url");
        String token = qa.api.steps.AuthClient.getToken("admin.username", "admin.password");
        for (int attempt = 0; attempt < 5; attempt++) {
            Response resp = given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .get("/api/sales");
            if (resp.getStatusCode() != 200) return;
            List<java.util.Map<String, Object>> sales = resp.jsonPath().getList("");
            if (sales == null || sales.isEmpty()) return;
            for (var sale : sales) {
                Object idObj = sale.get("id");
                if (idObj == null) continue;
                int id = idObj instanceof Integer ? (Integer) idObj : Integer.parseInt(idObj.toString());
                given()
                    .baseUri(baseUrl)
                    .header("Authorization", "Bearer " + token)
                    .delete("/api/sales/" + id);
            }
        }
    }

    private boolean isDescending(List<String> values) {
        List<LocalDateTime> dates = new ArrayList<>();
        for (String raw : values) {
            LocalDateTime parsed = tryParse(raw);
            if (parsed != null) {
                dates.add(parsed);
            }
        }
        if (dates.size() <= 1) return true;
        for (int i = 0; i < dates.size() - 1; i++) {
            if (dates.get(i).isBefore(dates.get(i + 1))) return false;
        }
        return true;
    }

    private LocalDateTime tryParse(String raw) {
        if (raw == null) return null;
        DateTimeFormatter[] fmts = new DateTimeFormatter[] {
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm[:ss]"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm[:ss]")
        };
        for (DateTimeFormatter fmt : fmts) {
            try {
                return LocalDateTime.parse(raw, fmt);
            } catch (DateTimeParseException ignored) {}
        }
        return null;
    }
}
