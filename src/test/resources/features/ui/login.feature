# src/test/resources/features/ui/login.feature
Feature: User Login

  Background:
    Given the QA Training App is running

  @Smoke @Admin
  Scenario: Admin user login with valid credentials
    Given I am on the login page
    When I login as admin
    Then I should be redirected to the dashboard

  @Smoke @User
  Scenario: Regular user login with valid credentials
    Given I am on the login page
    When I login as regular user
    Then I should be redirected to the dashboard

  @Negative
  Scenario: Login with invalid credentials
    Given I am on the login page
    When I login with username "invalid" and password "wrongpass"
    Then I should see error message "Invalid username or password"