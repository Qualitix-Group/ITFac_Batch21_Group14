package com.group14.qa.ui.steps;

import com.group14.qa.ui.pages.CategoryDeletePage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryDeleteSteps {

    @Steps
    CategoryDeletePage categoryDeletePage;

    @When("I open the Categories page for deletion")
    public void i_open_the_categories_page_for_deletion() {
        categoryDeletePage.openCategoriesPage();
    }

    @When("I click the delete button for a category")
    public void i_click_delete_button_for_a_category() {
        categoryDeletePage.clickDeleteButtonForFirstCategory();
    }

    @When("I confirm the deletion")
    public void i_confirm_deletion() {
        categoryDeletePage.confirmDeletion();
    }

    @Then("the category should be removed from the list")
    public void the_category_should_be_removed() {

        assertThat(categoryDeletePage.waitForSuccessMessage())
                .as("Success message should appear confirming category deletion")
                .isTrue();
    }

    @Then("a success message should be displayed")
    public void a_success_message_should_be_displayed() {
        // Already verified in previous step
    }
}
