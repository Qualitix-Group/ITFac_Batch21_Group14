Feature: Admin View Plants
  As an admin
  I want to view plants
  So that I can manage the plant inventory

  Background:
    Given the QA Training App is running
    And I am on the login page
    When I login as admin
    Then I should be redirected to the dashboard

  @Smoke @Admin @Plants
  Scenario: Verify Admin can view all plants
    Given I am on the plants page
    Then I should see the plants page
    And I should see at least 1 plant

  @Smoke @Admin @Plants @Inventory
  Scenario: Verify Low badge is displayed when plant quantity is below 5
  Description: Ensure low stock indicator appears for plants with quantity < 5
    Given I am on the plants page
    And at least one plant exists
    And at least one plant has quantity less than 5
    Then Low badge should be displayed for plants with quantity less than 5
    And the low quantity plant should have Low badge
    And Low badge logic should be correct for all plants
    And print debug information for troubleshooting