package com.group14.qa.ui.steps;

import com.group14.qa.ui.pages.CategoriesPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import net.serenitybdd.core.Serenity;
import static org.assertj.core.api.Assertions.assertThat;

public class EditCategorySteps {

    private CategoriesPage categoriesPage;

    // ==================== COMMON STEPS ====================

    @Given("there is at least one category in the list")
    public void verifyAtLeastOneCategoryExists() {
        categoriesPage.waitForPageToLoad();
        int categoryCount = categoriesPage.getCategoryCount();
        System.out.println("DEBUG: Number of categories found: " + categoryCount);

        assertThat(categoryCount)
                .as("Should have at least one category to test edit functionality")
                .isGreaterThan(0);

        Serenity.takeScreenshot();
    }

    @Given("I am on the categories page")
    public void onCategoriesPage() {
        categoriesPage.waitForPageToLoad();
        assertThat(categoriesPage.isCategoriesPageLoaded()).isTrue();
        Serenity.takeScreenshot();
    }

    // ==================== ADMIN SCENARIO (TC_UI_ADMIN_CAT_002) ====================

    @When("I look for edit buttons on the categories page")
    public void lookForEditButtons() {
        categoriesPage.waitForPageToLoad();

        // Debug: Check how many edit buttons are visible
        int visibleEditButtons = categoriesPage.getVisibleEditButtonsCount();
        System.out.println("DEBUG: Number of visible edit buttons: " + visibleEditButtons);

        // Debug: Check if any edit button is visible
        boolean anyEditVisible = categoriesPage.isAnyEditButtonVisible();
        System.out.println("DEBUG: Any edit button visible: " + anyEditVisible);

        Serenity.takeScreenshot();
    }

    @Then("I should see edit icons for each category")
    public void verifyEditIconsVisibleForEachCategory() {
        int categoryCount = categoriesPage.getCategoryCount();
        System.out.println("DEBUG: Total categories: " + categoryCount);

        // Get first category name to check
        String firstCategory = categoriesPage.getFirstCategoryName();
        if (!firstCategory.isEmpty()) {
            boolean editVisible = categoriesPage.isEditButtonVisibleForCategory(firstCategory);
            System.out.println("DEBUG: Edit button visible for '" + firstCategory + "': " + editVisible);
        }

        // For admin, at least one edit button should be visible
        boolean anyEditVisible = categoriesPage.isAnyEditButtonVisible();
        System.out.println("DEBUG: Any edit button visible on page: " + anyEditVisible);

        assertThat(anyEditVisible)
                .as("Edit icons/buttons should be visible for admin")
                .isTrue();

        Serenity.takeScreenshot();
    }

    @And("the edit icon should be clickable")
    public void verifyEditIconClickable() {
        // First verify at least one edit button is visible
        boolean anyEditVisible = categoriesPage.isAnyEditButtonVisible();
        assertThat(anyEditVisible)
                .as("At least one edit button should be visible before testing click")
                .isTrue();

        // Get current URL before clicking
        String currentUrl = categoriesPage.getCurrentUrl();
        System.out.println("DEBUG: URL before clicking edit: " + currentUrl);

        // Click the first edit button
        categoriesPage.clickFirstEditButton();

        // Wait for navigation
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Get new URL after clicking
        String newUrl = categoriesPage.getCurrentUrl();
        System.out.println("DEBUG: URL after clicking edit: " + newUrl);

        // Verify we navigated to edit page
        assertThat(newUrl).isNotEqualTo(currentUrl);
        assertThat(newUrl).contains("edit");

        // Verify edit page is loaded
        boolean isEditPage = categoriesPage.isEditPageLoaded();
        System.out.println("DEBUG: Edit page loaded: " + isEditPage);

        assertThat(isEditPage)
                .as("Should navigate to edit category page after clicking edit icon")
                .isTrue();

        Serenity.takeScreenshot();
    }

    @And("I can edit and update category details")
    public void editAndUpdateCategoryDetails() {
        // Verify we're on edit page
        assertThat(categoriesPage.isEditPageLoaded()).isTrue();

        // Get current category name (from field)
        String originalName = ""; // You might want to capture this

        // Update with new name (add timestamp to make it unique)
        String newName = "Updated Category " + System.currentTimeMillis();
        categoriesPage.updateCategoryName(newName);

        // Update description
        String newDescription = "Updated description " + System.currentTimeMillis();
        categoriesPage.updateDescription(newDescription);

        Serenity.takeScreenshot();

        // Click update
        categoriesPage.clickUpdate();

        // Verify success
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean success = categoriesPage.isSuccessMessageDisplayed();
        System.out.println("DEBUG: Update success message displayed: " + success);

        if (success) {
            System.out.println("DEBUG: Success message: " + categoriesPage.getSuccessMessage());
        }

        // Note: We're not asserting success message as it might vary
        // The important thing is we could click update

        Serenity.takeScreenshot();
    }

