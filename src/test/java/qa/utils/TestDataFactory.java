package qa.utils;

import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qa.api.steps.AuthClient;
import qa.steps.api.ApiContext;

import static io.restassured.RestAssured.given;

/**
 * Factory for creating test data dynamically.
 * Tests should use this instead of relying on pre-seeded database IDs.
 *
 * All created data is automatically tracked for cleanup via ApiContext.
 */
public class TestDataFactory {

    private static final Logger LOG = LoggerFactory.getLogger(TestDataFactory.class);

    // Cache for created test data (within a test scenario)
    private static final ThreadLocal<Integer> TEST_PARENT_CATEGORY_ID = new ThreadLocal<>();
    private static final ThreadLocal<Integer> TEST_SUB_CATEGORY_ID = new ThreadLocal<>();
    private static final ThreadLocal<Integer> TEST_PLANT_ID = new ThreadLocal<>();
    private static final ThreadLocal<Integer> TEST_SALE_ID = new ThreadLocal<>();

    /**
     * Reset all cached test data IDs.
     * Called automatically by TestHooks before each scenario.
     */
    public static void reset() {
        TEST_PARENT_CATEGORY_ID.remove();
        TEST_SUB_CATEGORY_ID.remove();
        TEST_PLANT_ID.remove();
        TEST_SALE_ID.remove();
    }

    /**
     * Get or create a test category (returns parent category for general use).
     * Creates a new category if none exists for this test scenario.
     */
    public static int getOrCreateCategory() {
        Integer cachedId = TEST_PARENT_CATEGORY_ID.get();
        if (cachedId != null && cachedId > 0) {
            return cachedId;
        }

        int id = createCategory(null); // Parent category (no parent)
        TEST_PARENT_CATEGORY_ID.set(id);
        return id;
    }

    /**
     * Get or create a sub-category (required for plant creation).
     * The API requires plants to be added to sub-categories only.
     */
    public static int getOrCreateSubCategory() {
        Integer cachedId = TEST_SUB_CATEGORY_ID.get();
        if (cachedId != null && cachedId > 0) {
            return cachedId;
        }

        int parentId = getOrCreateCategory();
        int id = createCategory(parentId); // Sub-category
        TEST_SUB_CATEGORY_ID.set(id);
        return id;
    }

    /**
     * Get or create a test plant.
     * Creates a new plant (and sub-category if needed) if none exists for this test
     * scenario.
     * Note: Plants can only be added to sub-categories per API requirements.
     */
    public static int getOrCreatePlant() {
        Integer cachedId = TEST_PLANT_ID.get();
        if (cachedId != null && cachedId > 0) {
            return cachedId;
        }

        int subCategoryId = getOrCreateSubCategory(); // Must use sub-category for plants
        int id = createPlant(subCategoryId, 100); // Default quantity 100
        TEST_PLANT_ID.set(id);
        return id;
    }

    /**
     * Get or create a test plant with specific quantity.
     */
    public static int getOrCreatePlantWithQuantity(int quantity) {
        int subCategoryId = getOrCreateSubCategory(); // Must use sub-category for plants
        int id = createPlant(subCategoryId, quantity);
        TEST_PLANT_ID.set(id);
        return id;
    }

    /**
     * Create a new category and track for cleanup.
     * 
     * @param parentId Parent category ID, or null for root category
     */
    public static int createCategory(Integer parentId) {
        String baseUrl = TestData.get("base.url");
        String token = getAdminToken();
        // Use UUID fragment to avoid duplicate-name 400 errors from the API
        String name = "C" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 7);

        // Build JSON body - include parent if provided (API requires "parent" field,
        // not "parentCategory")
        String body;
        if (parentId != null && parentId > 0) {
            body = String.format("{\"name\":\"%s\",\"parent\":{\"id\":%d}}", name, parentId);
        } else {
            body = "{\"name\":\"" + name + "\"}";
        }

        Response response = given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(body)
                .post("/api/categories");

