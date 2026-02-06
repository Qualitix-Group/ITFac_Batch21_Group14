Feature: Category Management - Verify Deleted Category Not Displayed (Admin)

  Background:
    Given the QA Training App is running
    And I am on the login page

  @Regression @Smoke @Admin @DeleteCategory
  Scenario: Verify deleted category is not displayed after deletion
    When I login as admin
    And I open the Categories page for deletion
    And I click the delete button for a category
    And I confirm the deletion
    Then the category should be removed from the list
    And a success message should be displayed
    When I search for the deleted category in the search bar
    Then the deleted category should not be displayed in search results
