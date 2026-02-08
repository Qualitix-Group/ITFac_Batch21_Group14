package qa.utils;

import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Centralized test data management with ENVIRONMENT variable support.
 *
 * Priority order for resolving values:
 * 1. System ENVIRONMENT variables (e.g., ADMIN_PASSWORD)
 * 2. System PROPERTIES (e.g., -Dadmin.password=xxx)
 * 3. Environment-specific PROPERTIES file (e.g., testdata-staging.PROPERTIES)
 * 4. Default testdata.PROPERTIES
 *
 * Usage:
 *   TestData.get("admin.password")  // Returns value based on priority
 *   TestData.getEnv()               // Returns current ENVIRONMENT name
 */
public class TestData {

    private static final Logger LOG = LoggerFactory.getLogger(TestData.class);
    private static final Properties PROPERTIES = new Properties();
    private static final String ENVIRONMENT;

    static {
        // Determine ENVIRONMENT from system property or env variable
        ENVIRONMENT = resolveEnvironment();
        LOG.info("Test ENVIRONMENT: {}", ENVIRONMENT);

        // Load default PROPERTIES first
        loadProperties("testdata.PROPERTIES");

        // Override with ENVIRONMENT-specific PROPERTIES if available
        String envPropertiesFile = "testdata-" + ENVIRONMENT + ".PROPERTIES";
        loadProperties(envPropertiesFile);

        LOG.info("TestData initialized with {} PROPERTIES", PROPERTIES.size());
    }

    private static String resolveEnvironment() {
        // Check system property first, then ENVIRONMENT variable
        String env = System.getProperty("ENVIRONMENT");
        if (env == null || env.isBlank()) {
            env = System.getenv("TEST_ENVIRONMENT");
        }
        if (env == null || env.isBlank()) {
            env = "local";
        }
        return env.toLowerCase();
    }

    private static void loadProperties(String filename) {
        try (InputStream input = TestData.class.getClassLoader().getResourceAsStream(filename)) {
            if (input != null) {
                PROPERTIES.load(input);
                LOG.debug("Loaded PROPERTIES from: {}", filename);
            } else {
                LOG.debug("Properties file not found (optional): {}", filename);
            }
        } catch (Exception e) {
            LOG.warn("Failed to load PROPERTIES file: {} - {}", filename, e.getMessage());
        }
    }

    /**
     * Get a test data value with ENVIRONMENT variable override support.
     *
     * @param key Property key (e.g., "admin.password")
     * @return Value from env var, system prop, or PROPERTIES file
     */
    public static String get(String key) {
        // 1. Check ENVIRONMENT variable (convert key to ENV_VAR format)
        String envVarName = key.toUpperCase().replace(".", "_");
        String envValue = System.getenv(envVarName);
        if (envValue != null && !envValue.isBlank()) {
            LOG.trace("Using env var {} for key {}", envVarName, key);
            return envValue;
        }

        // 2. Check system property
        String sysPropValue = System.getProperty(key);
        if (sysPropValue != null && !sysPropValue.isBlank()) {
            LOG.trace("Using system property for key {}", key);
            return sysPropValue;
        }

        // 3. Fall back to PROPERTIES file
        return PROPERTIES.getProperty(key);
    }

    /**
     * Get a test data value with a default fallback.
     */
    public static String get(String key, String defaultValue) {
        String value = get(key);
        return (value != null && !value.isBlank()) ? value : defaultValue;
    }

    /**
     * Get current test ENVIRONMENT name.
     */
    public static String getEnv() {
        return ENVIRONMENT;
    }

    /**
     * Check if running in a specific ENVIRONMENT.
     */
    public static boolean isEnv(String envName) {
        return ENVIRONMENT.equalsIgnoreCase(envName);
    }

    /**
     * Get integer value with default.
     */
    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            LOG.warn("Invalid integer for key {}: {}", key, value);
            return defaultValue;
        }
    }

    /**
     * Get boolean value with default.
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value.trim());
    }
}