        int status = response.getStatusCode();
        if (status != 200 && status != 201) {
            LOG.error("Failed to create category: {} - {}", status, response.getBody().asString());
            throw new RuntimeException("Failed to create test category: " + status);
        }

        Integer id = response.jsonPath().getInt("id");
        if (id == null) {
            id = response.jsonPath().getInt("categoryId");
        }

        if (id != null && id > 0) {
            ApiContext.trackCreatedEntity("categories", id);
            LOG.debug("Created test category: {} (parent: {})", id, parentId);
            return id;
        }

        throw new RuntimeException("Failed to get category ID from response");
    }

    /**
     * Create a new plant and track for cleanup.
     */
    public static int createPlant(int categoryId, int quantity) {
        if (categoryId <= 0) {
            throw new RuntimeException("Invalid categoryId for plant creation: " + categoryId);
        }

        String baseUrl = TestData.get("base.url");
        String token = getAdminToken();
        // Use UUID fragment to avoid duplicate-name collisions
        String name = "P" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 7);

        String body = String.format("""
                {
                  "name": "%s",
                  "price": 99,
                  "quantity": %d,
                  "category": {"id": %d}
                }
                """, name, quantity, categoryId);

        LOG.debug("Creating plant with name={}, categoryId={}, quantity={}", name, categoryId, quantity);

        Response response = given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(body)
                .post("/api/plants/category/" + categoryId);

        int status = response.getStatusCode();
        if (status != 200 && status != 201) {
            LOG.error("Failed to create plant: {} - {} (name={}, catId={})",
                    status, response.getBody().asString(), name, categoryId);
            throw new RuntimeException("Failed to create test plant: " + status);
        }

        Integer id = response.jsonPath().getInt("id");
        if (id == null) {
            id = response.jsonPath().getInt("plantId");
        }

        if (id != null && id > 0) {
            ApiContext.trackCreatedEntity("plants", id);
            LOG.debug("Created test plant: {} (category: {})", id, categoryId);
            return id;
        }

        throw new RuntimeException("Failed to get plant ID from response");
    }

    /**
     * Create a new sale and track for cleanup.
     */
    public static int createSale(int plantId, int quantity) {
        String baseUrl = TestData.get("base.url");
        String token = getAdminToken();

        Response response = given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .formParam("quantity", quantity)
                .post("/api/sales/plant/" + plantId);

        int status = response.getStatusCode();
        if (status != 200 && status != 201) {
            LOG.error("Failed to create sale: {} - {}", status, response.getBody().asString());
            throw new RuntimeException("Failed to create test sale: " + status);
        }

        Integer id = response.jsonPath().getInt("id");
        if (id == null) {
            id = response.jsonPath().getInt("saleId");
        }

        if (id != null && id > 0) {
            ApiContext.trackCreatedEntity("sales", id);
            LOG.debug("Created test sale: {} (plant: {})", id, plantId);
            return id;
        }

        throw new RuntimeException("Failed to get sale ID from response");
    }

    /**
     * Get admin token for test data creation.
     * Always uses admin credentials regardless of current ApiContext state.
     */
    private static String getAdminToken() {
        // Always use admin credentials for test data creation
        // This ensures test data can be created even when authenticated as regular user
        return AuthClient.getToken("admin.username", "admin.password");
    }

    /**
     * Get cached test category ID (null if not created yet).
     */
    public static Integer getCachedCategoryId() {
        return TEST_PARENT_CATEGORY_ID.get();
    }

    /**
     * Get cached test sub-category ID (null if not created yet).
     */
    public static Integer getCachedSubCategoryId() {
        return TEST_SUB_CATEGORY_ID.get();
    }

    /**
     * Get cached test plant ID (null if not created yet).
     */
    public static Integer getCachedPlantId() {
        return TEST_PLANT_ID.get();
    }

    /**
     * Get cached test sale ID (null if not created yet).
     */
    public static Integer getCachedSaleId() {
        return TEST_SALE_ID.get();
    }

    /**
     * Set cached sale ID (when sale is created by step definition).
     */
    public static void setCachedSaleId(int saleId) {
        TEST_SALE_ID.set(saleId);
    }
}
