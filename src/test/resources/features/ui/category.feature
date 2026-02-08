Feature: Category Management (Admin)

  Background:
    Given the QA Training App is running
    And I am on the login page

  @Smoke @Admin
  Scenario: Admin user can access Category List page
    When I login as admin
    And I open the Categories page
    Then I should see the category list page


  @Smoke @User
  Scenario: Regular user can access Category List page
    When I login as regular user
    And I open the Categories page
    Then I should see the category list page


  @Authorization
  Scenario: Unauthorized user cannot access Categories page
    When I login with invalid credentials
    And I try to open the Categories page
    Then I should be redirected to the login page
    And I should see an authorization error message
