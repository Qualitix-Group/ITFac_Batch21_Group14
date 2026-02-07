@ui @categories @pagination
Feature: Categories Page Pagination Functionality

  Background:
    Given I am logged in as a user
    And I am on the dashboard page

  @ui @categories @pagination @TC_UI_USER_CAT_004
  Scenario: TC_UI_USER_CAT_004 Verify pagination displays correctly on Categories page
    Given I am logged in as a user
    And I am on the dashboard page
    When I navigate to the Categories page
    Then the Categories page should load successfully
    And the pagination component should be displayed
    And I should see page numbers in the pagination
    And the Previous button should be disabled on the first page
    And the Next button should be available

  @ui @categories @pagination @TC_UI_USER_CAT_003
  Scenario: TC_UI_USER_CAT_003 Verify page navigation using pagination
    Given I am logged in as a user
    And I am on the dashboard page
    When I navigate to the Categories page
    Then the Categories page should load successfully
    And I should see page numbers in the pagination
    When I click on page number "2"
    Then I should be navigated to page "2"
    And the page should display different category data
    When I click the Previous button
    Then I should be navigated back to page "1"
    And the original category data should be displayed