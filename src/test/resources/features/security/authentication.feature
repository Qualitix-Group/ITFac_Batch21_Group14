@security @authentication @ui
Feature: Authentication and Session Management
  As a user
  I want secure authentication
  So that my account is protected

  @UI-SEC-01
  Scenario: Unauthenticated user redirected to login
    When I access the categories page without authentication
    Then I should be redirected to the login page

  @UI-SEC-07
  Scenario: Validation message visibility on category search
    Given I am logged in as user
    When I trigger validation on the category search
    Then I should see a category search validation message

  @UI-SEC-10
  Scenario: Session expiry redirects to login
    Given I am logged in as user
    When my session expires
    And I try to access the categories page
    Then I should be redirected to the login page

  @UI-SEC-ADMIN-02
  Scenario: Admin validation message displayed on category form
    Given I am logged in as admin
    When I navigate to the add category form
    Then I should see validation message for empty name
