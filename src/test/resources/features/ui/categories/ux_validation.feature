# File: src/test/resources/features/ui/categories/ux_validation.feature
Feature: UX Validation Flow
  As an Admin user
  I want clear validation messages
  So that I can correct form errors easily

  Background:
    Given I am logged in as an admin user
    And I navigate to the categories page
    And I click on "Add Category" button

  @UX @Validation @Category
  Scenario: TC_UX_ADMIN_CAT_001 - Verify validation message order
    When I clear the category name field
    And I click "Save" button
    Then I should see validation error "Category name is required" below the field
    And no other validation messages should appear

    When I enter "ab" in category name field
    And I click "Save" button
    Then I should see validation error "Category name must be at least 3 characters"
    And the "required" error should disappear