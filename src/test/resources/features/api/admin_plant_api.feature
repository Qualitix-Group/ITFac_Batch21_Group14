@admin @api @plant
Feature: Admin Plant API Operations

  Background:
    Given admin is authenticated

  @admin @plant @positive
  Scenario: Verify admin can retrieve plants by valid category ID
    Given admin has a valid category ID 3
    When admin sends GET request to retrieve plants by category ID
    Then the response status code should be 200
    And the response should contain list of plants
    And each plant should belong to the requested category
    And the response content type should be application/json

  @admin @plant @negative
  Scenario: Verify admin receives an error response when category ID does not exist
    Given admin has a non-existent category ID 99999
    When admin sends GET request to retrieve plants by category ID
    Then the response status code should be 404
    And the error response should indicate category not found
    And the error response should have status 404