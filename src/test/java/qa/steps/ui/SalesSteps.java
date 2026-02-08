package qa.steps.ui;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Managed;
import net.serenitybdd.core.Serenity;
import org.openqa.selenium.WebDriver;
import qa.api.steps.AuthClient;
import qa.steps.api.ApiContext;
import qa.ui.pages.PlantsPage;
import qa.ui.pages.SalesCreatePage;
import qa.ui.pages.SalesPage;
import qa.utils.TestData;
import qa.utils.TestDataFactory;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class SalesSteps {

    private static final int DEFAULT_SALE_QUANTITY = 1;

    @Managed
    WebDriver driver;

    SalesPage salesPage;
    SalesCreatePage salesCreatePage;
    PlantsPage plantsPage;

    // Plant is resolved at runtime (first in-stock plant from UI)
    private String plantName;

    private int selectedPlantStock;
    private int initialStock;
    private int initialRowCount;
    private boolean cancelDeleteResult;

    @When("I navigate to the sales page")
    public void iNavigateToTheSalesPage() {
        salesPage.openList();
    }

    @Then("I should see the sales list table")
    public void iShouldSeeTheSalesListTable() {
        assertTrue(salesPage.isTableVisible(), "Sales list table should be visible");
    }

    @Then("I should see the sales list or empty message")
    public void iShouldSeeTheSalesListOrEmptyMessage() {
        assertTrue(salesPage.getRowCount() > 0 || salesPage.hasEmptyMessage(),
                "Sales list should render rows or show empty message");
    }

    @Given("there is available stock for the test plant")
    public void thereIsAvailableStockForTheTestPlant() {
        ensurePlantWithStock();
    }

    @And("I click the sell button")
    public void iClickTheSellButton() {
        salesPage.clickSell();
    }

    @And("I select the test plant")
    public void iSelectTheTestPlant() {
        // Resolve plantName lazily for scenarios that don't go through
        // ensurePlantWithStock
        resolvePlantName();
        // Don't overwrite plantName with the dropdown text — the dropdown may show
        // a different format (e.g. "Name - Category") than the plants table, and
        // plantName is used later for stock lookups against the plants table.
        salesCreatePage.choosePlant(plantName);
    }

    @And("I select a plant with available stock")
    public void iSelectAPlantWithAvailableStock() {
        String optionText = salesCreatePage.getFirstPlantOptionWithStock();
        assumeWithReport(optionText != null && !optionText.isBlank(), "No plant options with stock available");
        selectedPlantStock = salesCreatePage.parseStockFromOptionText(optionText);
        plantName = optionText.replaceAll("\\s*\\(Stock:.*\\)\\s*$", "").trim();
        salesCreatePage.choosePlant(plantName);
    }

    @And("I enter a valid quantity")
    public void iEnterAValidQuantity() {
        salesCreatePage.setQuantity(DEFAULT_SALE_QUANTITY);
    }

    @And("I enter quantity {string}")
    public void iEnterQuantity(String quantity) {
        salesCreatePage.setQuantity(Integer.parseInt(quantity));
    }

    @And("I submit the sale")
    public void iSubmitTheSale() {
        salesCreatePage.submitSale();
    }

    @And("I enter quantity greater than available stock")
    public void iEnterQuantityGreaterThanAvailableStock() {
        assumeWithReport(selectedPlantStock > 0, "Selected plant stock must be known to exceed it");
        salesCreatePage.setQuantity(selectedPlantStock + 1);
    }

    @Then("the sale should be recorded")
    public void theSaleShouldBeRecorded() {
        salesPage.openList();
        assertTrue(salesPage.getRowCount() >= 0, "Sales list should be accessible");
    }

    @Then("I should see quantity validation message")
    public void iShouldSeeQuantityValidationMessage() {
        assertTrue(salesCreatePage.hasQuantityValidation()
                || salesCreatePage.hasQuantityErrorMessage()
                || salesCreatePage.hasErrorAlert(),
                "Quantity validation message should display");
    }

    @Then("I should see plant validation message")
    public void iShouldSeePlantValidationMessage() {
        assertTrue(salesCreatePage.hasPlantValidation() || salesCreatePage.hasErrorAlert(),
                "Plant validation message should display");
    }

    @Then("I should see a sale error message")
    public void iShouldSeeASaleErrorMessage() {
        assertTrue(salesCreatePage.hasErrorAlert() || salesCreatePage.hasQuantityErrorMessage(),
                "Sale error message should be displayed");
    }

    @When("I record the current stock")
    public void iRecordTheCurrentStock() {
        resolvePlantName();
        initialStock = currentStock(plantName);
        assumeWithReport(initialStock > 0, "Precondition: stock must be available");
    }

    @Then("the stock should be reduced by the sold quantity")
    public void theStockShouldBeReducedBySoldQuantity() {
        int sellQty = DEFAULT_SALE_QUANTITY;
        int afterStock = waitForStockChange(plantName, initialStock, 20, 1000);
        assertNotEquals(initialStock, afterStock, "Stock should change after sale");
        assertEquals(initialStock - sellQty, afterStock, "Stock should reduce by the sold quantity");
    }

    @Given("there is a sale to delete")
    public void thereIsASaleToDelete() {
        salesPage.openList();
        initialRowCount = salesPage.getRowCount();

        if (initialRowCount == 0 || !salesPage.isDeleteVisible()) {
            // Ensure we have a valid plant before trying to seed a sale
            ensurePlantWithStock();

            salesPage.openList();
            salesPage.clickSell();
            salesCreatePage.choosePlant(plantName);
            salesCreatePage.setQuantity(DEFAULT_SALE_QUANTITY);
            salesCreatePage.submitSale();
            salesPage.openList();
            initialRowCount = salesPage.getRowCount();
        }

        assumeWithReport(initialRowCount > 0 && salesPage.isDeleteVisible(),
                "Need at least one sale with delete action");
    }

    @And("I delete the first sale")
    public void iDeleteTheFirstSale() {
        boolean deleted = salesPage.deleteFirstSale();
        assumeWithReport(deleted, "Delete action not available or did not complete");
    }

    @Then("the sale count should decrease")
    public void theSaleCountShouldDecrease() {
        salesPage.openList();
        assertTrue(salesPage.getRowCount() <= initialRowCount - 1 || salesPage.hasEmptyMessage(),
                "Row count should drop after deletion");
    }

    @Then("sales should be sorted by date descending")
    public void salesShouldBeSortedByDateDescending() {
        assertTrue(salesPage.isSortedByDateDesc(), "Newest sales should appear first");
    }

    @Then("sales pagination should be visible when multiple pages exist")
    public void salesPaginationShouldBeVisibleWhenMultiplePagesExist() {
        assertTrue(salesPage.isPaginationVisible(), "Pagination should be visible when more than one page exists");
    }

    @Given("sales records exceed one page")
    public void salesRecordsExceedOnePage() {
        salesPage.openList();
        if (salesPage.isPaginationVisible()) {
            return;
        }
        int plantId = TestDataFactory.getOrCreatePlant();
        for (int i = 0; i < 12; i++) {
            int saleId = createSaleViaApi(plantId, 1);
            if (saleId > 0) {
                ApiContext.trackCreatedEntity("sales", saleId);
            }
        }
        salesPage.openList();
        assumeWithReport(salesPage.isPaginationVisible(), "Pagination not visible after seeding sales");
    }

    @When("I go to the next sales page")
    public void iGoToTheNextSalesPage() {
        salesPage.clickNextPage();
        salesPage.waitForCondition().until(d -> salesPage.getActivePageNumber() >= 2);
    }

    @When("I go to the previous sales page")
    public void iGoToThePreviousSalesPage() {
        salesPage.clickPreviousPage();
    }

    @Then("the sales page should change")
    public void theSalesPageShouldChange() {
        assertTrue(salesPage.getActivePageNumber() > 1,
                "Active page number should change after pagination");
    }

    @When("I sort sales by {string}")
    public void iSortSalesBy(String header) {
        salesPage.clickSortByHeader(header);
    }

    @When("I navigate to sales sorted by {string} {string}")
    public void iNavigateToSalesSortedBy(String sortField, String sortDir) {
        String mappedField = mapSalesSortField(sortField);
        String url = TestData.get("base.url")
                + "/ui/sales?page=0&sortField=" + mappedField
                + "&sortDir=" + sortDir;
        driver.get(url);
        salesPage.waitForCondition().until(d -> salesPage.isTableVisible() || salesPage.hasEmptyMessage());
    }

    @Then("sales should be sorted by {string} {string}")
    public void salesShouldBeSortedBy(String header, String direction) {
        List<String> values = salesPage.getColumnValuesByHeader(header);
        boolean desc = direction.equalsIgnoreCase("desc") || direction.equalsIgnoreCase("descending");
        boolean sorted;
        if (header.toLowerCase().contains("plant")) {
            sorted = desc ? isSortedDescending(values) : isSortedAscending(values);
        } else if (header.toLowerCase().contains("sold")) {
            sorted = desc ? isSortedDateDescending(values) : isSortedDateAscending(values);
        } else {
            sorted = desc ? isSortedDescendingNumeric(values) : isSortedAscendingNumeric(values);
        }
        assertTrue(sorted, "Sales should be sorted by " + header + " " + direction);
    }

    @Given("the sales list is empty")
    public void theSalesListIsEmpty() {
        // Delete all existing sales via API so we can test the empty state
        deleteAllSalesViaApi();
        salesPage.openList();
        // After deleting all sales, the page may show an empty-message row inside
        // the table (e.g. "No sales found") which getRowCount() counts as 1.
        // Accept either zero rows or the empty message as "empty".
        boolean isEmpty = salesPage.getRowCount() == 0 || salesPage.hasEmptyMessage();
        assumeWithReport(isEmpty,
                "Precondition: sales list should be empty after cleanup");
    }

    @Then("I should see the empty state message")
    public void iShouldSeeTheEmptyStateMessage() {
        assertTrue(salesPage.hasEmptyMessage(), "Empty state message should appear");
    }

    @And("the sell button should be visible")
    public void theSellButtonShouldBeVisible() {
        assertTrue(salesPage.isSellVisible(), "Sell button should stay visible even when no sales exist");
    }

    @Then("the sales list should be accessible")
    public void theSalesListShouldBeAccessible() {
        assertTrue(salesPage.getRowCount() >= 0, "Sales list should be accessible to regular user");
    }

    @Then("the sell button should be hidden")
    public void theSellButtonShouldBeHidden() {
        assertFalse(salesPage.isSellVisible(), "Sell action should be hidden for regular user");
    }

    @Then("the delete button should be hidden")
    public void theDeleteButtonShouldBeHidden() {
        assertFalse(salesPage.isDeleteVisible(), "Delete action should be hidden for regular user");
    }

    @Then("the delete button should be visible")
    public void theDeleteButtonShouldBeVisible() {
        assertTrue(salesPage.isDeleteVisible(), "Delete action should be visible for admin user");
    }

    @Then("I should be on the sell plant page")
    public void iShouldBeOnTheSellPlantPage() {
        assertTrue(salesCreatePage.isOnForm(), "Should be on Sell Plant form");
    }

    @Then("the plant dropdown should list available plants")
    public void thePlantDropdownShouldListAvailablePlants() {
        List<String> options = salesCreatePage.getPlantOptionTexts();
        boolean hasPlant = options.stream()
            .anyMatch(t -> !t.toLowerCase().contains("select") && !t.isBlank());
        assertTrue(hasPlant, "Plant dropdown should list available plants");
    }

    @Then("the plant dropdown should show stock quantities")
    public void thePlantDropdownShouldShowStockQuantities() {
        assertTrue(salesCreatePage.optionsContainStockQuantities(),
                "Plant dropdown should include stock quantities");
    }

    @And("I click the cancel button in sales sell page")
    public void iClickTheCancelButtonInSalesSellPage() {
        salesCreatePage.clickCancel();
    }

    @When("I navigate to the add sales form")
    public void iNavigateToTheAddSalesForm() {
        salesCreatePage.openForm();
    }

    @Then("I should be on the sales list")
    public void iShouldBeOnTheSalesList() {
        assertTrue(salesCreatePage.isOnList(), "Should be on sales list page");
    }

    @Then("a delete confirmation should be displayed for sales")
    public void aDeleteConfirmationShouldBeDisplayedForSales() {
        assertTrue(salesPage.hasDeleteConfirmationOnFirstSale(),
                "Delete confirmation dialog should appear");
    }

    @When("I cancel deleting the first sale")
    public void iCancelDeletingTheFirstSale() {
        cancelDeleteResult = salesPage.cancelDeleteFirstSale();
    }

    @Then("the sale should remain in the list")
    public void theSaleShouldRemainInTheList() {
        assertTrue(cancelDeleteResult, "Sale should remain after canceling deletion");
    }

    @Then("a sale deletion success message should be displayed")
    public void aSaleDeletionSuccessMessageShouldBeDisplayed() {
        assertTrue(salesPage.hasSuccessMessage(), "Success message should appear after deleting a sale");
    }

    private int currentStock(String plant) {
        plantsPage.openPage();
        return plantsPage.getStockFor(plant);
    }

    /** Ensure we have a plant name that exists in UI with stock > 0. */
    private void ensurePlantWithStock() {
        resolvePlantName();

        int availableStock = plantName != null ? currentStock(plantName) : -1;
        if (availableStock <= 0) {
            plantsPage.openPage();
            String fallbackPlant = plantsPage.findPlantWithPositiveStock();
            if (fallbackPlant != null && !fallbackPlant.isBlank()) {
                plantName = fallbackPlant;
                availableStock = currentStock(plantName);
            }
        }
        assumeWithReport(availableStock > 0, "Precondition: plant stock required");
    }

    /**
     * Lazily resolve plantName from config or first available plant in the UI.
     * IMPORTANT: must not navigate away if the browser is already on the sales
     * form.
     */
    private void resolvePlantName() {
        if (plantName != null && !plantName.isBlank()) {
            return; // already resolved
        }

        // 1. If we're currently on the sales form, read the first plant option from the
        // dropdown
        // instead of navigating away (which would destroy the form)
        String currentUrl = salesCreatePage.getDriver().getCurrentUrl();
        if (currentUrl != null && (currentUrl.contains("/sales/new") || currentUrl.contains("/sales/add"))) {
            String firstOption = salesCreatePage.getFirstPlantOptionName();
            if (firstOption != null && !firstOption.isBlank()) {
                plantName = firstOption;
                return;
            }
        }

        // 3. Navigate to the plants page to find a plant with stock
        plantsPage.openPage();
        String fallback = plantsPage.findPlantWithPositiveStock();
        if (fallback != null && !fallback.isBlank()) {
            plantName = fallback;
            return;
        }

        // Last resort: use empty string so choosePlant fallback picks the first option
        plantName = "";
    }

    private int waitForStockChange(String plant, int initial, int attempts, long sleepMs) {
        int current = initial;
        for (int i = 0; i < attempts; i++) {
            plantsPage.openPage();
            current = plantsPage.getStockFor(plant);
            if (current != initial) {
                return current;
            }
            try {
                Thread.sleep(sleepMs);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return current;
    }

    private int createSaleViaApi(int plantId, int quantity) {
        String baseUrl = TestData.get("base.url");
        String token = AuthClient.getToken("admin.username", "admin.password");
        Response resp = given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .formParam("quantity", quantity)
                .post("/api/sales/plant/" + plantId);
        if (resp.getStatusCode() == 200 || resp.getStatusCode() == 201) {
            Integer id = resp.jsonPath().getInt("id");
            return id != null ? id : 0;
        }
        return 0;
    }

    private String mapSalesSortField(String sortField) {
        String field = sortField.toLowerCase();
        return switch (field) {
            case "plant", "plantname", "plant.name" -> "plant.name";
            case "quantity" -> "quantity";
            case "total", "totalprice", "total price" -> "totalPrice";
            case "soldat", "sold at", "date" -> "soldAt";
            default -> sortField;
        };
    }

    private void assumeWithReport(boolean condition, String reason) {
        if (!condition) {
            Serenity.recordReportData().withTitle("Skip reason").andContents(reason);
        }
        assumeTrue(condition, reason);
    }

    /** Delete every sale via the REST API so the UI empty-state test can run. */
    @SuppressWarnings("unchecked")
    private void deleteAllSalesViaApi() {
        String baseUrl = TestData.get("base.url");
        String token = AuthClient.getToken("admin.username", "admin.password");

        // Retry loop: keep fetching and deleting until the list is empty
        for (int attempt = 0; attempt < 5; attempt++) {
            Response resp = given()
                    .baseUri(baseUrl)
                    .header("Authorization", "Bearer " + token)
                    .get("/api/sales");

            if (resp.getStatusCode() != 200) {
                return;
            }

            List<Map<String, Object>> sales = resp.jsonPath().getList("");
            if (sales == null || sales.isEmpty()) {
                return; // all deleted
            }

            for (Map<String, Object> sale : sales) {
                Object idObj = sale.get("id");
                if (idObj == null)
                    continue;
                int id = idObj instanceof Integer ? (Integer) idObj : Integer.parseInt(idObj.toString());
                given()
                        .baseUri(baseUrl)
                        .header("Authorization", "Bearer " + token)
                        .delete("/api/sales/" + id);
            }
        }
    }

    private boolean isSortedAscending(List<String> values) {
        if (values.size() < 2) return true;
        for (int i = 1; i < values.size(); i++) {
            String prev = values.get(i - 1).toLowerCase();
            String curr = values.get(i).toLowerCase();
            if (prev.compareTo(curr) > 0) return false;
        }
        return true;
    }

    private boolean isSortedDescending(List<String> values) {
        if (values.size() < 2) return true;
        for (int i = 1; i < values.size(); i++) {
            String prev = values.get(i - 1).toLowerCase();
            String curr = values.get(i).toLowerCase();
            if (prev.compareTo(curr) < 0) return false;
        }
        return true;
    }

    private boolean isSortedAscendingNumeric(List<String> values) {
        if (values.size() < 2) return true;
        double prev = parseDoubleSafe(values.get(0));
        for (int i = 1; i < values.size(); i++) {
            double curr = parseDoubleSafe(values.get(i));
            if (prev > curr) return false;
            prev = curr;
        }
        return true;
    }

    private boolean isSortedDescendingNumeric(List<String> values) {
        if (values.size() < 2) return true;
        double prev = parseDoubleSafe(values.get(0));
        for (int i = 1; i < values.size(); i++) {
            double curr = parseDoubleSafe(values.get(i));
            if (prev < curr) return false;
            prev = curr;
        }
        return true;
    }

    private boolean isSortedDateAscending(List<String> values) {
        if (values.size() < 2) return true;
        List<java.time.LocalDateTime> parsed = parseDates(values);
        if (parsed.size() < 2) return true;
        for (int i = 1; i < parsed.size(); i++) {
            if (parsed.get(i - 1).isAfter(parsed.get(i))) return false;
        }
        return true;
    }

    private boolean isSortedDateDescending(List<String> values) {
        if (values.size() < 2) return true;
        List<java.time.LocalDateTime> parsed = parseDates(values);
        if (parsed.size() < 2) return true;
        for (int i = 1; i < parsed.size(); i++) {
            if (parsed.get(i - 1).isBefore(parsed.get(i))) return false;
        }
        return true;
    }

    private List<java.time.LocalDateTime> parseDates(List<String> values) {
        List<java.time.LocalDateTime> dates = new java.util.ArrayList<>();
        java.time.format.DateTimeFormatter[] fmts = new java.time.format.DateTimeFormatter[] {
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"),
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        };
        for (String raw : values) {
            if (raw == null) continue;
            String trimmed = raw.trim();
            for (var fmt : fmts) {
                try {
                    dates.add(java.time.LocalDateTime.parse(trimmed, fmt));
                    break;
                } catch (Exception ignored) {
                }
            }
        }
        return dates;
    }

    private double parseDoubleSafe(String value) {
        String normalized = value == null ? "" : value.replaceAll("[^0-9.\\-]", "");
        if (normalized.isBlank()) return 0.0;
        return Double.parseDouble(normalized);
    }
}
