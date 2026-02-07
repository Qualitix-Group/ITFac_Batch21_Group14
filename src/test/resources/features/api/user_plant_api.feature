@user @api @plant
Feature: User Plant API Operations

  Background:
    Given user is authenticated

  @user @plant @positive
  Scenario: Verify user can retrieve plants by valid category ID
    Given user has a valid category ID 3
    When user sends GET request to retrieve plants by category ID
    Then the user response status code should be 200
    And the user response should contain list of plants
    And each plant in user response should belong to the requested category
    And the user response content type should be application/json

  @user @plant @negative
  Scenario: Verify user receives an error response when category ID does not exist
    Given user has a non-existent category ID 99999
    When user sends GET request to retrieve plants by category ID
    Then the user response status code should be 404
    And the user error response should indicate category not found