package com.group14.qa.ui.steps;

import com.group14.qa.ui.pages.CategoriesPage;
import com.group14.qa.ui.pages.EditCategoryPage;
import net.serenitybdd.core.Serenity;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import java.util.concurrent.TimeUnit;
import java.util.UUID;
import net.serenitybdd.annotations.Steps;

import static org.assertj.core.api.Assertions.assertThat;

public class EditCategorySteps {

    private CategoriesPage categoriesPage;
    private EditCategoryPage editCategoryPage;

    @Steps
    private CategoriesSteps categoriesSteps;

    // Store test data between steps
    private String originalCategoryName;
    private String updatedCategoryName;

    public EditCategorySteps() {
        this.categoriesPage = new CategoriesPage();
        this.editCategoryPage = new EditCategoryPage();
    }

    // ==================== NAVIGATION STEPS ====================

    @And("I am on the categories listing page")
    public void iAmOnTheCategoriesListingPage() {
        categoriesPage.waitForPageToLoad();
        assertThat(categoriesPage.isCategoriesPageLoaded())
                .as("Should be on categories listing page")
                .isTrue();
        System.out.println("DEBUG: On categories listing page");
        Serenity.takeScreenshot();
    }

    // ==================== EDIT CATEGORY STEPS ====================

    @When("I click on the {string} button for category {string}")
    public void iClickOnTheButtonForCategory(String buttonText, String categoryName) {
        if ("Edit Category".equals(buttonText) || "Edit".equals(buttonText)) {
            // Find and click the edit button for the specific category
            categoriesPage.clickEditButtonForCategory(categoryName);

            // Wait for edit page to load
            Awaitility.await()
                    .atMost(10, TimeUnit.SECONDS)
                    .until(() -> editCategoryPage.isEditCategoryPageLoaded());

            System.out.println("DEBUG: Clicked edit button for category: " + categoryName);
            System.out.println("DEBUG: Edit page loaded: " + editCategoryPage.isEditCategoryPageLoaded());

            // Store original category name
            this.originalCategoryName = categoryName;

        } else if ("Update".equals(buttonText) || "Save".equals(buttonText)) {
            // Click update/save button
            String currentUrl = editCategoryPage.getDriver().getCurrentUrl();
            System.out.println("DEBUG: URL before clicking Update: " + currentUrl);

            editCategoryPage.clickUpdateButton();

            // Wait for response
            Awaitility.await()
                    .atMost(10, TimeUnit.SECONDS)
                    .pollInterval(1, TimeUnit.SECONDS)
                    .until(() -> {
                        String newUrl = editCategoryPage.getDriver().getCurrentUrl();
                        boolean urlChanged = !newUrl.equals(currentUrl);

                        // Check for success or error
                        boolean hasSuccess = categoriesPage.isSuccessMessageDisplayed();
                        boolean hasError = editCategoryPage.isValidationErrorDisplayed();

                        return urlChanged || hasSuccess || hasError;
                    });

            System.out.println("DEBUG: URL after clicking Update: " + editCategoryPage.getDriver().getCurrentUrl());
        }

        Serenity.takeScreenshot();
    }

    @And("I update category name to {string}")
    public void iUpdateCategoryNameTo(String newCategoryName) {
        editCategoryPage.enterCategoryName(newCategoryName);
        this.updatedCategoryName = newCategoryName;
        System.out.println("DEBUG: Updated category name to: " + newCategoryName);
        Serenity.takeScreenshot();
    }

    @And("I update description to {string}")
    public void iUpdateDescriptionTo(String description) {
        editCategoryPage.enterDescription(description);
        System.out.println("DEBUG: Updated description to: " + description);
        Serenity.takeScreenshot();
    }

    @And("I update parent category to {string}")
    public void iUpdateParentCategoryTo(String parentCategory) {
        editCategoryPage.selectParentCategory(parentCategory);
        System.out.println("DEBUG: Updated parent category to: " + parentCategory);
        Serenity.takeScreenshot();
    }

    @And("I clear the category name field in edit form")
    public void iClearTheCategoryNameFieldInEditForm() {
        editCategoryPage.clearCategoryName();
        System.out.println("DEBUG: Cleared category name field in edit form");
        Serenity.takeScreenshot();
    }

    // ==================== VALIDATION STEPS ====================

    @Then("I should see update success message")
    public void iShouldSeeUpdateSuccessMessage() {
        // Wait for success message
        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .until(() -> categoriesPage.isSuccessMessageDisplayed() ||
                        editCategoryPage.isSuccessMessageDisplayed());

        boolean successMessageVisible = categoriesPage.isSuccessMessageDisplayed() ||
                editCategoryPage.isSuccessMessageDisplayed();

        assertThat(successMessageVisible)
                .as("Update success message should be displayed")
                .isTrue();

        if (categoriesPage.isSuccessMessageDisplayed()) {
            String successMsg = categoriesPage.getSuccessMessageText();
            System.out.println("DEBUG: Success message on categories page: " + successMsg);
            assertThat(successMsg.toLowerCase())
                    .as("Success message should indicate update")
                    .containsAnyOf("updated", "saved", "success");
        }
        if (editCategoryPage.isSuccessMessageDisplayed()) {
            String successMsg = editCategoryPage.getSuccessMessageText();
            System.out.println("DEBUG: Success message on edit page: " + successMsg);
        }

        Serenity.takeScreenshot();
    }

