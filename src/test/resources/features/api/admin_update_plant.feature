@admin @positive @update @TC_API_ADMIN_PLANT_012
Feature: Admin Plant API - Update Plant Tests
  As an admin user
  I want to update existing plant details
  So that plant information can be kept current

  Background:
    Given the API server is running
    And admin user is logged in
    And I have a valid admin authentication token

  @TC_API_ADMIN_PLANT_012
  Scenario: Verify plant details are updated successfully using valid plant ID
    Given a plant with ID exists in the system
    And I have valid update data
    When I send PUT request to "/api/plants/{plantId}" with updated plant details
    Then the response status code should be 200 OK
    And the response body should contain updated plant details
    And plant name, price, and quantity should reflect updated values
    And the database record should be updated successfully
    And the response should match the format:
      """
      {
        "id": 0,
        "name": "Anthurium",
        "price": 150,
        "quantity": 25,
        "category": {
          "id": 0,
          "name": "Anthurium",
          "parent": "string",
          "subCategories": ["string"]
        }
      }
      """

  @TC_API_ADMIN_PLANT_012_Partial
  Scenario: Verify partial update of plant details
    Given a plant exists in the system
    When I send PUT request to "/api/plants/{plantId}" with only name updated
    Then the response status code should be 200 OK
    And only the specified fields should be updated
    And other fields should remain unchanged

  @TC_API_ADMIN_PLANT_012_Multiple
  Scenario: Verify multiple sequential updates
    Given a plant exists in the system
    When I update the plant multiple times with different values
    Then each update should be successful
    And the plant should reflect the last update values