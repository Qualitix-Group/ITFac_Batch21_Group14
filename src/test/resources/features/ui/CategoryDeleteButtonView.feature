Feature: Verify Delete button visibility for Admin (Admin)

  Background:
    Given the QA Training App is running
    And I am on the login page

  @Smoke @Admin
  Scenario: Verify Delete buttons are visible and clickable for Admin
    When I login as admin
    When Admin navigates to the Category list page
    Then all Delete buttons should be visible and clickable








