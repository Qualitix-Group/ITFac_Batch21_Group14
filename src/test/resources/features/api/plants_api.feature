@api @plants
Feature: Plants API
  As an API consumer
  I want to manage plants via REST API

  # ========== Admin Tests ==========

  @API-PLANT-01
  Scenario: Admin can create a plant under a valid sub-category
    Given I am authenticated as admin via API
    When I send a POST request to create a plant with valid data
    Then the response status should be 200 or 201
    And the response should contain an id

  @API-PLANT-02
  Scenario: Admin error when creating duplicate plant under same category
    Given I am authenticated as admin via API
    When I send a POST request to create a plant with valid data
    And I send a POST request to create a duplicate plant in the same category
    Then the response status should be 400 or 422

  @API-PLANT-03
  Scenario: Admin error when invalid category ID is used
    Given I am authenticated as admin via API
    When I send a POST request to create a plant with invalid category id
    Then the response status should be 400 or 404

  @API-PLANT-04
  Scenario: Admin error when creating plant under deleted category
    Given I am authenticated as admin via API
    When I send a POST request to create a plant under a deleted category
    Then the response status should be 400 or 404

  @API-PLANT-05
  Scenario: Admin can update plant with valid ID
    Given I am authenticated as admin via API
    And I have created a plant for testing
    When I send a PUT request to update the plant
    Then the response status should be 200 or 204

  @API-PLANT-06
  Scenario: Admin can delete plant with valid ID
    Given I am authenticated as admin via API
    And I have created a plant for testing
    When I send a DELETE request to delete the plant
    Then the response status should be 200 or 202 or 204

  @API-PLANT-07
  Scenario: Admin delete plant with invalid ID
    Given I am authenticated as admin via API
    When I send a DELETE request to "/api/plants/999999"
    Then the response status should be 404 or 400

  @API-PLANT-08
  Scenario: Admin delete already deleted plant
    Given I am authenticated as admin via API
    And I have created a plant for testing
    When I send a DELETE request to delete the plant
    Then the response status should be 200 or 202 or 204
    When I send a DELETE request to delete the plant
    Then the response status should be 404 or 400

  @API-PLANT-09
  Scenario: Admin can retrieve plants by valid category ID
    Given I am authenticated as admin via API
    When I send a GET request to plants by valid category id
    Then the response status should be 200

  @API-PLANT-10
  Scenario: Admin receives error when category ID format is invalid
    Given I am authenticated as admin via API
    When I send a GET request to plants by invalid category format
    Then the response status should be 400 or 404

  @API-PLANT-11
  Scenario: Admin receives error when category ID does not exist
    Given I am authenticated as admin via API
    When I send a GET request to plants by non existing category
    Then the response status should be 404 or 400

  @API-PLANT-12
  Scenario: Admin retrieves plants for empty category
    Given I am authenticated as admin via API
    When I send a GET request to plants by empty category
    Then the response status should be 200
    And the plant list should be empty

  # ========== User Tests ==========

  @API-PLANT-13
  Scenario: User can retrieve plant details using valid plant ID
    Given I am authenticated as admin via API
    And I have created a plant for testing
    Given I am authenticated as user via API
    When I send a GET request to get the plant
    Then the response status should be 200

  @API-PLANT-14
  Scenario: User receives error when plant ID format is invalid
    Given I am authenticated as user via API
    When I send a GET request to "/api/plants/abc"
    Then the response status should be 400 or 404

  @API-PLANT-15
  Scenario: User receives error when plant ID does not exist
    Given I am authenticated as user via API
    When I send a GET request to "/api/plants/999999"
    Then the response status should be 404 or 400

  @API-PLANT-16
  Scenario: User can retrieve all plants
    Given I am authenticated as user via API
    When I send a GET request to "/api/plants"
    Then the response status should be 200
    And the response should contain data

  @API-PLANT-17
  Scenario: User gets empty array when no plants exist
    Given I am authenticated as admin via API
    And no plants exist in the system
    Given I am authenticated as user via API
    When I send a GET request to "/api/plants"
    Then the response status should be 200
    And the plant list should be empty

  @API-PLANT-18
  Scenario: User receives 403 when attempting to delete a plant
    Given I am authenticated as user via API
    When I send a DELETE request to the existing plant
    Then the response status should be 403

  @API-PLANT-19
  Scenario: User cannot retrieve data without authentication
    When I send a GET request to "/api/plants/1" without authentication
    Then the response status should be 401

  @API-PLANT-20
  Scenario: User cannot add plant
    Given I am authenticated as user via API
    When I send a POST request to create a plant with valid data
    Then the response status should be 403

  @API-PLANT-21
  Scenario: User cannot edit plant
    Given I am authenticated as user via API
    When I send a PUT request to update an existing plant
    Then the response status should be 403

  @API-PLANT-22
  Scenario: Retrieve plants with pagination and sorting
    Given I am authenticated as user via API
    When I send a GET request to paged plants with page 0 and size 5
    Then the response status should be 200

  @API-PLANT-23
  Scenario: Invalid pagination values
    Given I am authenticated as user via API
    When I send a GET request to paged plants with page -1 and size 0
    Then the response status should be 400 or 422

  @API-PLANT-24
  Scenario: Get plant summary
    Given I am authenticated as user via API
    When I send a GET request to plant summary
    Then the response status should be 200
    And the plant summary should contain totals
