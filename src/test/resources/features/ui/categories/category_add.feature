@Admin @Category @Smoke
Feature: Category Creation - TC_UI_ADMIN_CAT_003 - Verify category creation with valid category name and parent category

  Background:
    Given I open the application
    And I am logged in as an admin user

  @Positive @CategoryCreation
  Scenario: TC_UI_ADMIN_CAT_007 Create new category with valid name and parent category
    Given I navigate to the categories page
    And I click on the "Add A Category" button
    When I fill category name field with "Herbs8"
    And I select "Roses1" as parent category
    And I click on the "Save" button
    Then I should be redirected to the categories listing page
    And I should see a success message
    And the new category "Herbs8" should be visible in the list

  @TC_UI_ADMIN_CAT_006
  Scenario: Create new category with name less than 3 characters
    When I click on the "Add A Category" button
    And I fill category name field with "AB"
    And I select "Roses1" as parent category
    And I click on the "Save" button
    Then I should see error message for name less than 3 characters
    And I should see validation error 'Category name must be between 3 and 10 characters'
    And category should not be created

  @TC_UI_ADMIN_CAT_007
  Scenario: Create new category with name more than 10 characters
    When I click on the "Add A Category" button
    And I fill category name field with "Summer Flowers and Plants"
    And I select "Roses1" as parent category
    And I click on the "Save" button
    Then I should see error message for name more than 10 characters
    And I should see validation error 'Category name must be between 3 and 10 characters'
    And category should not be created

