package com.group14.qa.ui.steps;

import com.group14.qa.ui.pages.PlantsPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.annotations.ManagedPages;

import static org.assertj.core.api.Assertions.assertThat;

public class UserPlantSteps {


    PlantsPage plantsPage;

    @When("I am on the Plants list page")
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

    @Then("I should see pagination controls")
    public void i_should_see_pagination_controls() {
        assertThat(plantsPage.isPaginationVisible())
                .as("Pagination controls should be visible")
                .isTrue();
    }

}
