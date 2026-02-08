package qa.steps.api;

import io.cucumber.java.en.And;
import io.restassured.path.json.JsonPath;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DashboardAPISteps {

    @And("the category summary should contain counts")
    public void theCategorySummaryShouldContainCounts() {
        ApiContext.getResponse().then()
            .body("mainCategories", notNullValue())
            .body("subCategories", notNullValue());
    }

    @And("the response should be a list")
    public void theResponseShouldBeAList() {
        JsonPath json = ApiContext.getResponse().then().extract().jsonPath();
        List<?> list = json.getList("$");
        assertNotNull(list, "Response should be a JSON array/list");
    }

    @And("the paginated response should contain content and total elements")
    public void thePaginatedResponseShouldContainContentAndTotalElements() {
        JsonPath json = ApiContext.getResponse().then().extract().jsonPath();
        Object content = json.get("content");
        Object totalElements = json.get("totalElements");

        assertNotNull(content, "Paginated response should include content");
        assertNotNull(totalElements, "Paginated response should include totalElements");
        assertTrue(content instanceof List, "content should be a list");
    }
}
