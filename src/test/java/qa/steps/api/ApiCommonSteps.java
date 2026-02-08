package qa.steps.api;


import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Shared API glue to avoid duplicate step definitions and share state via {@link ApiContext}.
 */
public class ApiCommonSteps {

    @Before(value = "@api", order = 0)
    public void resetApiContext() {
        ApiContext.reset();
    }

    @Given("I am authenticated as admin via API")
    public void iAmAuthenticatedAsAdminViaAPI() {
        ApiContext.authenticateAsAdmin();
    }

    @Given("I am authenticated as user via API")
    public void iAmAuthenticatedAsUserViaAPI() {
        ApiContext.authenticateAsUser();
    }

    @When("I send a GET request to {string}")
    public void iSendAGETRequestTo(String endpoint) {
        ApiContext.setResponse(given()
            .baseUri(ApiContext.getBaseUri())
            .header("Authorization", ApiContext.authHeader())
        .when()
            .get(endpoint));
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int expectedStatus) {
        ApiContext.getResponse().then().statusCode(expectedStatus);
    }

    @Then("the response status should be {int} or {int}")
    public void theResponseStatusShouldBeOr(int status1, int status2) {
        ApiContext.getResponse().then().statusCode(anyOf(is(status1), is(status2)));
    }

    @Then("the response status should be {int} or {int} or {int}")
    public void theResponseStatusShouldBeOrOr(int status1, int status2, int status3) {
        ApiContext.getResponse().then().statusCode(anyOf(is(status1), is(status2), is(status3)));
    }

    @And("the response should contain data")
    public void theResponseShouldContainData() {
        ApiContext.getResponse().then().body("$", notNullValue());
    }

    @And("the response should contain an id")
    public void theResponseShouldContainAnId() {
        ApiContext.getResponse().then().body("id", notNullValue());
    }
}
