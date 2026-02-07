


Feature: Get Category by ID - Null ID
  Verify that the Get Category by ID API returns an error message when ID is empty
  @Smoke @API @Admin
  Scenario:Category ID is empty
    Given a valid admin bearer token is available for null category ID
    When I send a GET request with null category id
    Then the response status code should be 400 for null category ID
    And the response should contain validation error message for missing ID
