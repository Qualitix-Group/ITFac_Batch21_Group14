Feature: Category Sort by Name (User)

  Background:
    Given the QA Training App is running
    And I am on the login page

  @Smoke @User
  Scenario: Verify sorting categories by Name
    When I login as regular user
    And I open the Categories page
    Then I should see the category list page
    When I click on the Name column header
    Then categories should be sorted descending by Name
    When I click on the Name column header again
    Then categories should be sorted ascending by Name
