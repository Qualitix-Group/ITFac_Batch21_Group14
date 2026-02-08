#Feature: Category Management - Verify Pagination
#
#  Background:
#    Given the QA Training App is running
#    And I am on the login page
#    And I login as admin
#    And I open the Categories page for deletion
#
#  @Regression @Admin
#  Scenario: Verify pagination displays correctly
#    Then pagination controls should be visible at the bottom of the list
#    When I click the Next button
#    # Optionally, click a specific page number
#    When I click page number 2
