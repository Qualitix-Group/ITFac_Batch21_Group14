package com.group14.qa.ui.steps;

import com.group14.qa.ui.pages.UserPlantsPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.annotations.Managed;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;

public class UserPlantsSteps {

    @Managed
    WebDriver driver;

    @Steps
    UserPlantsPage userPlantsPage;

    private int lowQuantityPlantRow = -1;
    private String searchedPlantName = "";
    private String partialSearchName = "";
    private String plantNameWithSpaces = "";

    @Given("I am on the plants page as a user")
    public void i_am_on_the_plants_page_as_a_user() {
        userPlantsPage.navigateToPlantsPage();
        assertThat(userPlantsPage.isPlantsPageDisplayed())
                .as("Should be on plants page as user")
                .isTrue();
    }

    @Given("I navigate to plants by category {string} as user")
    public void i_navigate_to_plants_by_category_as_user(String categoryId) {
        userPlantsPage.navigateToPlantsByCategory(categoryId);
        userPlantsPage.waitFor(1000);
    }

    @Given("at least one plant exists for user view")
    public void at_least_one_plant_exists_for_user_view() {
        int plantCount = userPlantsPage.getPlantCount();
        assertThat(plantCount)
                .as("Should have at least 1 plant for user view, but found " + plantCount)
                .isGreaterThanOrEqualTo(1);
    }


    @Then("No Delete button should be displayed for any plant in the list")
    public void no_delete_button_should_be_displayed_for_any_plant_in_the_list() {
        boolean anyDeleteButtonVisible = userPlantsPage.isAnyDeleteButtonVisible();
        int deleteButtonCount = userPlantsPage.getDeleteButtonCount();

        assertThat(anyDeleteButtonVisible)
                .as("User should not see any delete button. Found " + deleteButtonCount + " delete buttons")
                .isFalse();

        assertThat(deleteButtonCount)
                .as("Delete button count should be 0 for user")
                .isEqualTo(0);
    }

    @Then("Delete button should not be visible for any plant row")
    public void delete_button_should_not_be_visible_for_any_plant_row() {
        boolean deleteButtonVisibleForAnyPlant = userPlantsPage.isDeleteButtonVisibleForAnyPlant();

        System.out.println("Delete button visibility check per row: " + deleteButtonVisibleForAnyPlant);

        assertThat(deleteButtonVisibleForAnyPlant)
                .as("User should not see delete button in any plant row")
                .isFalse();
    }

    @When("I search for a plant by name as user")
    public void i_search_for_a_plant_by_name_as_user() {
        searchedPlantName = userPlantsPage.getFirstPlantName();
        assertThat(searchedPlantName)
                .as("Should get a plant name to search for")
                .isNotEmpty();

        System.out.println("Searching for plant by full name: " + searchedPlantName);
        userPlantsPage.searchForPlant(searchedPlantName);
        userPlantsPage.waitFor(2000);
    }

    @When("I search for a plant by name with spaces as user")
    public void i_search_for_a_plant_by_name_with_spaces_as_user() {

        plantNameWithSpaces = userPlantsPage.getFirstPlantNameWithSpaces();
        assertThat(plantNameWithSpaces)
                .as("Should find a plant name with spaces to search for")
                .isNotEmpty();

        assertThat(plantNameWithSpaces.contains(" "))
                .as("Plant name should contain spaces for this test")
                .isTrue();

        System.out.println("Searching for plant with spaces: '" + plantNameWithSpaces + "'");
        userPlantsPage.searchForPlant(plantNameWithSpaces);
        userPlantsPage.waitFor(2000);
    }


    @When("I search for a plant by partial name as user")
    public void i_search_for_a_plant_by_partial_name_as_user() {
        String fullName = userPlantsPage.getFirstPlantName();
        assertThat(fullName)
                .as("Should get a plant name to create partial search")
                .isNotEmpty();

        if (fullName.length() >= 3) {
            partialSearchName = fullName.substring(0, 3);
        } else if (fullName.length() > 0) {
            partialSearchName = fullName.substring(0, 1);
        }

        assertThat(partialSearchName)
                .as("Should have a partial name to search")
                .isNotEmpty();

        System.out.println("Full plant name: " + fullName);
        System.out.println("Searching by partial name: " + partialSearchName);

        userPlantsPage.searchByPartialName(partialSearchName);
        userPlantsPage.waitFor(2000);
    }

    @When("I search for a plant by non-existent name as user")
    public void i_search_for_a_plant_by_non_existent_name_as_user() {
        System.out.println("Searching by non-existent plant name");
        userPlantsPage.searchByNonExistentName();
        userPlantsPage.waitFor(2000);
    }

