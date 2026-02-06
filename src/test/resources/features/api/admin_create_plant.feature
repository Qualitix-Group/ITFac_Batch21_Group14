Feature: Admin Plant API

  Scenario: Admin creates a plant under a valid sub-category
    Given Admin is authenticated
    When Admin sends POST request to create plant under sub-category
    Then Plant should be created successfully

  Scenario: Admin tries to create duplicate plant under same category
    Given Admin is authenticated
    And A plant already exists in the selected category
    When Admin sends POST request to create plant with same name
    Then Response status code should be 400 Bad Request
    And Error message should indicate duplicate plant