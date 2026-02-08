Feature: Category Filter by Parent (User)

  Background:
    Given the QA Training App is running
    And I am on the login page

  @Smoke @User
  Scenario: Verify Filter by parent category
    When I login as regular user
    And I open the Categories page
    Then I should see the category list page
    When I select parent category "abc"
    And I click on Search
    Then only subcategories of "abc" should be displayed
