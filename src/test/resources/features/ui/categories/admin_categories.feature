# File: src/test/resources/features/ui/categories/admin_categories.feature
Feature: Admin Category Management
  As an Admin user
  I want to manage categories
  So that I can organize content in the system

  Background:
    Given I am logged in as an admin user
    And I navigate to the categories page

  @Admin @Category @Smoke
  Scenario: TC_UI_ADMIN_CAT_001 - Verify Add Category Button Visible for Admin
    When I view the categories page
    Then I should see the "Add Category" button
    And the "Add Category" button should be clickable

  @Admin @Category
  Scenario: TC_UI_ADMIN_CAT_002 - Verify Edit Category Button Visible for Admin
    Given there is at least one existing category
    When I view the categories list
    Then I should see edit icons for each category
    And the edit icons should be clickable

  @Admin @Category
  Scenario: TC_UI_ADMIN_CAT_003 - Verify category creation with valid data
    When I click on "Add Category" button
    And I enter category name as "Electronics"
    And I select parent category as "None"
    And I click "Save" button
    Then I should see success message "Category created successfully"
    And the category "Electronics" should appear in the list

  @Admin @Category
  Scenario: TC_UI_ADMIN_CAT_004 - Verify category creation without parent category
    When I click on "Add Category" button
    And I enter category name as "Main Category"
    And I leave parent category as default
    And I click "Save" button
    Then I should see success message
    And "Main Category" should be marked as main category

  @Admin @Category @Validation
  Scenario: TC_UI_ADMIN_CAT_005 - Verify category creation with empty name
    When I click on "Add Category" button
    And I clear the category name field
    And I click "Save" button
    Then I should see validation error "Category name is required"
    And the save should not be successful

  @Admin @Category @Validation
  Scenario: TC_UI_ADMIN_CAT_006 - Verify name less than 3 characters
    When I click on "Add Category" button
    And I enter category name as "ab"
    And I click "Save" button
    Then I should see validation error "Category name must be at least 3 characters"

  @Admin @Category @Validation
  Scenario: TC_UI_ADMIN_CAT_007 - Verify name more than 10 characters
    When I click on "Add Category" button
    And I enter category name as "ThisNameIsTooLongForCategory"
    And I click "Save" button
    Then I should see validation error "Category name cannot exceed 10 characters"

  @Admin @Category
  Scenario: TC_UI_ADMIN_CAT_008 - Verify Cancel navigation
    When I click on "Add Category" button
    And I click "Cancel" button
    Then I should be redirected to categories listing page
    And the category form should be closed

  @Admin @Category
  Scenario: TC_UI_ADMIN_CAT_009 - Verify Edit category with valid data
    Given there is an existing category named "Old Category"
    When I click edit icon for "Old Category"
    And I change category name to "Updated Category"
    And I click "Save" button
    Then I should see success message "Category updated successfully"
    And "Updated Category" should appear in the list

  @Admin @Category @Validation
  Scenario: TC_UI_ADMIN_CAT_010 - Verify Clear category name and save
    Given there is an existing category named "Test Category"
    When I click edit icon for "Test Category"
    And I clear the category name field
    And I click "Save" button
    Then I should see validation error "Category name is required"
    And the original category name should remain unchanged