    @Then("the searched plant should be displayed for user")
    public void the_searched_plant_should_be_displayed_for_user() {
        boolean plantFound = userPlantsPage.isPlantInPlantsTable(searchedPlantName);
        System.out.println("Plant '" + searchedPlantName + "' found in table: " + plantFound);

        assertThat(plantFound)
                .as("Searched plant '" + searchedPlantName + "' should be displayed for user")
                .isTrue();
    }

    @Then("the plant with spaces in name should be displayed for user")
    public void the_plant_with_spaces_in_name_should_be_displayed_for_user() {
        assertThat(plantNameWithSpaces)
                .as("Should have a plant name with spaces to verify")
                .isNotEmpty();

        userPlantsPage.printDebugInfo();

        boolean plantFound = userPlantsPage.isPlantExactMatchInTable(plantNameWithSpaces);
        System.out.println("Plant with spaces '" + plantNameWithSpaces + "' found in table (exact match): " + plantFound);

        boolean plantFoundByContains = userPlantsPage.isPlantInPlantsTable(plantNameWithSpaces);
        System.out.println("Plant with spaces '" + plantNameWithSpaces + "' found in table (contains): " + plantFoundByContains);

        assertThat(plantFound || plantFoundByContains)
                .as("Plant with spaces '" + plantNameWithSpaces + "' should be displayed for user. " +
                        "Exact match: " + plantFound + ", Contains: " + plantFoundByContains)
                .isTrue();
    }


    @Then("plants matching the partial name should be displayed for user")
    public void plants_matching_the_partial_name_should_be_displayed_for_user() {
        assertThat(partialSearchName)
                .as("Should have a partial name to verify")
                .isNotEmpty();

        boolean anyPlantContainsPartial = userPlantsPage.isAnyPlantContainsPartialName(partialSearchName);
        int matchingCount = userPlantsPage.getCountOfPlantsMatchingPartialName(partialSearchName);

        assertThat(anyPlantContainsPartial)
                .as("At least one plant should contain the partial name '" + partialSearchName + "'")
                .isTrue();

        assertThat(matchingCount)
                .as("Should find at least 1 plant matching partial name '" + partialSearchName + "'")
                .isGreaterThan(0);
    }


    @Then("I should see no plants found message for user")
    public void i_should_see_no_plants_found_message_for_user() {
        userPlantsPage.waitFor(2000);

        boolean noPlantsMessageDisplayed = userPlantsPage.isNoPlantsMessageDisplayed();
        int plantCountAfterSearch = userPlantsPage.getPlantCount();
        int actualPlantCount = userPlantsPage.getActualPlantCount();

        userPlantsPage.printDebugInfo();

        assertThat(noPlantsMessageDisplayed)
                .as("No plants found message should be displayed for non-existent search")
                .isTrue();

        assertThat(plantCountAfterSearch)
                .as("Plant count should be 0 for non-existent search. Expected: 0, Actual: " + plantCountAfterSearch)
                .isEqualTo(0);
    }


    @When("I scroll through the plant list as user")
    public void i_scroll_through_the_plant_list_as_user() {
        userPlantsPage.scrollThroughPlantList();
    }

    @When("I check the action column for Delete buttons as user")
    public void i_check_the_action_column_for_delete_buttons_as_user() {
        userPlantsPage.waitFor(500);
    }

    @Then("I should see the plants page as user")
    public void i_should_see_the_plants_page_as_user() {
        assertThat(userPlantsPage.isPlantsPageDisplayed())
                .as("Plants page should be displayed for user")
                .isTrue();
    }

    @Then("I should see at least {int} plant as user")
    public void i_should_see_at_least_plant_as_user(int minCount) {
        int actualCount = userPlantsPage.getPlantCount();
        assertThat(actualCount)
                .as("Should see at least " + minCount + " plants as user, but found: " + actualCount)
                .isGreaterThanOrEqualTo(minCount);
    }

    @Then("I should see {int} plants as user")
    public void i_should_see_plants_as_user(int expectedCount) {
        int actualCount = userPlantsPage.getPlantCount();
        assertThat(actualCount)
                .as("Should see " + expectedCount + " plants as user, but found: " + actualCount)
                .isEqualTo(expectedCount);
    }

    @Then("I should see no plants message as user")
    public void i_should_see_no_plants_message_as_user() {
        assertThat(userPlantsPage.isNoPlantsMessageDisplayed())
                .as("No plants message should be displayed for user")
                .isTrue();
    }

    @And("print debug information for troubleshooting as user")
    public void print_debug_information_for_troubleshooting_as_user() {
        userPlantsPage.printDebugInfo();
    }
}