    @Then("I should see validation error for empty category name")
    public void iShouldSeeValidationErrorForEmptyCategoryName() {
        // Wait for error to appear
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean hasError = editCategoryPage.isValidationErrorDisplayed();
        boolean isNameFieldInError = editCategoryPage.isNameFieldInErrorState();
        String errorMessage = editCategoryPage.getValidationErrorMessage();

        System.out.println("DEBUG: Validation error displayed: " + hasError);
        System.out.println("DEBUG: Name field in error state: " + isNameFieldInError);
        System.out.println("DEBUG: Error message: " + errorMessage);

        // Check for validation error message
        boolean hasCorrectError = errorMessage.toLowerCase().contains("required") ||
                errorMessage.toLowerCase().contains("empty") ||
                errorMessage.toLowerCase().contains("at least") ||
                errorMessage.toLowerCase().contains("between 3 and 10");

        assertThat(hasError || isNameFieldInError || hasCorrectError)
                .as("Should show validation error for empty category name. " +
                        "Error displayed: " + hasError + ", " +
                        "Field error: " + isNameFieldInError + ", " +
                        "Has correct error: " + hasCorrectError)
                .isTrue();

        Serenity.takeScreenshot();
    }

    @And("the category update should fail")
    public void theCategoryUpdateShouldFail() {
        // Check if we're still on edit page
        boolean isStillOnEditPage = editCategoryPage.isEditCategoryPageLoaded();
        String currentUrl = editCategoryPage.getDriver().getCurrentUrl();

        System.out.println("DEBUG: Still on edit page: " + isStillOnEditPage);
        System.out.println("DEBUG: Current URL: " + currentUrl);

        // Should either be on edit page or show error
        boolean updateFailed = isStillOnEditPage ||
                editCategoryPage.isValidationErrorDisplayed() ||
                currentUrl.contains("/edit");

        assertThat(updateFailed)
                .as("Category update should fail. Still on edit page: " + isStillOnEditPage)
                .isTrue();

        Serenity.takeScreenshot();
    }

    @Then("the updated category {string} should be visible in the list")
    public void theUpdatedCategoryShouldBeVisibleInTheList(String expectedCategoryName) {
        categoriesPage.waitForPageToLoad();

        // Wait for the updated category to appear
        Awaitility.await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .until(() -> categoriesPage.isCategoryDisplayedInList(expectedCategoryName));

        boolean categoryExists = categoriesPage.isCategoryDisplayedInList(expectedCategoryName);
        System.out.println("DEBUG: Updated category '" + expectedCategoryName + "' exists in list: " + categoryExists);

        assertThat(categoryExists)
                .as("Updated category '" + expectedCategoryName + "' should be visible in the list")
                .isTrue();

        Serenity.takeScreenshot();
    }

    @And("the original category {string} should not be visible in the list")
    public void theOriginalCategoryShouldNotBeVisibleInTheList(String originalCategoryName) {
        categoriesPage.waitForPageToLoad();

        // Check that original category is not in list
        boolean originalCategoryExists = categoriesPage.isCategoryDisplayedInList(originalCategoryName);
        System.out.println("DEBUG: Original category '" + originalCategoryName + "' exists in list: " + originalCategoryExists);

        assertThat(originalCategoryExists)
                .as("Original category '" + originalCategoryName + "' should not be visible in the list")
                .isFalse();

        Serenity.takeScreenshot();
    }

    // ==================== UTILITY STEPS ====================

    @And("I wait for {int} seconds for edit page")
    public void iWaitForSecondsForEditPage(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
            System.out.println("DEBUG: Waited for " + seconds + " seconds");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @And("I create a test category named {string} with parent {string}")
    public void iCreateATestCategoryNamedWithParent(String categoryName, String parentCategory) {
        // This would use your existing CreateCategorySteps
        // For now, we'll assume the category already exists
        System.out.println("DEBUG: Assuming category '" + categoryName + "' already exists with parent '" + parentCategory + "'");
    }

    @Given("I am on the edit page for category {string}")
    public void iAmOnTheEditPageForCategory(String categoryName) {
        categoriesSteps.navigateToCategoriesPage();
        iClickOnTheButtonForCategory("Edit Category", categoryName);
    }

    @And("I verify current category name is {string}")
    public void iVerifyCurrentCategoryNameIs(String expectedName) {
        String actualName = editCategoryPage.getCategoryNameValue();
        System.out.println("DEBUG: Current category name in edit form: " + actualName);

        assertThat(actualName)
                .as("Category name in edit form should be: " + expectedName)
                .isEqualTo(expectedName);

        Serenity.takeScreenshot();
    }

    @And("I generate unique category name with prefix {string}")
    public String iGenerateUniqueCategoryNameWithPrefix(String prefix) {
        String uniqueName = prefix + " " + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("DEBUG: Generated unique category name: " + uniqueName);
        return uniqueName;
    }

    // ==================== PREPARATION STEPS ====================

    @Given("there is an existing category named {string}")
    public void thereIsAnExistingCategoryNamed(String categoryName) {
        // Check if category exists, if not create it
        categoriesSteps.navigateToCategoriesPage();

        boolean categoryExists = categoriesPage.isCategoryDisplayedInList(categoryName);

        if (!categoryExists) {
            System.out.println("DEBUG: Category '" + categoryName + "' does not exist. Need to create it first.");
            // You would call your CreateCategorySteps here
        } else {
            System.out.println("DEBUG: Category '" + categoryName + "' already exists");
        }

        Serenity.takeScreenshot();
    }

    @And("I get the first available category for editing")
    public String iGetTheFirstAvailableCategoryForEditing() {
        categoriesPage.waitForPageToLoad();
        String firstCategory = categoriesPage.getFirstCategoryName();
        System.out.println("DEBUG: First available category: " + firstCategory);
        return firstCategory;
    }
}