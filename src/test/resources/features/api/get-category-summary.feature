
Feature: Get Category Summary (Admin)


  @Smoke @API @Admin
  Scenario: Verify category summary API response
    Given a valid admin bearer token is available for category summary
    When I send a GET request to category summary API
    Then the response status code for category summary should be 200
    And the response should contain category summary details
