package qa.steps.api;

import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qa.api.steps.AuthClient;
import qa.utils.TestData;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared context for API step definitions.
 * Uses ThreadLocal fields to share state across step definition classes (thread-safe for parallel execution).
 * Tracks created entities for automatic cleanup after tests.
 */
public class ApiContext {

    private static final Logger LOG = LoggerFactory.getLogger(ApiContext.class);

    private static final ThreadLocal<String> TOKEN = new ThreadLocal<>();
    private static final ThreadLocal<String> BASE_URI = new ThreadLocal<>();
    private static final ThreadLocal<Response> RESPONSE = new ThreadLocal<>();
    private static final ThreadLocal<List<CreatedEntity>> CREATED_ENTITIES = ThreadLocal.withInitial(ArrayList::new);

    /**
     * Represents an entity created during a test that needs cleanup.
     */
    public static class CreatedEntity {
        private final String entityType;  // e.g., "categories", "plants", "sales"
        private final String entityId;
        private final String deleteEndpoint;

        public CreatedEntity(String entityType, String entityId) {
            this.entityType = entityType;
            this.entityId = entityId;
            this.deleteEndpoint = "/api/" + entityType + "/" + entityId;
        }

        public CreatedEntity(String entityType, String entityId, String customDeleteEndpoint) {
            this.entityType = entityType;
            this.entityId = entityId;
            this.deleteEndpoint = customDeleteEndpoint;
        }

        public String getEntityType() { return entityType; }
        public String getEntityId() { return entityId; }
        public String getDeleteEndpoint() { return deleteEndpoint; }

        @Override
        public String toString() {
            return entityType + ":" + entityId;
        }
    }

    // ==================== Authentication ====================

    public static void authenticateAsAdmin() {
        String token = AuthClient.getToken("admin.username", "admin.password");
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("Admin token could not be obtained from /api/auth/login");
        }
        setToken(token);
        setBaseUri(TestData.get("base.url"));
        LOG.debug("Authenticated as admin");
    }

    public static void authenticateAsUser() {
        String token = AuthClient.getToken("user.username", "user.password");
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("User token could not be obtained from /api/auth/login");
        }
        setToken(token);
        setBaseUri(TestData.get("base.url"));
        LOG.debug("Authenticated as user");
    }

    // ==================== Context Management ====================

    public static void reset() {
        TOKEN.remove();
        BASE_URI.remove();
        RESPONSE.remove();
        CREATED_ENTITIES.remove();
        LOG.trace("ApiContext reset");
    }

    public static String authHeader() {
        String token = getToken();
        if (token == null) return null;
        return token.startsWith("Bearer") ? token : "Bearer " + token;
    }

    // ==================== Token ====================

    public static String getToken() {
        return TOKEN.get();
    }

    public static void setToken(String token) {
        TOKEN.set(token);
    }

    // ==================== Base URI ====================

    public static String getBaseUri() {
        return BASE_URI.get();
    }

    public static void setBaseUri(String baseUri) {
        BASE_URI.set(baseUri);
    }

    // ==================== Response ====================

    public static Response getResponse() {
        return RESPONSE.get();
    }

    public static void setResponse(Response response) {
        RESPONSE.set(response);
    }

    // ==================== Created Entities Tracking ====================

    /**
     * Track a created entity for cleanup after the test.
     * @param entityType The entity type (e.g., "categories", "plants", "sales")
     * @param entityId The ID of the created entity
     */
    public static void trackCreatedEntity(String entityType, String entityId) {
        if (entityId == null || entityId.isBlank() || "0".equals(entityId)) {
            LOG.warn("Attempted to track invalid entity ID for type: {}", entityType);
            return;
        }
        CreatedEntity entity = new CreatedEntity(entityType, entityId);
        CREATED_ENTITIES.get().add(entity);
        LOG.debug("Tracking created entity for cleanup: {}", entity);
    }

    /**
     * Track a created entity with a custom delete endpoint.
     */
    public static void trackCreatedEntity(String entityType, String entityId, String customDeleteEndpoint) {
        if (entityId == null || entityId.isBlank() || "0".equals(entityId)) {
            LOG.warn("Attempted to track invalid entity ID for type: {}", entityType);
            return;
        }
        CreatedEntity entity = new CreatedEntity(entityType, entityId, customDeleteEndpoint);
        CREATED_ENTITIES.get().add(entity);
        LOG.debug("Tracking created entity for cleanup: {} (endpoint: {})", entity, customDeleteEndpoint);
    }

    /**
     * Track a created entity by integer ID.
     */
    public static void trackCreatedEntity(String entityType, int entityId) {
        trackCreatedEntity(entityType, String.valueOf(entityId));
    }

    /**
     * Get all created entities for cleanup.
     */
    public static List<CreatedEntity> getCreatedEntities() {
        return new ArrayList<>(CREATED_ENTITIES.get());
    }

    /**
     * Check if there are entities to clean up.
     */
    public static boolean hasCreatedEntities() {
        return !CREATED_ENTITIES.get().isEmpty();
    }

    /**
     * Clear the created entities list (after cleanup).
     */
    public static void clearCreatedEntities() {
        CREATED_ENTITIES.get().clear();
    }

    // ==================== Legacy Support ====================

    /**
     * @deprecated Use trackCreatedEntity() instead
     */
    @Deprecated
    public static String getCreatedEntityId() {
        List<CreatedEntity> entities = CREATED_ENTITIES.get();
        if (entities.isEmpty()) {
            return null;
        }
        return entities.get(entities.size() - 1).getEntityId();
    }

    /**
     * @deprecated Use trackCreatedEntity() instead
     */
    @Deprecated
    public static void setCreatedEntityId(int createdEntityId) {
        // For backwards compatibility, track as unknown type
        trackCreatedEntity("unknown", createdEntityId);
    }
}
