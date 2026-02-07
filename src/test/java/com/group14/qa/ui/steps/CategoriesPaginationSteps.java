// src/test/java/com/group14/qa/ui/steps/CategoriesPaginationSteps.java
package com.group14.qa.ui.steps;

import com.group14.qa.ui.pages.CategoriesPage;
import com.group14.qa.testdata.TestUsers;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.annotations.Steps;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

public class CategoriesPaginationSteps {

    private CategoriesPage categoriesPage;

    @Steps
    private LoginSteps loginSteps;

    private WebDriverWait wait;
    private List<String> firstPageData = new ArrayList<>();
    private List<String> secondPageData = new ArrayList<>();

    @Given("I am logged in as a user")
    public void i_am_logged_in_as_a_user() {
        loginSteps.i_am_on_the_login_page();
        loginSteps.i_login_as_regular_user();
        loginSteps.i_should_be_redirected_to_the_dashboard();
    }

    @Given("I am on the dashboard page")
    public void i_am_on_the_dashboard_page() {
        categoriesPage.waitFor(2000);
        String currentUrl = categoriesPage.getCurrentUrl();
        System.out.println("DEBUG: Current URL after login: " + currentUrl);

        // Simple check - just ensure we're not on login page
        Assertions.assertThat(currentUrl)
                .as("Should not be on login page")
                .doesNotContain("/login");
    }

    @When("I navigate to the Categories page")
    public void i_navigate_to_the_categories_page() {
        categoriesPage.openCategoriesTab();
    }

    @Then("the Categories page should load successfully")
    public void the_categories_page_should_load_successfully() {
        Assertions.assertThat(categoriesPage.isCategoriesPageLoaded())
                .as("Categories page should load successfully")
                .isTrue();
    }

    @Then("the pagination component should be displayed")
    public void the_pagination_component_should_be_displayed() {
        Assertions.assertThat(categoriesPage.isPaginationDisplayed())
                .as("Pagination component should be displayed")
                .isTrue();
    }

    @Then("I should see page numbers in the pagination")
    public void i_should_see_page_numbers_in_the_pagination() {
        List<String> pageNumbers = categoriesPage.getPageNumbers();
        Assertions.assertThat(pageNumbers)
                .as("Should see page numbers in pagination")
                .isNotEmpty();

        System.out.println("DEBUG: Found page numbers: " + pageNumbers);
    }

    @Then("the Previous button should be disabled on the first page")
    public void the_previous_button_should_be_disabled_on_the_first_page() {
        boolean isPreviousDisabled = !categoriesPage.isPreviousButtonEnabled();
        Assertions.assertThat(isPreviousDisabled)
                .as("Previous button should be disabled on first page")
                .isTrue();
    }

    @Then("the Next button should be available")
    public void the_next_button_should_be_available() {
        boolean isNextAvailable = categoriesPage.isNextButtonEnabled();
        Assertions.assertThat(isNextAvailable)
                .as("Next button should be available")
                .isTrue();
    }

    @Then("the pagination should be properly aligned")
    public void the_pagination_should_be_properly_aligned() {
        boolean isAligned = categoriesPage.verifyPaginationAlignment();
        Assertions.assertThat(isAligned)
                .as("Pagination should be properly aligned")
                .isTrue();
    }

    // TC_UI_USER_CAT_003 Steps
    @When("I click on page number {string}")
    public void i_click_on_page_number(String pageNumber) {
        // Store current page data before navigation
        firstPageData = categoriesPage.getCurrentPageData();
        Serenity.setSessionVariable("firstPageData").to(firstPageData);

        System.out.println("DEBUG: Clicking page number: " + pageNumber);
        System.out.println("DEBUG: First page data size: " + firstPageData.size());

        categoriesPage.clickPageNumber(pageNumber);
    }

    @Then("I should be navigated to page {string}")
    public void i_should_be_navigated_to_page(String expectedPage) {
        String activePage = categoriesPage.getActivePageNumber();
        Assertions.assertThat(activePage)
                .as("Should be navigated to page " + expectedPage)
                .isEqualTo(expectedPage);
    }

    @Then("the page should display different category data")
    public void the_page_should_display_different_category_data() {
        // Get second page data
        secondPageData = categoriesPage.getCurrentPageData();

        System.out.println("DEBUG: Second page data size: " + secondPageData.size());

        // Verify we have data
        Assertions.assertThat(secondPageData)
                .as("Second page should have data")
                .isNotEmpty();

        // Verify data is different from first page
        if (firstPageData != null && !firstPageData.isEmpty()) {
            Assertions.assertThat(secondPageData)
                    .as("Second page data should be different from first page")
                    .isNotEqualTo(firstPageData);
        }
    }

    @When("I click the Previous button")
    public void i_click_the_previous_button() {
        categoriesPage.clickPreviousPage();
    }

    @Then("I should be navigated back to page {string}")
    public void i_should_be_navigated_back_to_page(String expectedPage) {
        String activePage = categoriesPage.getActivePageNumber();
        Assertions.assertThat(activePage)
                .as("Should be navigated back to page " + expectedPage)
                .isEqualTo(expectedPage);
    }

    @Then("the original category data should be displayed")
    public void the_original_category_data_should_be_displayed() {
        if (Serenity.hasASessionVariableCalled("firstPageData")) {
            List<String> storedFirstData = Serenity.sessionVariableCalled("firstPageData");
            List<String> currentData = categoriesPage.getCurrentPageData();

            Assertions.assertThat(currentData)
                    .as("Should display original category data")
                    .isEqualTo(storedFirstData);
        }
    }
}