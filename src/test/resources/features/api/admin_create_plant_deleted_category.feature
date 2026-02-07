@admin
Feature: Admin create plant under valid sub-category

  Scenario: Verify that Admin can successfully create a plant under a valid sub-category
    Given admin has a valid sub-category ID 5
    And admin has valid plant data
    When admin sends POST request to create plant under the sub-category
    Then the response status code should be 201
    And the response should contain created plant details
    And the plant category ID should match the requested sub-category
