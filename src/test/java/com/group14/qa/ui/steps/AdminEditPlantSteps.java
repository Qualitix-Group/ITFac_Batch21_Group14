package com.group14.qa.ui.steps;

import com.group14.qa.ui.pages.LoginPage;
import com.group14.qa.ui.pages.PlantsAddEditPage;
import com.group14.qa.testdata.TestUsers;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.annotations.Steps;

import static org.assertj.core.api.Assertions.assertThat;

public class AdminEditPlantSteps {

    @Steps
    LoginPage loginPage;

    @Steps
    PlantsAddEditPage plantsPage;

    private String originalPlantName = "Rosemerry";   // from your table HTML
    private String updatedPlantName = "Cactus";

    @Given("I am logged in as admin and on plant list page")
    public void i_am_logged_in_as_admin_and_on_plant_list_page() {
        loginPage.openLoginPage();
        loginPage.login(TestUsers.Admin.USERNAME, TestUsers.Admin.PASSWORD);
        loginPage.waitFor(2000);
        plantsPage.openPlantsTab();
    }

    @When("I update a plant with valid data")
    public void i_update_a_plant_with_valid_data() {
        plantsPage.clickEditForPlant(originalPlantName);

        plantsPage.updatePlantName(updatedPlantName);
        plantsPage.updatePrice("250.00");
        plantsPage.updateQuantity("30");

        plantsPage.saveUpdatedPlant();
    }

    @Then("the plant should be updated successfully")
    public void the_plant_should_be_updated_successfully() {

        plantsPage.waitForCondition().until(driver ->
                plantsPage.isPlantDisplayedInList(updatedPlantName)
        );

        assertThat(plantsPage.isPlantDisplayedInList(updatedPlantName))
                .as("Updated plant should appear in list")
                .isTrue();
    }
}
