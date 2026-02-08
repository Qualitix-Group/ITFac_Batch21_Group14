@security @authorization @ui
Feature: Authorization and Access Control
  As the system
  I want to enforce role-based access
  So that users can only access authorized resources

  @UI-SEC-06
  Scenario: User cannot access admin-only plant page
    Given I am logged in as user
    When I try to access the add plant page directly
    Then I should not remain on the admin-only page

  @UI-SEC-08
  Scenario: User blocked from admin category form
    Given I am logged in as user
    When I try to access the add category page directly
    Then I should not remain on the admin-only page
