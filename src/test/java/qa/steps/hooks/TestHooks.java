package qa.steps.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qa.steps.api.ApiContext;
import qa.utils.TestData;
import qa.utils.TestDataFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 * Cucumber hooks for test setup, teardown, and cleanup.
 * Implements best practices for:
 * - Test isolation (clean state before each test)
 * - Detailed failure logging
 * - Environment-aware reporting
 * - API context reset for parallel execution
 * - Automatic cleanup of created test data
 */
public class TestHooks {

    private static final Logger LOG = LoggerFactory.getLogger(TestHooks.class);
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private static final String LOG_SEPARATOR = "========================================";

    private long startTime;

    // ==================== BEFORE HOOKS ====================

    @Before(order = 0)
    public void setBaseUrl() {
        String baseUrl = TestData.get("base.url");
        if (baseUrl != null && !baseUrl.isBlank()) {
            System.setProperty("webdriver.base.url", baseUrl);
            LOG.debug("Set webdriver.base.url to {}", baseUrl);
        }
    }

    @Before(order = 1)
    public void logScenarioStart(Scenario scenario) {
        startTime = System.currentTimeMillis();
        LOG.info(LOG_SEPARATOR);
        LOG.info("STARTING: {} [{}]", scenario.getName(), TestData.getEnv());
        LOG.info("Tags: {}", scenario.getSourceTagNames());
        LOG.info("Time: {}", LocalDateTime.now().format(TIME_FORMAT));
        LOG.info(LOG_SEPARATOR);
    }

    @Before(order = 2)
    public void resetApiContext(Scenario scenario) {
        ApiContext.reset();
        TestDataFactory.reset();
        LOG.debug("API context and test data factory reset for scenario: {}", scenario.getName());
    }

    @Before(order = 3)
    public void recordScenarioMetadata(Scenario scenario) {
        Serenity.recordReportData()
            .withTitle("Scenario Info")
            .andContents(String.format(
                "Name: %s%nEnvironment: %s%nTags: %s%nStarted: %s",
                scenario.getName(),
                TestData.getEnv(),
                scenario.getSourceTagNames(),
                LocalDateTime.now().format(TIME_FORMAT)
            ));
    }

    // ==================== AFTER HOOKS ====================

    @After(order = 10)
    public void cleanupCreatedEntities(Scenario scenario) {
        if (!ApiContext.hasCreatedEntities()) {
            return;
        }

        LOG.info("Cleaning up {} created entities...", ApiContext.getCreatedEntities().size());

        // Get admin token for cleanup (need admin rights to delete)
        String adminToken = getAdminTokenForCleanup();
        if (adminToken == null) {
            LOG.warn("Could not obtain admin token for cleanup - entities may remain");
            return;
        }

        // Delete in reverse order (to handle dependencies)
        List<ApiContext.CreatedEntity> entities = ApiContext.getCreatedEntities();
        Collections.reverse(entities);

        for (ApiContext.CreatedEntity entity : entities) {
            deleteEntity(entity, adminToken);
        }

        ApiContext.clearCreatedEntities();
        LOG.info("Cleanup completed");
    }

    @After(order = 5)
    public void captureFailureDetails(Scenario scenario) {
        if (scenario.isFailed()) {
            LOG.error("FAILED: {} - Status: {}", scenario.getName(), scenario.getStatus());

            Serenity.recordReportData()
                .withTitle("Failure Details")
                .andContents(String.format(
                    "Scenario: %s%nStatus: %s%nEnvironment: %s%nTags: %s%nFailed at: %s",
                    scenario.getName(),
                    scenario.getStatus(),
                    TestData.getEnv(),
                    scenario.getSourceTagNames(),
                    LocalDateTime.now().format(TIME_FORMAT)
                ));
        }
    }

    @After(order = 1)
    public void logScenarioEnd(Scenario scenario) {
        long duration = System.currentTimeMillis() - startTime;
        String status = scenario.isFailed() ? "FAILED" : "PASSED";

        LOG.info(LOG_SEPARATOR);
        LOG.info("{}: {} ({}ms)", status, scenario.getName(), duration);
        LOG.info(LOG_SEPARATOR);

        Serenity.recordReportData()
            .withTitle("Execution Time")
            .andContents(String.format("%dms", duration));
    }

    // ==================== CLEANUP HELPERS ====================

    private String getAdminTokenForCleanup() {
        try {
            String baseUrl = TestData.get("base.url");
            String username = TestData.get("admin.username");
            String password = TestData.get("admin.password");

            Response response = RestAssured.given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .body(String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password))
                .post("/api/auth/login");

            if (response.getStatusCode() == 200) {
                String token = response.jsonPath().getString("token");
                if (token == null) {
                    token = response.jsonPath().getString("accessToken");
                }
                return token;
            }
        } catch (Exception e) {
            LOG.warn("Failed to get admin token for cleanup: {}", e.getMessage());
        }
        return null;
    }

    private void deleteEntity(ApiContext.CreatedEntity entity, String adminToken) {
        try {
            String baseUrl = TestData.get("base.url");
            String endpoint = entity.getDeleteEndpoint();

            LOG.debug("Deleting {} via {}", entity, endpoint);

            Response response = RestAssured.given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + adminToken)
                .delete(endpoint);

            int status = response.getStatusCode();
            if (status == 200 || status == 204 || status == 404) {
                LOG.info("Cleaned up {}: {} (status: {})", entity.getEntityType(), entity.getEntityId(), status);
            } else {
                LOG.warn("Failed to cleanup {}: status={}, body={}",
                    entity, status, response.getBody().asString());
            }
        } catch (Exception e) {
            LOG.error("Error cleaning up {}: {}", entity, e.getMessage());
        }
    }
}
