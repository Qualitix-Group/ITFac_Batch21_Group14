Feature: Unauthorized plant access

  Scenario: Retrieve plant without token
    When user requests plant by id without authentication
    Then response status should be 401
    And error message should be returned
