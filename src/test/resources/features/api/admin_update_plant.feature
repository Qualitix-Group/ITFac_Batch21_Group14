@admin @positive
Feature: Admin updates plant details

  Scenario: Updating an existing plant should succeed
    When the admin updates an existing plant
    Then the plant details should be updated successfully
