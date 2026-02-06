package com.group14.qa.ui.steps;

import com.group14.qa.ui.pages.CategoryFilterParentUserPage;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.annotations.Steps;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryFilterParentUserSteps {

    @Steps
    CategoryFilterParentUserPage categoryPage;

    @When("I select parent category {string}")
    public void i_select_parent_category(String parentName) {
        categoryPage.selectParentCategory(parentName);
    }

    @When("I click on Search")
    public void i_click_on_search() {
        categoryPage.clickSearch();
    }

    @Then("only subcategories of {string} should be displayed")
    public void only_subcategories_of_should_be_displayed(String parentName) {
        assertThat(categoryPage.areAllRowsFilteredByParent(parentName))
                .as("All displayed categories should belong to parent: " + parentName)
                .isTrue();
    }
}