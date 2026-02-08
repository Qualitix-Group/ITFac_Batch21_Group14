@plants @user @ui
Feature: Plant Management - User
  As a regular user
  I want to view and search plants
  But I should not have admin privileges

  Background:
    Given I am logged in as user

  @UI-PLANT-06
  Scenario: View plant list as user
    When I navigate to the plants page
    Then I should see the plant list table

  @UI-PLANT-07
  Scenario: Search plant by name
    When I navigate to the plants page
    And I search for an existing plant name
    Then the results should only contain matching plants

  @UI-PLANT-08
  Scenario: Search with invalid name shows empty message
    When I navigate to the plants page
    And I search for a non-existing plant name
    Then the no plants message should be displayed

  @UI-PLANT-09
  Scenario: Filter by category
    When I navigate to the plants page
    And I select the first category filter
    Then all results should belong to the selected category

  @UI-PLANT-10
  Scenario: Sort by name
    When I navigate to the plants page
    And I click sort by name
    Then the name column should be sorted

  @UI-PLANT-11
  Scenario: Sort by price
    When I navigate to the plants page
    And I click sort by price
    Then the price column should be sorted

  @UI-PLANT-12
  Scenario: Sort by quantity
    When I navigate to the plants page
    And I click sort by quantity
    Then the quantity column should be sorted

  @UI-PLANT-13
  Scenario: Low badge appears for low stock
    Given a low stock plant exists
    When I navigate to the plants page
    Then a low stock badge should be displayed

  @UI-PLANT-14
  Scenario: Low badge not shown for sufficient stock
    Given a plant with sufficient stock exists
    When I navigate to the plants page
    Then no low stock badge should be displayed for sufficient stock

  @UI-PLANT-15 @security
  Scenario: Add Plant button not visible for User
    When I navigate to the plants page
    Then the Add Plant button should be hidden

  @UI-PLANT-16 @security
  Scenario: Edit button not visible for User
    When I navigate to the plants page
    Then the Edit button should be hidden

  @UI-PLANT-17 @security
  Scenario: Delete button not visible for User
    When I navigate to the plants page
    Then the Delete button should be hidden

  @UI-PLANT-18
  Scenario: Pagination visible when multiple pages exist
    Given plant records exceed one page
    When I navigate to the plants page
    Then plant pagination should be visible when multiple pages exist

  @UI-PLANT-19
  Scenario: Pagination navigation works
    Given plant records exceed one page
    When I navigate to the plants page
    And I go to the next plant page
    Then the plant page should change

  @UI-PLANT-20
  Scenario: Empty plant list message
    Given the plant list is empty
    When I navigate to the plants page
    Then the no plants message should be displayed
