package com.group14.qa.ui.steps;

import com.group14.qa.ui.pages.VerifyDeletedCategoryNotDisplayedPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;

import static org.assertj.core.api.Assertions.assertThat;

public class VerifyDeletedCategoryNotDisplayedSteps {

    @Steps
    VerifyDeletedCategoryNotDisplayedPage verifyDeletedPage;

    // SAME deleted category used in delete test
    private final String deletedCategoryName = "Test Category";

    @When("I search for the deleted category in the search bar")
    public void i_search_for_the_deleted_category_in_search_bar() {
        verifyDeletedPage.searchDeletedCategory(deletedCategoryName);
    }

    @Then("the deleted category should not be displayed in search results")
    public void the_deleted_category_should_not_be_displayed_in_search_results() {

        assertThat(verifyDeletedPage.isNoCategoryFoundDisplayed())
                .as("Expected 'No category found' message for deleted category")
                .isTrue();
    }
}
