package com.group14.qa.ui.steps;

import com.group14.qa.ui.pages.CategoriesPage;
import com.group14.qa.ui.pages.LoginPage;
import com.group14.qa.testdata.TestUsers;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import org.assertj.core.api.Assertions;
import java.util.List;

public class CategoriesEmptyStateSteps {

    @Steps
    private CategoriesPage categoriesPage;

    @Steps
    private LoginPage loginPage;

    @Given("the QA Training App is running")
    public void the_qa_training_app_is_running() {
        // Do nothing - app should be running
    }

    @Given("I am on the login page")
    public void i_am_on_the_login_page() {
        loginPage.openLoginPage();
    }

    @When("I login and access the Categories page")
    public void i_login_and_access_the_categories_page() {
        // Login
        loginPage.login(TestUsers.RegularUser.USERNAME, TestUsers.RegularUser.PASSWORD);
        categoriesPage.waitFor(2000);

        // Navigate to Categories
        categoriesPage.openCategoriesTab();
        categoriesPage.waitFor(2000);
    }

    @Then("the Category page should load successfully")
    public void the_category_page_should_load_successfully() {
        boolean isLoaded = categoriesPage.isCategoriesPageLoaded();
        Assertions.assertThat(isLoaded)
                .as("Categories page should load successfully")
                .isTrue();
    }

    @Then("the table should include {string} message")
    public void the_table_should_include_message(String expectedMessage) {
        categoriesPage.waitFor(1000);

        // Find all possible empty state elements
        List<WebElementFacade> emptyStateElements = categoriesPage.findAll(
                By.xpath("//td[contains(text(),'No')] | " +
                        "//td[contains(text(),'found')] | " +
                        "//td[contains(text(),'empty')] | " +
                        "//td[@colspan] | " +
                        "//div[contains(@class,'empty')] | " +
                        "//div[contains(@class,'no-data')]")
        );

        boolean foundMessage = false;
        for (WebElementFacade element : emptyStateElements) {
            if (element.isVisible()) {
                String actualText = element.getText();
                System.out.println("Found empty state text: " + actualText);

                if (actualText.toLowerCase().contains(expectedMessage.toLowerCase())) {
                    foundMessage = true;
                    break;
                }
            }
        }

        Assertions.assertThat(foundMessage)
                .as("Table should include message: " + expectedMessage)
                .isTrue();
    }

    @Then("no pagination should be displayed when table is empty")
    public void no_pagination_should_be_displayed_when_table_is_empty() {
        // First check if table appears empty
        List<WebElementFacade> tableRows = categoriesPage.findAll(By.cssSelector("table tbody tr"));
        boolean isTableEmpty = false;

        for (WebElementFacade row : tableRows) {
            String rowText = row.getText().toLowerCase();
            if (rowText.contains("no category") ||
                    rowText.contains("no data") ||
                    rowText.contains("no records")) {
                isTableEmpty = true;
                break;
            }
        }

        if (isTableEmpty) {
            // Check pagination elements
            List<WebElementFacade> paginationElements = categoriesPage.findAll(
                    By.cssSelector(".pagination, .page-item, .page-link")
            );

            boolean isPaginationVisible = false;
            for (WebElementFacade element : paginationElements) {
                if (element.isVisible()) {
                    isPaginationVisible = true;
                    break;
                }
            }

            Assertions.assertThat(isPaginationVisible)
                    .as("Pagination should not be displayed when table is empty")
                    .isFalse();
        } else {
            System.out.println("INFO: Table has data, skipping pagination check");
        }
    }

    @Then("the empty state should be properly formatted")
    public void the_empty_state_should_be_properly_formatted() {
        // Find empty state elements
        List<WebElementFacade> emptyStateElements = categoriesPage.findAll(
                By.xpath("//td[contains(text(),'No category')] | " +
                        "//td[contains(text(),'No data')] | " +
                        "//td[@colspan]")
        );

        boolean isEmptyStateVisible = false;
        for (WebElementFacade element : emptyStateElements) {
            if (element.isVisible()) {
                isEmptyStateVisible = true;

                // Basic formatting check - text should not be empty
                String text = element.getText();
                Assertions.assertThat(text)
                        .as("Empty state should have text")
                        .isNotEmpty();

                break;
            }
        }

        Assertions.assertThat(isEmptyStateVisible)
                .as("Empty state should be visible")
                .isTrue();
    }
}