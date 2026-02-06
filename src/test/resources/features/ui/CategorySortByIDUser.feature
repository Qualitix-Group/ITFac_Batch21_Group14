Feature: Category Sort by ID (User)

  Background:
    Given the QA Training App is running
    And I am on the login page

  @Smoke @User
  Scenario: Verify sorting categories by ID
    When I login as regular user
    And I open the Categories page
    Then I should see the category list page
    When I click on the ID column header
    Then categories should be sorted ascending by ID
    When I click on the ID column header again
    Then categories should be sorted descending by ID