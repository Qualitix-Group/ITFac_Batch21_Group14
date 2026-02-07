Feature: Get Category by ID - Invalid ID (Admin)




  @Smoke @API @Admin
  Scenario:Verify that the Get Category by ID API returns an error message for an invalid ID- Category ID does not exist
    Given a valid admin bearer token is available for invalid category ID
    When I send a GET request with invalid category id 9999
    Then the response status code should be 404 for invalid category
    And the response should contain category not found message
