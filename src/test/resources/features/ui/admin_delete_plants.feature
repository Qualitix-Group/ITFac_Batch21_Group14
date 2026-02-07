Feature: Admin Delete Plants
  As an admin
  I want to delete plants
  So that I can manage the plant inventory

  Background:
    Given the QA Training App is running
    And I am on the login page
    When I login as admin
    Then I should be redirected to the dashboard

  @Smoke @Admin @Plants
  Scenario: Verify Delete button is visible for Admin for all plants
    Given I am on the plants page
    And at least one plant exists
    When I scroll through the plant list
    And I check the action column for Delete buttons
    Then A Delete button should be displayed for each plant in the list
    And the number of delete buttons should match the number of plants
    And add plant button should be visible
    And print debug information for troubleshooting

  @Smoke @Admin @Plants @Delete
  Scenario: Verify Admin can delete a plant successfully
    Given I am on the plants page
    And at least one plant exists
    And I have recorded the initial plant count
    When I click the Delete button for a plant
    And I confirm deletion in the confirmation dialog
    Then the plant should be removed from the list
    And the plant count should decrease by 1
    And I should see a success message
    And the success message should contain "Plant deleted successfully"
    And print debug information for troubleshooting

  @Smoke @Admin @Plants @Delete
  Scenario: Verify Admin can cancel plant deletion
    Given I am on the plants page
    And at least one plant exists
    And I have recorded the initial plant count
    When I click the Delete button for a plant
    And I cancel deletion in the confirmation dialog
    Then the plant count should remain the same

  @Smoke @Admin @Plants @Sales @Security
  Scenario: Verify Admin cannot delete a plant while it is being used in an active sale
  Description: Prevent deletion of plants that are part of an ongoing transaction
    Given a plant is referenced in an active sale
    And I am on the plants page
    And I search for the plant in active sale
    And I have recorded the initial plant count
    When I try to delete the plant in active sale
    And I confirm deletion in the confirmation dialog
    Then an error message should be displayed
    And the plant should remain in the list
    And the plant count should not decrease
    And print debug information for troubleshooting