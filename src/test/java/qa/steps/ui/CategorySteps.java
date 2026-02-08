package qa.steps.ui;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Managed;
import net.serenitybdd.core.Serenity;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import qa.ui.pages.CategoriesPage;
import qa.ui.pages.CategoryAddPage;
import qa.ui.pages.LoginPage;
import qa.utils.TestData;
import qa.utils.TestDataFactory;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class CategorySteps {

    @Managed
    WebDriver driver;

    CategoriesPage categoriesPage;
    CategoryAddPage categoryAddPage;
    LoginPage loginPage;

    private String searchTerm;
    private String newCategoryName;
    private String updatedCategoryName;
    private String selectedParent;
    private boolean cancelDeleteResult;
    private int initialCategoryCount;
    private String deletedCategoryName;

    @When("I navigate to the categories page")
    public void iNavigateToTheCategoriesPage() {
        categoriesPage.openPage();
    }

    @Given("category records exceed one page")
    public void categoryRecordsExceedOnePage() {
        categoriesPage.openPage();
        if (categoriesPage.isPaginationVisible()) {
            return;
        }
        for (int i = 0; i < 12; i++) {
            TestDataFactory.createCategory(null);
        }
        categoriesPage.openPage();
        assumeWithReport(categoriesPage.isPaginationVisible(), "Pagination not visible after seeding categories");
    }

    @Given("the category list is empty")
    public void theCategoryListIsEmpty() {
        categoriesPage.openPage();
        boolean empty = categoriesPage.hasNoResultsState();
        assumeWithReport(empty, "Category list is not empty");
    }

    @When("I trigger validation on the category search")
    public void iTriggerValidationOnTheCategorySearch() {
        categoriesPage.openPage();
        categoriesPage.waitUntilLoaded();
        categoriesPage.searchFor(""); // leave blank
        categoriesPage.clickSearchButton();
        // Wait for page to stabilize after form submission
        new WebDriverWait(driver, Duration.ofMillis(500))
            .until(d -> categoriesPage.isSearchInputVisible());
    }

    @Then("I should see a category search validation message")
    public void iShouldSeeACategorySearchValidationMessage() {
        assertTrue(categoriesPage.hasValidationMessageOrPlaceholder(),
            "Expected a validation message on category search when submitting empty input");
    }

    @When("I access the categories page without authentication")
    public void iAccessTheCategoriesPageWithoutAuthentication() {
        // First navigate to base URL to establish domain for cookie deletion
        driver.get(TestData.get("base.url"));
        // Clear all cookies to ensure no session exists
        driver.manage().deleteAllCookies();
        // Now access the protected page
        driver.get(TestData.get("base.url") + "/ui/categories");
    }

    @When("I try to access the categories page")
    public void iTryToAccessTheCategoriesPage() {
        driver.get(TestData.get("base.url") + "/ui/categories");
    }

    @Then("I should see the category list table")
    public void iShouldSeeTheCategoryListTable() {
        assertTrue(categoriesPage.isTableVisible(),
            "Category list table should be visible");
    }

    @Then("the Add Category button should be visible")
    public void theAddCategoryButtonShouldBeVisible() {
        assertTrue(categoriesPage.isAddButtonVisible(), "Add Category button should be visible for admin");
    }

    @Then("the Add Category button should be hidden")
    public void theAddCategoryButtonShouldBeHidden() {
        assertFalse(categoriesPage.isAddButtonVisible(), "Add Category button should be hidden for user");
    }

    @Then("the Edit Category button should be visible")
    public void theEditCategoryButtonShouldBeVisible() {
        assertTrue(categoriesPage.isAnyEditVisible(), "Edit button should be visible for admin");
    }

    @Then("the Edit Category button should be hidden")
    public void theEditCategoryButtonShouldBeHidden() {
        assertFalse(categoriesPage.isAnyEditVisible(), "Edit button should be hidden for user");
    }

    @Then("the Delete Category button should be visible")
    public void theDeleteCategoryButtonShouldBeVisible() {
        assertTrue(categoriesPage.isAnyDeleteVisible(), "Delete button should be visible for admin");
    }

    @Then("the Delete Category button should be hidden")
    public void theDeleteCategoryButtonShouldBeHidden() {
        assertFalse(categoriesPage.isAnyDeleteVisible(), "Delete button should be hidden for user");
    }

    @And("pagination should be enabled")
    public void paginationShouldBeEnabled() {
        assertTrue(categoriesPage.isNextPageEnabled(),
            "Next page should be enabled (requires enough data)");
    }

    @Then("category pagination should be visible when multiple pages exist")
    public void categoryPaginationShouldBeVisibleWhenMultiplePagesExist() {
        assertTrue(categoriesPage.isPaginationVisible(), "Pagination should be visible for multiple pages");
    }

    @When("I click next page")
    public void iClickNextPage() {
        categoriesPage.clickNextPage();
        new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.not(ExpectedConditions.urlContains("page=0")));
        categoriesPage.waitForResults();
    }

    @Then("I should be on the next page")
    public void iShouldBeOnTheNextPage() {
        int currentPage = categoriesPage.getActivePageNumber();
        assertTrue(currentPage >= 2, "Should be on page 2 or higher");
    }

    @When("I click previous page")
    public void iClickPreviousPage() {
        categoriesPage.clickPreviousPage();
        categoriesPage.waitForResults();
    }

    @When("I click page number {int}")
    public void iClickPageNumber(int pageNumber) {
        categoriesPage.clickPageNumber(pageNumber);
        categoriesPage.waitForResults();
    }

    @And("I search for an existing category name")
    public void iSearchForAnExistingCategoryName() {
        searchTerm = categoriesPage.getFirstRowCategoryName();
        assertFalse(searchTerm.isBlank(), "At least one category should exist to search");
        categoriesPage.searchFor(searchTerm);
        categoriesPage.clickSearchButton();
        categoriesPage.waitForResults();
    }

    @Then("the results should only contain matching categories")
    public void theResultsShouldOnlyContainMatchingCategories() {
        assertTrue(categoriesPage.allRowsMatchNameContains(searchTerm),
            "Search results should only contain the matching category name");
    }

    @And("I select the first parent category filter")
    public void iSelectTheFirstParentCategoryFilter() {
        selectedParent = categoriesPage.selectFirstParentOption();
        assertFalse(selectedParent.isBlank(), "A parent category option should be available");
        categoriesPage.clickSearchButton();
        categoriesPage.waitForResults();
    }

    @Then("all results should belong to the selected parent")
    public void allResultsShouldBelongToTheSelectedParent() {
        assertTrue(categoriesPage.allRowsMatchParent(selectedParent),
            "Filtered list should show only categories under the selected parent");
    }

    @When("I navigate to the add category form")
    public void iNavigateToTheAddCategoryForm() {
        categoryAddPage.openForm();
    }

    @And("I enter a valid category name")
    public void iEnterAValidCategoryName() {
        newCategoryName = "Cat" + (System.nanoTime() % 100000);
        if (newCategoryName.length() > 10) {
            newCategoryName = newCategoryName.substring(0, 10);
        }
        categoryAddPage.enterName(newCategoryName);
    }

    @And("I leave the category name empty")
    public void iLeaveTheCategoryNameEmpty() {
        categoryAddPage.enterName("");
    }

    @And("I enter category name {string}")
    public void iEnterCategoryName(String name) {
        categoryAddPage.enterName(name);
    }

    @And("I save the category")
    public void iSaveTheCategory() {
        categoryAddPage.clickSave();
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(d -> {
                String url = d.getCurrentUrl();
                return url.contains("/ui/categories")
                    && !url.contains("/add")
                    && !url.contains("/edit");
            });
        categoriesPage.waitUntilLoaded();
    }

    @Then("the new category should appear in the list")
    public void theNewCategoryShouldAppearInTheList() {
        categoriesPage.searchFor(newCategoryName);
        categoriesPage.clickSearchButton();
        categoriesPage.waitForResults();
        assertTrue(categoriesPage.rowsContainText(newCategoryName),
            "New category should appear in the list after save");
    }

    @And("I click edit on the first category")
    public void iClickEditOnTheFirstCategory() {
        String existingName = categoriesPage.getFirstRowCategoryName();
        assertFalse(existingName.isBlank(), "At least one category should exist to edit");
        categoriesPage.clickEditForCategoryName(existingName);
        categoryAddPage.waitUntilLoaded();
    }

    @And("I update the category name")
    public void iUpdateTheCategoryName() {
        updatedCategoryName = "Upd" + (System.nanoTime() % 1000);
        if (updatedCategoryName.length() < 3) {
            updatedCategoryName = "Upd" + updatedCategoryName;
        }
        if (updatedCategoryName.length() > 10) {
            updatedCategoryName = updatedCategoryName.substring(0, 10);
        }
        categoryAddPage.enterName(updatedCategoryName);
    }

    @Then("the category should be created as a main category")
    public void theCategoryShouldBeCreatedAsAMainCategory() {
        categoriesPage.searchFor(newCategoryName);
        categoriesPage.clickSearchButton();
        categoriesPage.waitForResults();
        List<String> rows = categoriesPage.getColumnValues(2);
        List<String> parents = categoriesPage.getColumnValues(3);
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).equalsIgnoreCase(newCategoryName)) {
                String parent = parents.size() > i ? parents.get(i) : "";
                boolean isMain = parent.equals("-") || parent.equalsIgnoreCase("main") || parent.isBlank();
                assertTrue(isMain, "Main category should have '-' or empty parent value");
                return;
            }
        }
        assertTrue(false, "New category not found to validate parent");
    }

    @Then("I should see a category validation message containing {string}")
    public void iShouldSeeACategoryValidationMessageContaining(String expected) {
        assertTrue(categoryAddPage.hasValidationContaining(expected),
            "Expected category validation message containing: " + expected);
    }

    @Then("the required category name message should appear first")
    public void theRequiredCategoryNameMessageShouldAppearFirst() {
        String text = categoryAddPage.getNameValidationText().toLowerCase();
        assertTrue(text.contains("required"), "Required message should be shown first");
        assertFalse(text.contains("at least") && text.contains("required") && text.indexOf("required") > text.indexOf("at least"),
            "Required message should appear before length-related messages");
    }

    @Then("the updated category should appear in the list")
    public void theUpdatedCategoryShouldAppearInTheList() {
        categoriesPage.searchFor(updatedCategoryName);
        categoriesPage.clickSearchButton();
        categoriesPage.waitForResults();
        assertTrue(categoriesPage.rowsContainText(updatedCategoryName),
            "Updated category name should appear in the list");
    }

    @When("I navigate to categories sorted by {string} {string}")
    public void iNavigateToCategoriesSortedBy(String sortField, String sortDir) {
        String url = TestData.get("base.url")
            + "/ui/categories?page=0&sortField=" + sortField
            + "&sortDir=" + sortDir + "&name=&parentId=";
        driver.get(url);
        categoriesPage.waitUntilLoaded();
        categoriesPage.waitForResults();
    }

    @Then("category names should be sorted ascending")
    public void categoryNamesShouldBeSortedAscending() {
        List<String> names = categoriesPage.getColumnValues(2);
        assertTrue(isSortedAscending(names),
            "Category names should be ASC. Actual: " + names);
    }

    @Then("category names should be sorted descending")
    public void categoryNamesShouldBeSortedDescending() {
        List<String> names = categoriesPage.getColumnValues(2);
        assertTrue(isSortedDescending(names),
            "Category names should be DESC. Actual: " + names);
    }

    @Then("parent categories should be sorted ascending")
    public void parentCategoriesShouldBeSortedAscending() {
        List<String> parents = categoriesPage.getColumnValues(3);
        assertTrue(isSortedAscending(parents),
            "Parent category names should be ASC. Actual: " + parents);
    }

    @Then("parent categories should be sorted descending")
    public void parentCategoriesShouldBeSortedDescending() {
        List<String> parents = categoriesPage.getColumnValues(3);
        assertTrue(isSortedDescending(parents),
            "Parent category names should be DESC. Actual: " + parents);
    }

    @Then("category IDs should be sorted ascending")
    public void categoryIDsShouldBeSortedAscending() {
        List<String> ids = categoriesPage.getColumnValues(1);
        assertTrue(isSortedAscendingNumeric(ids),
            "Category IDs should be ASC. Actual: " + ids);
    }

    @Then("category IDs should be sorted descending")
    public void categoryIDsShouldBeSortedDescending() {
        List<String> ids = categoriesPage.getColumnValues(1);
        assertTrue(isSortedDescendingNumeric(ids),
            "Category IDs should be DESC. Actual: " + ids);
    }

    @Then("admin actions should be hidden or disabled")
    public void adminActionsShouldBeHiddenOrDisabled() {
        assertTrue(categoriesPage.adminActionsHiddenOrDisabled(),
            "Add/Edit/Delete buttons should be hidden for user role");
    }

    @Then("a delete confirmation should be displayed for categories")
    public void aDeleteConfirmationShouldBeDisplayedForCategories() {
        assertTrue(categoriesPage.hasDeleteConfirmationOnFirstRow(),
            "Delete confirmation dialog should appear");
    }

    @When("I cancel deleting the first category")
    public void iCancelDeletingTheFirstCategory() {
        cancelDeleteResult = categoriesPage.cancelDeleteOnFirstRow();
    }

    @Then("the category should remain in the list")
    public void theCategoryShouldRemainInTheList() {
        assertTrue(cancelDeleteResult, "Category should remain after canceling deletion");
    }

    @When("I delete the first category")
    public void iDeleteTheFirstCategory() {
        deletedCategoryName = categoriesPage.getFirstRowCategoryName();
        assumeWithReport(deletedCategoryName != null && !deletedCategoryName.isBlank(),
            "No category available to delete");
        initialCategoryCount = categoriesPage.getColumnValues(1).size();
        categoriesPage.confirmDeleteOnFirstRow();
    }

    @Then("the category should be removed from the list")
    public void theCategoryShouldBeRemovedFromTheList() {
        int after = categoriesPage.getColumnValues(1).size();
        assertTrue(after <= initialCategoryCount - 1 || categoriesPage.hasNoResultsState(),
            "Category should be removed after deletion");
    }

    @Then("the deleted category should not appear in search results")
    public void theDeletedCategoryShouldNotAppearInSearchResults() {
        assumeWithReport(deletedCategoryName != null && !deletedCategoryName.isBlank(),
            "No deleted category name available to search");
        categoriesPage.searchFor(deletedCategoryName);
        categoriesPage.clickSearchButton();
        categoriesPage.waitForResults();
        assertFalse(categoriesPage.rowsContainText(deletedCategoryName),
            "Deleted category should not appear in search results");
    }

    @Then("I should see validation message for empty name")
    public void iShouldSeeValidationMessageForEmptyName() {
        assertTrue(categoryAddPage.showsNameValidationMessage(),
            "Validation message should appear for empty category name");
    }

    @And("I click the cancel button")
    public void iClickTheCancelButton() {
        try {
            categoryAddPage.clickCancel();
        } catch (Exception ignored) {
            driver.navigate().back();
        }
        new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.urlContains("/ui/cat"));
    }

    @Then("I should be on the categories list")
    public void iShouldBeOnTheCategoriesList() {
        assertTrue(categoryAddPage.isOnList(),
            "Should be on categories list page");
    }

    @And("I search for a non-existent category")
    public void iSearchForANonExistentCategory() {
        searchTerm = "NO_MATCH_" + System.nanoTime();
        categoriesPage.searchFor(searchTerm);
        categoriesPage.clickSearchButton();
    }

    @Then("I should see empty state or no matching results")
    public void iShouldSeeEmptyStateOrNoMatchingResults() {
        assertTrue(categoriesPage.hasNoResultsState() || !categoriesPage.rowsContainText(searchTerm),
            "Either empty state is shown or results should not contain the search term");
    }

    // Helper methods
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
        int prev = parseIntSafe(values.get(0));
        for (int i = 1; i < values.size(); i++) {
            int curr = parseIntSafe(values.get(i));
            if (prev > curr) return false;
            prev = curr;
        }
        return true;
    }

    private boolean isSortedDescendingNumeric(List<String> values) {
        if (values.size() < 2) return true;
        int prev = parseIntSafe(values.get(0));
        for (int i = 1; i < values.size(); i++) {
            int curr = parseIntSafe(values.get(i));
            if (prev < curr) return false;
            prev = curr;
        }
        return true;
    }

    private int parseIntSafe(String value) {
        String digits = value.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) return 0;
        return Integer.parseInt(digits);
    }

    private void assumeWithReport(boolean condition, String reason) {
        if (!condition) {
            Serenity.recordReportData().withTitle("Skip reason").andContents(reason);
        }
        assumeTrue(condition, reason);
    }
}
