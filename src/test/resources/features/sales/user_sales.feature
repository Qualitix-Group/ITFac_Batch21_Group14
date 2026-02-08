@sales @user @ui
Feature: Sales Management - User
  As a regular user
  I want to view sales
  But I should not have admin privileges

  Background:
    Given I am logged in as user

  @UI-SALES-06
  Scenario: View sales list as user
    When I navigate to the sales page
    Then I should see the sales list table

  @UI-SALES-07 @security
  Scenario: Sell button hidden for user
    When I navigate to the sales page
    Then the sell button should be hidden

  @UI-SALES-08 @security
  Scenario: Delete button hidden for user
    When I navigate to the sales page
    Then the delete button should be hidden

  @UI-SALES-20
  Scenario: Pagination displayed when multiple pages exist
    Given sales records exceed one page
    When I navigate to the sales page
    Then sales pagination should be visible when multiple pages exist

  @UI-SALES-21
  Scenario: Pagination navigation works
    Given sales records exceed one page
    When I navigate to the sales page
    And I go to the next sales page
    Then the sales page should change

  @UI-SALES-22
  Scenario: Sort by Plant Name
    When I navigate to sales sorted by "plant" "asc"
    Then sales should be sorted by "Plant" "asc"

  @UI-SALES-23
  Scenario: Sort by Quantity
    When I navigate to sales sorted by "quantity" "asc"
    Then sales should be sorted by "Quantity" "asc"

  @UI-SALES-24
  Scenario: Sort by Total Price ascending and descending
    When I navigate to sales sorted by "totalPrice" "asc"
    Then sales should be sorted by "Total Price" "asc"
    When I navigate to sales sorted by "totalPrice" "desc"
    Then sales should be sorted by "Total Price" "desc"

  @UI-SALES-25
  Scenario: Sort by Sold Date default descending and toggle
    When I navigate to the sales page
    Then sales should be sorted by "Sold At" "desc"
    When I navigate to sales sorted by "soldAt" "asc"
    Then sales should be sorted by "Sold At" "asc"
    When I navigate to sales sorted by "soldAt" "desc"
    Then sales should be sorted by "Sold At" "desc"

  @UI-SALES-26
  Scenario: Empty sales list message
    Given the sales list is empty
    When I navigate to the sales page
    Then I should see the empty state message
