Feature: Admin Category Operations
  As an administrator
  I want to manage categories
  So that I can maintain the product catalog

  @admin
  Scenario: Admin gets category by valid id
    When admin requests category with id 1
    Then category details should be returned

#  @admin
#  Scenario: Admin tries to get non-existent category
#    Given admin is authenticated
#    When admin requests category with id 999
#    Then error response with status 404 should be returned