package com.group14.qa.ui.steps;

import com.group14.qa.ui.pages.PlantsAddEditPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class UserPlantSteps {


    PlantsAddEditPage plantsPage;
    String firstPlantNamePage1;

    @When("I am on the Plants list page")
    @When("I navigate to the Plant List page")
    public void i_am_on_the_plants_list_page() {
        plantsPage.openPlantsTab();
    }

    @Then("I should see at least one plant with quantity less than 5")
    public void i_should_see_plant_with_low_quantity() {
        assertThat(plantsPage.isAnyPlantWithLowQuantityPresent())
                .as("There should be a plant with quantity < 5")
                .isTrue();
    }

    @Then("I should see Low badge displayed next to that quantity")
    public void i_should_see_low_badge() {
        assertThat(plantsPage.isLowBadgeDisplayedForLowStockPlant())
                .as("Low badge should be visible for low stock plant")
                .isTrue();
    }

    @Then("I should see no Low badge for plants with quantity 5 or more")
    public void i_should_not_see_low_badge_for_sufficient_stock() {
        assertThat(plantsPage.isLowBadgeAbsentForSufficientStockPlant())
                .as("Low badge should NOT appear for sufficient stock plants")
                .isTrue();
    }

    @Then("I should see no plants in the plant list")
    public void i_should_see_no_plants_in_the_plant_list() {
        assertThat(plantsPage.isPlantTableEmpty())
                .as("Plant list table should be empty")
                .isTrue();
    }

    @Then("I should see pagination controls")
    public void i_should_see_pagination_controls() {
        assertThat(plantsPage.isPaginationVisible())
                .as("Pagination controls should be visible")
                .isTrue();
    }

    @Then("I should see the empty plant list message")
    public void i_should_see_the_empty_plant_list_message() {

        assertThat(plantsPage.isPlantTableEmpty())
                .as("Plant list should contain no plant records")
                .isTrue();

        assertThat(plantsPage.isEmptyPlantListMessageDisplayed())
                .as("Empty plant list message should be visible")
                .isTrue();

        assertThat(plantsPage.getEmptyPlantListMessage())
                .as("Empty plant list message text mismatch")
                .isEqualTo("No plants found");
    }

    @When("I store the first plant name on the current page")
    public void store_first_plant_name() {
        firstPlantNamePage1 = plantsPage.getFirstPlantName();
        assertThat(firstPlantNamePage1).isNotBlank();
    }

    @When("I click the Next pagination button")
    public void click_next_pagination() {
        plantsPage.clickNextPage();
    }

    @When("I click the Previous pagination button")
    public void click_previous_pagination() {
        plantsPage.clickPreviousPage();
    }

    @Then("I should see different plant entries than the previous page")
    public void verify_different_page_data() {
        String firstPlantNamePage2 = plantsPage.getFirstPlantName();
        assertThat(firstPlantNamePage2)
                .as("Plant list did not change after clicking Next")
                .isNotEqualTo(firstPlantNamePage1);
    }

    @Then("I should see the original plant entries again")
    public void verify_back_to_original_page() {
        String firstPlantNameAgain = plantsPage.getFirstPlantName();
        assertThat(firstPlantNameAgain)
                .as("Did not return to original page after clicking Previous")
                .isEqualTo(firstPlantNamePage1);
    }

}
