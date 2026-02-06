@admin @negative
Feature: Admin cannot create plant with invalid category ID

  Scenario: Creating a plant using an invalid category ID should fail
    When the admin creates a plant using an invalid category ID
    Then the API should return a client error
    And no plant should be created
