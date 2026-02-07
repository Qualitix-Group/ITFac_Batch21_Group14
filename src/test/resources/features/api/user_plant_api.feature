@user @api @plant
Feature: User Plant API Operations

  Background:
    Given user is authenticated

  @user @plant @positive @get
  Scenario: Verify user can retrieve plant details by valid plant ID
    Given user has a valid plant ID
    When user sends GET request to retrieve plant by ID
    Then the user response status code should be 200
    And the user response should contain plant details
    And the plant details should match the requested ID
    And the user response content type should be application/json

  @user @plant @negative @get
  Scenario: Verify user receives error when retrieving non-existent plant ID
    Given user has a non-existent plant ID 99999
    When user sends GET request to retrieve plant by ID
    Then the user response status code should be 404
    And the user error response should indicate plant not found

  @user @plant @positive @get-all
  Scenario: Verify user can retrieve all plants
    When user sends GET request to retrieve all plants
    Then the user response status code should be 200
    And the user response should contain list of all plants
    And each plant in response should have valid details
    And the user response content type should be application/json
    And no error should occur for user

  @user @plant @negative @delete @security
  Scenario: Verify user receives a 403 Forbidden when attempting to delete a plant
    Given user has a valid plant ID for delete test
    When user sends DELETE request to delete plant by ID
    Then the user response status code should be 403
    And the user error response should indicate forbidden access

  @user @plant @negative @delete
  Scenario: Verify user receives 403 Forbidden even for non-existent plant ID
    Given user has a non-existent plant ID 99999
    When user sends DELETE request to delete plant by ID
    Then the user response status code should be 403
    And the user error response should indicate forbidden access