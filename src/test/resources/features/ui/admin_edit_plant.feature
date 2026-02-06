Feature: Admin Edit Plant

  @Smoke @AdminPlantEdit
  Scenario: Verify Update plant with valid data
    Given I am logged in as admin and on plant list page
    When I update a plant with valid data
    Then the plant should be updated successfully
