@Smoke @API @User
Feature: Retrieve All Categories-Verify user can retrieve all categories (User)




  Scenario: Verify all categories API response
    Given a valid JWT token is available for all categories
    When I send a GET request to retrieve all categories
    Then the response status code for all categories should be 200
    And the response should contain a list of categories
