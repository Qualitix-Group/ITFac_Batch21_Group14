//
//package com.group14.qa.ui.steps;
//
//import com.group14.qa.ui.pages.CategoriesPage;
//import com.group14.qa.ui.pages.CreateCategoryPage;
//import net.serenitybdd.core.Serenity;
//import org.awaitility.Awaitility;
//import org.openqa.selenium.By;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.When;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.And;
//import java.util.concurrent.TimeUnit;
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class CreateCategorySteps {
//
//    private CategoriesPage categoriesPage;
//    private CreateCategoryPage createCategoryPage;
//
//    // Store test data between steps
//    private String newCategoryName;
//    private String selectedParentCategory;
//
//    // ==================== NAVIGATION STEP ====================
//
//    @And("I go to categories page")
//    public void iGoToCategoriesPage() {
//        // Direct navigation to categories page
//        String categoriesUrl = "http://localhost:8080/ui/categories";
//        createCategoryPage.getDriver().get(categoriesUrl);
//
//        // Wait for page to load
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        System.out.println("DEBUG: Navigated to categories page: " + categoriesUrl);
//        System.out.println("DEBUG: Current URL: " + createCategoryPage.getDriver().getCurrentUrl());
//        Serenity.takeScreenshot();
//    }
//
//    // ==================== BASIC STEP DEFINITIONS ====================
//
//    @Given("I click on the {string} button")
//    public void iClickOnTheButton(String buttonText) {
//        if ("Add A Category".equals(buttonText)) {
//            // FIRST: Make sure we're on the categories page
//            String currentUrl = createCategoryPage.getDriver().getCurrentUrl();
//            if (!currentUrl.contains("/categories")) {
//                System.out.println("DEBUG: Not on categories page. Current URL: " + currentUrl);
//                iGoToCategoriesPage(); // Navigate to categories page first
//            }
//
//            // Now click the Add A Category button
//            try {
//                createCategoryPage.clickAddCategoryButton();
//                System.out.println("DEBUG: Clicked 'Add A Category' button");
//            } catch (Exception e) {
//                // If button not found, try alternative approach
//                System.out.println("DEBUG: Button not found, trying alternative...");
//                // Try to find and click the button directly
//                createCategoryPage.getDriver().findElement(
//                        By.xpath("//a[contains(text(),'Add A Category')]")
//                ).click();
//            }
//
//            // Wait for create category page to load
//            Awaitility.await()
//                    .atMost(10, TimeUnit.SECONDS)
//                    .until(() -> createCategoryPage.isCreateCategoryPageLoaded());
//
//            System.out.println("DEBUG: Create category page loaded: " + createCategoryPage.isCreateCategoryPageLoaded());
//            System.out.println("DEBUG: Current URL after clicking Add: " + createCategoryPage.getDriver().getCurrentUrl());
//
//        } else if ("Save".equals(buttonText)) {
//            // Click Save button
//            String currentUrl = createCategoryPage.getDriver().getCurrentUrl();
//            System.out.println("DEBUG: URL before clicking Save: " + currentUrl);
//
//            createCategoryPage.clickSaveButton();
//
//            // Wait for navigation OR error to appear
//            Awaitility.await()
//                    .atMost(10, TimeUnit.SECONDS)
//                    .pollInterval(1, TimeUnit.SECONDS)
//                    .until(() -> {
//                        String newUrl = createCategoryPage.getDriver().getCurrentUrl();
//                        boolean urlChanged = !newUrl.equals(currentUrl);
//
//                        // Also check if error message appeared (for validation errors)
//                        String pageSource = createCategoryPage.getDriver().getPageSource().toLowerCase();
//                        boolean hasError = pageSource.contains("error") ||
//                                pageSource.contains("invalid") ||
//                                pageSource.contains("validation") ||
//                                pageSource.contains("failed");
//
//                        return urlChanged || hasError;
//                    });
//
//            System.out.println("DEBUG: URL after clicking Save: " + createCategoryPage.getDriver().getCurrentUrl());
//        }
//
//        Serenity.takeScreenshot();
//    }
//
//    @When("I fill category name field with {string}")
//    public void iFillCategoryNameFieldWith(String categoryName) {
//        createCategoryPage.enterCategoryName(categoryName);
//        this.newCategoryName = categoryName;
//        System.out.println("DEBUG: Filled category name with: " + categoryName);
//        Serenity.takeScreenshot();
//    }
//
//    @And("I select {string} as parent category")
//    public void iSelectAsParentCategory(String parentCategory) {
//        createCategoryPage.selectParentCategory(parentCategory);
//        this.selectedParentCategory = parentCategory;
//        System.out.println("DEBUG: Selected parent category: " + parentCategory);
//        Serenity.takeScreenshot();
//    }
//
//    @Then("I should be redirected to the categories listing page")
//    public void iShouldBeRedirectedToTheCategoriesListingPage() {
//        // Wait for categories page to load
//        Awaitility.await()
//                .atMost(10, TimeUnit.SECONDS)
//                .until(() -> categoriesPage.isCategoriesPageLoaded());
//
//        String currentUrl = categoriesPage.getCurrentUrl();
//        System.out.println("DEBUG: Current URL after creation: " + currentUrl);
//
//        assertThat(categoriesPage.isCategoriesPageLoaded())
//                .as("Should be redirected to categories listing page. Current URL: " + currentUrl)
//                .isTrue();
//
//        Serenity.takeScreenshot();
//    }
//
//    @And("I should see a success message")
//    public void iShouldSeeASuccessMessage() {
//        // Wait for success message
//        Awaitility.await()
//                .atMost(5, TimeUnit.SECONDS)
//                .until(() -> categoriesPage.isSuccessMessageDisplayed() ||
//                        createCategoryPage.isSuccessMessageDisplayed());
//
//        boolean successMessageVisible = categoriesPage.isSuccessMessageDisplayed() ||
//                createCategoryPage.isSuccessMessageDisplayed();
//
//        assertThat(successMessageVisible)
//                .as("Success message should be displayed")
//                .isTrue();
//
//        if (categoriesPage.isSuccessMessageDisplayed()) {
//            System.out.println("DEBUG: Success message on categories page: " + categoriesPage.getSuccessMessageText());
//        }
//        if (createCategoryPage.isSuccessMessageDisplayed()) {
//            System.out.println("DEBUG: Success message on create page: " + createCategoryPage.getSuccessMessageText());
//        }
//
//        Serenity.takeScreenshot();
//    }
//
//    @Then("the new category {string} should be visible in the list")
//    public void theNewCategoryShouldBeVisibleInTheList(String expectedCategoryName) {
//        categoriesPage.waitForPageToLoad();
//
//        // Wait for the category to appear
//        Awaitility.await()
//                .atMost(10, TimeUnit.SECONDS)
//                .pollInterval(1, TimeUnit.SECONDS)
//                .until(() -> categoriesPage.isCategoryDisplayedInList(expectedCategoryName));
//
//        boolean categoryExists = categoriesPage.isCategoryDisplayedInList(expectedCategoryName);
//        System.out.println("DEBUG: Category '" + expectedCategoryName + "' exists in list: " + categoryExists);
//
//        assertThat(categoryExists)
//                .as("New category '" + expectedCategoryName + "' should be visible in the list")
//                .isTrue();
//
//        Serenity.takeScreenshot();
//    }
//
//    // ==================== NEGATIVE TEST STEP DEFINITIONS ====================
//
//    @Then("I should see error message for name less than 3 characters")
//    public void iShouldSeeErrorMessageForNameLessThan3Characters() {
//        // Wait a moment for any validation/error to appear
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        // Check page source for error messages
//        String pageSource = createCategoryPage.getDriver().getPageSource().toLowerCase();
//        System.out.println("DEBUG: Page source contains 'error': " + pageSource.contains("error"));
//        System.out.println("DEBUG: Page source contains 'invalid': " + pageSource.contains("invalid"));
//        System.out.println("DEBUG: Page source contains 'validation': " + pageSource.contains("validation"));
//
//        // Check for various possible error messages
//        boolean hasError = pageSource.contains("at least 3") ||
//                pageSource.contains("minimum 3") ||
//                pageSource.contains("3 characters") ||
//                pageSource.contains("less than 3") ||
//                pageSource.contains("too short") ||
//                pageSource.contains("name must") ||
//                pageSource.contains("required");
//
//        // Also check if we're still on the create page (indicating validation prevented submission)
//        boolean isStillOnCreatePage = createCategoryPage.isCreateCategoryPageLoaded();
//        System.out.println("DEBUG: Still on create page: " + isStillOnCreatePage);
//
//        assertThat(hasError || isStillOnCreatePage)
//                .as("Should show validation error for name less than 3 characters or remain on create page. " +
//                        "Has error text: " + hasError + ", Still on create page: " + isStillOnCreatePage)
//                .isTrue();
//
//        Serenity.takeScreenshot();
//    }
//
//    @Then("I should see error message for name more than 10 characters")
//    public void iShouldSeeErrorMessageForNameMoreThan10Characters() {
//        // Wait a moment for any validation/error to appear
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        // Check page source for error messages
//        String pageSource = createCategoryPage.getDriver().getPageSource().toLowerCase();
//        System.out.println("DEBUG: Page source contains 'error': " + pageSource.contains("error"));
//        System.out.println("DEBUG: Page source contains 'invalid': " + pageSource.contains("invalid"));
//        System.out.println("DEBUG: Page source contains 'validation': " + pageSource.contains("validation"));
//
//        // Check for various possible error messages
//        boolean hasError = pageSource.contains("more than 10") ||
//                pageSource.contains("exceeds 10") ||
//                pageSource.contains("maximum 10") ||
//                pageSource.contains("10 characters") ||
//                pageSource.contains("too long") ||
//                pageSource.contains("max length") ||
//                pageSource.contains("max 10");
//
//        // Also check if we're still on the create page (indicating validation prevented submission)
//        boolean isStillOnCreatePage = createCategoryPage.isCreateCategoryPageLoaded();
//        System.out.println("DEBUG: Still on create page: " + isStillOnCreatePage);
//
//        assertThat(hasError || isStillOnCreatePage)
//                .as("Should show validation error for name more than 10 characters or remain on create page. " +
//                        "Has error text: " + hasError + ", Still on create page: " + isStillOnCreatePage)
//                .isTrue();
//
//        Serenity.takeScreenshot();
//    }
//
//    @And("category should not be created")
//    public void categoryShouldNotBeCreated() {
//        // Wait for page state to stabilize
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        String currentUrl = createCategoryPage.getDriver().getCurrentUrl();
//        System.out.println("DEBUG: Current URL after save attempt: " + currentUrl);
//
//        // Check if we're still on the create page
//        boolean isStillOnCreatePage = createCategoryPage.isCreateCategoryPageLoaded();
//        System.out.println("DEBUG: Still on create page: " + isStillOnCreatePage);
//
//        // Check if URL indicates we're still on create/add page
//        boolean urlIndicatesCreatePage = currentUrl.contains("/add") ||
//                currentUrl.contains("/create") ||
//                currentUrl.contains("new");
//        System.out.println("DEBUG: URL indicates create page: " + urlIndicatesCreatePage);
//
//        // Should NOT be on categories list page
//        boolean isOnListPage = currentUrl.contains("/categories") &&
//                !currentUrl.contains("/add") &&
//                !currentUrl.contains("/create") &&
//                !currentUrl.contains("new");
//        System.out.println("DEBUG: Is on list page: " + isOnListPage);
//
//        // Category should not be created if:
//        // 1. We're still on create page, OR
//        // 2. URL indicates create page, OR
//        // 3. We're NOT on list page
//        boolean categoryNotCreated = isStillOnCreatePage || urlIndicatesCreatePage || !isOnListPage;
//
//        assertThat(categoryNotCreated)
//                .as("Category should not be created. " +
//                        "Still on create page: " + isStillOnCreatePage + ", " +
//                        "URL indicates create: " + urlIndicatesCreatePage + ", " +
//                        "Is on list page: " + isOnListPage)
//                .isTrue();
//
//        Serenity.takeScreenshot();
//    }
//
//    // ==================== ADDITIONAL HELPER METHODS ====================
//
//    @And("I wait for the category list to load")
//    public void iWaitForTheCategoryListToLoad() {
//        categoriesPage.waitForPageToLoad();
//        System.out.println("DEBUG: Category count: " + categoriesPage.getCategoryCount());
//        Serenity.takeScreenshot();
//    }
//
//    // ==================== ADDITIONAL UTILITY STEPS ====================
//
//    @And("I wait for {int} seconds")
//    public void iWaitForSeconds(int seconds) {
//        try {
//            Thread.sleep(seconds * 1000L);
//            System.out.println("DEBUG: Waited for " + seconds + " seconds");
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//    }
//
//    @And("I clear the category name field")
//    public void iClearTheCategoryNameField() {
//        createCategoryPage.enterCategoryName("");
//        System.out.println("DEBUG: Cleared category name field");
//        Serenity.takeScreenshot();
//    }
//
//    @Given("I am on the create category page")
//    public void iAmOnTheCreateCategoryPage() {
//        // First go to categories page
//        iGoToCategoriesPage();
//        // Then click Add A Category
//        iClickOnTheButton("Add A Category");
//    }
//}

package com.group14.qa.ui.steps;

import com.group14.qa.ui.pages.CategoriesPage;
import com.group14.qa.ui.pages.CreateCategoryPage;
import net.serenitybdd.core.Serenity;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import java.util.concurrent.TimeUnit;
import static org.assertj.core.api.Assertions.assertThat;

public class CreateCategorySteps {

    private CategoriesPage categoriesPage;
    private CreateCategoryPage createCategoryPage;

    // Store test data between steps
    private String newCategoryName;
    private String selectedParentCategory;

    // ==================== NAVIGATION STEP ====================

    @And("I go to categories page")
    public void iGoToCategoriesPage() {
        // Direct navigation to categories page
        String categoriesUrl = "http://localhost:8080/ui/categories";
        createCategoryPage.getDriver().get(categoriesUrl);

        // Wait for page to load
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("DEBUG: Navigated to categories page: " + categoriesUrl);
        System.out.println("DEBUG: Current URL: " + createCategoryPage.getDriver().getCurrentUrl());
        Serenity.takeScreenshot();
    }

    // ==================== BASIC STEP DEFINITIONS ====================

    @Given("I click on the {string} button")
    public void iClickOnTheButton(String buttonText) {
        if ("Add A Category".equals(buttonText)) {
            // FIRST: Make sure we're on the categories page
            String currentUrl = createCategoryPage.getDriver().getCurrentUrl();
            if (!currentUrl.contains("/categories")) {
                System.out.println("DEBUG: Not on categories page. Current URL: " + currentUrl);
                iGoToCategoriesPage(); // Navigate to categories page first
            }

            // Now click the Add A Category button
            try {
                createCategoryPage.clickAddCategoryButton();
                System.out.println("DEBUG: Clicked 'Add A Category' button");
            } catch (Exception e) {
                // If button not found, try alternative approach
                System.out.println("DEBUG: Button not found, trying alternative...");
                // Try to find and click the button directly
                createCategoryPage.getDriver().findElement(
                        By.xpath("//a[contains(text(),'Add A Category')]")
                ).click();
            }

            // Wait for create category page to load
            Awaitility.await()
                    .atMost(10, TimeUnit.SECONDS)
                    .until(() -> createCategoryPage.isCreateCategoryPageLoaded());

            System.out.println("DEBUG: Create category page loaded: " + createCategoryPage.isCreateCategoryPageLoaded());
            System.out.println("DEBUG: Current URL after clicking Add: " + createCategoryPage.getDriver().getCurrentUrl());

        } else if ("Save".equals(buttonText)) {
            // Click Save button
            String currentUrl = createCategoryPage.getDriver().getCurrentUrl();
            System.out.println("DEBUG: URL before clicking Save: " + currentUrl);

            createCategoryPage.clickSaveButton();

            // Wait for navigation OR validation error to appear
            Awaitility.await()
                    .atMost(10, TimeUnit.SECONDS)
                    .pollInterval(1, TimeUnit.SECONDS)
                    .until(() -> {
                        String newUrl = createCategoryPage.getDriver().getCurrentUrl();
                        boolean urlChanged = !newUrl.equals(currentUrl);

                        // Check if we're still on the create page (validation error)
                        boolean stillOnCreatePage = createCategoryPage.isCreateCategoryPageLoaded();

                        // Check page for validation errors
                        boolean hasValidationError = checkForValidationError();

                        return urlChanged || !stillOnCreatePage || hasValidationError;
                    });

            System.out.println("DEBUG: URL after clicking Save: " + createCategoryPage.getDriver().getCurrentUrl());
        }

        Serenity.takeScreenshot();
    }

    @When("I fill category name field with {string}")
    public void iFillCategoryNameFieldWith(String categoryName) {
        createCategoryPage.enterCategoryName(categoryName);
        this.newCategoryName = categoryName;
        System.out.println("DEBUG: Filled category name with: " + categoryName);
        Serenity.takeScreenshot();
    }

    @And("I select {string} as parent category")
    public void iSelectAsParentCategory(String parentCategory) {
        createCategoryPage.selectParentCategory(parentCategory);
        this.selectedParentCategory = parentCategory;
        System.out.println("DEBUG: Selected parent category: " + parentCategory);
        Serenity.takeScreenshot();
    }

    @Then("I should be redirected to the categories listing page")
    public void iShouldBeRedirectedToTheCategoriesListingPage() {
        // Wait for categories page to load
        Awaitility.await()
                .atMost(10, TimeUnit.SECONDS)
                .until(() -> categoriesPage.isCategoriesPageLoaded());

        String currentUrl = categoriesPage.getCurrentUrl();
        System.out.println("DEBUG: Current URL after creation: " + currentUrl);

        assertThat(categoriesPage.isCategoriesPageLoaded())
                .as("Should be redirected to categories listing page. Current URL: " + currentUrl)
                .isTrue();

        Serenity.takeScreenshot();
    }

    @And("I should see a success message")
    public void iShouldSeeASuccessMessage() {
        // Wait for success message
        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .until(() -> categoriesPage.isSuccessMessageDisplayed() ||
                        createCategoryPage.isSuccessMessageDisplayed());

        boolean successMessageVisible = categoriesPage.isSuccessMessageDisplayed() ||
                createCategoryPage.isSuccessMessageDisplayed();

        assertThat(successMessageVisible)
                .as("Success message should be displayed")
                .isTrue();

        if (categoriesPage.isSuccessMessageDisplayed()) {
            System.out.println("DEBUG: Success message on categories page: " + categoriesPage.getSuccessMessageText());
        }
        if (createCategoryPage.isSuccessMessageDisplayed()) {
            System.out.println("DEBUG: Success message on create page: " + createCategoryPage.getSuccessMessageText());
        }

        Serenity.takeScreenshot();
    }

    @Then("the new category {string} should be visible in the list")
    public void theNewCategoryShouldBeVisibleInTheList(String expectedCategoryName) {
        categoriesPage.waitForPageToLoad();

        // Wait for the category to appear
        Awaitility.await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .until(() -> categoriesPage.isCategoryDisplayedInList(expectedCategoryName));

        boolean categoryExists = categoriesPage.isCategoryDisplayedInList(expectedCategoryName);
        System.out.println("DEBUG: Category '" + expectedCategoryName + "' exists in list: " + categoryExists);

        assertThat(categoryExists)
                .as("New category '" + expectedCategoryName + "' should be visible in the list")
                .isTrue();

        Serenity.takeScreenshot();
    }

    // ==================== NEGATIVE TEST STEP DEFINITIONS (FIXED) ====================

    @Then("I should see error message for name less than 3 characters")
    public void iShouldSeeErrorMessageForNameLessThan3Characters() {
        // Wait a moment for validation error to appear
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // SIMPLE APPROACH: Check if we're still on the create page (validation prevented submission)
        boolean isStillOnCreatePage = createCategoryPage.isCreateCategoryPageLoaded();
        System.out.println("DEBUG: Still on create page after save (validation error): " + isStillOnCreatePage);

        // Check page source for validation error
        String pageSource = createCategoryPage.getDriver().getPageSource();
        System.out.println("DEBUG: Checking page source for validation errors...");

        // Look for validation error in page source
        boolean hasValidationErrorInSource = pageSource.contains("Category name must be between 3 and 10 characters") ||
                pageSource.contains("between 3 and 10") ||
                pageSource.contains("3 and 10 characters");

        System.out.println("DEBUG: Has validation error in page source: " + hasValidationErrorInSource);

        // Validation should have prevented submission - we should still be on create page
        assertThat(isStillOnCreatePage || hasValidationErrorInSource)
                .as("Should show validation error for name less than 3 characters. " +
                        "Still on create page: " + isStillOnCreatePage + ", " +
                        "Error in page source: " + hasValidationErrorInSource)
                .isTrue();

        Serenity.takeScreenshot();
    }

    @Then("I should see error message for name more than 10 characters")
    public void iShouldSeeErrorMessageForNameMoreThan10Characters() {
        // Wait a moment for validation error to appear
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // SIMPLE APPROACH: Check if we're still on the create page (validation prevented submission)
        boolean isStillOnCreatePage = createCategoryPage.isCreateCategoryPageLoaded();
        System.out.println("DEBUG: Still on create page after save (validation error): " + isStillOnCreatePage);

        // Check page source for validation error
        String pageSource = createCategoryPage.getDriver().getPageSource();
        System.out.println("DEBUG: Checking page source for validation errors...");

        // Look for validation error in page source
        boolean hasValidationErrorInSource = pageSource.contains("Category name must be between 3 and 10 characters") ||
                pageSource.contains("between 3 and 10") ||
                pageSource.contains("3 and 10 characters");

        System.out.println("DEBUG: Has validation error in page source: " + hasValidationErrorInSource);

        // Validation should have prevented submission - we should still be on create page
        assertThat(isStillOnCreatePage || hasValidationErrorInSource)
                .as("Should show validation error for name more than 10 characters. " +
                        "Still on create page: " + isStillOnCreatePage + ", " +
                        "Error in page source: " + hasValidationErrorInSource)
                .isTrue();

        Serenity.takeScreenshot();
    }

    @And("category should not be created")
    public void categoryShouldNotBeCreated() {
        // Wait for page state to stabilize
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String currentUrl = createCategoryPage.getDriver().getCurrentUrl();
        System.out.println("DEBUG: Current URL after save attempt: " + currentUrl);

        // Check if we're still on the create page (validation prevented creation)
        boolean isStillOnCreatePage = createCategoryPage.isCreateCategoryPageLoaded();
        System.out.println("DEBUG: Still on create page: " + isStillOnCreatePage);

        // Check if URL indicates we're still on create/add page
        boolean urlIndicatesCreatePage = currentUrl.contains("/add") ||
                currentUrl.contains("/create") ||
                currentUrl.contains("new");
        System.out.println("DEBUG: URL indicates create page: " + urlIndicatesCreatePage);

        // Should NOT be on categories list page
        boolean isOnListPage = currentUrl.contains("/categories") &&
                !currentUrl.contains("/add") &&
                !currentUrl.contains("/create") &&
                !currentUrl.contains("new");
        System.out.println("DEBUG: Is on list page: " + isOnListPage);

        // Category should not be created if:
        // 1. We're still on create page, OR
        // 2. URL indicates create page, OR
        // 3. We're NOT on list page
        boolean categoryNotCreated = isStillOnCreatePage || urlIndicatesCreatePage || !isOnListPage;

        assertThat(categoryNotCreated)
                .as("Category should not be created. " +
                        "Still on create page: " + isStillOnCreatePage + ", " +
                        "URL indicates create: " + urlIndicatesCreatePage + ", " +
                        "Is on list page: " + isOnListPage)
                .isTrue();

        Serenity.takeScreenshot();
    }

    // ==================== HELPER METHODS ====================

    private boolean checkForValidationError() {
        try {
            // Try to find the validation error specifically for the name field
            String pageSource = createCategoryPage.getDriver().getPageSource();

            // Check for the exact error message
            if (pageSource.contains("Category name must be between 3 and 10 characters")) {
                System.out.println("DEBUG: Found exact validation error message in page source");
                return true;
            }

            // Check for the name field having error styling
            try {
                String nameFieldClass = createCategoryPage.getDriver()
                        .findElement(By.id("name"))
                        .getAttribute("class");
                if (nameFieldClass != null &&
                        (nameFieldClass.contains("is-invalid") ||
                                nameFieldClass.contains("error") ||
                                nameFieldClass.contains("invalid"))) {
                    System.out.println("DEBUG: Name field has error styling: " + nameFieldClass);
                    return true;
                }
            } catch (Exception e) {
                // Name field not found or error - that's ok
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== ADDITIONAL HELPER METHODS ====================

    @And("I wait for the category list to load")
    public void iWaitForTheCategoryListToLoad() {
        categoriesPage.waitForPageToLoad();
        System.out.println("DEBUG: Category count: " + categoriesPage.getCategoryCount());
        Serenity.takeScreenshot();
    }

    // ==================== ADDITIONAL UTILITY STEPS ====================

    @And("I wait for {int} seconds")
    public void iWaitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
            System.out.println("DEBUG: Waited for " + seconds + " seconds");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @And("I clear the category name field")
    public void iClearTheCategoryNameField() {
        createCategoryPage.enterCategoryName("");
        System.out.println("DEBUG: Cleared category name field");
        Serenity.takeScreenshot();
    }

    @Given("I am on the create category page")
    public void iAmOnTheCreateCategoryPage() {
        // First go to categories page
        iGoToCategoriesPage();
        // Then click Add A Category
        iClickOnTheButton("Add A Category");
    }

    // Additional step for debugging - this will help see what's on the page
    @And("I print page info for debugging")
    public void iPrintPageInfoForDebugging() {
        String currentUrl = createCategoryPage.getDriver().getCurrentUrl();
        String pageTitle = createCategoryPage.getDriver().getTitle();
        String pageSource = createCategoryPage.getDriver().getPageSource();

        System.out.println("DEBUG: === PAGE INFO ===");
        System.out.println("DEBUG: Current URL: " + currentUrl);
        System.out.println("DEBUG: Page title: " + pageTitle);
        System.out.println("DEBUG: Page source contains 'between 3 and 10': " + pageSource.contains("between 3 and 10"));
        System.out.println("DEBUG: Page source contains 'Category name': " + pageSource.contains("Category name"));
        System.out.println("DEBUG: Page source length: " + pageSource.length());
        System.out.println("DEBUG: === END PAGE INFO ===");
    }

    // New step to specifically check the validation error
    @Then("I should see validation error 'Category name must be between 3 and 10 characters'")
    public void iShouldSeeValidationErrorMessage() {
        // Wait for validation to appear
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // First use the debug step to see what's on the page
        iPrintPageInfoForDebugging();

        // Check page source directly
        String pageSource = createCategoryPage.getDriver().getPageSource();
        boolean hasExactMessage = pageSource.contains("Category name must be between 3 and 10 characters");
        boolean hasPartialMessage = pageSource.contains("between 3 and 10");

        System.out.println("DEBUG: Page has exact error message: " + hasExactMessage);
        System.out.println("DEBUG: Page has partial error message: " + hasPartialMessage);

        assertThat(hasExactMessage || hasPartialMessage)
                .as("Should show validation error about 3-10 characters. " +
                        "Exact message found: " + hasExactMessage + ", " +
                        "Partial message found: " + hasPartialMessage)
                .isTrue();

        Serenity.takeScreenshot();
    }
}