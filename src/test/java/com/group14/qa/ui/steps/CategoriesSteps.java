//package com.group14.qa.ui.steps;
//
//import com.group14.qa.ui.pages.CategoriesPage;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.When;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.And;
//import net.serenitybdd.core.Serenity;
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class CategoriesSteps {
//
//    private CategoriesPage categoriesPage;
//
//    @Given("I navigate to the categories page")
//    public void navigateToCategoriesPage() {
//        // Wait after login
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        // Try to click Categories tab (like Plants tab in your example)
//        try {
//            categoriesPage.openCategoriesTab();
//        } catch (Exception e) {
//            // Fallback: direct navigation
//            categoriesPage.open();
//        }
//
//        categoriesPage.waitForPageToLoad();
//        assertThat(categoriesPage.isCategoriesPageLoaded()).isTrue();
//    }
//
//    @When("I view the categories page")
//    public void viewCategoriesPage() {
//        categoriesPage.waitForPageToLoad();
//        Serenity.takeScreenshot();
//    }
//
//    @Then("I should see the {string} button")
//    public void verifyAddCategoryButtonVisible(String buttonName) {
//        assertThat(categoriesPage.isAddButtonVisible())
//                .as("Add Category button should be visible to admin")
//                .isTrue();
//
//        Serenity.takeScreenshot();
//    }
//
//    @And("the {string} button should be clickable")
//    public void verifyAddCategoryButtonClickable(String buttonName) {
//        assertThat(categoriesPage.isAddButtonVisible()).isTrue();
//        assertThat(categoriesPage.isAddButtonEnabled())
//                .as("Add Category button should be enabled/clickable")
//                .isTrue();
//
//        // Click and verify navigation
//        String currentUrl = categoriesPage.getCurrentUrl();
//        categoriesPage.clickAddCategory();
//
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        String newUrl = categoriesPage.getCurrentUrl();
//        assertThat(newUrl).isNotEqualTo(currentUrl);
//        assertThat(newUrl).contains("add");
//
//        Serenity.takeScreenshot();
//    }
//
//    @Then("I should not see the {string} button")
//    public void verifyAddCategoryButtonNotVisible(String buttonName) {
//        assertThat(categoriesPage.isAddButtonVisible())
//                .as("Add Category button should NOT be visible to regular user")
//                .isFalse();
//
//        Serenity.takeScreenshot();
//    }
//}

package com.group14.qa.ui.steps;

import com.group14.qa.ui.pages.CategoriesPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import net.serenitybdd.core.Serenity;
import static org.assertj.core.api.Assertions.assertThat;
import org.openqa.selenium.By;

public class CategoriesSteps {

    private CategoriesPage categoriesPage;

    // ==================== NAVIGATION ====================

    @Given("I navigate to the categories page")
    public void navigateToCategoriesPage() {
        // Wait after login (login is handled in CommonSteps)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Try to click Categories tab
        try {
            categoriesPage.openCategoriesTab();
            System.out.println("DEBUG: Clicked Categories tab");
        } catch (Exception e) {
            System.out.println("DEBUG: Could not click tab, trying direct navigation: " + e.getMessage());
            // Fallback: direct navigation
            categoriesPage.open();
        }

        categoriesPage.waitForPageToLoad();

        // Debug info
        System.out.println("DEBUG: Current URL: " + categoriesPage.getCurrentUrl());
        System.out.println("DEBUG: Page title: " + categoriesPage.getPageTitle());

        // Verify we're on categories page
        assertThat(categoriesPage.isCategoriesPageLoaded())
                .as("Should be on categories page. Current URL: " + categoriesPage.getCurrentUrl())
                .isTrue();

        Serenity.takeScreenshot();
    }

    @When("I view the categories page")
    public void viewCategoriesPage() {
        categoriesPage.waitForPageToLoad();

        // Debug: Check if Add button is present
        boolean isButtonVisible = categoriesPage.isAddButtonVisible();
        boolean isButtonEnabled = categoriesPage.isAddButtonEnabled();
        System.out.println("DEBUG: Add button visible: " + isButtonVisible);
        System.out.println("DEBUG: Add button enabled: " + isButtonEnabled);

        Serenity.takeScreenshot();
    }

    // ==================== ADMIN SCENARIO ====================

    @Then("I should see the {string} button")
    public void verifyAddCategoryButtonVisible(String buttonName) {
        // Wait a bit more
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean isVisible = categoriesPage.isAddButtonVisible();

        if (!isVisible) {
            // Additional checks
            System.out.println("DEBUG: Button not immediately visible, checking page state...");
            System.out.println("DEBUG: Page URL: " + categoriesPage.getCurrentUrl());
            System.out.println("DEBUG: Page source contains 'Add A Category': " +
                    categoriesPage.getDriver().getPageSource().contains("Add A Category"));

            // Try waiting longer
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            isVisible = categoriesPage.isAddButtonVisible();
            System.out.println("DEBUG: Button visible after longer wait: " + isVisible);
        }

        assertThat(isVisible)
                .as("Add Category button should be visible to admin")
                .isTrue();

        Serenity.takeScreenshot();
    }

    @And("the {string} button should be clickable")
    public void verifyAddCategoryButtonClickable(String buttonName) {
        assertThat(categoriesPage.isAddButtonVisible())
                .as("Add Category button should be visible")
                .isTrue();

        assertThat(categoriesPage.isAddButtonEnabled())
                .as("Add Category button should be enabled/clickable for admin")
                .isTrue();

        // Test clicking the button
        String currentUrl = categoriesPage.getCurrentUrl();
        System.out.println("DEBUG: URL before clicking Add button: " + currentUrl);

        categoriesPage.clickAddCategory();

        // Wait for navigation
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String newUrl = categoriesPage.getCurrentUrl();
        System.out.println("DEBUG: URL after clicking Add button: " + newUrl);

        // Verify we navigated to add category page
        assertThat(newUrl).isNotEqualTo(currentUrl);
        assertThat(newUrl).contains("add");

        Serenity.takeScreenshot();
    }

    // ==================== REGULAR USER SCENARIO ====================

    @Then("I should not see the {string} button")
    public void verifyAddCategoryButtonNotVisible(String buttonName) {
        // Wait for page to stabilize
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean isButtonVisible = categoriesPage.isAddButtonVisible();

        System.out.println("DEBUG: Regular user - Add button visible: " + isButtonVisible);
        System.out.println("DEBUG: Regular user - Current URL: " + categoriesPage.getCurrentUrl());

        // For regular users, the button should NOT be visible
        assertThat(isButtonVisible)
                .as("Add Category button should NOT be visible to regular user")
                .isFalse();

        Serenity.takeScreenshot();
    }

    // ==================== COMMON METHODS ====================

    @And("I go back to categories list")
    public void goBackToCategoriesList() {
        // Navigate back to categories list
        categoriesPage.open();
        categoriesPage.waitForPageToLoad();
        Serenity.takeScreenshot();
    }

    @And("I should see the categories list")
    public void verifyCategoriesListDisplayed() {
        assertThat(categoriesPage.getCategoryCount() > 0 || categoriesPage.isEmptyStateDisplayed())
                .as("Categories list should be displayed")
                .isTrue();
    }
}
