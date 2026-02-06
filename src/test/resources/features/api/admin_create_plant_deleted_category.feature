@admin @negative @TC_API_ADMIN_PLANT_011
Feature: Admin cannot create plant with deleted category

  Scenario: Creating a plant using a deleted category ID should fail
    Given a deleted category ID exists
    When the admin creates a plant using the deleted category ID
    Then the API should return a client error
    And no plant should be created
