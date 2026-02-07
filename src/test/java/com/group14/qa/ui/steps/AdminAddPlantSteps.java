package com.group14.qa.ui.steps;

import com.group14.qa.ui.pages.LoginPage;
import com.group14.qa.ui.pages.PlantsAddEditPage;
import com.group14.qa.testdata.TestUsers;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.annotations.Steps;

import static org.assertj.core.api.Assertions.assertThat;

public class AdminAddPlantSteps {

    @Steps
    LoginPage loginPage;

    @Steps
    PlantsAddEditPage plantsPage;

    // Dynamic names prevent duplicate failures
    private String plantName = "Orange Plant";
    private String longPlantName = "VeryLongPlantNameExceedingLimit123";

    @Given("I am logged in as admin and on Plants page")
    public void i_am_logged_in_as_admin_and_on_plants_page() {
        loginPage.openLoginPage();
        loginPage.login(TestUsers.Admin.USERNAME, TestUsers.Admin.PASSWORD);
        loginPage.waitFor(2000);
        plantsPage.openPlantsTab();
    }

    // ================= TC014 =================
    @When("I add a plant with valid data")
    public void i_add_a_plant_with_valid_data() {
        plantsPage.clickAddPlant();
        plantsPage.enterPlantName(plantName);
        plantsPage.selectCategoryByVisibleText("Red");
        plantsPage.enterPrice("150.00");
        plantsPage.enterQuantity("20");
        plantsPage.clickSave();
    }

    @Then("the plant should be added successfully")
    public void the_plant_should_be_added_successfully() {
        plantsPage.waitForCondition().until(driver ->
                plantsPage.isPlantDisplayedInList(plantName)
        );

        assertThat(plantsPage.isPlantDisplayedInList(plantName))
                .as("Plant should appear in list")
                .isTrue();
    }

    // ================= TC015 =================
    @When("I try to add a plant without a name")
    public void i_try_to_add_a_plant_without_a_name() {
        plantsPage.clickAddPlant();
        plantsPage.clearPlantName();
        plantsPage.selectCategoryByVisibleText("Red");
        plantsPage.enterPrice("150.00");
        plantsPage.enterQuantity("20");
        plantsPage.clickSave();
    }

    @Then("I should see plant name required validation message")
    public void i_should_see_plant_name_required_validation_message() {
        assertThat(plantsPage.isNameValidationMessageDisplayed("required"))
                .as("Name required validation should appear")
                .isTrue();
    }

    // ================= TC017 =================
    @When("I try to add a plant with name longer than allowed")
    public void i_try_to_add_a_plant_with_name_longer_than_allowed() {
        plantsPage.clickAddPlant();
        plantsPage.enterPlantName(longPlantName);
        plantsPage.selectCategoryByVisibleText("Red");
        plantsPage.enterPrice("120.00");
        plantsPage.enterQuantity("10");
        plantsPage.clickSave();
    }

    @Then("I should see plant name length validation message")
    public void i_should_see_plant_name_length_validation_message() {
        assertThat(plantsPage.isNameValidationMessageDisplayed("between 3 and 25"))
                .as("Name length validation should appear")
                .isTrue();
    }

    // ================= TC018 =================
    @When("I try to add a plant without price")
    public void i_try_to_add_a_plant_without_price() {
        plantsPage.clickAddPlant();
        plantsPage.enterPlantName("MoneyPlant");
        plantsPage.selectCategoryByVisibleText("Red");
        plantsPage.clearPrice();   // <-- make sure this method exists in PlantsPage
        plantsPage.enterQuantity("15");
        plantsPage.clickSave();
    }

    @Then("I should see price required validation message")
    public void i_should_see_price_required_validation_message() {
        assertThat(plantsPage.isPriceValidationMessageDisplayed("required"))
                .as("Price required validation should appear")
                .isTrue();
    }
}
