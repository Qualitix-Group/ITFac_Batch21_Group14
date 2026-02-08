# QA Training App – Serenity BDD + Cucumber

## Prerequisites

- Java 17+
- Maven 3.9+
- Chrome browser

## Run the App

Start the Spring Boot application on port 8080:

```bash
java -jar qa-training-app.jar
```

UI available at: `http://localhost:8080`

---

## Environment Configuration

### Available Environments

| Environment | Usage | Description |
|-------------|-------|-------------|
| `local` | Default | Local development (localhost:8080) |
| `dev` | `-Denvironment=dev` | Development server |
| `staging` | `-Denvironment=staging` | Staging/QA server |
| `prod` | `-Denvironment=prod` | Production (read-only tests) |

### Running Tests Against Different Environments

```bash
# Local (default)
mvn verify

# Development environment
mvn verify -Denvironment=dev

# Staging environment
mvn verify -Denvironment=staging

# Or use environment variable
export TEST_ENVIRONMENT=staging
mvn verify
```

### Secure Credentials with Environment Variables

For CI/CD pipelines, use environment variables instead of hardcoded passwords:

```bash
# Set credentials via environment variables
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=secure_password
export USER_USERNAME=testuser
export USER_PASSWORD=secure_password

# Run tests (credentials will be picked up automatically)
mvn verify -Denvironment=staging
```

**Priority order for configuration values:**
1. Environment variables (e.g., `ADMIN_PASSWORD`)
2. System properties (e.g., `-Dadmin.password=xxx`)
3. Environment-specific properties (e.g., `testdata-staging.properties`)
4. Default properties (`testdata.properties`)

---

## Running Tests

### Run All Tests

```bash
# Full suite (UI + API)
mvn clean verify -Dnet.bytebuddy.agent.disabled=true -Djdk.attach.allowAttachSelf=true

# Only API scenarios
mvn clean verify -Dnet.bytebuddy.agent.disabled=true -Djdk.attach.allowAttachSelf=true -Dcucumber.filter.tags=@api

# Single feature (example)
mvn clean verify -Dnet.bytebuddy.agent.disabled=true -Djdk.attach.allowAttachSelf=true -Dcucumber.features=src/test/resources/features/api/plants_api.feature
```

### Run by Module

```bash
# Categories tests (UI + API)
mvn verify -Dcucumber.filter.tags="@categories"

# Plants tests (UI + API)
mvn verify -Dcucumber.filter.tags="@plants"

# Sales tests (UI + API)
mvn verify -Dcucumber.filter.tags="@sales"


# Security tests (UI + API)
mvn verify -Dcucumber.filter.tags="@security"
```

### Run by Type

```bash
# All API tests
mvn verify -Dcucumber.filter.tags="@api"

# All UI tests
mvn verify -Dcucumber.filter.tags="@ui"
```

### Run by Role

```bash
# Admin tests only
mvn verify -Dcucumber.filter.tags="@admin"

# User tests only
mvn verify -Dcucumber.filter.tags="@user"
```

### Run a Single Scenario (by Tag)

```bash
# Run specific test case by its tag
mvn verify -Dcucumber.filter.tags="@UI-CAT-ADMIN-01"
mvn verify -Dcucumber.filter.tags="@API-PLANT-05"
mvn verify -Dcucumber.filter.tags="@UI-SALES-02"
mvn verify -Dcucumber.filter.tags="@API-SEC-ADMIN-01"
```

### Run Multiple Specific Scenarios

```bash
# Run multiple tags (OR condition)
mvn verify -Dcucumber.filter.tags="@UI-CAT-ADMIN-01 or @UI-CAT-ADMIN-02"

# Run tests matching multiple tags (AND condition)
mvn verify -Dcucumber.filter.tags="@api and @categories"
mvn verify -Dcucumber.filter.tags="@ui and @admin"
```

### Exclude Scenarios

```bash
# Run all except security tests
mvn verify -Dcucumber.filter.tags="not @security"

# Run UI tests except user tests
mvn verify -Dcucumber.filter.tags="@ui and not @user"
```

### Run with Visible Browser (Default)

```bash
mvn verify -Dcucumber.filter.tags="@UI-CAT-ADMIN-01"
```

### Run in Headless Mode (No Browser Window)

```bash
mvn verify -Dheadless.mode=true

# Or use environment variable
export HEADLESS_MODE=true
mvn verify
```

---

## Test Tags Reference

| Tag | Description |
|-----|-------------|
| `@api` | All API tests |
| `@ui` | All UI tests |
| `@categories` | Category module tests |
| `@plants` | Plant module tests |
| `@sales` | Sales module tests |
| `@dashboard` | Dashboard module tests |
| `@security` | Security & auth tests |
| `@admin` | Admin role tests |
| `@user` | User role tests |
| `@creates-data` | Tests that create data (triggers cleanup hook) |
| `@modifies-state` | Tests that modify system state |

