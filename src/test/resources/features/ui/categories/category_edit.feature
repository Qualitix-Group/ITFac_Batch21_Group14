@EditCategory @Regression
Feature: Edit Category Functionality
  As an admin user
  I want to edit existing categories
  So that I can update category information when needed

  Background:
    Given I am logged in as an admin
    And I navigate to the categories page

  @TC_UI_ADMIN_CAT_009 @Positive
  Scenario: Verify Edit category with valid data
    Given there is an existing category named "Test Category"
    When I click on the "Edit Category" button for category "Test Category"
    And I update category name to "Updated Test Category"
    And I update description to "Updated description for testing"
    And I update parent category to "Roses1"
    And I click on the "Update" button for category "Updated Test Category"
    Then I should see update success message
    And I should be redirected to the categories listing page
    And the updated category "Updated Test Category" should be visible in the list
    And the original category "Test Category" should not be visible in the list

  @TC_UI_ADMIN_CAT_010 @Negative
  Scenario: Verify Clear category name and save
    Given there is an existing category named "Test Category 2"
    When I click on the "Edit Category" button for category "Test Category 2"
    And I clear the category name field in edit form
    And I click on the "Update" button for category ""
    Then I should see validation error for empty category name
    And the category update should fail
    And I should remain on the edit category page

  @EditCategory @Positive
  Scenario: Edit category with only name change
    Given I get the first available category for editing
    When I click on the "Edit Category" button for category "<firstCategory>"
    And I update category name to "Renamed Category"
    And I click on the "Update" button for category "Renamed Category"
    Then I should see update success message
    And the updated category "Renamed Category" should be visible in the list

  @EditCategory @Negative
  Scenario: Edit category with name less than 3 characters
    Given I am on the edit page for category "Test Category"
    When I update category name to "AB"
    And I click on the "Update" button for category "AB"
    Then I should see validation error for empty category name
    And the category update should fail

  @EditCategory @Negative
  Scenario: Edit category with name more than 10 characters
    Given I am on the edit page for category "Test Category"
    When I update category name to "Very Long Category Name That Exceeds Limit"
    And I click on the "Update" button for category "Very Long Category Name That Exceeds Limit"
    Then I should see validation error for empty category name
    And the category update should fail