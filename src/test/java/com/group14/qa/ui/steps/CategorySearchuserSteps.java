package com.group14.qa.ui.steps;

import com.group14.qa.ui.pages.CategorySearchuserPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import net.serenitybdd.annotations.Steps;

import static org.assertj.core.api.Assertions.assertThat;

public class CategorySearchuserSteps {

    @Steps
    CategorySearchuserPage searchPage;

    private String searchedName;

    @And("I search category by a valid existing name")
    public void i_search_category_by_valid_name() {
        // Pick first category dynamically
        searchedName = searchPage.getFirstCategoryName();
        assertThat(searchedName)
                .as("There should be at least one category in the table")
                .isNotNull();

        searchPage.searchCategory(searchedName);
    }

    @Then("the searched category should be displayed")
    public void searched_category_should_be_displayed() {
        assertThat(searchPage.isSearchedCategoryDisplayed())
                .as("[Searched category should be visible in results]")
                .isTrue();
    }
}