### Individual Test Case Tags

**Categories:**
- `@UI-CAT-ADMIN-01` to `@UI-CAT-ADMIN-05` (Admin UI)
- `@UI-CAT-USER-01` to `@UI-CAT-USER-05` (User UI)
- `@API-CAT-ADMIN-01` to `@API-CAT-ADMIN-05` (Admin API)
- `@API-CAT-USER-01` to `@API-CAT-USER-05` (User API)

**Plants:**
- `@UI-PLANT-01` to `@UI-PLANT-05` (Admin UI)
- `@UI-PLANT-06` to `@UI-PLANT-10` (User UI)
- `@API-PLANT-01` to `@API-PLANT-10` (API)

**Sales:**
- `@UI-SALES-01` to `@UI-SALES-10` (UI)
- `@API-SALES-01` to `@API-SALES-10` (API)

**Dashboard:**
- `@UI-DASH-ADMIN-01` to `@UI-DASH-ADMIN-05` (Admin UI)
- `@UI-DASH-USER-01` to `@UI-DASH-USER-05` (User UI)
- `@API-DASH-ADMIN-01` to `@API-DASH-ADMIN-05` (Admin API)
- `@API-DASH-USER-01` to `@API-DASH-USER-05` (User API)

**Security:**
- `@UI-SEC-01` to `@UI-SEC-10` (UI)
- `@API-SEC-ADMIN-01` to `@API-SEC-ADMIN-05` (Admin API)
- `@API-SEC-USER-01` to `@API-SEC-USER-05` (User API)

---

## View Test Reports

After running tests, open the Serenity report:

```bash
open target/site/serenity/index.html
```

---

## Project Structure

```
src/test/
├── java/qa/
│   ├── api/
│   │   └── steps/AuthClient.java
│   ├── ui/
│   │   └── pages/
│   │       ├── BasePage.java          # Base class for all page objects
│   │       ├── LoginPage.java
│   │       ├── CategoriesPage.java
│   │       └── ...
│   ├── steps/
│   │   ├── api/*.java                 # API Step Definitions
│   │   ├── ui/*.java                  # UI Step Definitions
│   │   └── hooks/TestHooks.java       # Setup/teardown hooks
│   └── utils/
│       └── TestData.java              # Environment-aware config
└── resources/
    ├── features/
    │   ├── api/*.feature
    │   ├── categories/*.feature
    │   ├── dashboard/*.feature
    │   ├── plants/*.feature
    │   ├── sales/*.feature
    │   └── security/*.feature
    ├── serenity.conf                  # Serenity & WebDriver config
    ├── testdata.properties            # Default test data
    ├── testdata-dev.properties        # Dev environment overrides
    ├── testdata-staging.properties    # Staging environment overrides
    └── junit-platform.properties      # Parallel execution & retry config
```

---

## Configuration

### Browser Mode

Edit `src/test/resources/serenity.conf`:

```hocon
# Show browser window
headless.mode = false

# Hide browser window (for CI/CD)
headless.mode = true
```

### Parallel Execution

Edit `src/test/resources/junit-platform.properties`:

```properties
cucumber.execution.parallel.enabled=true
cucumber.execution.parallel.config.fixed.parallelism=4
```

### Retry Failed Tests

Flaky tests are automatically retried once (configurable in `junit-platform.properties`):

```properties
cucumber.execution.retry.count=1
```

### Timeouts

Configure in `testdata.properties`:

```properties
timeout.default=10000
timeout.short=3000
timeout.long=30000
```

---

## CI/CD Integration

### GitHub Actions Example

```yaml
name: Run Tests
on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Run Tests
        env:
          ADMIN_PASSWORD: ${{ secrets.ADMIN_PASSWORD }}
          USER_PASSWORD: ${{ secrets.USER_PASSWORD }}
          HEADLESS_MODE: true
        run: mvn verify -Denvironment=staging

      - name: Upload Reports
        uses: actions/upload-artifact@v4
        with:
          name: serenity-report
          path: target/site/serenity/
```

---

## Best Practices Implemented

- **Page Object Model** with `BasePage` for shared functionality
- **Environment-based configuration** for multi-environment testing
- **Secure credential management** via environment variables
- **Parallel execution** with thread-safe context
- **Automatic retry** for flaky tests
- **Explicit waits** (no Thread.sleep)
- **Comprehensive logging** and failure reporting
- **Test data cleanup hooks** via `@creates-data` tag
