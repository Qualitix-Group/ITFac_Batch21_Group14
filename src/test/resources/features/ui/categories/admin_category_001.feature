# File: src/test/resources/features/ui/categories/admin_category_001.feature
Feature: Admin Category Management - TC_UI_ADMIN_CAT_001

  @Admin @Category @Smoke
  Scenario: Verify Add Category Button Visible for Admin
    Given I am logged in as an admin user
    And I navigate to the categories page
    When I view the categories page
    Then I should see the "Add Category" button
    And the "Add Category" button should be clickable

  @User @Category @Regression
  Scenario: Verify Add Category Button Not Visible for Regular User
    Given I am logged in as a regular user
    And I navigate to the categories page
    When I view the categories page
    Then I should not see the "Add Category" button