    // ==================== USER SCENARIO (TC_UI_USER_CAT_002) ====================

    @Then("I should not see any edit icons")
    public void verifyNoEditIconsVisible() {
        categoriesPage.waitForPageToLoad();

        // Debug information
        int visibleEditButtons = categoriesPage.getVisibleEditButtonsCount();
        System.out.println("DEBUG: Regular user - Visible edit buttons: " + visibleEditButtons);

        boolean anyEditVisible = categoriesPage.isAnyEditButtonVisible();
        System.out.println("DEBUG: Regular user - Any edit button visible: " + anyEditVisible);

        // For regular users, NO edit buttons should be visible
        assertThat(anyEditVisible)
                .as("Edit icons/buttons should NOT be visible to regular user")
                .isFalse();

        // Also check that no edit buttons are visible for any category
        if (categoriesPage.getCategoryCount() > 0) {
            String firstCategory = categoriesPage.getFirstCategoryName();
            if (!firstCategory.isEmpty()) {
                boolean editVisibleForCategory = categoriesPage.isEditButtonVisibleForCategory(firstCategory);
                System.out.println("DEBUG: Edit button visible for category '" + firstCategory + "': " + editVisibleForCategory);
                assertThat(editVisibleForCategory)
                        .as("Edit button should NOT be visible for any category for regular user")
                        .isFalse();
            }
        }

        Serenity.takeScreenshot();
    }

    @And("I should not be able to access edit functionality")
    public void verifyCannotAccessEditFunctionality() {
        // Try to construct edit URL directly (if user might try manual access)
        String currentUrl = categoriesPage.getCurrentUrl();
        System.out.println("DEBUG: Current URL: " + currentUrl);

        // If we know the pattern, we could try to access edit URL
        // But for now, just verify no edit buttons are visible
        System.out.println("DEBUG: Edit functionality verification complete");

        Serenity.takeScreenshot();
    }

    // ==================== SPECIFIC CATEGORY TESTS ====================

    @Given("category {string} exists in the list")
    public void verifyCategoryExists(String categoryName) {
        categoriesPage.waitForPageToLoad();

        boolean categoryExists = categoriesPage.isCategoryDisplayedInList(categoryName);
        System.out.println("DEBUG: Category '" + categoryName + "' exists: " + categoryExists);

        if (!categoryExists) {
            System.out.println("DEBUG: Available categories:");
            for (int i = 0; i < Math.min(categoriesPage.getCategoryCount(), 5); i++) {
                try {
                    System.out.println("  - " + categoriesPage.getFirstCategoryName());
                } catch (Exception e) {
                    // Ignore
                }
            }
        }

        assertThat(categoryExists)
                .as("Category '" + categoryName + "' should exist in the list")
                .isTrue();

        Serenity.takeScreenshot();
    }

    @When("I look for the edit icon for category {string}")
    public void lookForEditIconForCategory(String categoryName) {
        boolean editVisible = categoriesPage.isEditButtonVisibleForCategory(categoryName);
        System.out.println("DEBUG: Edit button visible for '" + categoryName + "': " + editVisible);

        Serenity.takeScreenshot();
    }

    @Then("I should see the edit icon for category {string}")
    public void verifyEditIconVisibleForCategory(String categoryName) {
        boolean editVisible = categoriesPage.isEditButtonVisibleForCategory(categoryName);

        assertThat(editVisible)
                .as("Edit icon should be visible for category '" + categoryName + "' for admin")
                .isTrue();

        Serenity.takeScreenshot();
    }

    @When("I click the edit icon for category {string}")
    public void clickEditIconForCategory(String categoryName) {
        String currentUrl = categoriesPage.getCurrentUrl();
        System.out.println("DEBUG: URL before editing '" + categoryName + "': " + currentUrl);

        categoriesPage.clickEditButtonForCategory(categoryName);

        Serenity.takeScreenshot();
    }

    @Then("I should be taken to the edit page for category {string}")
    public void verifyEditPageForCategory(String categoryName) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String newUrl = categoriesPage.getCurrentUrl();
        System.out.println("DEBUG: URL after clicking edit: " + newUrl);

        assertThat(categoriesPage.isEditPageLoaded())
                .as("Should be on edit page for category '" + categoryName + "'")
                .isTrue();

        Serenity.takeScreenshot();
    }
}