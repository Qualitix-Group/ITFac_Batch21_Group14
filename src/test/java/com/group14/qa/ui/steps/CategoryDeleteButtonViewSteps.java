package com.group14.qa.ui.steps;

import com.group14.qa.ui.pages.CategoryDeleteButtonView;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import com.group14.qa.ui.pages.CategoryDeleteButtonView;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class CategoryDeleteButtonViewSteps {

    @Steps
    CategoryDeleteButtonView categoryPage;

    @Given("Admin is logged in")
    public void admin_is_logged_in() {
        // You can add login steps here if needed
        // For now, assume admin is already logged in
    }

    @When("Admin navigates to the Category list page")
    public void admin_navigates_to_category_list_page() {
        categoryPage.openCategoryPage();
    }

    @Then("all Delete buttons should be visible and clickable")
    public void all_delete_buttons_should_be_visible_and_clickable() {
        categoryPage.verifyAllDeleteButtonsVisibleAndClickable();
    }
}