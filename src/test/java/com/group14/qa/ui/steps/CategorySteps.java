package com.group14.qa.ui.steps;

import com.group14.qa.ui.pages.CategoryPage;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.annotations.Steps;

import static org.assertj.core.api.Assertions.assertThat;

public class CategorySteps {

    @Steps
    CategoryPage categoryPage;

    @When("I open the Categories page")
    public void i_open_the_categories_page() {
        categoryPage.openCategoriesPage();
    }

    @Then("I should see the category list page")
    public void i_should_see_the_category_list_page() {
        assertThat(categoryPage.areCategoriesDisplayed())
                .as("Category list should be visible")
                .isTrue();
    }

    @Then("categories should be displayed with pagination")
    public void categories_should_be_displayed_with_pagination() {
        assertThat(categoryPage.isPaginationDisplayed())
                .as("Pagination should be visible")
                .isTrue();
    }
}
