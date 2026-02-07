Feature: Create plant permission

  Scenario: User cannot create plant
    When user attempts to create a plant
    Then response status should be 403
