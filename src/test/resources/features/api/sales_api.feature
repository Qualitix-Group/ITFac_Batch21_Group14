@api @sales
Feature: Sales API
  As an API consumer
  I want to manage sales via REST API

  # ========== Admin Tests ==========

  @API-SALES-01
  Scenario: Admin can create a sale with valid quantity
    Given I am authenticated as admin via API
    And the plant has available stock
    When I send a POST request to create a sale with valid quantity
    Then the response status should be 200 or 201
    And the sale response should contain required fields

  @API-SALES-02
  Scenario: Admin gets error for zero or negative quantity
    Given I am authenticated as admin via API
    When I send a POST request to create a sale with quantity 0
    Then the response status should be 400 or 422

  @API-SALES-03
  Scenario: Admin gets error for quantity greater than stock
    Given I am authenticated as admin via API
    When I send a POST request to create a sale with quantity 999999
    Then the response status should be 400 or 422

  @API-SALES-04
  Scenario: Admin gets error for invalid plant id
    Given I am authenticated as admin via API
    When I send a POST request to create a sale with invalid plant id
    Then the response status should be 404 or 400

  @API-SALES-05
  Scenario: Admin can delete a sale
    Given I am authenticated as admin via API
    And I have created a sale for testing
    When I send a DELETE request to delete the sale
    Then the response status should be 200 or 204

  @API-SALES-06
  Scenario: Admin gets error for invalid sale id
    Given I am authenticated as admin via API
    When I send a DELETE request to "/api/sales/999999"
    Then the response status should be 404 or 400

  @API-SALES-07
  Scenario: Stock reduces after sale
    Given I am authenticated as admin via API
    And the plant has available stock
    When I record the initial plant stock
    And I send a POST request to create a sale with valid quantity
    Then the plant stock should be reduced by the sold quantity

  # ========== User Tests ==========

  @API-SALES-08
  Scenario: User cannot create sale
    Given I am authenticated as user via API
    When I send a POST request to create a sale with quantity 1
    Then the response status should be 403 or 401

  @API-SALES-09
  Scenario: User cannot delete sale
    Given I am authenticated as admin via API
    And I have created a sale for testing
    Given I am authenticated as user via API
    When I send a DELETE request to delete the created sale
    Then the response status should be 403 or 401

  @API-SALES-10
  Scenario: User can get all sales
    Given I am authenticated as user via API
    When I send a GET request to "/api/sales"
    Then the response status should be 200
    And the response should contain data

  @API-SALES-11
  Scenario: User gets empty sales list when none exist
    Given I am authenticated as admin via API
    And no sales exist in the system
    Given I am authenticated as user via API
    When I send a GET request to "/api/sales"
    Then the response status should be 200
    And the sales list should be empty

  @API-SALES-12
  Scenario: User can get sale by valid ID
    Given I am authenticated as admin via API
    And I have created a sale for testing
    Given I am authenticated as user via API
    When I send a GET request to the created sale
    Then the response status should be 200
    And the sale response should contain required fields
