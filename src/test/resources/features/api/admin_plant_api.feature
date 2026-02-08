@admin @api @plant
Feature: Admin Plant API Operations

  Background:
    Given admin is authenticated

  @admin @plant @positive
  Scenario: Verify admin can retrieve plants by valid category ID
    Given admin has a valid category ID
    When admin sends GET request to retrieve plants by category ID
    Then the response status code should be 200
    And the response should contain list of plants
    And each plant should belong to the requested category
    And the response content type should be application/json

  @admin @plant @negative
  Scenario: Verify admin receives an error response when category ID does not exist
    Given admin has a non-existent category ID
    When admin sends GET request to retrieve plants by category ID
    Then the response status code should be 404
    And the error response should indicate category not found
    And the error response should have status 404

  @admin @plant @edge-case
  Scenario: Verify admin receives an appropriate response when selected category has no plants
    Given admin has a valid category ID with no plants
    When admin sends GET request to retrieve plants by category ID
    Then the response status code should be 200
    And the response should be an empty array
    And no error should occur
    And the response content type should be application/json

  @admin @plant @positive @delete
  Scenario: Verify admin can delete plant using valid plant ID
    Given a test plant exists for admin
    When admin sends DELETE request to delete plant by ID
    Then the response should have status 204
    And the response should have no content
    And the plant should be successfully deleted

  @admin @plant @negative @delete
  Scenario: Verify admin receives error when trying to delete non-existent plant
    Given admin has a non-existent plant ID
    When admin sends DELETE request to delete plant by ID
    Then the response should have status 404
    And the error response should indicate plant not found