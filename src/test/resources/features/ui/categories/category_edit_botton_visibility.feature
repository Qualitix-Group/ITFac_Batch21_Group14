# File: src/test/resources/features/ui/categories/admin_category_002.feature
Feature: Edit Category Button Visibility - TC_UI_ADMIN_CAT_002 & TC_UI_USER_CAT_002

  Background:
    Given I open the application

  @Admin @Category @Smoke
  Scenario Outline: TC_UI_ADMIN_CAT_002 - Verify Edit Category Button Visible for Admin
    Given I am logged in as an admin user
    And I navigate to the categories page
    And there is at least one category in the list
    When I look for edit buttons on the categories page
    Then I should see edit icons for each category
    And the edit icon should be clickable

    Examples:
      | test_case        |
      | admin_edit_test  |

  @User @Category @Regression
  Scenario Outline: TC_UI_USER_CAT_002 - Verify Edit Category Button Not Visible for User
    Given I am logged in as a regular user
    And I navigate to the categories page
    And there is at least one category in the list
    When I look for edit buttons on the categories page
    Then I should not see any edit icons
    And I should not be able to access edit functionality

    Examples:
      | test_case       |
      | user_edit_test  |

  @Admin @Category @Integration @TestData
  Scenario: Edit specific category as admin using test data
    Given I am logged in as an admin user
    And I navigate to the categories page
    And a test category exists in the list
    When I look for the edit icon for the test category
    Then I should see the edit icon for the test category
    When I click the edit icon for the test category
    Then I should be taken to the edit page for the test category

  @Admin @Category @Dynamic
  Scenario: Edit any existing category as admin
    Given I am logged in as an admin user
    And I navigate to the categories page
    And there is at least one category in the list
    And I store the first category name
    When I look for the edit icon for the stored category
    Then I should see the edit icon for the stored category
    When I click the edit icon for the stored category
    Then I should be taken to the edit page