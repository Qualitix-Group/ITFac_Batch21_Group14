Feature: Get paged plants

  Scenario: Retrieve plants with pagination
    When user requests plants page 0 size 5
    Then response status should be 200
    And returned records should not exceed page size
