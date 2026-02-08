package com.group14.qa.ui.steps;

import com.group14.qa.ui.pages.CategorySortByNameUserPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class CategorySortByNameUserSteps {

    CategorySortByNameUserPage categoryPage;

    @When("I click on the Name column header")
    public void i_click_on_the_name_column_header() {
        categoryPage.clickNameColumnHeader();
    }

    @Then("categories should be sorted ascending by Name")
    public void categories_should_be_sorted_ascending_by_name() {
        assertThat(categoryPage.isSortedAscendingByName()).isTrue();
    }

    @Then("categories should be sorted descending by Name")
    public void categories_should_be_sorted_descending_by_name() {
        assertThat(categoryPage.isSortedDescendingByName()).isTrue();
    }

    @When("I click on the Name column header again")
    public void i_click_on_the_name_column_header_again() {
        categoryPage.clickNameColumnHeader();
    }
}
