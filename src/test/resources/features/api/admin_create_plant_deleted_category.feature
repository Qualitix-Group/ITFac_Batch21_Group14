@admin @negative @TC_API_ADMIN_PLANT_011
Feature: Admin Plant API - Deleted Category Tests
  As an admin user
  I want to verify that plants cannot be created under deleted categories
  So that the API maintains data integrity

  Background:
    Given the API server is running
    And admin user account exists
    And I have a valid admin authentication token

  @TC_API_ADMIN_PLANT_011
  Scenario: Verify error when creating plant under deleted category
    Given category with ID 99998 has been deleted
    When I send POST request to "/api/plants/category/99998" with:
      | name   | TestPlant_001 |
      | price  | 150.0         |
      | quantity | 25          |
    Then the response status code should be 400 Bad Request
    And the error message should be displayed
    And no new plant record should be created
    And the response should contain error details:
      """
      {
        "status": 0,
        "error": "string",
        "message": "string",
        "timestamp": "2026-01-18T18:52:35.001Z"
      }
      """

  @TC_API_ADMIN_PLANT_011_Variations
  Scenario Outline: Verify error when creating plant under various non-existent categories
    Given category with ID <categoryId> does not exist
    When I send POST request to "/api/plants/category/<categoryId>" with valid plant data
    Then the response should be an error (4xx status code)
    And no new plant should be created

    Examples:
      | categoryId | description              |
      | 99998      | Recently deleted category|
      | 99999      | Never existed category   |
      | 99997      | Deleted long ago         |

  @TC_API_ADMIN_PLANT_011_EdgeCases
  Scenario: Verify consistent error response for deleted categories
    Given I have multiple deleted category IDs
    When I attempt to create plants under each deleted category
    Then all attempts should fail with appropriate error codes
    And no plants should be created in any category