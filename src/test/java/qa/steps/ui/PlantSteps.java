package qa.steps.ui;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Managed;
import net.serenitybdd.core.Serenity;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import qa.api.steps.AuthClient;
import qa.steps.api.ApiContext;
import qa.ui.pages.PlantAddPage;
import qa.ui.pages.PlantsPage;
import qa.utils.TestData;
import qa.utils.TestDataFactory;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class PlantSteps {

    @Managed
    WebDriver driver;

    PlantsPage plantsPage;
    PlantAddPage plantAddPage;

    private String createdPlantName;
    private String searchTerm;
    private String selectedCategory;
    private String updatedPlantName;
    private boolean cancelDeleteResult;
    private String blockedDeletePlantName;

    @When("I navigate to the plants page")
    public void iNavigateToThePlantsPage() {
        plantsPage.openPage();
    }

    @Then("I should see the plant list table")
    public void iShouldSeeThePlantListTable() {
        assertTrue(plantsPage.isTableVisible(), "Plant list should be visible");
    }

    @And("the table should contain plant data")
    public void theTableShouldContainPlantData() {
        assertFalse(plantsPage.getColumnValues(1).isEmpty(), "Plant rows should contain name values");
        assertFalse(plantsPage.getColumnValues(3).isEmpty(), "Plant rows should contain price values");
        assertFalse(plantsPage.getColumnValues(4).isEmpty(), "Plant rows should contain quantity values");
    }

    @When("I navigate to the add plant form")
    public void iNavigateToTheAddPlantForm() {
        plantAddPage.openForm();
    }

    @And("I select a category for the plant")
    public void iSelectACategoryForThePlant() {
        String categoryName = plantAddPage.selectFirstCategoryOption();
        assertFalse(categoryName.isBlank(), "At least one category must exist for plant creation");
    }

    @And("I select a main category if available")
    public void iSelectAMainCategoryIfAvailable() {
        int parentId = TestDataFactory.getOrCreateCategory();
        String mainName = getCategoryNameById(parentId);
        assumeWithReport(mainName != null && !mainName.isBlank(), "Main category name not available");
        plantAddPage.selectCategoryByVisibleText(mainName);
    }

    @And("I submit plant with name price {string} and quantity {string}")
    public void iSubmitPlantWithNamePriceAndQuantity(String price, String quantity) {
        createdPlantName = "Plt" + (System.nanoTime() % 100000);
        plantAddPage.submitWith(createdPlantName, price, quantity);
    }

    @Then("the new plant should appear in the list")
    public void theNewPlantShouldAppearInTheList() {
        waitForPlantsListUrl();
        plantsPage.searchFor(createdPlantName);
        plantsPage.clickSearchButton();
        plantsPage.waitForResults();
        assertTrue(plantsPage.rowsContainText(createdPlantName), "Created plant should appear in list");
    }

    @Then("I should see a validation message or remain on form")
    public void iShouldSeeAValidationMessageOrRemainOnForm() {
        assertTrue(
            driver.getCurrentUrl().contains("/ui/plants/add") || plantAddPage.hasValidationMessage(),
            "Form should remain on add page or show validation"
        );
    }

    @Given("I have created a plant via UI")
    public void iHaveCreatedAPlantViaUI() {
        plantAddPage.openForm();
        createdPlantName = "Del" + (System.nanoTime() % 100000);
        String categoryName = plantAddPage.selectFirstCategoryOption();
        assertFalse(categoryName.isBlank(), "At least one category must exist for plant creation");
        plantAddPage.submitWith(createdPlantName, "9.50", "6");
        waitForPlantsListUrl();
    }

    @And("I search for the created plant")
    public void iSearchForTheCreatedPlant() {
        plantsPage.searchAndSubmit(createdPlantName);
        assertTrue(plantsPage.rowsContainText(createdPlantName), "Plant must exist before delete");
    }

    @And("I delete the plant")
    public void iDeleteThePlant() {
        plantsPage.clickDeleteForPlantName(createdPlantName);
        plantsPage.acceptBrowserConfirmIfPresent();
        plantsPage.waitForResults();
    }

    @Then("the plant should not appear in the list")
    public void thePlantShouldNotAppearInTheList() {
        plantsPage.searchFor(createdPlantName);
        plantsPage.clickSearchButton();
        plantsPage.waitForResults();
        assertFalse(plantsPage.rowsContainText(createdPlantName), "Deleted plant should not appear in list");
    }

    @Then("the Add Plant button should be visible")
    public void theAddPlantButtonShouldBeVisible() {
        assertTrue(plantsPage.isAddPlantVisible(), "Add Plant button should be visible for admin");
    }

    @Then("the Add Plant button should be hidden")
    public void theAddPlantButtonShouldBeHidden() {
        assertFalse(plantsPage.isAddPlantVisible(), "Add Plant button should be hidden for user");
    }

    @Then("the Edit button should be visible")
    public void theEditButtonShouldBeVisible() {
        assertTrue(plantsPage.isAnyEditVisible(), "Edit button should be visible for admin");
    }

    @Then("the Edit button should be hidden")
    public void theEditButtonShouldBeHidden() {
        assertFalse(plantsPage.isAnyEditVisible(), "Edit button should be hidden for user");
    }

    @Then("the Delete button should be visible")
    public void theDeleteButtonShouldBeVisible() {
        assertTrue(plantsPage.isAnyDeleteVisible(), "Delete button should be visible for admin");
    }

    @Then("the Delete button should be hidden")
    public void theDeleteButtonShouldBeHidden() {
        assertFalse(plantsPage.isAnyDeleteVisible(), "Delete button should be hidden for user");
    }

    @Then("a delete confirmation should be displayed for plants")
    public void aDeleteConfirmationShouldBeDisplayedForPlants() {
        assertTrue(plantsPage.hasDeleteConfirmationOnFirstRow(),
            "Delete confirmation dialog should appear for plant deletion");
    }

    @When("I cancel deleting the first plant")
    public void iCancelDeletingTheFirstPlant() {
        cancelDeleteResult = plantsPage.cancelDeleteOnFirstRow();
    }

    @Then("the plant should remain in the list")
    public void thePlantShouldRemainInTheList() {
        assertTrue(cancelDeleteResult, "Plant should remain after canceling deletion");
    }

    @And("I search for an existing plant name")
    public void iSearchForAnExistingPlantName() {
        plantsPage.waitForResults();
        searchTerm = plantsPage.getFirstPlantName();
        assertFalse(searchTerm.isBlank(), "At least one plant should exist to test search");
        plantsPage.searchAndSubmit(searchTerm);
    }

    @And("I search for a non-existing plant name")
    public void iSearchForANonExistingPlantName() {
        searchTerm = "NO_MATCH_" + System.nanoTime();
        plantsPage.searchAndSubmit(searchTerm);
    }

    @Then("the results should only contain matching plants")
    public void theResultsShouldOnlyContainMatchingPlants() {
        assertTrue(plantsPage.allRowsMatchPlantNameContains(searchTerm),
            "Search should show only matching plants");
    }

    @And("I select the first category filter")
    public void iSelectTheFirstCategoryFilter() {
        selectedCategory = plantsPage.selectFirstCategoryOption();
        assertFalse(selectedCategory.isBlank(), "At least one category option should be present");
    }

    @Then("all results should belong to the selected category")
    public void allResultsShouldBelongToTheSelectedCategory() {
        assertTrue(plantsPage.allRowsMatchCategory(selectedCategory),
            "Filtered plants should belong to selected category");
    }

    @And("I click sort by price")
    public void iClickSortByPrice() {
        plantsPage.clickSortByPrice();
        new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.urlContains("sortField=price"));
        plantsPage.waitForResults();
    }

    @Then("the price column should be sorted")
    public void thePriceColumnShouldBeSorted() {
        assertTrue(isSortedNumericEitherDirection(plantsPage.getColumnValues(3)),
            "Price column should be sorted after clicking price sort");
    }

    @When("I click sort by quantity")
    public void iClickSortByQuantity() {
        plantsPage.clickSortByQuantity();
        new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.urlContains("sortField=quantity"));
        plantsPage.waitForResults();
    }

    @Then("the quantity column should be sorted")
    public void theQuantityColumnShouldBeSorted() {
        assertTrue(isSortedNumericEitherDirection(plantsPage.getColumnValues(4)),
            "Quantity column should be sorted after clicking quantity sort");
    }

    @When("I click sort by name")
    public void iClickSortByName() {
        plantsPage.clickSortByName();
        new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.urlContains("sortField=name"));
        plantsPage.waitForResults();
    }

    @Then("the name column should be sorted")
    public void theNameColumnShouldBeSorted() {
        assertTrue(isSortedEitherDirection(plantsPage.getColumnValues(1)),
            "Name column should be sorted after clicking name sort");
    }

    @Then("plant admin actions should be hidden")
    public void plantAdminActionsShouldBeHidden() {
        assertTrue(plantsPage.adminActionsHiddenForUser(),
            "Add/Edit/Delete controls should be hidden for user role");
    }

    @Then("a low stock badge should be displayed")
    public void aLowStockBadgeShouldBeDisplayed() {
        assertTrue(plantsPage.anyRowHasLowBadge(),
            "Low badge should be displayed for plants with quantity < 5");
    }

    @Then("no low stock badge should be displayed for sufficient stock")
    public void noLowStockBadgeShouldBeDisplayedForSufficientStock() {
        assertTrue(plantsPage.anyRowWithQtyAtLeastHasNoLowBadge(5),
            "Low badge should not be shown when quantity >= 5");
    }

    @Given("a low stock plant exists")
    public void aLowStockPlantExists() {
        int subCategoryId = TestDataFactory.getOrCreateSubCategory();
        TestDataFactory.createPlant(subCategoryId, 3);
    }

    @Given("a plant with sufficient stock exists")
    public void aPlantWithSufficientStockExists() {
        int subCategoryId = TestDataFactory.getOrCreateSubCategory();
        TestDataFactory.createPlant(subCategoryId, 6);
    }

    @Then("the no plants message should be displayed")
    public void theNoPlantsMessageShouldBeDisplayed() {
        plantsPage.waitForResults();
        assertTrue(plantsPage.hasNoPlantsMessage(), "No plants found message should be visible");
    }

    @Given("the plant list is empty")
    public void thePlantListIsEmpty() {
        plantsPage.openPage();
        boolean empty = plantsPage.hasNoPlantsMessage();
        assumeWithReport(empty, "Plant list is not empty");
    }

    @Given("plant records exceed one page")
    public void plantRecordsExceedOnePage() {
        plantsPage.openPage();
        if (plantsPage.isPaginationVisible()) {
            return;
        }
        int subCategoryId = TestDataFactory.getOrCreateSubCategory();
        for (int i = 0; i < 12; i++) {
            TestDataFactory.createPlant(subCategoryId, 10);
        }
        plantsPage.openPage();
        assumeWithReport(plantsPage.isPaginationVisible(), "Pagination not visible after seeding plants");
    }

    @Then("plant pagination should be visible when multiple pages exist")
    public void plantPaginationShouldBeVisibleWhenMultiplePagesExist() {
        assertTrue(plantsPage.isPaginationVisible(), "Pagination should be visible when multiple pages exist");
    }

    @When("I go to the next plant page")
    public void iGoToTheNextPlantPage() {
        plantsPage.clickNextPage();
        new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(d -> plantsPage.getActivePageNumber() >= 2);
    }

    @When("I go to the previous plant page")
    public void iGoToThePreviousPlantPage() {
        plantsPage.clickPreviousPage();
    }

    @Then("the plant page should change")
    public void thePlantPageShouldChange() {
        assertTrue(plantsPage.getActivePageNumber() > 1, "Active plant page should change after pagination");
    }

    @When("I try to access the add plant page directly")
    public void iTryToAccessTheAddPlantPageDirectly() {
        driver.get(qa.utils.TestData.get("base.url") + "/ui/plants/add");
    }

    @Then("I should not remain on the admin-only page")
    public void iShouldNotRemainOnTheAdminOnlyPage() {
        assertFalse(driver.getCurrentUrl().contains("/add"),
            "User should NOT stay on admin-only page");
    }
    @And("I click the cancel button in plant add page")
    public void iClickTheCancelButtonInPlantAddPage() {
        plantAddPage.clickCancel();
    }

    @Then("I should be on the plants list")
    public void iShouldBeOnThePlantsList() {
        assertTrue(plantAddPage.isOnList(),
            "Should be on plants list page");
    }

    @Then("I should see a plant validation message containing {string}")
    public void iShouldSeeAPlantValidationMessageContaining(String expected) {
        String needle = expected.toLowerCase();
        boolean matches = plantAddPage.hasValidationTextContaining(expected);
        if (!matches && "negative".equals(needle)) {
            matches = plantAddPage.hasValidationTextContaining("greater")
                || plantAddPage.hasValidationTextContaining("min")
                || plantAddPage.hasValidationTextContaining("positive");
        }
        if (!matches && "between".equals(needle)) {
            matches = plantAddPage.hasValidationTextContaining("length");
        }
        assertTrue(matches, "Expected validation message containing: " + expected);
    }

    @Then("I should not see any plant validation message")
    public void iShouldNotSeeAnyPlantValidationMessage() {
        assertFalse(plantAddPage.hasValidationMessage(), "No validation message should be shown");
    }

    @And("I leave the plant name empty")
    public void iLeaveThePlantNameEmpty() {
        plantAddPage.enterPlantName("");
    }

    @And("I enter plant name {string}")
    public void iEnterPlantName(String name) {
        plantAddPage.enterPlantName(name);
    }

    @And("I enter plant price {string}")
    public void iEnterPlantPrice(String price) {
        plantAddPage.enterPrice(price);
    }

    @And("I enter plant quantity {string}")
    public void iEnterPlantQuantity(String qty) {
        plantAddPage.enterQuantity(qty);
    }

    @And("I submit the plant form")
    public void iSubmitThePlantForm() {
        plantAddPage.clickSave();
    }

    @When("I create a low stock plant via UI")
    public void iCreateALowStockPlantViaUI() {
        plantAddPage.openForm();
        createdPlantName = "Low" + (System.nanoTime() % 100000);
        String categoryName = plantAddPage.selectFirstCategoryOption();
        assertFalse(categoryName.isBlank(), "At least one category must exist for plant creation");
        plantAddPage.submitWith(createdPlantName, "9.00", "3");
        waitForPlantsListUrl();
    }

    @When("I edit the first plant")
    public void iEditTheFirstPlant() {
        plantsPage.openPage();
        String name = plantsPage.clickEditFirstPlant();
        assumeWithReport(name != null && !name.isBlank(), "No plant available to edit");
        plantAddPage.waitUntilLoaded();
        updatedPlantName = "Upd" + (System.nanoTime() % 100000);
        if (updatedPlantName.length() > 25) {
            updatedPlantName = updatedPlantName.substring(0, 25);
        }
        plantAddPage.enterPlantName(updatedPlantName);
        plantAddPage.clickSave();
        waitForPlantsListUrl();
    }

    @Then("the updated plant should appear in the list")
    public void theUpdatedPlantShouldAppearInTheList() {
        plantsPage.searchAndSubmit(updatedPlantName);
        assertTrue(plantsPage.rowsContainText(updatedPlantName), "Updated plant should appear in list");
    }

    @When("I attempt to delete a plant used in an active sale")
    public void iAttemptToDeleteAPlantUsedInAnActiveSale() {
        int plantId = TestDataFactory.getOrCreatePlantWithQuantity(10);
        String plant = getPlantNameById(plantId);
        blockedDeletePlantName = plant;
        int saleId = createSaleViaApi(plantId, 1);
        if (saleId > 0) {
            ApiContext.trackCreatedEntity("sales", saleId);
        }
        plantsPage.openPage();
        plantsPage.searchAndSubmit(plant);
        plantsPage.clickDeleteForPlantName(plant);
        plantsPage.acceptBrowserConfirmIfPresent();
        plantsPage.waitForResults();
    }

    @Then("the plant deletion should be blocked")
    public void thePlantDeletionShouldBeBlocked() {
        assertTrue(plantsPage.hasErrorMessage(),
            "An error message should be shown when deleting a plant in active sale");
        if (blockedDeletePlantName != null && !blockedDeletePlantName.isBlank()) {
            plantsPage.searchAndSubmit(blockedDeletePlantName);
            assertTrue(plantsPage.rowsContainText(blockedDeletePlantName),
                "Plant should remain in the list after blocked deletion");
        }
    }

    private void waitForPlantsListUrl() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(d -> {
                String url = d.getCurrentUrl();
                return url.contains("/ui/plants") && !url.contains("/add") && !url.contains("/edit");
            });
    }

    private String getPlantNameById(int plantId) {
        String baseUrl = TestData.get("base.url");
        String token = AuthClient.getToken("admin.username", "admin.password");
        Response resp = io.restassured.RestAssured.given()
            .baseUri(baseUrl)
            .header("Authorization", "Bearer " + token)
            .get("/api/plants/" + plantId);
        if (resp.getStatusCode() == 200) {
            return resp.jsonPath().getString("name");
        }
        return "";
    }

    private String getCategoryNameById(int categoryId) {
        String baseUrl = TestData.get("base.url");
        String token = AuthClient.getToken("admin.username", "admin.password");
        Response resp = io.restassured.RestAssured.given()
            .baseUri(baseUrl)
            .header("Authorization", "Bearer " + token)
            .get("/api/categories/" + categoryId);
        if (resp.getStatusCode() == 200) {
            return resp.jsonPath().getString("name");
        }
        return "";
    }

    private int createSaleViaApi(int plantId, int quantity) {
        String baseUrl = TestData.get("base.url");
        String token = AuthClient.getToken("admin.username", "admin.password");
        Response resp = io.restassured.RestAssured.given()
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

    private void assumeWithReport(boolean condition, String reason) {
        if (!condition) {
            Serenity.recordReportData().withTitle("Skip reason").andContents(reason);
        }
        assumeTrue(condition, reason);
    }

    private boolean isSortedNumericEitherDirection(List<String> values) {
        return isSortedAscendingNumeric(values) || isSortedDescendingNumeric(values);
    }

    private boolean isSortedEitherDirection(List<String> values) {
        return isSortedAscending(values) || isSortedDescending(values);
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

    private double parseDoubleSafe(String value) {
        String normalized = value.replaceAll("[^0-9.\\-]", "");
        if (normalized.isBlank()) return 0.0;
        return Double.parseDouble(normalized);
    }
}
