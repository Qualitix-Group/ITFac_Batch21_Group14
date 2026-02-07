#@user
#Feature: User Get Category by ID
#
#  Scenario: User gets category by valid id
#    When user requests category with id 1
#    Then category details should be returned
#


Feature: User Category Access
  As a regular user
  I want to view categories
  So that I can browse products

  @user
  Scenario: User gets category by valid id
    When user requests category with id 1
    Then category details should be returned

#  @user
#  Scenario: User tries to get non-existent category
#    Given user is authenticated
#    When user requests category with id 999
#    Then error response with status 404 should be returned