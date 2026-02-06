@user @positive @TC_API_USER_PLANT_012
Feature: Plant API - Get Plant Summary
  As an authorized user
  I want to retrieve plant summary details
  So that I can view inventory statistics

  Background:
    Given application server is running
    And user has a valid authentication token

  @TC_API_USER_PLANT_012
  Scenario: Verify that get plant summary returns valid data
    When user sends GET request to "/api/plants/summary"
    Then the response status code should be 200 OK
    And response body should contain totalPlants and lowStockPlants
    And both values should be non-negative integers
