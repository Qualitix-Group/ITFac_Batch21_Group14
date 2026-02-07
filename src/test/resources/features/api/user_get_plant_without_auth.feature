@user @negative @TC_API_USER_PLANT_007
Feature: User Plant API - Unauthorized Access
  As an unauthenticated user
  I should not be able to retrieve plant details
  So that plant data is protected

  Background:
    Given API server is running
    And a valid plant ID exists
    And user is not authenticated

  @TC_API_USER_PLANT_007
  Scenario: Verify user receives 401 Unauthorized when retrieving plant without authentication
    When user sends GET request to "/api/plants/{id}" without authorization header
    Then the response status code should be 401 Unauthorized
    And authentication error message should be returned
    And no plant details should be returned
