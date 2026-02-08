@categories @admin @ui
Feature: Category Management - Admin
  As an administrator
  I want to manage categories
  So that I can organize the plant catalog

  Background:
    Given I am logged in as admin

  @UI-CAT-ADMIN-01
  Scenario: View category list
    When I navigate to the categories page
    Then I should see the category list table

  @UI-CAT-ADMIN-02
  Scenario: Pagination displays correctly
    Given category records exceed one page
    When I navigate to the categories page
    Then category pagination should be visible when multiple pages exist
    And pagination should be enabled
    When I click next page
    Then I should be on the next page

  @UI-CAT-ADMIN-03
  Scenario: Add Category button visible for Admin
    When I navigate to the categories page
    Then the Add Category button should be visible

  @UI-CAT-ADMIN-04
  Scenario: Edit Category button visible for Admin
    When I navigate to the categories page
    Then the Edit Category button should be visible

  @UI-CAT-ADMIN-05
  Scenario: Delete Category button visible for Admin
    When I navigate to the categories page
    Then the Delete Category button should be visible

  @UI-CAT-ADMIN-06
  Scenario: Add a new category with valid data
    When I navigate to the add category form
    And I enter a valid category name
    And I save the category
    Then the new category should appear in the list

  @UI-CAT-ADMIN-07
  Scenario: Create category without parent (main category)
    When I navigate to the add category form
    And I enter a valid category name
    And I save the category
    Then the category should be created as a main category

  @UI-CAT-ADMIN-12
  Scenario: Cancel button navigation
    When I navigate to the add category form
    And I click the cancel button
    Then I should be on the categories list

  @UI-CAT-ADMIN-13
  Scenario: Edit category with valid data
    When I navigate to the categories page
    And I click edit on the first category
    And I update the category name
    And I save the category
    Then the updated category should appear in the list

  @UI-CAT-ADMIN-15
  Scenario: Delete confirmation popup appears
    When I navigate to the categories page
    Then a delete confirmation should be displayed for categories

  @UI-CAT-ADMIN-16
  Scenario: Delete category successfully
    When I navigate to the categories page
    And I delete the first category
    Then the category should be removed from the list

  @UI-CAT-ADMIN-17
  Scenario: Deleted category not displayed after deletion
    When I navigate to the categories page
    And I delete the first category
    Then the deleted category should not appear in search results

  @UI-CAT-ADMIN-18
  Scenario: Cancel delete confirmation
    When I navigate to the categories page
    And I cancel deleting the first category
    Then the category should remain in the list
