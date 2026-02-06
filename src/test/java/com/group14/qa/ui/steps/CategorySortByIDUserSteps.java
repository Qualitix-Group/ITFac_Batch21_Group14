package com.group14.qa.ui.steps;

import com.group14.qa.ui.pages.CategorySortByIDUserPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.core.pages.PageObject;

import static org.assertj.core.api.Assertions.assertThat;

public class CategorySortByIDUserSteps {

    CategorySortByIDUserPage categoryPage;

    @When("I click on the ID column header")
    public void i_click_on_the_id_column_header() {
        categoryPage.clickIDColumnHeader();
    }

    @Then("categories should be sorted ascending by ID")
    public void categories_should_be_sorted_ascending_by_id() {
        assertThat(categoryPage.isSortedAscendingByID()).isTrue();
    }

    @When("I click on the ID column header again")
    public void i_click_on_the_id_column_header_again() {
        categoryPage.clickIDColumnHeader();
    }

    @Then("categories should be sorted descending by ID")
    public void categories_should_be_sorted_descending_by_id() {
        assertThat(categoryPage.isSortedDescendingByID()).isTrue();
    }
}
