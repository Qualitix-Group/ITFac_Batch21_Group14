@categories @user @ui
Feature: Category Management - User
  As a regular user
  I want to view and search categories
  But I should not have admin privileges

  Background:
    Given I am logged in as user

  @UI-CAT-USER-01
  Scenario: View category list as user
    When I navigate to the categories page
    Then I should see the category list table

  @UI-CAT-USER-02
  Scenario: Add Category button not visible for User
    When I navigate to the categories page
    Then the Add Category button should be hidden

  @UI-CAT-USER-03
  Scenario: Edit Category button not visible for User
    When I navigate to the categories page
    Then the Edit Category button should be hidden

  @UI-CAT-USER-04
  Scenario: Delete button not visible for User
    When I navigate to the categories page
    Then the Delete Category button should be hidden

  @UI-CAT-USER-05
  Scenario: Pagination displays correctly
    Given category records exceed one page
    When I navigate to the categories page
    Then category pagination should be visible when multiple pages exist

  @UI-CAT-USER-06
  Scenario: Pagination navigation works
    Given category records exceed one page
    When I navigate to the categories page
    And I click next page
    Then I should be on the next page

  @UI-CAT-USER-07
  Scenario: Search category by valid name
    When I navigate to the categories page
    And I search for an existing category name
    Then the results should only contain matching categories

  @UI-CAT-USER-08
  Scenario: Search with invalid name
    When I navigate to the categories page
    And I search for a non-existent category
    Then I should see empty state or no matching results

  @UI-CAT-USER-09
  Scenario: Filter by parent category
    When I navigate to the categories page
    And I select the first parent category filter
    Then all results should belong to the selected parent

  @UI-CAT-USER-10
  Scenario: Sort by ID
    When I navigate to categories sorted by "id" "asc"
    Then category IDs should be sorted ascending
    When I navigate to categories sorted by "id" "desc"
    Then category IDs should be sorted descending

  @UI-CAT-USER-11
  Scenario: Sort by Name
    When I navigate to categories sorted by "name" "asc"
    Then category names should be sorted ascending
    When I navigate to categories sorted by "name" "desc"
    Then category names should be sorted descending

  @UI-CAT-USER-12
  Scenario: Sort by Parent Category
    When I navigate to categories sorted by "parent.name" "asc"
    Then parent categories should be sorted ascending
    When I navigate to categories sorted by "parent.name" "desc"
    Then parent categories should be sorted descending

  @UI-CAT-USER-13
  Scenario: Empty list message
    Given the category list is empty
    When I navigate to the categories page
    Then I should see empty state or no matching results
