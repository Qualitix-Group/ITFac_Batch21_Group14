
Feature: Verify Parent Category Mapping (User)



  @Smoke @API @User
  Scenario: Verify categories return correct parentName
    Given a valid JWT token is available for parent category mapping
    When I send a GET request to retrieve categories with parent mapping
    Then the response status code for parent category mapping should be 200
    And the parentName field should be correct for each category
