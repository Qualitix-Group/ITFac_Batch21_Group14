# File: src/test/resources/features/ui/categories/user_categories.feature
Feature: User Category View
  As a Regular user
  I want to view categories
  So that I can browse organized content

  Background:
    Given I am logged in as a regular user
    And I navigate to the categories page

  @User @Category @Smoke
  Scenario: TC_UI_USER_CAT_001 - Verify Add Category Button Not Visible for User
    When I view the categories page
    Then I should NOT see the "Add Category" button

  @User @Category
  Scenario: TC_UI_USER_CAT_002 - Verify Edit Category Button Not Visible for User
    Given there is at least one existing category
    When I view the categories list
    Then I should NOT see edit icons for categories

  @User @Category @Pagination
  Scenario: TC_UI_USER_CAT_003 - Verify page navigation using pagination
    Given there are more than 10 categories in the system
    When I view the categories list
    Then I should see pagination controls
    When I click on page number "2"
    Then I should see different categories on page 2
    And the URL should indicate page 2

  @User @Category @Pagination
  Scenario: TC_UI_USER_CAT_004 - Verify pagination displays correctly
    Given there are more than 10 categories in the system
    When I view the categories list
    Then I should see pagination with:
      | Element           | Should Be Visible |
      | Previous button   | No (on first page)|
      | Page numbers      | Yes               |
      | Next button       | Yes               |
      | Current page      | Highlighted       |

  @User @Category
  Scenario: TC_UI_USER_CAT_005 - Verify empty list message
    Given there are no categories in the system
    When I view the categories page
    Then I should see message "No categories found"
    And I should see suggestion to add categories (for admin only)