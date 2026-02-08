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

  @user @plant @negative @security @unauthorized
  Scenario: Verify user can not retrieve data without authentication
    Given user has a valid plant ID
    When user sends GET request to retrieve plant by ID without authentication
    Then the user response status code should be 401
    And the error response should indicate authentication is required
    And no plant details should be returned

  @user @plant @negative @security @add
  Scenario: Verify User Cannot Add Plant
    Given user has a valid category ID
    And user prepares plant data for creation
    When user sends POST request to create plant
    Then the user response status code should be 403
    And the user error response should indicate forbidden access

  @user @plant @negative @security @edit
  Scenario: Verify User Cannot Edit Plant
    Given user has a valid plant ID
    When user sends PUT request to update the plant
    Then the user response status code should be 403
    And the user error response should indicate forbidden access

  @user @plant @positive @pagination
  Scenario: Verify that Retrieve Plants with Pagination and Sorting
    When user sends GET request to retrieve paginated plants with page 0 and size 5
    Then the user response status code should be 200
    And the response should contain paginated plant data
    And the pagination metadata should be valid
    And the number of returned records should be less than or equal to page size
    And the user response content type should be application/json

  @user @plant @positive @summary
  Scenario: Verify that Get Plant Summary
    When user sends GET request to retrieve plant summary
    Then the user response status code should be 200
    And the response should contain plant summary
    And the summary values should be non-negative integers
    And the summary should match database records
    And the user response content type should be application/json