@sales @admin @ui
Feature: Sales Management - Admin
  As an administrator
  I want to manage sales
  So that I can track plant transactions

  Background:
    Given I am logged in as admin

  @UI-SALES-01
  Scenario: View sales list page
    When I navigate to the sales page
    Then I should see the sales list table

  @UI-SALES-02
  Scenario: Sell Plant button visible for admin
    When I navigate to the sales page
    Then the sell button should be visible

  @UI-SALES-03
  Scenario: Delete button visible for admin
    Given there is a sale to delete
    When I navigate to the sales page
    Then the delete button should be visible

  @UI-SALES-04
  Scenario: Admin can access Sell Plant page
    When I navigate to the sales page
    And I click the sell button
    Then I should be on the sell plant page

  @UI-SALES-05
  Scenario: Plant dropdown lists available plants
    When I navigate to the sales page
    And I click the sell button
    Then the plant dropdown should list available plants

  @UI-SALES-09
  Scenario: Plant dropdown shows stock quantities
    When I navigate to the sales page
    And I click the sell button
    Then the plant dropdown should show stock quantities

  @UI-SALES-10
  Scenario: Sell plant with valid quantity
    Given there is available stock for the test plant
    When I navigate to the sales page
    And I click the sell button
    And I select the test plant
    And I enter a valid quantity
    And I submit the sale
    Then I should be on the sales list

  @UI-SALES-11
  Scenario: Stock reduces after a successful sale
    Given there is available stock for the test plant
    When I record the current stock
    And I navigate to the sales page
    And I click the sell button
    And I select the test plant
    And I enter a valid quantity
    And I submit the sale
    Then the stock should be reduced by the sold quantity

  @UI-SALES-12
  Scenario: Sell plant with quantity greater than available stock
    When I navigate to the sales page
    And I click the sell button
    And I select a plant with available stock
    And I enter quantity greater than available stock
    And I submit the sale
    Then I should see a sale error message

  @UI-SALES-13
  Scenario: Submit without selecting plant
    When I navigate to the sales page
    And I click the sell button
    And I enter quantity "1"
    And I submit the sale
    Then I should see plant validation message

  @UI-SALES-14
  Scenario: Submit with quantity 0
    When I navigate to the sales page
    And I click the sell button
    And I select a plant with available stock
    And I enter quantity "0"
    And I submit the sale
    Then I should see quantity validation message

  @UI-SALES-15
  Scenario: Submit with negative quantity
    When I navigate to the sales page
    And I click the sell button
    And I select a plant with available stock
    And I enter quantity "-5"
    And I submit the sale
    Then I should see quantity validation message

  @UI-SALES-16
  Scenario: Cancel button navigates back to Sales List
    When I navigate to the sales page
    And I click the sell button
    And I click the cancel button in sales sell page
    Then I should be on the sales list

  @UI-SALES-17
  Scenario: Delete confirmation popup appears
    Given there is a sale to delete
    When I navigate to the sales page
    Then a delete confirmation should be displayed for sales

  @UI-SALES-18
  Scenario: Admin can delete a sale record
    Given there is a sale to delete
    When I navigate to the sales page
    And I delete the first sale
    Then the sale count should decrease
    And a sale deletion success message should be displayed

  @UI-SALES-19
  Scenario: Cancel sale deletion
    Given there is a sale to delete
    When I navigate to the sales page
    And I cancel deleting the first sale
    Then the sale should remain in the list
