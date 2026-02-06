Feature: Admin Plant API - Invalid Category Tests
  As an admin user
  I want to verify error handling when creating plants with invalid category IDs
  So that the API properly validates category IDs

  Background:
    Given the API server is running
    And admin user account exists
    And I have a valid admin authentication token

  @TC_API_ADMIN_PLANT_010_ZeroID
  Scenario: Verify error when category ID is zero
    When I send POST request to "/api/plants/category/0" with valid plant data
    Then the response status code should be 400 Bad Request
    And no new plant record should be created

  @TC_API_ADMIN_PLANT_010_NegativeID
  Scenario: Verify error when category ID is negative
    When I send POST request to "/api/plants/category/-1" with valid plant data
    Then the response status code should be 400 Bad Request
    And no new plant record should be created

  @TC_API_ADMIN_PLANT_010_SpecialChars
  Scenario Outline: Verify error when category ID contains special characters
    When I send POST request to "/api/plants/category/<invalidId>" with valid plant data
    Then the response status code should be 400 Bad Request

    Examples:
      | invalidId | description          |
      | "abc"     | Alphabetic characters|
      | "123abc"  | Alphanumeric mix     |
      | "@#$%"    | Special characters   |
      | " "       | Empty space          |
      | ""        | Empty string         |