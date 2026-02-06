package com.group14.qa.ui.steps;

import com.group14.qa.ui.pages.CategorySearchInvalidUserPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import net.serenitybdd.annotations.Steps;

import static org.assertj.core.api.Assertions.assertThat;

public class CategorySearchInvalidUserSteps {

    @Steps
    CategorySearchInvalidUserPage invalidCategoryPage;

    private String invalidCategoryName;

    @And("I search category by invalid name {string}")
    public void i_search_category_by_invalid_name(String categoryName) {
        // Store invalid category name for reference
        invalidCategoryName = categoryName;

        // Perform search
        invalidCategoryPage.searchInvalidCategory(invalidCategoryName);
    }

    @Then("I should see no category found message")
    public void i_should_see_no_category_found_message() {
        assertThat(invalidCategoryPage.isNoCategoryFoundMessageDisplayed())
                .as("[No category found message should be displayed for '" + invalidCategoryName + "']")
                .isTrue();
    }
}
