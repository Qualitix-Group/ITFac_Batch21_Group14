@plants @admin @ui
Feature: Plant Management - Admin
  As an administrator
  I want to manage plants
  So that I can maintain the plant catalog

  Background:
    Given I am logged in as admin

  @UI-PLANT-01
  Scenario: View plant list page
    When I navigate to the plants page
    Then I should see the plant list table
    And the table should contain plant data

  @UI-PLANT-02
  Scenario: Pagination visible when multiple pages exist
    Given plant records exceed one page
    When I navigate to the plants page
    Then plant pagination should be visible when multiple pages exist

  @UI-PLANT-03
  Scenario: Pagination navigation works
    Given plant records exceed one page
    When I navigate to the plants page
    And I go to the next plant page
    Then the plant page should change

  @UI-PLANT-04
  Scenario: Add Plant button visible for Admin
    When I navigate to the plants page
    Then the Add Plant button should be visible

  @UI-PLANT-05
  Scenario: Edit button visible for Admin
    When I navigate to the plants page
    Then the Edit button should be visible

  @UI-PLANT-06
  Scenario: Delete button visible for Admin
    When I navigate to the plants page
    Then the Delete button should be visible

  @UI-PLANT-07
  Scenario: Add plant with valid data
    When I navigate to the add plant form
    And I select a category for the plant
    And I submit plant with name price "10.00" and quantity "5"
    Then the new plant should appear in the list

  @UI-PLANT-10
  Scenario: Plant name more than 25 characters
    When I navigate to the add plant form
    And I enter plant name "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    And I select a category for the plant
    And I enter plant price "10"
    And I enter plant quantity "2"
    And I submit the plant form
    Then I should see a plant validation message containing "between"

  @UI-PLANT-11
  Scenario: Leave price empty
    When I navigate to the add plant form
    And I enter plant name "PlantA"
    And I select a category for the plant
    And I enter plant price ""
    And I enter plant quantity "2"
    And I submit the plant form
    Then I should see a plant validation message containing "price"

  @UI-PLANT-12
  Scenario: Price validation requires positive number
    When I navigate to the add plant form
    And I select a category for the plant
    And I enter plant name "PlantB"
    And I enter plant price "0"
    And I enter plant quantity "2"
    And I submit the plant form
    Then I should see a plant validation message containing "greater"

  @UI-PLANT-14
  Scenario: Leave quantity empty
    When I navigate to the add plant form
    And I enter plant name "PlantD"
    And I select a category for the plant
    And I enter plant price "10"
    And I enter plant quantity ""
    And I submit the plant form
    Then I should see a plant validation message containing "quantity"

  @UI-PLANT-15
  Scenario: Negative quantity validation
    When I navigate to the add plant form
    And I enter plant name "PlantE"
    And I select a category for the plant
    And I enter plant price "10"
    And I enter plant quantity "-1"
    And I submit the plant form
    Then I should see a plant validation message containing "negative"

  @UI-PLANT-16
  Scenario: Quantity zero accepted
    When I navigate to the add plant form
    And I enter plant name "PlantF"
    And I select a category for the plant
    And I enter plant price "10"
    And I enter plant quantity "0"
    And I submit the plant form
    Then I should not see any plant validation message

  @UI-PLANT-17
  Scenario: Leave category empty
    When I navigate to the add plant form
    And I enter plant name "PlantG"
    And I enter plant price "10"
    And I enter plant quantity "2"
    And I submit the plant form
    Then I should see a plant validation message containing "category"

  @UI-PLANT-19
  Scenario: Cancel button navigates back to Plant list
    When I navigate to the add plant form
    And I click the cancel button in plant add page
    Then I should be on the plants list

  @UI-PLANT-20
  Scenario: Update plant with valid data
    When I edit the first plant
    Then the updated plant should appear in the list

  @UI-PLANT-21
  Scenario: Delete plant successfully
    Given I have created a plant via UI
    When I navigate to the plants page
    And I search for the created plant
    And I delete the plant
    Then the plant should not appear in the list

  @UI-PLANT-22
  Scenario: Cancel plant deletion
    Given I have created a plant via UI
    When I navigate to the plants page
    Then a delete confirmation should be displayed for plants
    When I cancel deleting the first plant
    Then the plant should remain in the list

  @UI-PLANT-23
  Scenario: Delete plant with low stock
    When I create a low stock plant via UI
    And I navigate to the plants page
    And I search for the created plant
    And I delete the plant
    Then the plant should not appear in the list

  @UI-PLANT-25
  Scenario: No plants found message when list is empty
    Given the plant list is empty
    When I navigate to the plants page
    Then the no plants message should be displayed

  @UI-PLANT-26
  Scenario: Low badge displayed for low stock
    Given a low stock plant exists
    When I navigate to the plants page
    Then a low stock badge should be displayed
