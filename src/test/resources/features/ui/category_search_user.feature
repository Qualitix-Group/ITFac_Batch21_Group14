Feature: Category Search-valid category (User)

  Background:
    Given the QA Training App is running
    And I am on the login page

  @Smoke @User
  Scenario: Verify search category by valid name
    When I login as regular user
    And I open the Categories page
    Then I should see the category list page
    And I search category by a valid existing name
    Then the searched category should be displayed
