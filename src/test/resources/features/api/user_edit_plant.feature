Feature: Edit plant permission

  Scenario: User cannot edit plant
    When user attempts to update a plant
    Then response status should be 403
