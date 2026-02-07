Feature: User View Plants
  As a regular user
  I want to view plants
  So that I can browse the plant inventory without admin privileges

  Background:
    Given the QA Training App is running
    And I am on the login page
    When I login as regular user
    Then I should be redirected to the dashboard

  @Smoke @User @Plants @View
  Scenario: Verify User can view all plants
  Description: Ensure user can access and view plant list
    Given I am on the plants page as a user
    Then I should see the plants page as user
    And I should see at least 1 plant as user
    And print debug information for troubleshooting as user

  @Smoke @User @Plants @Security
  Scenario: Verify Hidden Delete button for the user in Plant List
  Description: Ensure Delete action is hidden from User role
    Given I am on the plants page as a user
    And at least one plant exists for user view
    When I scroll through the plant list as user
    And I check the action column for Delete buttons as user
    Then No Delete button should be displayed for any plant in the list
    And Delete button should not be visible for any plant row
    And print debug information for troubleshooting as user

  @Smoke @User @Plants @Search
  Scenario: Verify User can search for plants
  Description: Ensure user can search plants by name
    Given I am on the plants page as a user
    And at least one plant exists for user view
    When I search for a plant by name as user
    Then the searched plant should be displayed for user
    And print debug information for troubleshooting as user

  @Smoke @User @Plants @Search @Spaces
  Scenario: Verify User can search for plants with spaces in name
  Description: Ensure user can search plants when name contains spaces
    Given I am on the plants page as a user
    And at least one plant exists for user view
    When I search for a plant by name with spaces as user
    Then the plant with spaces in name should be displayed for user
    And print debug information for troubleshooting as user

  @Smoke @User @Plants @Search @Partial
  Scenario: Verify User can search for plants by partial name
  Description: Ensure user can search plants by partial name and see matching results
    Given I am on the plants page as a user
    And at least one plant exists for user view
    When I search for a plant by partial name as user
    Then plants matching the partial name should be displayed for user
    And print debug information for troubleshooting as user

  @Smoke @User @Plants @Search @NonExistent
  Scenario: Verify User sees "No plants found" when searching by non-existent name
  Description: Ensure proper message appears when no plants match search
    Given I am on the plants page as a user
    When I search for a plant by non-existent name as user
    Then I should see no plants found message for user
    And print debug information for troubleshooting as user

