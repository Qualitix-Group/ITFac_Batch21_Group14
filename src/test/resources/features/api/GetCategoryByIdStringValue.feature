@Smoke @API @Admin
Feature: Get Category by ID -valid

  Scenario: Verify Get Category by ID returns correct category details
    Given a valid admin bearer token is available
    When I send a GET request to get category by id 3
    Then the response status code should be 200
    And the response should contain valid category details
