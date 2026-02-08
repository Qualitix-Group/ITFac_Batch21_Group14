@admin @api @plant @add-edit
Feature: Admin Plant Add/Edit API Operations

  Background:
    Given admin is authenticated

  @admin @plant @positive @add
  Scenario: Verify that Admin can successfully create a plant under a valid sub-category
    Given admin has a valid sub-category ID
    And admin prepares valid plant data for creation
    When admin sends POST request to create plant
    Then the plant should be created successfully with status 201
    And the response should contain created plant details
    And the created plant should be retrievable from the system
    And clean up the created test plant

  @admin @plant @negative @add @duplicate
  Scenario: Verify that admin gets error when creating duplicate plant under same category
    Given admin has a valid sub-category ID
    And a plant with name "DuplicateTestPlant" already exists in the category
    And admin prepares duplicate plant data with existing name
    When admin sends POST request to create plant
    Then the response status should be 400
    And the error response should indicate duplicate plant name

  @admin @plant @negative @add @invalid-category
  Scenario: Verify error response when invalid category ID is used
    Given admin has an invalid category ID that does not exist
    And admin prepares valid plant data for creation
    When admin sends POST request to create plant
    Then the response status should be 404
    And the error response should indicate invalid or deleted category

  @admin @plant @negative @add @deleted-category
  Scenario: Verify the admin gets error when using deleted category ID
    Given admin has a deleted category ID
    And admin prepares valid plant data for creation
    When admin sends POST request to create plant
    Then the response status should be 404
    And the error response should indicate invalid or deleted category

  @admin @plant @positive @update
  Scenario: Verify that plant details are updated successfully using valid plant ID
    Given an existing plant is available for updating
    And admin prepares updated plant data
    When admin sends PUT request to update the plant
    Then the plant should be updated successfully with status 200
    And the response should contain updated plant details
    And the updated plant should reflect changes in the system