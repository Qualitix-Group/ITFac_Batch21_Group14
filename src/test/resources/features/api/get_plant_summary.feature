Feature: Get plant summary

  Scenario: Retrieve plant summary
    When user requests plant summary
    Then response status should be 200
    And summary values should be non-negative
