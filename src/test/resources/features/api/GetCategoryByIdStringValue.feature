# src/test/resources/features/api/GetCategoryByIdStringValue.feature

Feature: Get Category by ID - String ID (Admin)
  @Smoke @API @Admin
  Scenario:  Category ID as string
    Given a valid admin bearer token is available for string category ID
    When I send a GET request with string category id "abc"
    Then the response status code should be 400 for string category ID
    And the response should display an error message for string ID
