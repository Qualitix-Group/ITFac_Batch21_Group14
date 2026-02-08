Feature: Category Management - Delete Category (Admin)

  Background:
    Given the QA Training App is running
    And I am on the login page

  @Smoke @Admin
  Scenario: Admin can delete an existing category
    When I login as admin
    And I open the Categories page for deletion
    And I click the delete button for a category
    And I confirm the deletion
    Then the category should be removed from the list
    And a success message should be displayed
